package com.moneytransferapp.moneytransfer.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import com.moneytransferapp.moneytransfer.exception.CustomException;
import com.moneytransferapp.moneytransfer.pojo.Account;

/**
 * unit test cases for account service class
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({FactoryClass.class})
public class AccountServiceImplUnitTests {

	private AccountServiceImpl accountServiceImpl = new AccountServiceImpl();

	@Mock
	private AccountDao accountDao;

	private Account account = new Account();

	@Before
	public void setup() {
		PowerMockito.mockStatic(FactoryClass.class);
		when(FactoryClass.getNewInstance(FactoryClassConstants.ACCOUNT_DAO)).thenReturn(accountDao);
	}

	private void setAccountDetails() {
		account.setAccountId(1L);
		account.setBalance(new BigDecimal(10000.00));
		account.setCurrencyCode(CurrencyCode.USD.getCurrency());
	}
	@Test
	public void testGetAccountWithValidValues() throws CustomException, SQLException {
		setAccountDetails();
		when(accountDao.getAccountById(1L)).thenReturn(account);
		assertNotNull(accountServiceImpl.getAccount("1"));
	}

	@Test
	public void testGetAccountWithIdIsNull() throws CustomException, SQLException {
		setAccountDetails();
		when(accountDao.getAccountById(1L)).thenReturn(account);
		assertNull(accountServiceImpl.getAccount(null));	
	}

	@Test
	public void testGetAccountWithIdIsNegative() throws CustomException, SQLException {
		setAccountDetails();
		when(accountDao.getAccountById(1L)).thenReturn(account);
		assertNull(accountServiceImpl.getAccount("-1"));
	}

	@Test
	public void testGetAccountWithAccountsBalanceIsNull() throws CustomException, SQLException {
		Account acc = new Account();
		acc.setBalance(null);	
		when(accountDao.getAccountById(1L)).thenReturn(acc);
		assertNull(accountServiceImpl.getAccount("1"));
	}

	@Test
	public void testGetAccountWithAccountsBalanceIsNotNullButCurrencyCodeIsNull() throws CustomException, SQLException {
		Account acc = new Account();
		acc.setBalance(new BigDecimal(500.00));
		acc.setCurrencyCode(null);
		when(accountDao.getAccountById(1L)).thenReturn(acc);
		assertNull(accountServiceImpl.getAccount("1"));
	}

	@Test
	public void testGetAllAccounts() throws SQLException, CustomException {
		List<Account> listOfAccounts = new ArrayList<>();
		Account a1 = new Account();
		listOfAccounts.add(a1);
		when(accountDao.getAllAccounts()).thenReturn(listOfAccounts);
		assertNotNull(accountServiceImpl.getAllAccounts());
	}

	@Test
	public void testAddAccount() throws CustomException, SQLException {
		List<Account> listOfAccounts = new ArrayList<>();
		listOfAccounts.add(account);
		Account accountObj = new Account();
		accountObj.setBalance(new BigDecimal(1000.00));
		accountObj.setCurrencyCode(CurrencyCode.USD.getCurrency());
		when(accountDao.addAccount(accountObj)).thenReturn(listOfAccounts);
		assertNotNull(accountServiceImpl.addAccount(accountObj));
	}

	@Test
	public void testAddAccountWhenAccountIsNull() throws CustomException, SQLException {
		assertNull(accountServiceImpl.addAccount(null));
	}

	@Test
	public void testAddAccountBalanceIsLessThanZero() throws CustomException, SQLException {
		Account acc = new Account();
		acc.setBalance(new BigDecimal(-1000.00));
		acc.setCurrencyCode("USD");
		assertNull(accountServiceImpl.addAccount(acc));
	}

	@Test
	public void testDeleteAccountWithvalidId() throws SQLException, CustomException {
		setAccountDetails();	
		PowerMockito.doNothing().when(accountDao).deleteAccountById(3L);
		assertEquals(204, accountServiceImpl.deleteAccount(3L));
	}

	@Test
	public void testDeleteAccountWithInvalidId() throws SQLException, CustomException {
		setAccountDetails();
		assertEquals(422, accountServiceImpl.deleteAccount(-12));
	}
}
