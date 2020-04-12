DROP TABLE IF EXISTS user_account;

CREATE TABLE user_account (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  account_number VARCHAR(5) NOT NULL,
  -- usually the pin (password) should hashed, but for simplicity it's plain text
  pin VARCHAR(4) NOT NULL,
  balance DOUBLE(15) NOT NULL
);

INSERT INTO user_account (name, account_number, pin, balance) VALUES
  ('alex', '11111', '1234', 500.12),
  ('george', '22222', '0000', 10000),
  ('andreea', '33333', '8080', 2500),
  ('alina', '55555', '5555', 3.14);