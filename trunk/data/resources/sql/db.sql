/**
 * SQL genereted by Jiemamy
 * Create: 2008/05/12 17:52:11
 */

-- define: t_account
CREATE TABLE t_account (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE,
  description VARCHAR(200),
  bank_name VARCHAR(100),
  bank_branch VARCHAR(20),
  bank_account_no VARCHAR(20),
  bank_url VARCHAR(255),
  interest_rate INT,
  start_balance INT DEFAULT 0,
  current_balance INT DEFAULT 0 NOT NULL,
  currency INT DEFAULT 1,
  closing_day INT DEFAULT 1
  , insert_datetime LONG DEFAULT (strftime('%s','now')*1000),
  update_datetime LONG DEFAULT (strftime('%s','now')*1000),
  updater VARCHAR(50) DEFAULT '' NOT NULL,
  is_deleted BOOLEAN DEFAULT 0 NOT NULL
);

INSERT INTO t_account (id, name, description)
  VALUES (1, '������', '�ƒ��A���M��A��ʔ�Ȃ�');
INSERT INTO t_account (bank_name, id, name, description)
  VALUES (NULL, 2, '�H��', '�������H�ɏ��������');
INSERT INTO t_account (id, name, description, bank_name)
  VALUES (3, '��s����', '��s�c��������o���̗���', '����������s');


-- define: t_category
CREATE TABLE t_category (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  code BIGINT NOT NULL,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(200),
  parent_id BIGINT,
  insert_datetime LONG DEFAULT (strftime('%s','now')*1000),
  update_datetime LONG DEFAULT (strftime('%s','now')*1000),
  updater VARCHAR(50) DEFAULT '' NOT NULL,
  is_deleted BOOLEAN DEFAULT 0 NOT NULL,
  FOREIGN KEY (parent_id)
    REFERENCES t_category (id)
);

INSERT INTO t_category (id, name, description, parent_id, code)
  VALUES (1, 'Debit', '', NULL, 1000000);
INSERT INTO t_category (id, name, parent_id, description, code)
  VALUES (2, '�H��', 1, '���H�ɂ�����������', 1010000);
INSERT INTO t_category (id, name, parent_id, description, code)
  VALUES (3, '�O�H', 2, '�O�H�̏���', 1010100);
INSERT INTO t_category (id, name, description, parent_id, code)
  VALUES (4, 'Credit', '', NULL, 2000000);
INSERT INTO t_category (id, name, parent_id, description, code)
  VALUES (5, '���^', 4, '������{�[�i�X', 2010000);
INSERT INTO t_category (id, name, parent_id, description, code)
  VALUES (6, '���', 5, '�����o�΂̉�Ђ���', 2010100);
INSERT INTO t_category (id, name, description, code, parent_id)
  VALUES (7, 'Transfer', '', 3000000, NULL);


-- define: t_repeat_payment
CREATE TABLE t_repeat_payment (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  category_id BIGINT,
  amount INT NOT NULL,
  period INT NOT NULL,
  period_unit INT NOT NULL,
  auto_confirm BOOLEAN DEFAULT 0 NOT NULL,
  insert_datetime LONG DEFAULT (strftime('%s','now')*1000),
  update_datetime LONG DEFAULT (strftime('%s','now')*1000),
  updater VARCHAR(50) DEFAULT '' NOT NULL,
  is_deleted BOOLEAN DEFAULT 0 NOT NULL,
  FOREIGN KEY (category_id)
    REFERENCES t_category (id)
);



-- define: t_transaction
CREATE TABLE t_transaction (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  account_id BIGINT NOT NULL,
  _date long NOT NULL,
  category_id BIGINT NOT NULL,
  budget INT,
  debit INT NOT NULL,
  credit INT NOT NULL,
  balance INT NOT NULL,
  is_confirmed BOOLEAN DEFAULT true NOT NULL,
  repeat_payment_id BIGINT,
  from_or_to BIGINT,
  memo VARCHAR(100),
  insert_datetime LONG DEFAULT (strftime('%s','now')*1000),
  update_datetime LONG DEFAULT (strftime('%s','now')*1000),
  updater VARCHAR(50) DEFAULT '' NOT NULL,
  is_deleted BOOLEAN DEFAULT 0 NOT NULL,
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
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  transaction_id BIGINT NOT NULL,
  is_buy_or_sell BOOLEAN NOT NULL,
  price INT NOT NULL,
  quantity INT NOT NULL,
  amount INT NOT NULL,
  related_id BIGINT,
  insert_datetime LONG DEFAULT (strftime('%s','now')*1000),
  update_datetime LONG DEFAULT (strftime('%s','now')*1000),
  updater VARCHAR(50) DEFAULT '' NOT NULL,
  is_deleted BOOLEAN DEFAULT 0 NOT NULL,
  FOREIGN KEY (transaction_id)
    REFERENCES t_transaction (id),
  FOREIGN KEY (related_id)
    REFERENCES t_investment (id)
);



-- define: t_receipt_item
CREATE TABLE t_breakdown (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  transaction_id BIGINT NOT NULL,
  item_name VARCHAR(50) NOT NULL,
  category_id BIGINT,
  price INT,
  quantity INT,
  amount INT NOT NULL,
  insert_datetime LONG DEFAULT (strftime('%s','now')*1000),
  update_datetime LONG DEFAULT (strftime('%s','now')*1000),
  updater VARCHAR(50) DEFAULT '' NOT NULL,
  is_deleted BOOLEAN DEFAULT 0 NOT NULL,
  FOREIGN KEY (transaction_id)
    REFERENCES t_transaction (id)
);



-- define: t_asset
CREATE TABLE t_asset (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  transaction_id BIGINT NOT NULL,
  item_name VARCHAR(50),
  item_description VARCHAR(1000),
  depreciation_rate INT,
  depreciation_period INT,
  depreciation_period_unit INT,
  insert_datetime LONG DEFAULT (strftime('%s','now')*1000),
  update_datetime LONG DEFAULT (strftime('%s','now')*1000),
  updater VARCHAR(50) DEFAULT '' NOT NULL,
  is_deleted BOOLEAN DEFAULT 0 NOT NULL,
  FOREIGN KEY (transaction_id)
    REFERENCES t_transaction (id)
);



CREATE TABLE t_loan (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  transaction_id BIGINT NOT NULL,
  interest INT,
  interest_rate INT,
  insert_datetime LONG DEFAULT (strftime('%s','now')*1000),
  update_datetime LONG DEFAULT (strftime('%s','now')*1000),
  updater VARCHAR(50) DEFAULT '' NOT NULL,
  is_deleted BOOLEAN DEFAULT 0 NOT NULL,
  FOREIGN KEY (transaction_id)
    REFERENCES t_transaction (id)
);


-- define: t_setting
CREATE TABLE t_setting (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  key VARCHAR(100) NOT NULL UNIQUE,
  value VARCHAR
);

INSERT INTO t_setting (key, value)
  VALUES ('chenance.version', '${chenance.version}');
INSERT INTO t_setting (key, value)
  VALUES ('chenance.data.version', '${chenance.data.version}');

