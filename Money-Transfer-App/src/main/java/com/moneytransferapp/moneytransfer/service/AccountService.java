package com.moneytransferapp.moneytransfer.service;

import java.util.List;

import com.moneytransferapp.moneytransfer.pojo.Account;

/**
 * interface for account related operations 
 */
public interface AccountService {

	Object getAccount(String id);
	List<Account> getAllAccounts();
	Object addAccount(Account account);
	int deleteAccount(long deleteAccountId);
}
