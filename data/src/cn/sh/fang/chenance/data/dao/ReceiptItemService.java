package cn.sh.fang.chenance.data.dao;

import java.util.List;
import java.util.Date;
import javax.persistence.Query;
import cn.sh.fang.chenance.data.entity.ReceiptItem;

/**
 * 's DAO
 */
public class ReceiptItemService extends BaseService {

    @SuppressWarnings("unchecked")
	public List<ReceiptItem> findAll() {
        Query query = em.createQuery("SELECT e FROM ReceiptItem e WHERE is_deleted = 0");
        return query.getResultList();
    }

    public void save(ReceiptItem entity) {
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
        ReceiptItem entity = find(id);
        if (entity != null) {
            //em.remove(entity);
            entity.setDeleted(true);
            entity.setUpdater(updater);
            em.merge(entity);
        }
    }

    public ReceiptItem find(Integer id) {
        return em.find(ReceiptItem.class, id);
    }

}
