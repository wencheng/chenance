ALTER TABLE t_transaction ADD insert_datetime DATETIME DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_transaction ADD update_datetime TIMESTAMP DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_transaction ADD updater VARCHAR(50) DEFAULT '' NOT NULL;
ALTER TABLE t_transaction ADD is_deleted BOOLEAN DEFAULT false NOT NULL;
