ALTER TABLE t_transaction_breakdown ADD insert_datetime DATETIME DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_transaction_breakdown ADD update_datetime TIMESTAMP DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_transaction_breakdown ADD updater VARCHAR(50) DEFAULT '' NOT NULL;
ALTER TABLE t_transaction_breakdown ADD is_deleted BOOLEAN DEFAULT false NOT NULL;
