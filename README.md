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
   * **200 OK** - this is for the happy path (the account is existing & the pin is correct), response body:
   ```
	{
		"operationType": "authentication",
		"accountNumber": "11111"
	}
	```
	* **401 UNAUTHORIZED** and - if the account is existing & the pin is incorrect, response body:
	```
	{
		"error": "the pin was incorrect for the accountNumber: 11111",
		"accountNumber": "11111"
	}
	```
	* **401 UNAUTHORIZED** - if the account is existing & the pin is correct, but you were already authenticated, response body:
	```
	{
		"error": "user with account number is already authenticated: 11111",
		"accountNumber": "11111"
	}
	```
	* **404 NOT FOUND** - if the account is not existing, response body:
	```
	{
	    "error": "account number not found: 111111",
	    "accountNumber": "111111"
	}
	```
	* once you are authenticated, you will be further allowed to do a single operations out of the 3 remaining operations, after which you will not be authenticated any more. you will have to re-authenticate, by calling this endpoint again, if you want to make other operations.
	* normally, the credentials should probably be sent through some HTML form, as request body, using HTTPS to encrypt the request body, but for the sake of simplicity... this app is simpler than that.
2. Account details: **GET** `http://localhost:8080/atm/userAccount/details`
	* note: you need to first authenticate in order to call this endpoint, otherwise you get 401 UNAUTHORIZED
	* the account number and the amount must be present as URL query params: 
	`http://localhost:8080/atm/userAccount/details?accountNumber=11111`
	* possible responses:
	* **200 OK** - this is for the happy path (if you are authenticated)
	```
	{
        "operationType": "accountDetails",
        "currentBalance": 500.12,
        "accountNumber": "11111",
        "holderName": "alex"
	}
	```
	* **401 UNAUTHORIZED** - if you previously did not authenticate, response body:
	```
	{
		"error": "user for the following account is not authenticated: 11111",
		"accountNumber": "11111"
	}
	```
* if the account is not existing, the response body will contain an error message and status code is 404 NOT FOUND
3. Withdraw: **POST** `http://localhost:8080/atm/userAccount/withdraw`
    * note: you need to first authenticate in order to call this endpoint, otherwise you get 401 UNAUTHORIZED
    * the account number and the amount must be present as URL query params:
    `http://localhost:8080/atm/userAccount/withdraw?accountNumber=11111&amount=100.12`
    * possible responses:
	* **200 OK** - this is for the happy path (if you are authenticated & the current balance >= amount and amount > 0)
    ```
    {
        "operationType": "withdraw",
        "operatedAmount": 100.0,
        "currentBalance": 400.12
    }
    ```
	* **401 UNAUTHORIZED** - if you previously did not authenticate, response body:
	```
	{
		"error": "user for the following account is not authenticated: 11111",
		"accountNumber": "11111"
	}
	```
	* **400 BAD REQUEST** - if currentBalance < amount, response body:
	```
	{
		"error": "insufficient balance; current balance: 500.12, requested withdraw amount: 1000.0",
		"accountNumber": "11111"
	}
	```
	* **400 BAD REQUEST** - if the amount is null or zero or negative, response body:
	```
	{
		"error": "invalid amount, amount must not be null, nor less than or equal to 0; amount: -1000.0",
		"accountNumber": "11111"
	}
	```
4. Deposit: **POST** http://localhost:8080/atm/userAccount/deposit`
   * similar behavior to the 'withdraw' endpoint (you have to authenticate first)
   * example request (accountNumber & amount must be in the URL): 
   `http://localhost:8080/atm/userAccount/deposit?accountNumber=11111&amount=45.32`
   * if everything goes as expected, the response should be in the following format, together with **200 OK**:
   ```
    {
        "operationType": "deposit",
        "operatedAmount": 100.0,
        "currentBalance": 600.12
    }
    ```
	* possible responses: similar to withdraw opration, **but** you won't receive the **400 BAD REQUEST** any more if currentBalance < deposited amount.

## Other details:
* there is a single DB table called user_account with the following schema:
```
CREATE TABLE user_account (
  id INT AUTO_INCREMENT PRIMARY KEY, -- this is not used in the app
  name VARCHAR(50) NOT NULL,
  account_number VARCHAR(5) NOT NULL,
  -- usually the pin (password) should hashed, but for simplicity it's plain text
  pin VARCHAR(4) NOT NULL,
  balance DOUBLE(15) NOT NULL
);
```
* there is a single @RestController called ATMController
* "auth", "withdraw" and "deposit" are POST requests because they modify/create resources on the server, while the the "details" endpoints is a GET because it is a *read* operation.
* exception handling is done outside of the controller, in the RestControllerExceptionHandler which is annotated with @ControllerAdvice and contains @ExceptionHandler methods that handle each of the custom exceptions thrown by services
* there are two @Services, one for authentication and one for user account operations (account details, withdraw, deposit)
* there is a single @Repository which both services access; normally, the authentication credentials should be in a different DB table than the user bank account data, but for simplicity, I am using a single table which have both kinds of data.
* the authentication mechanism is implemented by hand because I wanted it to be simple
* there is javadoc for the most interesting parts
* unit testing (with mocking) only for the BasicUserAccountService class (other unit tests would be done in a similar way, testing all differnet inputs)
* it does not mimic real life very well, because it is some sort of a mix between an ATM (client) and a bank API (server). In the real world, this is how I image a correct flow: the ATM is a client which the human user is using to make operations which are translated by the ATM into requests to the bank API server. But in my app, the ATM is also the API server.
* log messages are printed for all operations, either successful or not, together with representative data that can help debugging 
* don't modify the DB script to have negative values for balance, or to have identical account numbers for multiple accounts, or other hacks like that, the app is not prepared for this kind of shock :)
* thanks for reading all this, it was a long text for a little app
