

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import cn.sh.fang.chinance.data.entity.Account;

public class DBConnTest {
	EntityManagerFactory factory;
	EntityManager manager;

	/** Creates a new instance of Main */
	public DBConnTest() {
	}

	/** 初期化処理 */
	public void init() {
		/*
	      <property name="hibernate.connection.url" value="jdbc:h2:tcp://localhost/test"/>
	      */
		String filepath = "C:/Users/Wencheng/workspace/gfp swt jpa/h2db/db";
		HashMap<String,String> props = new HashMap<String,String>();
		props.put("hibernate.connection.url", "jdbc:h2:"+filepath);
		
		factory = Persistence.createEntityManagerFactory("gfp-swt-jpa", props);
		manager = factory.createEntityManager();
	}

	/** 終了処理 */
	private void shutdown() {
		manager.close();
		factory.close();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		DBConnTest main = new DBConnTest();
		main.init();
		try {
			main.create();
		} catch (RuntimeException ex) {
			ex.printStackTrace();
		} finally {
			main.shutdown();
		}
	}

	/**
	 * オブジェクトの生成と永続化
	 */
	private void create() {
		System.out.println("2つのPersonオブジェクトを永続化中");
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		try {
			Account person1 = new Account();
			person1.setName("Takaaki SUGIYAMA");
			manager.persist(person1);
			Account person2 = new Account();
			person2.setName("Daichi GOTO");
			manager.persist(person2);

			transaction.commit();
		} catch (Exception ex) {
			transaction.rollback();
		}
		System.out.println("2つのPersonオブジェクトを永続化しました.");
	}

}
