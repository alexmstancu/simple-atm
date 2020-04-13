# simple-atm
Spring Boot based ATM API simulator with basic functionality.

## About

Basic ATM API with very basic authentication. There are 4 operations supported:
* authentication (with credentials being accountNumber & pin)
* see the account's details (account holder name, balance, account number)
* withdraw an amount from an account
* deposit an amount into an account

Operations 2, 3, 4 are not allowed to be carried out without first authenticating into the system.

Once a user is authenticated, he is allowed to carry out a single operations out of the 3 aforementioned, after which
he will be 'deauthenticated' automatically (most ATMs function like that, or at least they did some time ago, they would allow to: enter the card -- which in our case, is simulated by the account number --, enter the pin, either see the balnce or withdraw and then they would throw the card out).

While a user is authenticated into the ATM with his accountNumber & pin, another user trying to authenticate
with the same credentials will not be permitted. A single user of an account is allowed to be logged into the system.

The app exposes a REST endpoint for each of the 4 operations, which will be details in the **REST endpoints** section below.

## Tech stack
1. maven as build tool
2. java 8
3. springboot as web framework
4. H2 in memory db
5. junit & jmockit for testing 
6. lombok for getter/setters
7. slf4j for logging (default configuration) 

## Development setup
1. win 10
2. IntelliJ IDEA
3. Postman
4. cmd

## Starting the app (win 10)

1. `git clone https://github.com/alexmstancu/simple-atm.git`
2. `cd simple-atm`
3. `mvnw clean install'
4. `mvnw spring-boot:run` or 
5. the web app will start on `http://localhost:8080/atm/userAccount`

## Playing with the app

There are a few users accounts already setup into the DB. Use them to test the app.
The app does not offer you a way to create/delete users, it relies on the idea they are already present, created by some other system/API beforehand. Here they are (this is actually the DB schema, sans the auto-generated id, which is not used in the app):
```
   name,       account_number, pin,   balance)
  ('alex',    '11111',        '1234', 500.12),
  ('george',  '22222',        '0000', 10000),
  ('andreea', '33333',        '8080', 2500),
  ('alina',   '55555',        '5555', 3.14);
```
The account_number is a string and the balance is a double. Mind the premise: the "account_numbers" should be unique across the DB.

### REST endpoints:

Note: The REST endpoints will do different sorts of validation (such as checking if the account number is existing in the DB before doing an operation, or if the amount to be withdraw is valid, or if the user is atuhenticated before doing an operation etc) and in case one of these validation rules is broken, an error HTTP status code will be returned together with a meaningful error message in the response body which states what was the problem with the request (e.g. for a "withdraw" request with an invalid amount, ```"error": "invalid amount, must be nonnull and strictly greater than 0"``` will be the response body. Also, for all such unsuccesful events, debug messages will be logged in the java app (as well as for successful events).

1. Authentication: **POST** `http://localhost:8080/atm/userAccount/auth`
   * you need to provide the credentials as URL query params; for example, for the 'alex' user:
   `http://localhost:8080/atm/userAccount/auth?accountNumber=11111&pin=1234`
   * possible responses:
   * **200 OK** and **below response body** - this is for the happy path (the account is existing & the pin is correct)
   ```
	{
		"operationType": "authentication",
		"accountNumber": "11111"
	}
	```
	* **401 UNAUTHORIZED** and **response body contains a meaningful error message** - if the account is existing & the pin is incorrect
	```
	{
		"error": "the pin was incorrect for the accountNumber: 11111"
	}
	```
	* **401 UNAUTHORIZED** and **response body contains a meaningful error message** - if the account is existing & the pin is correct, but you were already authenticated
	```
	{
		"error": "user with account number is already authenticated: 11111"
	}
	```
	* **404 NOT FOUND* and **response body contains a meaningful error message** - if the account is not existing TODO
	* once you are authenticated, you will be further allowed to do a single operations out of the 3 remaining operations, after which you will not be authenticated any more. you will have to re-authenticate, by calling this endpoint again, if you want to make other operations.
	* normally, the credentials should probably be sent through some HTML form, as request body, using HTTPS to encrypt the request body, but for the sake of simplicity... this app is simpler than that.
2. Account details: **GET** `http://localhost:8080/atm/userAccount/details`
	* note: you need to first authenticate in order to call this endpoint, otherwise you get 401 UNAUTHORIZED with an error message in the response
	* the account number must be present in the URL: `http://localhost:8080/atm/userAccount/details?accountNumber=11111`
	* if you are authenticated, the response will be in the following format:
	```
	{
        "operationType": "accountDetails",
        "currentBalance": 500.12,
        "accountNumber": "11111",
        "holderName": "alex"
	}
	``` 
    * if the account is not existing, the response body will contain an error message and status code is 404 NOT FOUND
3. Withdraw: **POST** `http://localhost:8080/atm/userAccount/withdraw`
    * note: you need to first authenticate in order to call this endpoint, otherwise you get 401 UNAUTHORIZED with an error message in the response
    * the account number and the amount must be present as URL query params:
    `http://localhost:8080/atm/userAccount/withdraw?accountNumber=11111&amount=100.12`
    * if you are authenticated & the current balance >= amount, the response will be in the following format:
    ```
    {
        "operationType": "withdraw",
        "operatedAmount": 100.0,
        "currentBalance": 400.12
    }
    ```
    * if the amount is null or zero or negative, response status will be 400 and an error message returned as response body
    * if currentBalance < amount, response status will be 400 and an error message returned as response body
4. Deposit: **POST** http://localhost:8080/atm/userAccount/deposit`
   * similar behavior to the 'withdraw' endpoint
   * example request: `http://localhost:8080/atm/userAccount/deposit?accountNumber=11111&amount=45.32`
   * if everything goes as expected, the response should be in the following format:
   ```
    {
        "operationType": "deposit",
        "operatedAmount": 100.0,
        "currentBalance": 600.12
    }
    ```
    * if the amount is invalid/accoutn does not exist/the user is not authenticated, error HTTP code & error messages will be returned

## Other details:
* there is a single DB table called user_account with the following schema:
```
CREATE TABLE user_account (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  account_number VARCHAR(5) NOT NULL,
  -- usually the pin (password) should hashed, but for simplicity it's plain text
  pin VARCHAR(4) NOT NULL,
  balance DOUBLE(15) NOT NULL
);
```
* there is a single @RestController called ATMController
* "auth", "withdraw" and "deposit" are POST requests because they modify/create resources on the server, while the the "details" endpoints is a GET because it is a *read* operation.
* exception handling is done outside of the controller, in the RestControllerExceptionHandler which is annotated with @ControllerAdvice and contains @ExceptionHandler methods that handle each of the custom exception thrown by services
* there are two @Services, one for authentication and one for user account operations (account details, withdraw, deposit)
* there is a single @Repository which both services access; normally, the authentication credentials should be in a different DB table than the user bank account data, but for simplicity, I am using a single table which have both kind of data.
* the authentication mechanism is implemented by hand because I wanted it to be simple; spring offers complex stuff which was not easily applicable (at least to me, as a spring novice)
* there is javadoc for the most interesting parts
* unit testing (with mocking) only for the BasicUserAccountService class
* it does not mimic real life that well, because it is some sort of a mix between an ATM (client) and a bank API (server). Normally this is how I image a correct flow: the ATM is a client which the user is using to make requests to the bank API server. But in my app, the ATM is also the API server.
* do check the console, log messages are printed for each of the beginning of operations, for the success or failure of them aswell.
