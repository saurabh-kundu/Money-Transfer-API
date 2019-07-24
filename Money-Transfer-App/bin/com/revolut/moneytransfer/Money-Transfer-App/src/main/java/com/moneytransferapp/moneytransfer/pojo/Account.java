package com.moneytransferapp.moneytransfer.pojo;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * POJO for Account
 */

public class Account {

	private static final AtomicInteger COUNTER = new AtomicInteger();
	private long accountId;
	private BigDecimal balance;
	private String currencyCode;
	private long userId;

	public Account(long accountId, BigDecimal balance, String currencyCode, long userId) {
		this.accountId = accountId;
		this.balance = balance;
		this.currencyCode = currencyCode;
		this.userId = userId;
	}

	public Account(BigDecimal balance, String currencyCode, long userId) {
		this.accountId = COUNTER.getAndIncrement();
		this.balance = balance;
		this.currencyCode = currencyCode;
		this.userId = userId;
	}

	public Account() {
		this.accountId = COUNTER.getAndIncrement();
	}

	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Account(id: " + this.accountId + "balance: "+ this.balance + 
				"currencyCode: "+ this.currencyCode + "userId: " + this.userId;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;

		if(!(o instanceof Account))
			return false;

		Account account = (Account) o;
		return (accountId == account.getAccountId() && userId == account.getUserId()
				&& balance.equals(account.getBalance()) && currencyCode.equals(account.getCurrencyCode()));
	}

	@Override
	public int hashCode() { 
		return (int) accountId;
	}
}
