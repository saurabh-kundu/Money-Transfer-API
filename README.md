# Money-Transfer-API App
A simple light weight money transfer API app

#Technology stack
Java 8
Maven
RESTful API
Vert.x framework
Json
H2-in memory Database
Junit4 for unit testing along with Mockito and PowerMockito
RestAssured for integration testing

#Building And Running

mvn clean install

Standalone executable jar is placed under target Money-Transfer-App-0.0.1-SNAPSHOT-exec.jar. So you can run this jar:

java -jar .\target\Money-Transfer-App-0.0.1-SNAPSHOT-exec.jar

#Available RESTful APIs

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

