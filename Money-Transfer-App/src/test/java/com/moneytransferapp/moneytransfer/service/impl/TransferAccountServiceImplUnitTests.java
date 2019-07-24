package com.moneytransferapp.moneytransfer.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.moneytransferapp.factory.FactoryClass;
import com.moneytransferapp.factory.FactoryClassConstants;
import com.moneytransferapp.moneytransfer.currencies.CurrencyCode;
import com.moneytransferapp.moneytransfer.dao.AccountDao;
import com.moneytransferapp.moneytransfer.dao.impl.AccountDaoImpl;
import com.moneytransferapp.moneytransfer.exception.CustomException;
import com.moneytransferapp.moneytransfer.pojo.Account;

/**
 * unit test cases for account service class
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({FactoryClass.class})
public class TransferAccountServiceImplUnitTests {

	@Mock
	private AccountDao accountDao;
	@Mock
	private ReentrantLock lock;
	private Account withDrawAccount = new Account();
	private Account depositAccount = new Account();

	TransferBalanceServiceImpl transferBalanceServiceImpl = new TransferBalanceServiceImpl(new AccountDaoImpl(), new ReentrantLock());

	@Before
	public void setup() {
		PowerMockito.mockStatic(FactoryClass.class);
		when(FactoryClass.getNewInstance(FactoryClassConstants.ACCOUNT_DAO)).thenReturn(accountDao);
		transferBalanceServiceImpl.setLock(lock);
		transferBalanceServiceImpl.setAccountDao(accountDao);
		when(lock.tryLock()).thenReturn(true);
	}
	private void setAccountDetails() {
		withDrawAccount.setAccountId(2L);
		withDrawAccount.setBalance(new BigDecimal(10000.00));
		withDrawAccount.setCurrencyCode(CurrencyCode.USD.getCurrency());

		depositAccount.setAccountId(1L);
		depositAccount.setBalance(new BigDecimal(1000.00));
		depositAccount.setCurrencyCode(CurrencyCode.USD.getCurrency());
	}

	//method: transfer balance
	//When depositAccountId, withdrawAccountId and balance are valid
	@Test
	public void testTransferBalance() throws SQLException, CustomException {
		setAccountDetails();
		when(accountDao.getAccountById(2L)).thenReturn(withDrawAccount);
		when(accountDao.getAccountById(1L)).thenReturn(depositAccount);
		assertEquals(200, transferBalanceServiceImpl.transferBalance(1L, 2L, new BigDecimal(1000.00)));
	}
	//method: transfer balance
	//When depositAccountId is negative
	@Test
	public void testTransferBalanceWithInvalidDepositId() throws SQLException, CustomException {
		setAccountDetails();
		assertEquals(422, transferBalanceServiceImpl.transferBalance(-1L, 2L, new BigDecimal(1000.00)));
	}
	//method: transfer balance
	//When withdrawAccountId is negative
	@Test
	public void testTransferBalanceWithInvalidWithdrawId() throws SQLException, CustomException {
		setAccountDetails();
		when(accountDao.getAccountById(2L)).thenReturn(withDrawAccount);
		when(accountDao.getAccountById(1L)).thenReturn(depositAccount);
		assertEquals(422, transferBalanceServiceImpl.transferBalance(1L, -2L, new BigDecimal(1000.00)));
	}
	//method: transfer balance
	//When balance is Negative
	@Test
	public void testTransferBalanceWithInvalidBalance() throws SQLException, CustomException {
		setAccountDetails();
		when(accountDao.getAccountById(2L)).thenReturn(withDrawAccount);
		when(accountDao.getAccountById(1L)).thenReturn(depositAccount);
		assertEquals(422, transferBalanceServiceImpl.transferBalance(1L, 2L, new BigDecimal(-1000.00)));
	}

	//method: transfer balance
	//When initial withdraw balance is null
	@Test
	public void testTransferBalanceWithInitialWithdrawBalanceIsNull() throws SQLException, CustomException {
		Account withDrawAcc = new Account();
		withDrawAccount.setAccountId(2L);
		withDrawAccount.setBalance(null);
		withDrawAccount.setCurrencyCode(CurrencyCode.USD.getCurrency());
		when(accountDao.getAccountById(2L)).thenReturn(withDrawAcc);
		when(accountDao.getAccountById(1L)).thenReturn(depositAccount);
		assertEquals(400, transferBalanceServiceImpl.transferBalance(1L, 2L, new BigDecimal(1000.00)));
	}

	//method: transfer balance
	//When initial withdraw balance is negative
	@Test
	public void testTransferBalanceWithInitialWithdrawBalanceIsNegative() throws SQLException, CustomException {
		Account withDrawAcc = new Account();
		withDrawAccount.setAccountId(2L);
		withDrawAccount.setBalance(new BigDecimal(-10000.00));
		withDrawAccount.setCurrencyCode(CurrencyCode.USD.getCurrency());
		when(accountDao.getAccountById(2L)).thenReturn(withDrawAcc);
		when(accountDao.getAccountById(1L)).thenReturn(depositAccount);
		assertEquals(400, transferBalanceServiceImpl.transferBalance(1L, 2L, new BigDecimal(1000.00)));
	}
	//method: transfer balance
	//When initial withdraw account has insufficient balance
	@Test
	public void testTransferBalanceIfWithdrawAccountHasInSufficientBalance() throws SQLException, CustomException {
		setAccountDetails();
		when(accountDao.getAccountById(2L)).thenReturn(withDrawAccount);
		when(accountDao.getAccountById(1L)).thenReturn(depositAccount);
		assertEquals(400, transferBalanceServiceImpl.transferBalance(1L, 2L, new BigDecimal(100000.00)));
	}
	//method: transfer balance
	//When initial deposit balance is null
	@Test
	public void testTransferBalanceIfDepositAccountBalanceIsNull() throws SQLException, CustomException {
		Account depositAcc = new Account();
		depositAcc.setAccountId(2L);
		depositAcc.setBalance(null);
		depositAcc.setCurrencyCode(CurrencyCode.USD.getCurrency());

		when(accountDao.getAccountById(2L)).thenReturn(withDrawAccount);
		when(accountDao.getAccountById(1L)).thenReturn(depositAcc);
		assertEquals(400, transferBalanceServiceImpl.transferBalance(1L, 2L, new BigDecimal(1000.00)));
	}
	//method: transfer balance
	//When initial deposit balance is negative
	@Test
	public void testTransferBalanceIfDepositAccountNegativeBalance() throws SQLException, CustomException {
		Account depositAcc = new Account();
		depositAcc.setAccountId(2L);
		depositAcc.setBalance(new BigDecimal(-1000.00));
		depositAcc.setCurrencyCode(CurrencyCode.USD.getCurrency());

		when(accountDao.getAccountById(2L)).thenReturn(withDrawAccount);
		when(accountDao.getAccountById(1L)).thenReturn(depositAcc);
		assertEquals(400, transferBalanceServiceImpl.transferBalance(1L, 2L, new BigDecimal(1000.00)));
	}
	//method: withdrawBalance
	//All values are valid
	@Test
	public void testWithdrawBalance() throws SQLException, CustomException {
		setAccountDetails();
		when(accountDao.getAccountById(2L)).thenReturn(withDrawAccount);
		assertEquals(200, transferBalanceServiceImpl.withdrawBalance(2L, new BigDecimal(1000.00)));
	}

	//method: withdrawBalance
	//When transaction balance is invalid
	@Test
	public void testWithdrawBalanceWhenTransactionBalanceIsInvalid() throws SQLException, CustomException {
		setAccountDetails();
		when(accountDao.getAccountById(2L)).thenReturn(withDrawAccount);
		assertEquals(422, transferBalanceServiceImpl.withdrawBalance(2L, new BigDecimal(-1000.00)));
	}

	//method: withdrawBalance
	//When initial withdraw balance is less than transaction balance
	@Test
	public void testWithdrawBalanceWhenInitialWithdrawAmountIsLessThanTransactionBalanceIsInvalid() throws SQLException, CustomException {
		setAccountDetails();
		when(accountDao.getAccountById(2L)).thenReturn(withDrawAccount);
		assertEquals(400, transferBalanceServiceImpl.withdrawBalance(2L, new BigDecimal(110000.00)));
	}

	//method: withdrawBalance
	//All values are valid
	@Test
	public void testDepositBalance() throws SQLException, CustomException {
		setAccountDetails();
		when(accountDao.getAccountById(1L)).thenReturn(depositAccount);
		assertEquals(200, transferBalanceServiceImpl.depositBalance(1L, new BigDecimal(1000.00)));
	}

	//method: withdrawBalance
	//When transaction balance is invalid
	@Test
	public void testDepositBalanceWhenTransactionBalanceIsInvalid() throws SQLException, CustomException {
		setAccountDetails();
		when(accountDao.getAccountById(1L)).thenReturn(depositAccount);
		assertEquals(422, transferBalanceServiceImpl.depositBalance(1L, new BigDecimal(-1000.00)));
	}
}
