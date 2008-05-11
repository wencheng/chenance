package cn.sh.fang.chenance.data.dao;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
	static String filepath = System.getProperty("user.home") + "/chenance/db";

	public BaseService() {
		if ( factory == null ) {
			// TODO allow user to open their own db
			HashMap<String, String> props = new HashMap<String, String>();
			props.put("hibernate.connection.url", "jdbc:h2:" + filepath);
			factory = Persistence
					.createEntityManagerFactory("chenance-data", props);
		}
		
		if (em == null) {
			em = factory.createEntityManager();
			em.setFlushMode(FlushModeType.AUTO);
			t = em.getTransaction();
			t.begin();
		}
	}

	public static void createTable() {
		if ( new File(filepath+".data.db").exists() ) {
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
			conn = DriverManager.getConnection("jdbc:h2:" + filepath + ";USER=sa");
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

		String basedir = "cn/sh/fang/chenance/data/sql/";
		String[] files = new String[] {
				"db.sql",
				"common-columns/Account.sql",
				"common-columns/Asset.sql",
				"common-columns/Category.sql",
				"common-columns/Investment.sql",
				"common-columns/ReceiptItem.sql",
				"common-columns/RepeatPayment.sql",
				"common-columns/Transaction.sql",
				};
		try {
			for (String f : files) {
				URL url = BaseService.class.getClassLoader().getResource(
						basedir + f);
				stmt.execute(FileUtils.readFileToString(new File(url.toURI()),"Shift-JIS"));
			}
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
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
