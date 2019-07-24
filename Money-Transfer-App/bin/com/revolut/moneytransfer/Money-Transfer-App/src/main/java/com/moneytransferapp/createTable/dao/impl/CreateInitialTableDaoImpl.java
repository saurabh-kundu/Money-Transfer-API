package com.moneytransferapp.createTable.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.moneytransferapp.createTable.dao.CreateInitialTableDao;
import com.moneytransferapp.database.connectionconfig.DatabaseConnectionConfig;
import com.moneytransferapp.moneytransfer.exception.CustomException;

/**
 * Implementation to create tables
 */

public class CreateInitialTableDaoImpl implements CreateInitialTableDao {

	private static Logger logger = Logger.getLogger(CreateInitialTableDaoImpl.class);

	public void createAccountTable() {
		Connection conn = null;
		Statement stmt =null;
		try {
			conn = DatabaseConnectionConfig.getConnection();
			conn.setAutoCommit(false);
			String sql = "CREATE TABLE Account (AccountId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,"
					+ "Balance DECIMAL(19,4),CurrencyCode VARCHAR(30),UserId VARCHAR(30))";
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			conn.commit();
		}catch (SQLException | CustomException e) {
			logger.error("Got exception while performing populateInitialData(): SQL exception", e);
			throw new RuntimeException(e);
		} finally {
			try {
				if(null != stmt)
					stmt.close();
				if(null != conn)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
