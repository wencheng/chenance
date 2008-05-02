package cn.sh.fang.chenance.data.dao;

import java.util.List;
import java.util.Date;
import javax.persistence.Query;
import cn.sh.fang.chenance.data.entity.TransactionBreakdown;

/**
 * 's DAO
 */
public class TransactionBreakdownService extends BaseService {

    @SuppressWarnings("unchecked")
	public List<TransactionBreakdown> findAll() {
        Query query = em.createQuery("SELECT e FROM TransactionBreakdown e WHERE is_deleted = 0");
        return query.getResultList();
    }

    public void save(TransactionBreakdown entity) {
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
        TransactionBreakdown entity = find(id);
        if (entity != null) {
            //em.remove(entity);
            entity.setDeleted(true);
            entity.setUpdater(updater);
            em.merge(entity);
        }
    }

    public TransactionBreakdown find(Integer id) {
        return em.find(TransactionBreakdown.class, id);
    }

}
