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
