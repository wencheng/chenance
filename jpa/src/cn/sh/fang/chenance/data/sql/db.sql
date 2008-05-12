/**
 * SQL genereted by Jiemamy
 * Create: 2008/05/12 10:47:05
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
  interest_rate INT,
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
  description VARCHAR(200),
  parent_id BIGINT,
  FOREIGN KEY (parent_id)
    REFERENCES t_category (id)
);

INSERT INTO t_category (id, name, description)
  VALUES (1000000, 'debit', '');
INSERT INTO t_category (id, name, parent_id)
  VALUES (1010000, '食費', 1000000);
INSERT INTO t_category (id, name, parent_id)
  VALUES (1010100, '外食', 1010100);
INSERT INTO t_category (id, name, description, parent_id)
  VALUES (2000000, 'credit', '', NULL);
INSERT INTO t_category (id, name, parent_id)
  VALUES (2010000, '給与', 2000000);
INSERT INTO t_category (id, name, parent_id)
  VALUES (2010100, '会社', 2010000);
INSERT INTO t_category (id, name, description)
  VALUES (3000000, 'transfer', '');


-- define: t_repeat_payment
CREATE TABLE t_repeat_payment (
  id IDENTITY NOT NULL PRIMARY KEY,
  category_id BIGINT,
  amount INT NOT NULL,
  period INT NOT NULL,
  period_unit INT NOT NULL,
  auto_approve BOOLEAN DEFAULT false NOT NULL,
  FOREIGN KEY (category_id)
    REFERENCES t_category (id)
);



-- define: t_transaction
CREATE TABLE t_transaction (
  id IDENTITY NOT NULL PRIMARY KEY,
  account_id BIGINT NOT NULL,
  _date DATE NOT NULL,
  category_id BIGINT NOT NULL,
  debit INT NOT NULL,
  credit INT NOT NULL,
  is_approved BOOLEAN DEFAULT true NOT NULL,
  repeat_payment_id BIGINT,
  from_or_to BIGINT,
  FOREIGN KEY (category_id)
    REFERENCES t_category (id),
  FOREIGN KEY (account_id)
    REFERENCES t_account (id),
  FOREIGN KEY (repeat_payment_id)
    REFERENCES t_repeat_payment (id),
  FOREIGN KEY (from_or_to)
    REFERENCES t_transaction (id)
);



-- define: t_investment
CREATE TABLE t_investment (
  id IDENTITY NOT NULL PRIMARY KEY,
  transaction_id BIGINT NOT NULL,
  is_buy_or_sell BOOLEAN NOT NULL,
  price INT NOT NULL,
  quantity INT NOT NULL,
  amount INT NOT NULL,
  related_id BIGINT,
  FOREIGN KEY (transaction_id)
    REFERENCES t_transaction (id),
  FOREIGN KEY (related_id)
    REFERENCES t_investment (id)
);



-- define: t_receipt_item
CREATE TABLE t_receipt_item (
  id IDENTITY NOT NULL PRIMARY KEY,
  transaction_id BIGINT NOT NULL,
  item_name VARCHAR(50) NOT NULL,
  price INT,
  quantity INT,
  amount INT NOT NULL,
  FOREIGN KEY (transaction_id)
    REFERENCES t_transaction (id)
);



-- define: t_asset
CREATE TABLE t_asset (
  id IDENTITY NOT NULL PRIMARY KEY,
  transaction_id BIGINT NOT NULL,
  item_name VARCHAR(50),
  item_description VARCHAR(1000),
  depreciation_rate INT,
  depreciation_period INT,
  depreciation_period_unit INT,
  FOREIGN KEY (transaction_id)
    REFERENCES t_transaction (id)
);



-- define: t_setting
CREATE TABLE t_setting (
  id IDENTITY NOT NULL PRIMARY KEY,
  key VARCHAR(100) NOT NULL UNIQUE,
  value VARCHAR
);



