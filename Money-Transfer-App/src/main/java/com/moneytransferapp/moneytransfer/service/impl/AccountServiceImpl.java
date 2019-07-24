package com.moneytransferapp.moneytransfer.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.moneytransferapp.factory.FactoryClass;
import com.moneytransferapp.factory.FactoryClassConstants;
import com.moneytransferapp.moneytransfer.dao.AccountDao;
import com.moneytransferapp.moneytransfer.exception.CustomException;
import com.moneytransferapp.moneytransfer.pojo.Account;
import com.moneytransferapp.moneytransfer.service.AccountService;

/**
 * receives the data from controller and performs the respective operations
 */

public class AccountServiceImpl implements AccountService {

	private static Logger logger = Logger.getLogger(AccountServiceImpl.class);
	private AccountDao accountDao;

	/**
	 * gets account based on provided accountId
	 * @param accountId
	 * @return account object
	 */
	public Object getAccount(String id) {
		logger.debug("Entered the getAccount() method");
		Account account = null;
		if(null != id) {
			long accountId = Long.parseLong(id);
			if(accountId > 0) {
				accountDao = (AccountDao) FactoryClass.getNewInstance(FactoryClassConstants.ACCOUNT_DAO);
				logger.debug("Entered the getAccount() method and id is not null"); 
				try {
					account = accountDao.getAccountById(accountId);
				} catch (SQLException | CustomException e) {
					e.printStackTrace();
				}
				if(account.getBalance() != null && account.getCurrencyCode() != null) {
					return account;
				}else {
					return null;
				}
			}
		} else {
			logger.debug("Entered the getAccount() method and id is null"); 
		}
		return account;
	}

	/**
	 * gets all accounts
	 * @return list of account object
	 */
	public List<Account> getAllAccounts() {
		AccountDao accDao = null;
		accDao = accountDao = (AccountDao) FactoryClass.getNewInstance(FactoryClassConstants.ACCOUNT_DAO);
		try {
			return accDao.getAllAccounts();
		} catch (SQLException | CustomException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * adds a new account
	 * @param account object to be added
	 * @return list of updated accounts
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object addAccount(Account account) {
		List<Account> accList = null;
		if(null != account && (account.getBalance().compareTo(BigDecimal.ZERO) > 0)) {
			AccountDao accountDao = (AccountDao) FactoryClass.getNewInstance(FactoryClassConstants.ACCOUNT_DAO);
			try {
				accList = (List<Account>) accountDao.addAccount(account);
			} catch (SQLException | CustomException e) {
				e.printStackTrace();
			}
		}
		return accList;
	}

	/**
	 * deletes an existing account
	 * @param accountId
	 */
	@Override
	public int deleteAccount(long deleteAccountId) {
		int responseCode = 0;
		if(deleteAccountId > 0) {
			AccountDao accountDao = (AccountDao) FactoryClass.getNewInstance(FactoryClassConstants.ACCOUNT_DAO);
			try {
				accountDao.deleteAccountById(deleteAccountId);
				responseCode = 204;
			} catch (SQLException | CustomException e) {
				e.printStackTrace();
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
}
