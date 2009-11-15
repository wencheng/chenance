/*
 * Copyright 2008 Wencheng FANG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sh.fang.chenance.data.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.data.entity.Category;

/**
 * 's DAO
 */
public class CategoryService extends BaseService {

    @SuppressWarnings("unchecked")
	public List<Category> findAll() {
        Query query = em.createQuery("SELECT e FROM Category e WHERE is_deleted = 0 ORDER BY code");
        return query.getResultList();
    }

    public void save(Category entity) {
        if (entity.getId() == null) {
            // new
            entity.setInsertDatetime(new Date());
            em.persist(entity);
            em.flush();
        } else {
            // update
            em.merge(entity);
        }
    }

    public void remove(Integer id, String updater) {
        Category entity = find(id);
        if (entity != null) {
            //em.remove(entity);
            entity.setParent(null);
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
        Query query = em.createQuery("SELECT e FROM Category e WHERE parent IS NULL AND is_deleted = 0");
        return query.getResultList();
	}

	/**
	 * 
	 * @return <code>List<Object[]></code> is a list of {Date, Long}
	 * from <= x < to
	 */
	public HashMap<Account, List<Object[]>> getDailyAmount(Integer id, Date from, Date to) {
		HashMap<Account, List<Object[]>> ret = new HashMap<Account, List<Object[]>>();

		List<Account> as = new AccountService().findAll();
		
		for ( Account a : as ) {
			Query query = em.createQuery("SELECT t.Date, sum(t.debit-t.credit) FROM Transaction t " +
					"WHERE account.id = ? AND category.id = ? " +
					"AND Date >= ? AND Date <= ? " +
					"AND isDeleted = 0 " +
					"GROUP BY Date");
			query.setParameter(1, a.getId());
			query.setParameter(2, id);
	        Calendar cal = Calendar.getInstance();
	        cal.setTime( from );
	        cal.set(Calendar.HOUR_OF_DAY, 0);
	        cal.set(Calendar.MINUTE, 0);
	        cal.set(Calendar.SECOND, 0);
	        cal.set(Calendar.MILLISECOND, 0);
	        query.setParameter(3, cal.getTime() );
	        cal.setTime( to );
	        cal.set(Calendar.HOUR_OF_DAY, 0);
	        cal.set(Calendar.MINUTE, 0);
	        cal.set(Calendar.SECOND, 0);
	        cal.set(Calendar.MILLISECOND, 0);
	        query.setParameter(4, cal.getTime() );
			
			List<Object[]> l = query.getResultList();
			for (int i = 0; i < l.size(); i++) {
				Object[] o = l.get(i);
				LOG.debug(o);
				
				if ( o[0] instanceof Long ) {
					o[0] = new Date((Long) o[0]);
				}
			}
			ret.put(a, l); 
			LOG.debug(ret.get(a));
		}

		return ret;
	}

	/**
	 * 
	 * @param id
	 * @param from
	 * @param to
	 * @return List<{"YYYYWW", Long}>
	 */
	public HashMap<Account,List<Object[]>> getWeeklyAmount(Integer id, Date from, Date to) {
		HashMap<Account, List<Object[]>> ret = new HashMap<Account, List<Object[]>>();

		List<Account> as = new AccountService().findAll();
		
		for ( Account a : as ) {
			Query query = em.createNativeQuery(
				"SELECT strftime('%Y%W', _date/1000,'unixepoch'), sum(t.debit - t.credit) " +
				"FROM t_transaction t WHERE account_id = ? AND category_id = ? " +
				"AND _date >= ? AND _date <= ? " +
				"AND is_deleted = 0 " +
				"GROUP BY strftime('%Y%W', _date/1000,'unixepoch')"
			);
			query.setParameter(1, a.getId());
			query.setParameter(2, id);
	        Calendar cal = Calendar.getInstance();
	        cal.setTime( from );
	        cal.set(Calendar.HOUR_OF_DAY, 0);
	        cal.set(Calendar.MINUTE, 0);
	        cal.set(Calendar.SECOND, 0);
	        cal.set(Calendar.MILLISECOND, 0);
	        query.setParameter(3, cal.getTime() );
	        cal.setTime( to );
	        cal.set(Calendar.HOUR_OF_DAY, 0);
	        cal.set(Calendar.MINUTE, 0);
	        cal.set(Calendar.SECOND, 0);
	        cal.set(Calendar.MILLISECOND, 0);
	        query.setParameter(4, cal.getTime() );

	        List<Object[]> l = query.getResultList();
			ret.put(a, l); 
			LOG.debug(ret.get(a));
		}
		
		return ret;
	}

	public HashMap<Account, List<Integer>> getTwoWeeklyAmount(Integer id, Date value, Date value2) {
		HashMap<Account, List<Integer>> ret = new HashMap<Account, List<Integer>>();

		List<Account> as = new AccountService().findAll();
		
		for ( Account a : as ) {
			Query query = em.createQuery(
				"SELECT (cast(strftime('%Y%W', _date/1000,'unixepoch')/2 as integer)+1)*2, sum(t.debit - t.credit) FROM t_transaction t WHERE account_id = ? AND category_id = ? AND is_deleted = 0"
				+ "GROUP BY (cast(strftime('%Y%W', _date/1000,'unixepoch')/2 as integer)+1)*2"
			);
			query.setParameter(1, a.getId());
			query.setParameter(2, id);
			ret.put(a, query.getResultList());
		}
		
		return ret;
	}

}
