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

## Running the app (win 10)

1. `git clone https://github.com/alexmstancu/simple-atm.git`
2. `cd simple-atm`
3. `mvnw clean install`
4. `mvnw spring-boot:run`
5. the web app will start on `http://localhost:8080`

## Trying out the app

There are a few users accounts already setup into the DB. Use them to test the app.
The app does not offer you a way to create/delete users, it relies on the idea they are already present, created by some other system/API beforehand. Here they are:
```
   name,       account_number, pin,   balance)
  ('alex',    '11111',        '1234', 500.12),
  ('george',  '22222',        '0000', 10000),
  ('andreea', '33333',        '8080', 2500),
  ('alina',   '55555',        '5555', 3.14);
```
