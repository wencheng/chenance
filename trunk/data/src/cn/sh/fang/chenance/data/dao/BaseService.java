/*
 * Copyright 2008 Wencheng FANG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sh.fang.chenance.data.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

//@Transactional(propagation=Propagation.MANDATORY)
//@Transactional
public abstract class BaseService {

	static Logger LOG = Logger.getLogger(BaseService.class);

	static EntityManagerFactory factory;
	static protected EntityManager em;
	static EntityTransaction t;
	public static String filepath = System.getProperty("user.home") + "/chenance.db";
	public static String jdbcUrl = "jdbc:sqlite:" + filepath;
	public static String driverClass = "org.sqlite.JDBC";
	
	static boolean needMigrate = false;

	static Connection conn;

	public BaseService() {
		if (em == null) {
			LOG.debug("creating em " + this);
			em = factory.createEntityManager();
			em.setFlushMode(FlushModeType.AUTO);
			t = em.getTransaction();
			t.begin();
		}
	}

	public static void init() throws SQLException {
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			// ignore
		}

		if (new File(filepath).exists() == false ) {
			LOG.warn("data file not exists, start creating table ...");

			conn = DriverManager.getConnection(jdbcUrl);
			createTable();
		} else {
			conn = DriverManager.getConnection(jdbcUrl);
		}

		if ( needMigrate ) {
			LOG.warn("merging old data ...");
			migrateData();
			
			// backup
			new File(System.getProperty("user.home") + "/chenance/").renameTo(new File(System.getProperty("user.home") + "/chenance.bak/"));

			LOG.warn("migration finished!");
		}
		
		String oldVer = getLocalDataVersion();
		String newVer = getCurrentDataVersion();
		if (Float.valueOf(oldVer) < Float.valueOf(newVer)) {
			try {
				FileUtils.copyFile(new File(filepath), new File(filepath + ".bak"));
			} catch (IOException e) {
				LOG.fatal("Backuping database failed", e);
				throw new SQLException("Backuping database failed", e);
			}
			updateData(oldVer, newVer);
		}

		try {
			conn.close();
		} catch (SQLException e) {
			LOG.warn(e);
		}
		
		HashMap<String, String> props = new HashMap<String, String>();
		props.put("hibernate.connection.driver_class", driverClass);
		props.put("hibernate.connection.url", jdbcUrl);
		props.put("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect");
		factory = Persistence
				.createEntityManagerFactory("chenance-data", props);
	}

	private static void migrateData() throws SQLException {
		String csv;
		try {
			Statement stmt = conn.createStatement();
			
			csv = readAsString(new FileInputStream(System.getProperty("user.home") + "/chenance/account.csv"), "UTF-8");
			String[] lines = csv.split("\n");
			LOG.debug(lines);

			stmt.execute("delete from t_account");
			
			for (String s : lines) {
				if ( s == lines[0] ) {
					continue;
				}
				LOG.debug(s);
				String sql = "insert into t_account values(";
				
				String[] datas = s.split(",");
				String[] datas2 = new String[datas.length+1];
				for (int i = 0; i < datas.length; i++) {
					datas2[i<=10?i:i+1] = datas[i];
				}
				datas = datas2;
				datas[11] = "31";
				for (int i = 0; i < datas.length; i++) {
					if (datas[i].equals("")) {
						sql += "null,";
					} else if ( i == 12 || i == 13 ) {
						sql += String.valueOf(new SimpleDateFormat("\"yyyy-MM-dd HH:mm:ss.SSS\"").parse(datas[i]).getTime());
						sql += ",";
					} else if ( i == 15 ) {
						sql += datas[i].equals("\"FALSE\"")?"0":"1";
					} else if (i == 0 || (i >= 7 && i <= 10 )) {
						sql += datas[i].replaceAll("\"", "") + ",";
					} else {
						sql += datas[i] + ",";
					}
				}
				
				sql += ")";
				LOG.debug(sql);
				stmt.execute(sql);
			}
			stmt.execute("update sqlite_sequence set seq = (select max(id) from t_account) where name = 't_account'");

			csv = readAsString(new FileInputStream(System.getProperty("user.home") + "/chenance/category.csv"), "UTF-8");
			lines = csv.split("\n");
			LOG.debug(lines);
			stmt.execute("delete from t_category");
			for (String s : lines) {
				if ( s == lines[0] ) {
					continue;
				}
				LOG.debug(s);
				String sql = "insert into t_category values(";
				
				String[] datas = s.split(",");
				for (int i = 0; i < datas.length; i++) {
					if (datas[i].equals("")) {
						sql += "null,";
					} else if ( i == 5 || i == 6 ) {
						sql += String.valueOf(new SimpleDateFormat("\"yyyy-MM-dd HH:mm:ss.SSS\"").parse(datas[i]).getTime());
						sql += ",";
					} else if ( i == 8 ) {
						sql += datas[i].equals("\"FALSE\"")?"0":"1";
					} else if (i == 0 || i == 1 || i == 4 || i == 5 ) {
						sql += datas[i].replaceAll("\"", "") + ",";
					} else {
						sql += datas[i] + ",";
					}
				}
				
				sql += ")";
				LOG.debug(sql);
				stmt.execute(sql);
			}
			stmt.execute("update sqlite_sequence set seq = (select max(id) from t_category) where name = 't_category'");

			csv = readAsString(new FileInputStream(System.getProperty("user.home") + "/chenance/transaction.csv"), "UTF-8");
			lines = csv.split("\n");
			LOG.debug(lines);
			stmt.execute("delete from t_transaction");
			for (String s : lines) {
				if ( s == lines[0] ) {
					continue;
				}
				LOG.debug(s);
				String sql = "insert into t_transaction values(";
				
				String[] datas = s.split(",");
				for (int i = 0; i < datas.length; i++) {
					if (datas[i].equals("")) {
						sql += "null,";
					} else if ( i == 2 || i == 11 || i == 12 ) {
						sql += String.valueOf(new SimpleDateFormat("\"yyyy-MM-dd HH:mm:ss.S\"").parse(datas[i]).getTime());
						sql += ",";
					} else if ( i == 14 ) {
						sql += datas[i].equals("\"FALSE\"")?"0":"1";
					} else if (i == 8 ) { 
						sql += datas[i].equals("\"FALSE\"")?"0,":"1,";
					} else if (i >= 0 && i <= 10 ) {
						sql += datas[i].replaceAll("\"", "") + ",";
					} else {
						sql += datas[i] + ",";
					}
				}
				
				sql += ")";
				LOG.debug(sql);
				stmt.execute(sql);
			}
			stmt.execute("update sqlite_sequence set seq = (select max(id) from t_transaction) where name = 't_transaction'");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getCurrentDataVersion() {
		InputStream as = BaseService.class.getResourceAsStream("/META-INF/VERSION");
		if ( as == null ) {
			LOG.debug("impl-ver: 0");
			return "0";
		} else {
			BufferedReader r = new BufferedReader(
				new InputStreamReader(as));
			String ver = null;
			try {
				ver = r.readLine();
			} catch (IOException e) {
				LOG.error(e);
			}
			LOG.debug("impl-ver:" + ver);
			return ver;
		}
	}

	private static String getLocalDataVersion() throws SQLException {
		String sql = "select key, value from t_setting where key = 'chenance.data.version'";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			return rs.getString(2);
		} else {
			return "99999";
		}
	}
	
	static String[] vers = { "1.0", "1.1", "1.2", "1.3", "1.4" };

	private static void updateData(String oldVer, String newVer)
			throws SQLException {
		// "select key, value from t_setting where key = 'chenance.version'";
		LOG.warn("Updating database from version " + oldVer + " to " + newVer);

		ArrayList<String> a = new ArrayList<String>();
		org.apache.commons.collections.CollectionUtils.addAll(a, vers);
		int i = a.indexOf(oldVer);

		for (int j = i; j < vers.length - 1; j++) {
			execUpdateSql(vers[j], vers[j + 1]);
		}

		try {
			conn.commit();
		} catch (SQLException e) {
			LOG.error("commit update failed", e);
		}

		updateVersion(newVer);

		LOG.warn("Updating database finished");
	}

	protected static void execUpdateSql(String from, String to)
			throws SQLException {
		LOG.warn("Updating database from version " + from + " to " + to);

		try {
			InputStream is = BaseService.class
			.getResourceAsStream("/sql/upgrades/" + from + "-" + to
					+ ".sql");
			
			if (is == null) {
				LOG.info("no database changes from " + from +
						" to " + to);
				return;
			}
			
			String sql = null;
			sql = readAsString(is, "Shift-JIS");
			LOG.debug(sql);

			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (IOException e) {
			LOG.error(e);
		}
	}

	private static void updateVersion(String ver) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("update t_setting set value = '" + ver
				+ "' where key = 'chenance.data.version'");
		LOG.warn("Updated to " + ver);
	}

	public static void createTable() throws SQLException {
		Statement stmt = conn.createStatement();

		try {
			String sql;
			sql = readAsString(BaseService.class
					.getResourceAsStream("/sql/db.sql"), "Shift-JIS");
			String[] sqls = sql.split(";");

			for (String s : sqls) {
				if ( s == sqls[sqls.length-1] ) {
					break;
				}
				LOG.debug(s);
				stmt.execute(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				LOG.warn(e);
			}
		}
	}

	public static String readAsString(InputStream is, String encoding)
			throws IOException {
		StringBuffer buf = new StringBuffer();
		InputStreamReader in = new InputStreamReader(is, encoding);
		BufferedReader r = new BufferedReader(in);
		try {
			for (String s = r.readLine(); s != null; s = r.readLine()) {
				buf.append(s).append('\n');
			}
			return buf.toString();
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	 * @PersistenceContext public void setEntityManager(EntityManager arg) { em
	 * = arg; }
	 * 
	 * public EntityManager getEntityManager() { return em; }
	 */

	public static void shutdown() {
		t.commit();
		em.close();
		factory.close();
	}

	public static void commit() {
		t.commit();
	}

}
