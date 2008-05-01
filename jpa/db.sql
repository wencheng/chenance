/**
 * SQL genereted by Jiemamy
 * Create: 2008/04/30 19:10:02
 */

-- define: t_account
CREATE TABLE t_account (
  id IDENTITY NOT NULL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE,
  description VARCHAR(200),
  bank_name VARCHAR(100),
  bank_branch VARCHAR(20),
  bank_account_no VARCHAR(20),
  bank_url VARCHAR(255),
  interest INT,
  initial_balance INT,
  currency INT DEFAULT 1
);



-- define: t_category
CREATE TABLE t_category (
  id INT NOT NULL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(200)
);

INSERT INTO t_category (id, name, description)
  VALUES (1000000, 'debit', '');
INSERT INTO t_category (id, name, description)
  VALUES (2000000, 'credit', '');
INSERT INTO t_category (id, name, description)
  VALUES (3000000, 'transfer', '');


-- define: t_transaction
CREATE TABLE t_transaction (
  id IDENTITY NOT NULL PRIMARY KEY,
  account_id INT NOT NULL,
  _date DATE NOT NULL,
  category_id INT NOT NULL,
  debit INT NOT NULL,
  credit INT NOT NULL,
  is_repeat BOOLEAN NOT NULL,
  FOREIGN KEY (category_id)
    REFERENCES t_category (id),
  FOREIGN KEY (account_id)
    REFERENCES t_account (id)
);



-- define: t_investment
CREATE TABLE t_investment (
  id IDENTITY NOT NULL PRIMARY KEY,
  account_id INT NOT NULL,
  is_buy_or_sell BOOLEAN NOT NULL,
  price INT NOT NULL,
  quantity INT NOT NULL,
  amount INT NOT NULL,
  FOREIGN KEY (account_id)
    REFERENCES t_account (id)
);



-- define: t_transaction_breakdown
CREATE TABLE t_transaction_breakdown (
  id IDENTITY NOT NULL PRIMARY KEY,
  transaction_id INT NOT NULL,
  FOREIGN KEY (transaction_id)
    REFERENCES t_transaction (id)
);



