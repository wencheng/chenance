/**
 * SQL genereted by Jiemamy
 * Create: 2008/05/08 15:13:24
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
  start_balance INT DEFAULT 0,
  current_balance INT DEFAULT 0 NOT NULL,
  currency INT DEFAULT 1
);

INSERT INTO t_account (id, name, description)
  VALUES (1, '生活費', '家賃、光熱費、交通費など');
INSERT INTO t_account (bank_name, id, name, description)
  VALUES (NULL, 2, '食費', '毎日飲食に消費したお金');
INSERT INTO t_account (id, name, description, bank_name)
  VALUES (3, '銀行貯金', '銀行残高や引き出しの履歴', 'いきいき銀行');


-- define: t_category
CREATE TABLE t_category (
  id IDENTITY NOT NULL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(200)
);

INSERT INTO t_category (id, name, description)
  VALUES (1000000, 'debit', '');
INSERT INTO t_category (id, name)
  VALUES (1010000, '食費');
INSERT INTO t_category (id, name)
  VALUES (1010100, '外食');
INSERT INTO t_category (id, name, description)
  VALUES (2000000, 'credit', '');
INSERT INTO t_category (id, name)
  VALUES (2010000, '給与');
INSERT INTO t_category (id, name)
  VALUES (2010100, '会社');
INSERT INTO t_category (id, name, description)
  VALUES (3000000, 'transfer', '');


-- define: t_transaction
CREATE TABLE t_transaction (
  id IDENTITY NOT NULL PRIMARY KEY,
  account_id BIGINT NOT NULL,
  _date DATE NOT NULL,
  category_id BIGINT NOT NULL,
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
  account_id BIGINT NOT NULL,
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
  transaction_id BIGINT NOT NULL,
  FOREIGN KEY (transaction_id)
    REFERENCES t_transaction (id)
);



