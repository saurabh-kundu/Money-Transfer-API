package com.moneytransferapp.createTable.service;

import java.sql.SQLException;

import com.moneytransferapp.moneytransfer.exception.CustomException;

/**
 * Service class to create table
 */

public interface CreateInitialTableService {

	void populateInitialData() throws CustomException, SQLException ;
}
