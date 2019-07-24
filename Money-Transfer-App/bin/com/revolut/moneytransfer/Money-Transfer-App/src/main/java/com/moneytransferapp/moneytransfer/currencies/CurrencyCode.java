package com.moneytransferapp.moneytransfer.currencies;

/**
 * ENUM for currency codes
 */

public enum CurrencyCode {
	USD("USD"), INR("INR"), EUR("EUR");
	
	private String currency; 
 
	public String getCurrency() 
	{ 
		return this.currency; 
	}
	
	CurrencyCode(String ccr){
		this.currency = ccr;
	}
}

