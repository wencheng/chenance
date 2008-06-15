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
    	int diff;
        if (entity.getId() == null) {
            // new
            entity.setInsertDatetime(new Date());
            diff = calcBalance(entity);
            em.persist(entity);
        } else {
            // update
    		diff = calcOldBalance(entity);
    		LOG.debug( "balance: " + entity.getBalance() );
            em.merge(entity);
        }
        
        updateBalance( entity, diff );
        Account a = entity.getAccount();
        a.setCurrentBalance( a.getCurrentBalance() + diff );
    }

    private int calcBalance(Transaction t) {
    	Query query;
    	query = em.createNativeQuery( "SELECT balance FROM t_transaction WHERE account_id = ? AND _date < ? AND is_deleted = 0 ORDER BY _date DESC" );
    	query.setParameter(1, t.getAccount().getId());
    	query.setParameter(2, t.getDate());
    	List<Number> l = query.getResultList();

    	int i;
    	if ( l.size() <= 0 ) {
    		i = 0;
    	} else {
    		i = l.get(0).intValue();
    	}
    	LOG.debug( String.format( "balance before %s: %d", t.getDate(), i ) );
    	
    	i += t.getCredit() - t.getDebit();
    	t.setBalance( i );

    	return t.getCredit() - t.getDebit();
    }

    /**
     * 
     * @param t
     * @return difference between before and after
     */
    private int calcOldBalance(Transaction t) {
    	if ( t.getId() == null ) {
    		throw new IllegalArgumentException();
    	}
    	
    	Query query;

    	query = em.createNativeQuery( "SELECT debit, credit FROM t_transaction WHERE id = ?" )
    		.setParameter(1, t.getId());
    	Object[] old = (Object[]) query.getSingleResult();
    	LOG.debug( "old: " + old[1].getClass() );

    	int i = t.getCredit() - t.getDebit() - ((Integer)old[1] - (Integer)old[0]);
    	t.setBalance( t.getBalance() + i );
    	return i;
    }

    public void updateBalance(Transaction a, int diff) {
    	Query query;
    	query = em.createQuery( "UPDATE Transaction SET balance = balance + ? WHERE account.id = ? AND _date > ? AND is_deleted = 0" );
   		query.setParameter( 1, diff );
    	query.setParameter(2, a.getAccount().getId());
    	query.setParameter(3, a.getDate());
    	query.executeUpdate();
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
    
	public List<Transaction> find(Date date, Account account) {
        Query query = em.createQuery("SELECT e FROM Transaction e WHERE account.id = ? AND _date >= ? AND _date < ? AND is_deleted = 0");
        query.setParameter(1, account.getId());
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        query.setParameter(2, cal.getTime() );
        cal.add(Calendar.DATE, 1);
        query.setParameter(3, cal.getTime() );
        return query.getResultList();
	}

}
