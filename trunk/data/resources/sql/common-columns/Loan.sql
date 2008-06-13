ALTER TABLE t_loan ADD insert_datetime DATETIME DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_loan ADD update_datetime TIMESTAMP DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_loan ADD updater VARCHAR(50) DEFAULT '' NOT NULL;
ALTER TABLE t_loan ADD is_deleted BOOLEAN DEFAULT false NOT NULL;
