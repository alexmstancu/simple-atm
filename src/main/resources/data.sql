DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  account_number VARCHAR(5) NOT NULL,
  pin_hash VARCHAR(256) NOT NULL,
  balance DECIMAL(15, 2) NOT NULL
);

INSERT INTO accounts (name, account_number, pin_hash, balance) VALUES
  ('alex', '11111', 'TODO', 500.12),
  ('george', '22222', 'TODO', 100),
  ('andreea', '33333', 'TODO', 200),
  ('alina', '55555', 'TODO', 2.14);