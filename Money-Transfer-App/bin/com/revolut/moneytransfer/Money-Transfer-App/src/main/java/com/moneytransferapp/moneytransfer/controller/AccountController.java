package com.moneytransferapp.moneytransfer.controller;

import java.util.List;

import org.apache.log4j.Logger;

import com.moneytransferapp.moneytransfer.pojo.Account;
import com.moneytransferapp.moneytransfer.service.AccountService;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

/**
 * receives the response from router for account opeartion related requests
 * and delegates the data to service class
 */

public class AccountController {

	private static Logger logger = Logger.getLogger(AccountController.class);

	private AccountService accountService;

	public AccountController(AccountService accountService){
		this.accountService = accountService;
	}

	/**
	 * receives the response from router and delegates the data to service class
	 * to get a single account based on accountId
	 * @return 200 for success
	 */

	public void getAccount(RoutingContext accountContext) {
		logger.info("Executing getAccount method..");
		String id = accountContext.request().getParam("id");
		Object account = accountService.getAccount(id);
		if(null == account) {
			accountContext.response()
			.setStatusCode(404)
			.putHeader("content-type", "text; charset=utf-8")
			.end("Account not found");
		}else {
			accountContext.response()
			.setStatusCode(200)
			.putHeader("content-type", "application/json; charset=utf-8")
			.end(Json.encodePrettily(account));
		}
	}

	/**
	 * receives the response from router and delegates the data to service class
	 * to get all accounts
	 * @return 200 for success
	 */

	public void getAllAccounts(RoutingContext accountsContext) {
		List<Account> allAccountList = accountService.getAllAccounts();
		if(null != allAccountList) {
			accountsContext.response()
			.setStatusCode(200)
			.putHeader("content-type", "application/json; charset=utf-8")
			.end(Json.encodePrettily(allAccountList));
		}else {
			accountsContext.response()
			.setStatusCode(404)
			.putHeader("content-type", "text; charset=utf-8")
			.end("No account found");
		}
	}

	/**
	 * receives the response from router and delegates the data to service class
	 * to add a new account
	 * @return 201 for success
	 */
	
	public void addAccount(RoutingContext addAccountContext) {
		Object account = Json.decodeValue(addAccountContext.getBodyAsString(),
				Account.class);
		if(account == null) {
			addAccountContext.response()
			.setStatusCode(404)
			.putHeader("content-type", "text; charset=utf-8")
			.end("Account not Added");
		}else {
			@SuppressWarnings("unchecked")
			List<Account> accountList = (List<Account>) accountService.addAccount((Account) account);
			if(null == accountList) {
				addAccountContext.response()
				.setStatusCode(404)
				.putHeader("content-type", "text; charset=utf-8")
				.end("Account not Added");
			}else {
				addAccountContext.response()
				.setStatusCode(201)
				.putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(accountList));
			}
		}
	}

	/**
	 * receives the response from router and delegates the data to service class
	 * to delete an existing account
	 * @return 204 for success
	 */
	
	public void deleteAccount(RoutingContext deleteAccountContext) {
		long deleteAccountId = Long.valueOf(deleteAccountContext.request().getParam("deleteAccountId"));
		int responseCode = accountService.deleteAccount(deleteAccountId);
		if(responseCode == 204) {
			deleteAccountContext.response()
			.setStatusCode(204)
			.putHeader("content-type", "text; charset=utf-8")
			.end("Account deleted successfully");
		}else if( responseCode == 422) {
			deleteAccountContext.response()
			.setStatusCode(422)
			.putHeader("content-type", "text; charset=utf-8")
			.end("Invalid request");
		}
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
}
