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

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import cn.sh.fang.chenance.data.entity.Breakdown;
import cn.sh.fang.chenance.data.entity.Transaction;

/**
 * 's DAO
 */
public class BreakdownService extends BaseService {

    @SuppressWarnings("unchecked")
	public List<Breakdown> findAll() {
        Query query = em.createQuery("SELECT e FROM Breakdown e WHERE is_deleted = 0");
        return query.getResultList();
    }

    public void save(Breakdown entity) {
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
        Breakdown entity = find(id);
        if (entity != null) {
            //em.remove(entity);
            entity.setDeleted(true);
            entity.setUpdater(updater);
            em.merge(entity);
        }
    }

    public Breakdown find(Integer id) {
        return em.find(Breakdown.class, id);
    }

	public List<Breakdown> findAll(Transaction t) {
        Query query = em.createQuery("SELECT e FROM Breakdown e WHERE is_deleted = 0 and transaction_id = ?");
        query.setParameter(1, t.getId());
        return query.getResultList();
	}

}
