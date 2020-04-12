# simple-atm
Spring Boot based ATM API simulator.

## About

Basic ATM API with very basic authentication. There are 4 operations supported:
* authentication (with credentials being accountNumber & pin)
* see the account's details (account holder name, balance, account number)
* withdraw an amount from an account
* deposit an amount into an account

Operations 2, 3, 4 are not allowed to be carried out without first authenticating into the system.

Once a user is authenticated, he is allowed to carry out a single operations out of the 3 aforementioned, after which
he will be 'deauthenticated' automatically (most ATMs function like that, or at least they did some time ago).

While a user is authenticated into the ATM with his accountNumber & pin, another user trying to authenticate
with the same credentials will not be permitted. A single user of an account is allowed to be logged into the system.

The app exposes a REST endpoint for each of the 4 operations.

## Tech stack
1. maven as build tool
2. java 8
3. springboot as web framework
4. H2 in memory db
5. junit & jmockit for testing 
6. lombok for getter/setters
7. slf4j for logging (default configuration) 

## Running the app (win 10)

1. `git clone https://github.com/alexmstancu/simple-atm.git`
2. `cd simple-atm`
3. `mvnw clean install`
4. `mvnw spring-boot:run`
5. the web app will start on `http://localhost:8080/atm/userAccount`

## Trying out the app

There are a few users accounts already setup into the DB. Use them to test the app.
The app does not offer you a way to create/delete users, it relies on the idea they are already present, created by some other system/API beforehand. Here they are (this is actually the DB schema, sand the auto-generated id, which is not used in the app):
```
   name,       account_number, pin,   balance)
  ('alex',    '11111',        '1234', 500.12),
  ('george',  '22222',        '0000', 10000),
  ('andreea', '33333',        '8080', 2500),
  ('alina',   '55555',        '5555', 3.14);
```
The account_number is a string and the balance is a double.

### REST endpoints:

The REST endpoints will do different sorts of validation (such as checking if the account number is existing in the DB before doing an operation, or if the amount to be withdraw is valid, or if the user is atuhenticated before doing an operation etc) and in case one of these validation rules is broken, an error HTTP status code will be returned together with a meaningful error message in the response body which states what was the problem with the request (e.g. for a "withdraw" request with an invalid amount, ```"error": "invalid amount, must be nonnull and strictly greater than 0"``` will be in the response. Also, for all such unsuccesful events, debug messages will be logged in the java app (as well as for successful events).


1. Authentication: **POST** `http://localhost:8080/atm/userAccount/auth`
   * this will authenticate you as a user into the ATM system
   * you need to provide the credentials as a request body, for example, for the user 'alex':
   ```
   {
	   "accountNumber": "11111",
	   "pin": "1234"
   }
   ```
   * if the account is existing & the pin is correct, the response body is empty and status code is 200 OK
   * if the account is existing & the pin is incorrect, the response body contains an error message and status code is 401 UNAUTHORIZED
   * if the account is not existing, the response body will contain an error message and status code is 404 NOT FOUND
   * if the account is existing & the pin is correct, but you were already authenticated, the response body contains an error message and status code is 401 UNAUTHORIZED
   * once you are authenticated, you will be further allowed to do a single operations out of the 3 remaining operations, after which you will not be authenticated any more. you will have to re-authenticate, by calling this endpoint, if you want to make other operations.
2. Account details: **GET** `http://localhost:8080/atm/userAccount/{accountNumber}/details`
	* note: you need to first authenticate in order to call this endpoint, otherwise you get 401 UNAUTHORIZED with an error message in the response
	* when you call the endpoint, the request body should be empty
	* the account number for the DB must be present in the URL: `http://localhost:8080/atm/userAccount/11111/details`
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
3. Withdraw: **POST** `http://localhost:8080/atm/userAccount/{accountNumber}/withdraw`
    * note: you need to first authenticate in order to call this endpoint, otherwise you get 401 UNAUTHORIZED with an error message in the response
    * the account number for the DB must be present in the URL and the request body should contain the following payload:
    ```
    {
        "amount": 100,
        "currency": "RON"
    }
    ```
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
4. Deposit: **POST** http://localhost:8080/atm/userAccount/{accountNumber}/deposit`
   * similar behavior to the 'withdraw' endpoint
   * if everything goes as expected, the response should be in the following format:
   ```
    {
        "operationType": "deposit",
        "operatedAmount": 100.0,
        "currentBalance": 600.12
    }
    ```
    * if the amount is invalid/accoutn does not exist/the user is not authenticated, error HTTP code & error messages will be returned

It does not mimic real life that well, because it is some sort of a mix between an ATM (client) and a bank API (server). Normally this is how I image a correct flow: the ATM is a client which the user is using to make requests to the bank API server. But in my app, the ATM is also the API server.
