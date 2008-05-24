package cn.sh.fang.chenance.data.dao;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

import cn.sh.fang.chenance.MainWindow;


public abstract class BaseService {

//	static Logger LOG = Logger.getLogger(BaseService.class);

	static protected EntityManager em;
	static EntityTransaction t;
	
//	public static EntityManagerFactory factory;
//
//	static {
//		HashMap<String, String> props = new HashMap<String, String>();
//		props.put("hibernate.connection.url", "jdbc:h2:" + SWTTest.filepath);
//		factory = Persistence
//				.createEntityManagerFactory("chenance-data", props);
//	}

	public BaseService() {
		if (em == null) {
			em = MainWindow.factory.createEntityManager();
//			em = factory.createEntityManager();
			em.setFlushMode(FlushModeType.AUTO);
			t = em.getTransaction();
			t.begin();
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

	public static void commit() {
		t.commit();
	}
}
