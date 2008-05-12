package cn.sh.fang.chenance.data.dao;

import java.util.List;
import java.util.Date;
import javax.persistence.Query;
import cn.sh.fang.chenance.data.entity.Account;

/**
 * 's DAO
 */
public class AccountService extends BaseService {

    @SuppressWarnings("unchecked")
	public List<Account> findAll() {
        Query query = em.createQuery("SELECT e FROM Account e WHERE is_deleted = 0");
        return query.getResultList();
    }

    public void save(Account entity) {
        if (entity.getId() == null) {
            // new
        	entity.setInsertDatetime(new Date());
            em.persist(entity);
        } else {
            // update
            em.merge(entity);
        }
    }

    public void remove(Integer id, String updater) {
        Account entity = find(id);
        if (entity != null) {
            //em.remove(entity);
            entity.setDeleted(true);
            entity.setUpdater(updater);
            em.merge(entity);
        }
    }

    public Account find(Integer id) {
        return em.find(Account.class, id);
    }

}
