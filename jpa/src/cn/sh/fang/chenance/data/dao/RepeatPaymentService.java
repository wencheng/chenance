package cn.sh.fang.chenance.data.dao;

import java.util.List;
import java.util.Date;
import javax.persistence.Query;
import cn.sh.fang.chenance.data.entity.RepeatPayment;

/**
 * 's DAO
 */
public class RepeatPaymentService extends BaseService {

    @SuppressWarnings("unchecked")
	public List<RepeatPayment> findAll() {
        Query query = em.createQuery("SELECT e FROM RepeatPayment e WHERE is_deleted = 0");
        return query.getResultList();
    }

    public void save(RepeatPayment entity) {
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
        RepeatPayment entity = find(id);
        if (entity != null) {
            //em.remove(entity);
            entity.setDeleted(true);
            entity.setUpdater(updater);
            em.merge(entity);
        }
    }

    public RepeatPayment find(Integer id) {
        return em.find(RepeatPayment.class, id);
    }

}
