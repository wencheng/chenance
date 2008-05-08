ALTER TABLE t_receipt_item ADD insert_datetime DATETIME DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_receipt_item ADD update_datetime TIMESTAMP DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_receipt_item ADD updater VARCHAR(50) DEFAULT '' NOT NULL;
ALTER TABLE t_receipt_item ADD is_deleted BOOLEAN DEFAULT false NOT NULL;
