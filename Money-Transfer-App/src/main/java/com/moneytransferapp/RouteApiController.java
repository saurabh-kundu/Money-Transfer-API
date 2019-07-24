package com.moneytransferapp;

import java.sql.SQLException;

import com.moneytransferapp.createTable.service.CreateInitialTableService;
import com.moneytransferapp.moneytransfer.controller.AccountController;
import com.moneytransferapp.moneytransfer.controller.TransferMoneyController;
import com.moneytransferapp.moneytransfer.exception.CustomException;
import com.moneytransferapp.moneytransfer.service.AccountService;
import com.moneytransferapp.moneytransfer.service.TransferBalanceService;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Routes the recieved rest apis to respective controller
 */
public class RouteApiController {

	private AccountService accountService;
	private TransferBalanceService transferBalanceService;
	private TransferMoneyController transferMoneyController;
	private AccountController accountController;
	private static final String CONTEXT_ROOT = "/moneytransfer-v1";
	
	public RouteApiController(AccountService accountService,
			CreateInitialTableService loadInitialDataService, TransferBalanceService transferBalanceService,
			TransferMoneyController transferMoneyController, AccountController accountController) {
		this.accountService = accountService;
		this.transferBalanceService = transferBalanceService;
		this.transferMoneyController = transferMoneyController;
		this.accountController = accountController;
	}
	
	public void routingRestApiCalls(Router router) throws CustomException, SQLException {
		
		router.route("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response
			.putHeader("content-type", "text/html")
			.end("<h1>Welcome to Money Transfer Api App</h1>");
		});

		router.route("/assets/*").handler(StaticHandler.create("assets"));
		router.route().handler(BodyHandler.create());

		router.get(CONTEXT_ROOT+"/accounts").handler(accountsContext -> {
				accountController.getAllAccounts(accountsContext);
		});
		router.get(CONTEXT_ROOT+"/accounts/:id").handler(accountContext -> {
			accountController.getAccount(accountContext);
		});

		router.delete(CONTEXT_ROOT+"/accounts/:deleteAccountId").handler(deleteAccountContext -> {
			accountController.deleteAccount(deleteAccountContext);
		});

		router.post(CONTEXT_ROOT+"/accounts").handler(addAccountContext -> {
			accountController.addAccount(addAccountContext);
		});

		router.post(CONTEXT_ROOT+"/transfers").handler(transferBalanceContext -> {
				transferMoneyController.transferBalance(transferBalanceContext);
		});

		router.post(CONTEXT_ROOT+"/deposits/:depositAccountId/:balance").handler(depositContext -> {
			transferMoneyController.depositBalance(depositContext);
		});

		router.post(CONTEXT_ROOT+"/withdraws/:withdrawAccountId/:balance").handler(withdrawContext -> {
			transferMoneyController.withdrawBalance(withdrawContext);
		});
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public TransferBalanceService getTransferBalanceService() {
		return transferBalanceService;
	}

	public void setTransferBalanceService(TransferBalanceService transferBalanceService) {
		this.transferBalanceService = transferBalanceService;
	}

	public TransferMoneyController getTransferMoneyController() {
		return transferMoneyController;
	}

	public void setTransferMoneyController(TransferMoneyController transferMoneyController) {
		this.transferMoneyController = transferMoneyController;
	}
}
