package cn.sh.fang.chenance.data.dao;

import java.util.List;
import java.util.Date;
import javax.persistence.Query;
import cn.sh.fang.chenance.data.entity.Category;

/**
 * 's DAO
 */
public class CategoryService extends BaseService {

    @SuppressWarnings("unchecked")
	public List<Category> findAll() {
        Query query = em.createQuery("SELECT e FROM Category e WHERE is_deleted = 0");
        return query.getResultList();
    }

    public void save(Category entity) {
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
        Category entity = find(id);
        if (entity != null) {
            //em.remove(entity);
            entity.setDeleted(true);
            entity.setUpdater(updater);
            em.merge(entity);
        }
    }

    public Category find(Integer id) {
        return em.find(Category.class, id);
    }

	@SuppressWarnings("unchecked")
	public List<Category> getTops() {
        Query query = em.createQuery("SELECT e FROM Category e WHERE MOD(id,1000000) = 0 AND is_deleted = 0");
        return query.getResultList();
	}

}
