package com.moneytransferapp.moneytransfer.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.moneytransferapp.database.connectionconfig.DatabaseConnectionConfig;
import com.moneytransferapp.moneytransfer.dao.AccountDao;
import com.moneytransferapp.moneytransfer.exception.CustomException;
import com.moneytransferapp.moneytransfer.pojo.Account;

/**
 * DAO implementation for account related operations
 */

public class AccountDaoImpl implements AccountDao{

	private static Logger logger = Logger.getLogger(AccountDaoImpl.class);

	@Override
	public Account getAccountById(long accountId) throws SQLException, CustomException {
		Statement stmt = null;
		Account acc = new Account();
		Connection conn = null;
		try {
			if(logger.isDebugEnabled())
				logger.debug("Executing getAccountById method");
			conn = DatabaseConnectionConfig.getConnection();
			//conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String query = "SELECT * FROM Account acc WHERE acc.AccountId = "+accountId;
			ResultSet resultSet = stmt.executeQuery(query);
			if(resultSet != null) {
				while(resultSet.next()) {
					acc.setAccountId(resultSet.getLong(1));
					acc.setBalance(resultSet.getBigDecimal(2));
					acc.setCurrencyCode(resultSet.getString(3));
					acc.setUserId(resultSet.getLong(4));
				}
			}
			//conn.commit();
			if(logger.isDebugEnabled())
				logger.debug("Finished executing getAccountById method");
		} catch (CustomException | SQLException e) {
			throw new CustomException("Got some exception while performing getAccountById operation", e);
		}finally {
			if(null != stmt)
				stmt.close();
			if(null != conn)
				conn.close();
		}

		return acc;
	}

	@Override
	public List<Account> getAllAccounts() throws SQLException, CustomException {
		List<Account> listOfAccounts = new ArrayList<>();
		Statement stmt = null;
		Connection conn = null;
		try {
			conn = DatabaseConnectionConfig.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String query = "SELECT * FROM Account";
			ResultSet resultSet = stmt.executeQuery(query);
			while(resultSet.next()) {
				Account acc = new Account(resultSet.getLong(1), resultSet.getBigDecimal(2),resultSet.getString(3),
						resultSet.getLong(4));
				listOfAccounts.add(acc);
			}
			conn.commit();
			if(logger.isDebugEnabled())
				logger.debug("Finished executing getAllAccounts method");
		}catch (CustomException e) {
			throw new CustomException("Got some exception while performing getAllAccounts operation", e);
		}finally {
			if(null != stmt)
				stmt.close();
			if(null != conn)
				conn.close();
		}
		return listOfAccounts;
	}

	@Override
	public void updateAccountWithUpdatedBalance(long accountId, BigDecimal updatedBalance) throws CustomException, SQLException {
		Statement stmt = null;
		Connection conn = null;
		try {
			if(logger.isDebugEnabled())
				logger.debug("Executing updateAccountWithUpdatedBalance method");
			conn = DatabaseConnectionConfig.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String query = "update Account set balance = " + updatedBalance +"where accountId = "+ accountId;
			stmt.executeUpdate(query);
			conn.commit();
			if(logger.isDebugEnabled())
				logger.debug("Finished executing updateAccountWithUpdatedBalance method");
		} catch (CustomException e) {
			throw new CustomException("Got some exception while performing updateAccountWithUpdatedBalance operation", e);
		}finally {
			if(null != stmt)
				stmt.close();
			if(null != conn)
				conn.close();
		}
	}

	@Override
	public Object addAccount(Account acc) throws SQLException, CustomException {
		if(logger.isDebugEnabled())
			logger.debug("Executing addAccount method");
		addAccountToDb(acc);
		if(logger.isDebugEnabled())
			logger.debug("Finished executing addAccount method");
		return getAllAccounts(); 
	}

	private void addAccountToDb(Account acc) throws SQLException, CustomException {
		Statement stmt = null;
		Connection conn = null;
		try {
			if(logger.isDebugEnabled())
				logger.debug("Executing addAccountToDb method");
			conn = DatabaseConnectionConfig.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String query = "insert into Account (Balance, CurrencyCode, UserId) values ("+acc.getBalance() +","+"'"+
					acc.getCurrencyCode()+"'"+","+acc.getUserId()+");";
			stmt.executeUpdate(query);
			conn.commit();
			if(logger.isDebugEnabled())
				logger.debug("Finished executing addAccountToDb method");
		}catch (CustomException e) {
			throw new CustomException("Got some exception while performing addAccountToDb operation", e);
		}finally {
			if(null != stmt)
				stmt.close();
			if(null != conn)
				conn.close();
		}
	}

	@Override
	public void deleteAccountById(long deleteAccountId) throws SQLException, CustomException {
		Statement stmt = null;
		Connection conn = null;
		try {
			if(logger.isDebugEnabled())
				logger.debug("Executing deleteAccountById method");
			conn = DatabaseConnectionConfig.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			String query = "delete from Account where accountId = "+ deleteAccountId;
			stmt.executeUpdate(query);
			conn.commit();
			if(logger.isDebugEnabled())
				logger.debug("Finished executing deleteAccountById method");
		}catch (CustomException e) {
			throw new CustomException("Got some exception while performing deleteAccountById operation", e);
		}finally {
			if(null != stmt)
				stmt.close();
			if(null != conn)
				conn.close();
		}
	}
}
