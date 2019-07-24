package com.moneytransferapp.moneytransfer.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.moneytransferapp.factory.FactoryClass;
import com.moneytransferapp.factory.FactoryClassConstants;
import com.moneytransferapp.moneytransfer.dao.AccountDao;
import com.moneytransferapp.moneytransfer.exception.CustomException;
import com.moneytransferapp.moneytransfer.pojo.Account;
import com.moneytransferapp.moneytransfer.service.TransferBalanceService;

/**
 * receives the data from controller and performs the respective operations
 */

public class TransferBalanceServiceImpl implements TransferBalanceService {

	private static Logger logger = Logger.getLogger(TransferBalanceServiceImpl.class);
	private AccountDao accountDao;
	private ReentrantLock lock;

	public TransferBalanceServiceImpl(AccountDao accountDao, ReentrantLock lock) {
		this.accountDao= accountDao;
		this.lock = lock;
	}

	/**
	 * transfers transaction amount from withdraw account to deposit account
	 * @param depositAccountid, withdrawAccountId and transaction balance
	 */
	@Override
	public int transferBalance(long depositAccountId, long withdrawAccountId, 
			BigDecimal transactionBalance){
		logger.debug("Executing transferbalance method..");
		int responseCode=0;
		if(depositAccountId > 0 && withdrawAccountId > 0 && 
				transactionBalance.compareTo(BigDecimal.ZERO) > 0) {
			logger.debug("The provided values are valid");
			responseCode = transferTransactionBalance(depositAccountId, withdrawAccountId, transactionBalance);
		}else {
			responseCode=422;
		}
		return responseCode;
	}

