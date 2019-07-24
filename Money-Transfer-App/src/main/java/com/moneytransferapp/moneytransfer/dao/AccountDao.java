package com.moneytransferapp.moneytransfer.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.moneytransferapp.moneytransfer.exception.CustomException;
import com.moneytransferapp.moneytransfer.pojo.Account;

/**
 * DAO interface for account related operations 
 */

public interface AccountDao {

	Account getAccountById(long accountId) throws SQLException, CustomException;
	
	List<Account> getAllAccounts() throws SQLException, CustomException;

	Object addAccount(Account acc) throws SQLException, CustomException;
	
	void updateAccountWithUpdatedBalance(long accountId, BigDecimal updatedBalance) 
			throws CustomException, SQLException;

	void deleteAccountById(long deleteAccountId) throws SQLException, CustomException;
}
