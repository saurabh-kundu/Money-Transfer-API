# Money-Transfer-API App
A simple light weight money transfer API app

# Technology stack

Java 8
Maven
RESTful API
Vert.x framework
Json
H2-in memory Database
Junit4 for unit testing along with Mockito and PowerMockito
RestAssured for integration testing

## Building And Running

Using maven for building the application

```bash
mvn clean install 
```
Standalone executable jar is placed under target Money-Transfer-App-0.0.1-SNAPSHOT-exec.jar. So you can run this jar:

```bash
java -jar .\target\Money-Transfer-App-0.0.1-SNAPSHOT-exec.jar
```
## Instructions to run Integration test cases
```bash
Step 1: Start the main application, if you have imported the code into any IDE then you can run the main 
application class (MainApplication.java).
or,
you can directly run the executable jar

Step2: Run the AccountServiceImplIntegrationTests class and then run the TransferBalanceServiceImplIntegrationTests class
(there is data dependency)
```
## Available RESTful APIs

```python
Get all accounts
GET - http://localhost:8080/moneytransfer-v1/accounts

Get one account by id
GET - http://localhost:8080/moneytransfer-v1/accounts/<accountId>

Delete account by id  
DELETE - http://localhost:8080/moneytransfer-v1/accounts/<accountId>

Add account
POST - http://localhost:8080/moneytransfer-v1/accounts

Tranfer balance
POST - http://localhost:8080/moneytransfer-v1/transfers

Deposit balance
POST - http://localhost:8080/moneytransfer-v1/deposits/<depositAccountId>/<balance>

Withdraw balance
POST - http://localhost:8080/moneytransfer-v1/withdraws/<withdrawAccountId>/<balance>
```

## Http Status
```python
200 OK: The request has succeeded
201 OK: New resource has been created
204 OK: The resource has been deleted successfully
400 Bad Request: The request was invalid or cannot be served
404 Not Found: There is no resource behind the URL
422 Unprocessed Entity: Server cannot process the request
500 Internal Server Error: The server encountered an unexpected condition
```
