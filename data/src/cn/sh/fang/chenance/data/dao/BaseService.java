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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

//@Transactional(propagation=Propagation.MANDATORY)
//@Transactional
public abstract class BaseService {

	static Logger LOG = Logger.getLogger(BaseService.class);

	static EntityManagerFactory factory;
	static protected EntityManager em;
	static EntityTransaction t;
	// public static String filepath = System.getProperty("user.home") +
	// "/chenance.db";
	public static String filepath = System.getProperty("user.home")
			+ "/chenance/db";
	// public static String jdbcUrl = "jdbc:sqlite:" + filepath;
	public static String jdbcUrl = "jdbc:h2:" + filepath + ";USER=sa";

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
			// Class.forName("org.sqlite.JDBC");
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			// ignore
		}

		createTable();

		try {
			if (conn == null) {
				conn = DriverManager.getConnection(jdbcUrl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String oldVer = getLocalDataVersion();
		String newVer = getCurrentDataVersion();
		if (Float.valueOf(oldVer) < Float.valueOf(newVer)) {
			updateData(oldVer, newVer);
		}

		try {
			conn.close();
		} catch (SQLException e) {
			LOG.warn(e);
		}

		HashMap<String, String> props = new HashMap<String, String>();
		props.put("hibernate.connection.url", jdbcUrl);
		factory = Persistence
				.createEntityManagerFactory("chenance-data", props);
	}

	private static String getCurrentDataVersion() {
		BufferedReader r = new BufferedReader(
				new InputStreamReader(BaseService.class.getResourceAsStream("/META-INF/VERSION")));
		String ver = null;
		try {
			ver = r.readLine();
		} catch (IOException e) {
			LOG.error(e);
		}
		LOG.debug("impl-ver:" + ver);
		return ver;
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

	static String[] vers = { "1.0", "1.1", "1.2" };

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		updateVersion(newVer);

		LOG.warn("Updating database finished");
	}

	protected static void execUpdateSql(String from, String to)
			throws SQLException {
		LOG.warn("Updating database from version " + from + " to " + to);

		Statement stmt = conn.createStatement();

		String sql = null;
		try {
			sql = readAsString(BaseService.class
					.getResourceAsStream("/sql/upgrades/" + from + "-" + to
							+ ".sql"), "Shift-JIS");
			LOG.debug(sql);

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
		if (new File(filepath + ".data.db").exists()) {
			return;
		}
		LOG.warn("data file not exists, start creating table ...");

		try {
			conn = DriverManager.getConnection(jdbcUrl);
		} catch (SQLException e) {
			throw e;
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			throw e;
		}

		String basedir = "/sql/";
		String[] files = new String[] { "db.sql", "common-columns/Account.sql",
				"common-columns/Category.sql",
				"common-columns/RepeatPayment.sql",
				"common-columns/Transaction.sql", "common-columns/Asset.sql",
				"common-columns/Investment.sql",
				"common-columns/Breakdown.sql", "common-columns/Loan.sql", };
		try {
			String sql;
			for (String f : files) {
				sql = readAsString(BaseService.class
						.getResourceAsStream(basedir + f), "Shift-JIS");
				LOG.debug(sql);
				stmt.execute(sql);
			}
			conn.commit();
		} catch (SQLException e) {
			throw e;
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