	private int transferTransactionBalance(long depositAccountId, long withdrawAccountId, BigDecimal transactionBalance) {
		boolean transfer = false;
		int responseCode=0;
		try {
			if(lock.tryLock()) {
				logger.debug("Lock acquired..");
				BigDecimal initialWithdrawAccountBalance = getAccountBalance(withdrawAccountId);
				if(null != initialWithdrawAccountBalance && initialWithdrawAccountBalance.compareTo(BigDecimal.ZERO) >0) {
					if(checkSuffiencientBalance(initialWithdrawAccountBalance, transactionBalance)) {
						BigDecimal initialDepositAccountBalance = getAccountBalance(depositAccountId);
						if(initialDepositAccountBalance != null && 
								initialDepositAccountBalance.compareTo(BigDecimal.ZERO) >= 0) {
							deductWithdrawAccountBalanceAndPersist(withdrawAccountId, 
									initialWithdrawAccountBalance, transactionBalance);
							depositTransactionAccountBalanceAndPersist(depositAccountId, 
									initialDepositAccountBalance, transactionBalance);
							transfer = true;
							responseCode = 200;
						}else {
							responseCode = 400;
						}
					}else {
						responseCode = 400;
					}
				}else {
					responseCode = 400;
				}
			}else {
				logger.debug("Failed to acquire lock...trying agian");
				transferTransactionBalance(depositAccountId, withdrawAccountId, transactionBalance);
			}
		}catch(CustomException | SQLException e) {
			logger.info("Got exception while executing transfer");
			e.printStackTrace();
		}finally {
			if(transfer) {
				lock.unlock();
			}
		}
		return responseCode;
	}

	
	//provides updated list of both deposit and withdraw account after transaction is completed successfully
	public List<Account> getUpdatedAccountList(long depositAccountId, long withdrawAccountId) {
		logger.debug("Executing updatedAccountList method");
		List<Account> updatedAccountList = new ArrayList<>();
		if(depositAccountId > 0 && withdrawAccountId > 0) {
			accountDao = (AccountDao) FactoryClass.getNewInstance(FactoryClassConstants.ACCOUNT_DAO);
			Account withdrawAccount = null;
			Account depositAccount = null;
			try {
				withdrawAccount = accountDao.getAccountById(withdrawAccountId);
				depositAccount = accountDao.getAccountById(depositAccountId);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (CustomException e) {
				e.printStackTrace();
			}
			updatedAccountList.add(depositAccount);
			updatedAccountList.add(withdrawAccount);
			return updatedAccountList;
		}else {
			return null;
		}
	}

	//provides updated list of either deposit or withdraw account after transaction is completed successfully based on id
	public Account getUpdatedAccountList(long updatedAccountId) {
		logger.debug("Executing updatedAccountList Method");
		Account updatedAccount = null;
		if(updatedAccountId > 0) {
			accountDao = (AccountDao) FactoryClass.getNewInstance(FactoryClassConstants.ACCOUNT_DAO);
			try {
				updatedAccount = accountDao.getAccountById(updatedAccountId);
			} catch (SQLException | CustomException e) {
				e.printStackTrace();
			}
		}else {
			updatedAccount = null;
		}
		return updatedAccount;
	}

	//performs calculation of final amount after deposit and then persist the amount in db
	private void depositTransactionAccountBalanceAndPersist(long depositAccountId,
			BigDecimal initialDepositAccountBalance, BigDecimal transactionBalance) 
					throws CustomException, SQLException {
		BigDecimal finalWithdrawAccountBalance = initialDepositAccountBalance.add(transactionBalance);
		accountDao = (AccountDao) FactoryClass.getNewInstance(FactoryClassConstants.ACCOUNT_DAO);
		accountDao.updateAccountWithUpdatedBalance(depositAccountId, finalWithdrawAccountBalance);
	}

	//performs calculation of final amount after withdraw and then persist the amount in db
	private void deductWithdrawAccountBalanceAndPersist(long withdrawAccountId, 
			BigDecimal initialWithdrawAccountBalance,BigDecimal transactionBalance) 
					throws CustomException, SQLException {
		BigDecimal finalWithdrawAccountBalance = initialWithdrawAccountBalance.subtract(transactionBalance);
		accountDao = (AccountDao) FactoryClass.getNewInstance(FactoryClassConstants.ACCOUNT_DAO);
		accountDao.updateAccountWithUpdatedBalance(withdrawAccountId, finalWithdrawAccountBalance);
	}

	//checks whether the account has sufficient balance before withdrawing
	private boolean checkSuffiencientBalance(BigDecimal withdrawAccountBalance, 
			BigDecimal transactionBalance) {
		if((withdrawAccountBalance.compareTo(transactionBalance)) >= 0)
			return true;
		else
			return false;
	}

	//gets the account balance based on accountId
	private BigDecimal getAccountBalance(long accountId) throws SQLException, CustomException {
		accountDao = (AccountDao) FactoryClass.getNewInstance(FactoryClassConstants.ACCOUNT_DAO);
		return accountDao.getAccountById(accountId).getBalance();
	}

	/**
	 * performs withdraw operation based on transaction amount and id
	 * @param withdrawAccountId and transaction balance
	 */
	@Override
	public int withdrawBalance(long withdrawAccountId, BigDecimal transactionBalance){
		logger.debug("Executing withdraw method..");
		int responseCode = 0;
		boolean withdraw = false;
		if(transactionBalance.compareTo(BigDecimal.ZERO) > 0) {
			try {
				if(lock.tryLock()) {
					BigDecimal initialWithdrawAccountBalance = getAccountBalance(withdrawAccountId);
					if(null != initialWithdrawAccountBalance && 
							initialWithdrawAccountBalance.compareTo(transactionBalance) > 0) {
						deductWithdrawAccountBalanceAndPersist(withdrawAccountId, 
								initialWithdrawAccountBalance, transactionBalance);
						withdraw = true;
						responseCode = 200;
					}else {
						responseCode = 400;
					}
				}else {
					logger.info("Failed to acquire lock...trying agian");
					withdrawBalance(withdrawAccountId, transactionBalance);
				}
			}catch(CustomException | SQLException ex) {
				ex.printStackTrace();
			}finally {
				if(withdraw)
					lock.unlock();
			}
		}else{
			responseCode = 422;
		}
		return responseCode;
	}

	/**
	 * performs deposit operation based on transaction amount and id
	 * @param depositAccountId and transaction balance
	 */
	@Override
	public int depositBalance(long depositAccountId, BigDecimal transactionBalance) {
		logger.debug("Executing depsit method..");
		boolean deposit = false;
		int responseCode = 0;
		if(transactionBalance.compareTo(BigDecimal.ZERO) > 0) {
			try {
				if(lock.tryLock()) {
					BigDecimal initialDepositAccountBalance = getAccountBalance(depositAccountId);
					depositTransactionAccountBalanceAndPersist(depositAccountId, 
							initialDepositAccountBalance, transactionBalance);
					deposit = true;
					responseCode = 200;
				}else {
					logger.info("Failed to acquire lock...trying agian");
					depositBalance(depositAccountId, transactionBalance);
				}
			} catch (CustomException | SQLException e) {
				e.printStackTrace();
			}finally {
				if(deposit)
					lock.unlock();
			}
		}else {
			responseCode = 422;
		}
		return responseCode;
	}

	public AccountDao getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	public ReentrantLock getLock() {
		return lock;
	}

	public void setLock(ReentrantLock lock) {
		this.lock = lock;
	}

}
