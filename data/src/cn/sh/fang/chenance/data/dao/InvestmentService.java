package cn.sh.fang.chenance.data.dao;

import java.util.List;
import java.util.Date;
import javax.persistence.Query;
import cn.sh.fang.chenance.data.entity.Investment;

/**
 * 's DAO
 */
public class InvestmentService extends BaseService {

    @SuppressWarnings("unchecked")
	public List<Investment> findAll() {
        Query query = em.createQuery("SELECT e FROM Investment e WHERE is_deleted = 0");
        return query.getResultList();
    }

    public void save(Investment entity) {
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
        Investment entity = find(id);
        if (entity != null) {
            //em.remove(entity);
            entity.setDeleted(true);
            entity.setUpdater(updater);
            em.merge(entity);
        }
    }

    public Investment find(Integer id) {
        return em.find(Investment.class, id);
    }

}
