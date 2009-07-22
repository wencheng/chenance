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
        Query query = em.createQuery("SELECT e FROM Account e WHERE is_deleted = 0 ORDER BY id");
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

	public boolean isUsableName(String name) {
		// FIXME change is_deleted to part of PK 
        //Query query = em.createQuery("SELECT e FROM Account e WHERE name = ? AND is_deleted = 0");
		Query query = em.createQuery("SELECT e FROM Account e WHERE name = ?");
        query.setParameter(1, name);
        return query.getResultList().isEmpty();
	}

}
