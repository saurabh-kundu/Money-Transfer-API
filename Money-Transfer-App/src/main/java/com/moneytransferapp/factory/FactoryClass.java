package com.moneytransferapp.factory;

import java.util.concurrent.locks.ReentrantLock;

import com.moneytransferapp.MainApplication;
import com.moneytransferapp.RouteApiController;
import com.moneytransferapp.createTable.dao.impl.CreateInitialTableDaoImpl;
import com.moneytransferapp.createTable.service.impl.CreateInitialTableServiceImpl;
import com.moneytransferapp.moneytransfer.controller.AccountController;
import com.moneytransferapp.moneytransfer.controller.TransferMoneyController;
import com.moneytransferapp.moneytransfer.dao.impl.AccountDaoImpl;
import com.moneytransferapp.moneytransfer.service.impl.AccountServiceImpl;
import com.moneytransferapp.moneytransfer.service.impl.TransferBalanceServiceImpl;

/**
 * Factory to create objects.
 */
public class FactoryClass {

	public static Object getNewInstance(String className) {
		if(className.equals(FactoryClassConstants.TRANSFER_BALANCE_SERVICE)) {
			return new TransferBalanceServiceImpl(new AccountDaoImpl(), new ReentrantLock());

		}else if(className.equals(FactoryClassConstants.MAIN_APPLICATION)) {
			return new MainApplication(new RouteApiController(new AccountServiceImpl(), 
					new CreateInitialTableServiceImpl(new CreateInitialTableDaoImpl()), 
					new TransferBalanceServiceImpl(new AccountDaoImpl(), new ReentrantLock()), 
					new TransferMoneyController(new TransferBalanceServiceImpl(new AccountDaoImpl(), new ReentrantLock())),
					new AccountController(new AccountServiceImpl())));

		}else if(className.equals(FactoryClassConstants.ROUTE_API_CONTROLLER)) {
			return new RouteApiController(new AccountServiceImpl(), 
					new CreateInitialTableServiceImpl(new CreateInitialTableDaoImpl()), 
					new TransferBalanceServiceImpl(new AccountDaoImpl(), new ReentrantLock()), 
					new TransferMoneyController(new TransferBalanceServiceImpl(new AccountDaoImpl(), new ReentrantLock())),
					new AccountController(new AccountServiceImpl()));

		}else if(className.equals(FactoryClassConstants.CREATE_INITIAL_TABLE_SERVICE)) {
			return new CreateInitialTableServiceImpl(new CreateInitialTableDaoImpl());

		}else if(className.equals(FactoryClassConstants.ACCOUNT_DAO)) {
			return new AccountDaoImpl();

		}else if(className.equals(FactoryClassConstants.CREATE_INITIAL_TABLE_DAO)) {
			return new CreateInitialTableDaoImpl();
		}

		return className;
	}
}
