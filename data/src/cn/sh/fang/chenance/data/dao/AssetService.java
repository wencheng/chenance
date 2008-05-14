package cn.sh.fang.chenance.data.dao;

import java.util.List;
import java.util.Date;
import javax.persistence.Query;
import cn.sh.fang.chenance.data.entity.Asset;

/**
 * 's DAO
 */
public class AssetService extends BaseService {

    @SuppressWarnings("unchecked")
	public List<Asset> findAll() {
        Query query = em.createQuery("SELECT e FROM Asset e WHERE is_deleted = 0");
        return query.getResultList();
    }

    public void save(Asset entity) {
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
        Asset entity = find(id);
        if (entity != null) {
            //em.remove(entity);
            entity.setDeleted(true);
            entity.setUpdater(updater);
            em.merge(entity);
        }
    }

    public Asset find(Integer id) {
        return em.find(Asset.class, id);
    }

}
