package cn.sh.fang.chenance.data.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//@Transactional(propagation=Propagation.MANDATORY)
//@Transactional
public abstract class BaseService {

	protected EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

	public EntityManager getEntityManager() {
		return em;
	}
    
}
