package com.moneytransferapp.createTable.dao;

import com.moneytransferapp.moneytransfer.exception.CustomException;

/**
 * create table for storing/handling data
 */

public interface CreateInitialTableDao {

	void createAccountTable() throws CustomException;
}
