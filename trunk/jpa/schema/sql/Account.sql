ALTER TABLE t_account ADD insert_datetime DATETIME DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_account ADD update_datetime TIMESTAMP DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_account ADD updater VARCHAR(50) DEFAULT '' NOT NULL;
ALTER TABLE t_account ADD is_deleted BOOLEAN DEFAULT false NOT NULL;
