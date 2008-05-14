ALTER TABLE t_repeat_payment ADD insert_datetime DATETIME DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_repeat_payment ADD update_datetime TIMESTAMP DEFAULT current_timestamp() NOT NULL;
ALTER TABLE t_repeat_payment ADD updater VARCHAR(50) DEFAULT '' NOT NULL;
ALTER TABLE t_repeat_payment ADD is_deleted BOOLEAN DEFAULT false NOT NULL;
