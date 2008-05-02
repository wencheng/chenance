ALTER TABLE t_transaction_breakdown ADD COLUMN (
  insert_datetime DATETIME NOT NULL,
  update_datetime TIMESTAMP NOT NULL,
  updater VARCHAR(50) NOT NULL,
  is_deleted BOOLEAN NOT NULL
);
