package cn.sh.fang.chenance.data.dao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;

import org.apache.log4j.Logger;

//@Transactional(propagation=Propagation.MANDATORY)
//@Transactional
public abstract class BaseService {

	static Logger LOG = Logger.getLogger(BaseService.class);

	static EntityManagerFactory factory;
	static protected EntityManager em;
	static EntityTransaction t;
	public static String filepath = System.getProperty("user.home") + "/chenance/db";
	
	public BaseService() {
		if (em == null) {
			em = factory.createEntityManager();
			em.setFlushMode(FlushModeType.AUTO);
			t = em.getTransaction();
			t.begin();
		}
	}

	public static void createTable() {
		if (new File(filepath + ".data.db").exists()) {
			return;
		}
		LOG.warn("data file not exists");

		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			// ignore
		}

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection("jdbc:h2:" + filepath
					+ ";USER=sa");
		} catch (SQLException e) {
			// TODO error message
			e.printStackTrace();
			return;
		}
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO error message
			e.printStackTrace();
		}

		String basedir = "/cn/sh/fang/chenance/data/sql/";
		String[] files = new String[] { "db.sql", "common-columns/Account.sql",
				"common-columns/Asset.sql", "common-columns/Category.sql",
				"common-columns/Investment.sql",
				"common-columns/ReceiptItem.sql",
				"common-columns/RepeatPayment.sql",
				"common-columns/Transaction.sql", };
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
			try {
				conn.close();
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

	public static void setFacotory(EntityManagerFactory factory2) {
		BaseService.factory = factory2;
	}
}
