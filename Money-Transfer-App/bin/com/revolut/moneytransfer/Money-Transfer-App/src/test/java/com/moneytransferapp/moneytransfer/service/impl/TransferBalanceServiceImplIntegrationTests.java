package com.moneytransferapp.moneytransfer.service.impl;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

/**
 * Integration tests. these test cases should be run once the vert.x starts listening to port 8080
 * First run AccountServiceImplIntegrationTests and then TransferBalanceServiceImplIntegrationTests
 */
public class TransferBalanceServiceImplIntegrationTests {

	private static final String CONTEXT_ROOT = "/moneytransfer-v1";

	@BeforeClass
	public static void configureRestAssured() {
		RestAssured.baseURI = "http://localhost"+CONTEXT_ROOT;
		RestAssured.port = 8080;
	}

	@AfterClass
	public static void unconfigureRestAssured() {
		RestAssured.reset();
	}

	/**
	 * Integration tests for success transfer request
	 * @param transfer object
	 * @return 200
	 */
	@Test
	public void testTransferBalancePositive() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("depositAccountId", 1);
		requestParams.put("withdrawAccountId", 2);
		requestParams.put("balance", 1000);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		Response response = request.post("/transfers");
		Assert.assertEquals(200, response.getStatusCode());
	}

	/**
	 * Integration tests failure transfer by sending negative balance
	 * @param transfer object
	 * @return 422
	 */
	@Test
	public void testTransferBalanceWithNegativeBalance() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("depositAccountId", 1);
		requestParams.put("withdrawAccountId", 2);
		requestParams.put("balance", -1000);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		Response response = request.post("/transfers");
		Assert.assertEquals(422, response.getStatusCode());
		Assert.assertEquals("The provided data is invalid", response.asString());
	}

	/**
	 * Integration tests failure when ids are not present in db
	 * @param transfer object
	 * @return 400
	 */
	@Test
	public void testTransferBalanceWithIdsNotPresentInDb() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("depositAccountId", 99);
		requestParams.put("withdrawAccountId", 199);
		requestParams.put("balance", 1000);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		Response response = request.post("/transfers");
		Assert.assertEquals(400, response.getStatusCode());
	}

	/**
	 * Integration tests failure deposit by sending negative balance
	 * @param depositAccountId and transaction balance
	 * @return 422
	 */
	@Test
	public void testDepositBalanceWithNegativeBalance() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("depositAccountId", 1);
		requestParams.put("withdrawAccountId", 2);
		requestParams.put("transactionBalance", -1000);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		Response response = request.post("/deposits/1/-1000");
		Assert.assertEquals(422, response.getStatusCode());
		Assert.assertEquals("The request was invalid", response.asString());
	}

	/**
	 * Integration tests success for deposit
	 * @param depositAccountId and transaction balance
	 * @return 200
	 */
	@Test
	public void testDepositBalanceWithValidBalance() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("depositAccountId", 1);
		requestParams.put("transactionBalance", -1000);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		Response response = request.post("/deposits/1/1000");
		Assert.assertEquals(200, response.getStatusCode());
	}

	/**
	 * Integration tests success for deposit
	 * @param depositAccountId and transaction balance
	 * @return 422
	 */
	@Test
	public void testWithdrawBalanceWithNegativeBalance() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("withdrawAccountId", 1);
		requestParams.put("transactionBalance", -1000);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		Response response = request.post("/withdraws/2/-1000");
		Assert.assertEquals(422, response.getStatusCode());
		Assert.assertEquals("The request was invalid", response.asString());
	}

	/**
	 * Integration tests success for withdraw
	 * @param depositAccountId and transaction balance
	 * @return 200
	 */
	@Test
	public void testWithdrawBalanceWithValidBalance() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("withdrawAccountId", 1);
		requestParams.put("transactionBalance", 1000);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		Response response = request.post("/withdraws/1/1000");
		Assert.assertEquals(200, response.getStatusCode());
	}

	/**
	 * Integration tests failure when transaction balance is greater than account balance
	 * @param depositAccountId and transaction balance
	 * @return 400
	 */
	@Test
	public void testWithdrawBalanceWhenTransactionBalanceIsGreaterThanAccountBalance() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("withdrawAccountId", 1);
		requestParams.put("transactionBalance", 100000);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		Response response = request.post("/withdraws/1/100000");
		Assert.assertEquals(400, response.getStatusCode());
		Assert.assertEquals("The account has insufficient balance", response.asString());
	}
}
