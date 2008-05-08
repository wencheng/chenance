ALTER TABLE t_asset ADD insert_datetime DATETIME DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_asset ADD update_datetime TIMESTAMP DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_asset ADD updater VARCHAR(50) DEFAULT '' NOT NULL;
ALTER TABLE t_asset ADD is_deleted BOOLEAN DEFAULT false NOT NULL;
