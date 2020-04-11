DROP TABLE IF EXISTS user_account;

CREATE TABLE user_account (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  accountNumber VARCHAR(5) NOT NULL,
  pinHash VARCHAR(256) NOT NULL,
  balance DOUBLE(15) NOT NULL
);

INSERT INTO user_account (name, accountNumber, pinHash, balance) VALUES
  ('alex', '11111', 'TODO', 500.12),
  ('george', '22222', 'TODO', 100),
  ('andreea', '33333', 'TODO', 200),
  ('alina', '55555', 'TODO', 2.14);