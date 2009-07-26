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
	//public static String filepath = System.getProperty("user.home") + "/chenance.db";
	public static String filepath = System.getProperty("user.home") + "/chenance/db";
	//public static String jdbcUrl = "jdbc:sqlite:" + filepath;
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

	public static void init() {
		createTable();


		try {
			if (conn == null) {
				conn = DriverManager.getConnection(jdbcUrl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float oldVer = getLocalDataVersion();
		float newVer = getCurrentDataVersion();
		if ( oldVer < newVer ) {
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

	private static float getCurrentDataVersion() {
        Package pkg = BaseService.class.getPackage();
        String implVersion = pkg.getImplementationVersion();
        LOG.debug("impl-ver:" + implVersion);
        if ( implVersion != null ) {
        	return Float.valueOf(implVersion);
        } else {
        	return 0;
        }
	}

	private static float getLocalDataVersion() {
		String sql = "select key, value from t_setting where key = 'chenance.data.version'"; 
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getFloat(2);
			} else {
				return Integer.MAX_VALUE;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	private static void updateData(float oldVer, float newVer) {
//		"select key, value from t_setting where key = 'chenance.version'";
		LOG.warn("Updating database from version " + oldVer + " to " + newVer);
		
		Method[] ms = BaseService.class.getDeclaredMethods();
		for (Method m : ms) {
			LOG.debug("has " + m.getName());
			if (m.getName().startsWith("doUpdate" + String.valueOf(oldVer).replace('.', '_'))) {
				LOG.debug("Invoke " + m.getName());

				try {
					m.invoke(null, new Object[]{});
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LOG.warn("Updating database finished");
	}
	
	protected static void doUpdate1_0to1_1() throws SQLException {
		LOG.warn("Updating database from version 1.0 to 1.1");
		
		Statement stmt = conn.createStatement();
		int i = stmt.executeUpdate(
				"update t_transaction set _date = formatdatetime(_date,'yyyy-MM-dd 00:00:00')");
		LOG.warn(i + " record(s) of t_transaction updated.");

		updateVersion(stmt, "1.1");	
	}

	private static void updateVersion(Statement stmt, String ver) throws SQLException {
		 stmt.executeUpdate("update t_setting set value = '" + ver + "' where key = 'chenance.data.version'");
		 LOG.warn("Updated to " + ver);
	}

	public static void createTable() {
		if (new File(filepath+".data.db").exists()) {
			return;
		}
		LOG.warn("data file not exists");

		try {
			//Class.forName("org.sqlite.JDBC");
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			// ignore
		}

		try {
			conn = DriverManager.getConnection(jdbcUrl);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO error message
			e.printStackTrace();
		}

		String basedir = "/sql/";
		String[] files = new String[] { "db.sql", "common-columns/Account.sql",
				"common-columns/Category.sql",
				"common-columns/RepeatPayment.sql",
				"common-columns/Transaction.sql",
				"common-columns/Asset.sql", 
				"common-columns/Investment.sql",
				"common-columns/Breakdown.sql",
				"common-columns/Loan.sql",
				 };
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO LOG thisp
			}
		}
	}

	public static String readAsString(InputStream is, String encoding)
			throws IOException {
		StringBuffer buf = new StringBuffer();
		InputStreamReader in = new InputStreamReader(is,encoding);
		try {
			for (int c = in.read(); c != -1; c = in.read()) {
				buf.append((char) c);
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
	 * @PersistenceContext public void setEntityManager(EntityManager arg) { em =
	 * arg; }
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
