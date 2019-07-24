package com.moneytransferapp.moneytransfer.controller;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import com.moneytransferapp.moneytransfer.pojo.Account;
import com.moneytransferapp.moneytransfer.pojo.Transfer;
import com.moneytransferapp.moneytransfer.service.TransferBalanceService;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

/**
 * receives the response from router for transfer related requests
 * and delegates the data to service class
 */

public class TransferMoneyController {

	private static Logger logger = Logger.getLogger(TransferMoneyController.class);

	private TransferBalanceService transferBalanceService;

	public TransferMoneyController(TransferBalanceService transferBalanceService) {
		this.transferBalanceService = transferBalanceService;
	}

	/**
	 * receives the response from router and delegates the data to service class
	 * for transfer of balance from one account to other
	 * @return 200 for success
	 */
	
	public void transferBalance(RoutingContext transferBalanceContext){
		logger.debug("Executing transferBalance controller method");
		Transfer transfer = Json.decodeValue(transferBalanceContext.getBodyAsString(),
				Transfer.class);
		long depositAccountId = Long.valueOf(transfer.getDepositAccountId());
		long withdrawAccountId = Long.valueOf(transfer.getWithdrawAccountId());
		BigDecimal transactionBalance = transfer.getBalance();
		int responseCode = transferBalanceService.transferBalance(depositAccountId, withdrawAccountId, transactionBalance);
		if(responseCode == 200) {
			List<Account> list = transferBalanceService.getUpdatedAccountList(depositAccountId, withdrawAccountId);
			transferBalanceContext.response()
			.setStatusCode(200)
			.putHeader("content-type", "application/json; charset=utf-8")
			.end(Json.encodePrettily(list));
		}else if(responseCode == 400) {
			transferBalanceContext.response()
			.setStatusCode(400)
			.putHeader("content-type", "text; charset=utf-8")
			.end("The account has insufficient balance");
		}else if(responseCode == 422) {
			transferBalanceContext.response()
			.setStatusCode(422)
			.putHeader("content-type", "text; charset=utf-8")
			.end("The provided data is invalid");
		}
	}

	/**
	 * receives the response from router and delegates the data to service class
	 * to withdraw balance from withdraw account id
	 * @return 200 for success
	 */
	
	public void withdrawBalance(RoutingContext withdrawContext) {
		long withdrawAccountId = Long.valueOf(withdrawContext.request().getParam("withdrawAccountId"));
		BigDecimal transactionBalance = new BigDecimal(withdrawContext.request().getParam("balance"));
		int responseCode = transferBalanceService.withdrawBalance(withdrawAccountId, transactionBalance);
		if(responseCode == 200) {
			withdrawContext.response()
			.setStatusCode(200)
			.putHeader("content-type", "application/json; charset=utf-8")
			.end(Json.encodePrettily(transferBalanceService.getUpdatedAccountList(withdrawAccountId)));
		}else if(responseCode == 400) {
			withdrawContext.response()
			.setStatusCode(400)
			.putHeader("content-type", "text; charset=utf-8")
			.end("The account has insufficient balance");
		}else if(responseCode == 422) {
			withdrawContext.response()
			.setStatusCode(422)
			.putHeader("content-type", "text; charset=utf-8")
			.end("The request was invalid");
		}
	}

	/**
	 * receives the response from router and delegates the data to service class
	 * for depositing balance in deposit account id
	 * @return 200 for success
	 */
	public void depositBalance(RoutingContext depositContext) {
		long depositAccountId = Long.valueOf(depositContext.request().getParam("depositAccountId"));
		BigDecimal transactionBalance = new BigDecimal(depositContext.request().getParam("balance"));
		int responseCode = transferBalanceService.depositBalance(depositAccountId, transactionBalance);
		if(responseCode == 200) {
			depositContext.response()
			.setStatusCode(200)
			.putHeader("content-type", "application/json; charset=utf-8")
			.end(Json.encodePrettily(transferBalanceService.getUpdatedAccountList(depositAccountId)));
		}else if(responseCode == 422) {
			depositContext.response()
			.setStatusCode(422)
			.putHeader("content-type", "text; charset=utf-8")
			.end("The request was invalid");
		}
	}

	public TransferBalanceService getTransferBalanceService() {
		return transferBalanceService;
	}

	public void setTransferBalanceService(TransferBalanceService transferBalanceService) {
		this.transferBalanceService = transferBalanceService;
	}
}
