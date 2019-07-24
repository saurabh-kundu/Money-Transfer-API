package com.moneytransferapp.database.connectionconfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import com.moneytransferapp.moneytransfer.exception.CustomException;

/**
 * TODO: In production systems, database connections should be handled via connection pools.
 * Haven't implemented here because of shortage of time.
 */
public class DatabaseConnectionConfig {

	private static Logger logger = Logger.getLogger(DatabaseConnectionConfig.class);
	
	private static final String JDBC_DRIVER = "org.h2.Driver";   
	private static final String DB_URL = "jdbc:h2:mem:moneytransfer;DB_CLOSE_DELAY=-1";

	private static final String USER = "sa"; 
	private static final String PASS = "sa";
    
    public static Connection getConnection() throws CustomException, SQLException {
    	try {
    		Class.forName(JDBC_DRIVER);
    		return DriverManager.getConnection(DB_URL, USER, PASS);
    	}catch(ClassNotFoundException e) {
    		logger.info("Got Exception while getting connection from databse!");
    		throw new CustomException("Got Exception while getting connection from databse!", e);
    	}
    	catch(SQLException e) {
    		logger.info("Got Exception while getting connection from databse!");
    		throw new CustomException("Got Exception while getting connection from databse!", e);
    	}
    }
}
