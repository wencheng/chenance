ALTER TABLE t_category ADD insert_datetime DATETIME DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_category ADD update_datetime TIMESTAMP DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_category ADD updater VARCHAR(50) DEFAULT '' NOT NULL;
ALTER TABLE t_category ADD is_deleted BOOLEAN DEFAULT false NOT NULL;
