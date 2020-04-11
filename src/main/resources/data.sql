DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  card_number VARCHAR(250) NOT NULL,
  pin_hash VARCHAR(250) NOT NULL,
  balance DOUBLE(15, 2)
);

INSERT INTO accounts (first_name, last_name, card_number, pin_hash, balance) VALUES
--
  ('Alex', 'Stancu', 'Billionaire Industrialist'),
  ('Bill', 'Gates', 'Billionaire Tech Entrepreneur'),
  ('Folrunsho', 'Alakija', 'Billionaire Oil Magnate');