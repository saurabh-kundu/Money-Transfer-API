package com.moneytransferapp.moneytransfer.pojo;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * POJO for transfer
 */

public class Transfer {

	private static final AtomicInteger COUNTER = new AtomicInteger();
	
	private long transferId;
	private long userId;
	private long depositAccountId;
	private long withdrawAccountId;
	private BigDecimal balance;
	
	public Transfer(long transferId, long userid, long depositAccountId, long withdrawAccountId, BigDecimal balance) {
		this.transferId = transferId;
		this.userId = userid;
		this.depositAccountId = depositAccountId;
		this.withdrawAccountId = withdrawAccountId;
		this.balance = balance;
	}
	
	public Transfer(long userid, long depositAccountId, long withdrawAccountId, BigDecimal balance) {
		this.userId = userid;
		this.depositAccountId = depositAccountId;
		this.withdrawAccountId = withdrawAccountId;
		this.balance = balance;
	}
	
	
	public Transfer() {
        this.transferId = COUNTER.getAndIncrement();
    }
	
	public long getTransferId() {
		return transferId;
	}
	public void setTransferId(long transferId) {
		this.transferId = transferId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getDepositAccountId() {
		return depositAccountId;
	}
	public void setDepositAccountId(long depositAccountId) {
		this.depositAccountId = depositAccountId;
	}
	public long getWithdrawAccountId() {
		return withdrawAccountId;
	}
	public void setWithdrawAccountId(long withdrawAccountId) {
		this.withdrawAccountId = withdrawAccountId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "Transfer(transferId: " + this.transferId + "balance: "+ this.balance + 
				"withdrawAccountId: "+ this.withdrawAccountId + "userId: " + this.userId+
				"depositAccountId" + this.depositAccountId;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;

		if(!(o instanceof Account))
			return false;

		Transfer transfer = (Transfer) o;
		return (transferId == transfer.getTransferId() && userId == transfer.getUserId()
				&& balance.equals(transfer.getBalance()) && depositAccountId == transfer.getDepositAccountId()
				&& withdrawAccountId == transfer.getWithdrawAccountId());
	}

	@Override
	public int hashCode() { 
		return (int) transferId;
	}
}
