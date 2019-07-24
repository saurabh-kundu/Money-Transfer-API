package com.moneytransferapp.moneytransfer.service.impl;

import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.moneytransferapp.createTable.service.CreateInitialTableService;
import com.moneytransferapp.moneytransfer.exception.CustomException;

import io.vertx.core.json.EncodeException;

/**
 * Integration tests. these test cases should be run once the vert.x starts listening to port 8080
 * First run AccountServiceImplIntegrationTests and then TransferBalanceServiceImplIntegrationTests
 *
 */

public class AccountServiceImplIntegrationTests {

	private static CreateInitialTableService createInitialTableService;
	private static final String CONTEXT_ROOT = "/moneytransfer-v1";

	@BeforeClass
	public static void configureRestAssured() throws CustomException, SQLException {
		RestAssured.baseURI = "http://localhost"+CONTEXT_ROOT;
		RestAssured.port = 8080;
		addFirstInitialAccountsDataSet();
		addSecondInitialAccountsDataSet();
	}

	@AfterClass
	public static void unconfigureRestAssured() throws CustomException {
		RestAssured.reset();
	}

	public static void addFirstInitialAccountsDataSet() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("balance", 7000);
		requestParams.put("currencyCode", "INR");
		requestParams.put("userId", 7);

		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		request.post("/accounts");
	}

	private static void addSecondInitialAccountsDataSet() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("balance", 3000);
		requestParams.put("currencyCode", "USD");
		requestParams.put("userId", 3);

		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		request.post("/accounts");
	}

	//When AccountId Is Negative
	@Test
	public void testGetAccountWhenAccountIdIsNegative() throws EncodeException, CustomException, SQLException {
		get("accounts/-1").then()
		.assertThat()
		.statusCode(404);
	}

	//When AccountId Is Zero
	@Test
	public void testGetAccountWhenAccountIdIsZero() throws EncodeException, CustomException, SQLException {
		get("accounts/0").then()
		.assertThat()
		.statusCode(404);
	}

	//Test add account
	@Test
	public void testAddAccount() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("balance", 6000);
		requestParams.put("currencyCode", "USD");
		requestParams.put("userId", 6);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		Response response = request.post("/accounts");
		Assert.assertEquals(201, response.getStatusCode());
	}

	//Test add account with invalid value
	@Test
	public void testAddAccountWhenAccountIsInvalid() {
		JSONObject requestParams = new JSONObject();
		requestParams.put("balance", -1000);
		requestParams.put("currencyCode", "USD");
		requestParams.put("userId", 6);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		Response response = request.post("/accounts");
		Assert.assertEquals(404, response.getStatusCode());
	}

	//Test delete with valid id
	@Test
	public void testDeleteAccount() {
		RequestSpecification request = RestAssured.given();
		Response postResponse = request.delete("/accounts/3");
		Assert.assertEquals(204, postResponse.getStatusCode());

		Response getResponse = request.get("/accounts/3");
		Assert.assertEquals(404, getResponse.getStatusCode());
	}

	//Test delete with invalid value
	@Test
	public void testDeleteAccountWhenIdIsInvalid() {
		RequestSpecification request = RestAssured.given();
		Response postResponse = request.delete("/accounts/-1");
		Assert.assertEquals(422, postResponse.getStatusCode());

		Response getResponse = request.get("/accounts/-1");
		Assert.assertEquals(404, getResponse.getStatusCode());
		Assert.assertEquals("Account not found", getResponse.asString());
	}
	//Test returns one account
	@Test
	public void testGetAccount() throws EncodeException, CustomException, SQLException {
		get("accounts/1").then()
		.assertThat()
		.statusCode(200)
		.body("accountId", equalTo(1))
		.body("balance", Matchers.comparesEqualTo(7000.0000F))
		.body("currencyCode", equalTo("INR"))
		.body("userId", equalTo(7));
	}

	@Test
	public void testGetAllAccounts() throws CustomException, SQLException {
		List<Float> balanceList = new ArrayList<>();
		List<String> currencyList = new ArrayList<>();
		List<Integer> userIdList = new ArrayList<>();
		balanceList.add(7000F); balanceList.add(3000F); balanceList.add(6000F);
		currencyList.add("INR"); currencyList.add("USD"); currencyList.add("USD");
		userIdList.add(7); userIdList.add(3); userIdList.add(6);

		get("/accounts").then()
		.assertThat()
		.statusCode(200)
		.body("balance", equalTo(balanceList))
		.body("currencyCode", equalTo(currencyList))
		.body("userId", equalTo(userIdList));
	}

	public static CreateInitialTableService getCreateInitialTableService() {
		return createInitialTableService;
	}

	public static void setCreateInitialTableService(CreateInitialTableService createInitialTableService) {
		AccountServiceImplIntegrationTests.createInitialTableService = createInitialTableService;
	}
}
