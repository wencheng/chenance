package cn.sh.fang.chenance.data.dao;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

//@Transactional(propagation=Propagation.MANDATORY)
//@Transactional
public abstract class BaseService {

	static EntityManagerFactory factory;
	static protected EntityManager em;
	
	static {
		String filepath = "C:/Users/Wencheng/workspace/jpa/h2db/db";
		HashMap<String,String> props = new HashMap<String,String>();
		props.put("hibernate.connection.url", "jdbc:h2:"+filepath);
		
		factory = Persistence.createEntityManagerFactory("chenance-data", props);
	}
	
	public BaseService() {
		if ( em == null ) {
			em = factory.createEntityManager();
		}
	}

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

	public EntityManager getEntityManager() {
		return em;
	}
    
}
