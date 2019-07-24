package com.moneytransferapp.moneytransfer.service;

import java.math.BigDecimal;
import java.util.List;

import com.moneytransferapp.moneytransfer.pojo.Account;

/**
 * interface for transfer related operations
 */
public interface TransferBalanceService {

	int transferBalance(long depositAccountId, long withdrawAccountId, BigDecimal transactionBalance);

	List<Account> getUpdatedAccountList(long depositAccountId, long withdrawAccountId);

	int withdrawBalance(long withdrawAccountId, BigDecimal transactionBalance);

	int depositBalance(long depositAccountId, BigDecimal transactionBalance);
	
	Account getUpdatedAccountList(long updatedAccountId);
}
