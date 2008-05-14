ALTER TABLE t_investment ADD insert_datetime DATETIME DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_investment ADD update_datetime TIMESTAMP DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_investment ADD updater VARCHAR(50) DEFAULT '' NOT NULL;
ALTER TABLE t_investment ADD is_deleted BOOLEAN DEFAULT false NOT NULL;
