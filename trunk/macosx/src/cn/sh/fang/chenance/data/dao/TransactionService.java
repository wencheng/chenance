package cn.sh.fang.chenance.data.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.persistence.Query;

import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.data.entity.Transaction;

/**
 * 's DAO
 */
public class TransactionService extends BaseService {

    @SuppressWarnings("unchecked")
	public List<Transaction> findAll() {
        Query query = em.createQuery("SELECT e FROM Transaction e WHERE is_deleted = 0");
        return query.getResultList();
    }

    public void save(Transaction entity) {
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
        Transaction entity = find(id);
        if (entity != null) {
            //em.remove(entity);
            entity.setDeleted(true);
            entity.setUpdater(updater);
            em.merge(entity);
        }
    }

    public Transaction find(Integer id) {
        return em.find(Transaction.class, id);
    }

	public List<Transaction> find(Account account) {
        Query query = em.createQuery("SELECT e FROM Transaction e WHERE account.id = ? AND is_deleted = 0");
        query.setParameter(1, account.getId());
        return query.getResultList();
	}

}
