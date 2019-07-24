package com.moneytransferapp.createTable.service.impl;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.moneytransferapp.createTable.dao.CreateInitialTableDao;
import com.moneytransferapp.createTable.dao.impl.CreateInitialTableDaoImpl;
import com.moneytransferapp.createTable.service.CreateInitialTableService;
import com.moneytransferapp.factory.FactoryClass;
import com.moneytransferapp.factory.FactoryClassConstants;
import com.moneytransferapp.moneytransfer.exception.CustomException;

/**
 * Service class implementation to create table
 */

public class CreateInitialTableServiceImpl implements CreateInitialTableService{

	private static Logger logger = Logger.getLogger(CreateInitialTableServiceImpl.class);
	
	CreateInitialTableDao createInitialTableDaoImpl;
	
	public CreateInitialTableServiceImpl(CreateInitialTableDao createInitialTableDaoImpl) {
		this.createInitialTableDaoImpl = createInitialTableDaoImpl;
	}
	
	@Override
	public void populateInitialData() throws CustomException, SQLException {
		logger.info("Populating db with intial data...");
		createAccountTable();
	}

	private void createAccountTable() throws SQLException, CustomException {
		createInitialTableDaoImpl = (CreateInitialTableDaoImpl) FactoryClass.getNewInstance(
				FactoryClassConstants.CREATE_INITIAL_TABLE_DAO);
		createInitialTableDaoImpl.createAccountTable();

	}

	public CreateInitialTableDao getCreateInitialTableDaoImpl() {
		return createInitialTableDaoImpl;
	}

	public void setCreateInitialTableDaoImpl(CreateInitialTableDao createInitialTableDaoImpl) {
		this.createInitialTableDaoImpl = createInitialTableDaoImpl;
	}
}
