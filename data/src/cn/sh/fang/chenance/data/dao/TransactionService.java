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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.data.entity.Transaction;

/**
 * 's DAO
 */
public class TransactionService extends BaseService {
	
	static Logger LOG = Logger.getLogger(TransactionService.class);

    @SuppressWarnings("unchecked")
	public List<Transaction> findAll() {
        Query query = em.createQuery("SELECT e FROM Transaction e WHERE is_deleted = 0");
        return query.getResultList();
    }

    public void save(Transaction entity) {
    	updateBalance( entity );

    	if (entity.getId() == null) {
            // new
            entity.setInsertDatetime(new Date());
            calcBalance(entity);
            em.persist(entity);
        } else {
            // update
    		calcBalance(entity);
    		LOG.debug( "balance: " + entity.getBalance() );
            em.merge(entity);
            em.flush();
        }
    }

    private void calcBalance(Transaction t) {
    	// get latest balance
    	LOG.debug( String.format( "old balance date %s", t.getDate() ) );
    	
    	Query query;
    	query = em.createNativeQuery( "SELECT balance FROM t_transaction" +
    			" WHERE account_id = ? AND (_date < ? or (_date = ? and insert_datetime < ?)) AND is_deleted = 0" +
    			" ORDER BY _date DESC, insert_datetime DESC" );
    	query.setParameter(1, t.getAccount().getId());
    	query.setParameter(2, t.getDate());
    	query.setParameter(3, t.getDate());
    	query.setParameter(4, t.getInsertDatetime());
    	List<Integer> l = query.getResultList();
    	
    	int i;
    	if ( l.size() <= 0 ) {
    		i = 0;
    	} else {
    		i = l.get(0);
    	}
    	LOG.debug( String.format( "balance before %s: %d", t.getDate(), i ) );
    	
    	i += t.getCredit() - t.getDebit();
    	t.setBalance( i );
    }

    /**
     * 
     * @param t
     * @return difference between before and after
     */
    private int updateBalance(Transaction t) {
    	int currDiff = t.getCredit() - t.getDebit();
		int diff = 0;

		// update balances if _date is modified
    	if ( t.getId() != null ) {
    		Query query
    			= em.createNativeQuery( "SELECT debit, credit, _date || '' FROM t_transaction WHERE id = ?" )
    			.setParameter(1, t.getId());
    		Object[] oldTrans = (Object[]) query.getSingleResult();

    		Date olddate = new Date(Long.valueOf((String)oldTrans[2]));
    		int oldDiff = (Integer)oldTrans[1] - (Integer)oldTrans[0];
    		diff = currDiff - oldDiff;
    		
			LOG.debug( "_date after modify: " + t.getDate() );
			LOG.debug( "_date before modify: " + olddate );
			LOG.debug( "diff: " + diff );
			LOG.debug( "old diff: " + oldDiff );

			if ( t.getDate().compareTo(olddate) < 0 ) {
	    		// move to earlier
	    		// add with current balance
				LOG.debug("updating 1");
	    		updateBalance(t.getAccount().getId(), t.getDate(), t.getInsertDatetime(), olddate, currDiff);
	    	} else if ( t.getDate().compareTo(olddate) > 0 ) {
	    		// move to later
	    		// minus old balance
	    		LOG.debug("updating 2");
	    		updateBalance(t.getAccount().getId(), olddate, t.getInsertDatetime(), t.getDate(), -oldDiff);
	    	}
    	}

		// update future transactions' balances and account's balance if current balance is modified
    	if ( diff != 0 ) {
    		updateTransactionBalance( t, diff );
    	}

    	return diff;
    }

    /**
     * All transactions which is between
     * bdate + insert_datetime
     * and
     * edate + insert_datetime
     * will be updated.
     * 
     * @param accountId
     * @param bdate
     * @param edate
     * @param diff
     */
    public void updateBalance(Integer accountId, Date bdate, Date insertDate, Date edate, int diff) {
    	Query query;
    	query = em.createQuery( "UPDATE Transaction SET balance = balance + ?" +
    			" WHERE account.id = ?" +
    			" AND (_date > ? or (_date = ? and insert_datetime > ?))" +
    			" AND (_date < ? or (_date = ? and insert_datetime < ?))" +
    			" AND is_deleted = 0" );
   		query.setParameter(1, diff );
    	query.setParameter(2, accountId);
    	query.setParameter(3, bdate);
    	query.setParameter(4, bdate);
    	query.setParameter(5, insertDate);
    	query.setParameter(6, edate);
    	query.setParameter(7, edate);
    	query.setParameter(8, insertDate);
    	query.executeUpdate();
    }

    /**
     * All transactions which is later than <code>date</code> and <code>insertDate</code>
     * will be updated.
     * 
     * @param accountId
     * @param date
     * @param insertDate
     * @param diff
     */
    public void updateTransactionBalance(Transaction t, int diff) {
    	Query query;
    	query = em.createQuery( "UPDATE Transaction SET balance = balance + ? WHERE account.id = ?" +
    			" AND (_date > ? or (_date = ? and insert_datetime > ?)) AND is_deleted = 0" );
   		query.setParameter(1, diff );
    	query.setParameter(2, t.getAccount().getId());
    	query.setParameter(3, t.getDate());
    	query.setParameter(4, t.getDate());
    	query.setParameter(5, t.getInsertDatetime());
    	query.executeUpdate();

		// update account's balance
        Account a = t.getAccount();
        a.setCurrentBalance( a.getCurrentBalance() + diff );
        new AccountService().save(a);
    }

    public void remove(Integer id, String updater) {
        Transaction entity = find(id);
        if (entity != null) {
            updateTransactionBalance( entity, -(entity.getCredit() - entity.getDebit()) );

            entity.setDeleted(true);
            entity.setUpdater(updater);
            em.merge(entity);
        }
    }

    public Transaction find(Integer id) {
        return em.find(Transaction.class, id);
    }
    
	public List<Transaction> find(Account account, Date date) {
        return find( account, date, date );
	}

	/**
	 * Hour, minute, second will be ignored.
	 * 
	 * @param account
	 * @param bDate
	 * @param eDate
	 * @return bDate <= x < eDate
	 */
	public List<Transaction> find(Account account, Date bDate, Date eDate) {
		return find(account, bDate, eDate, true, true);
	}
	
	public List<Transaction> find(Account account, Date bDate, Date eDate,
			boolean showConfirmed, boolean showUnconfirmed) {
		if (!showConfirmed && !showUnconfirmed) {
			return new ArrayList<Transaction>();
		}

        Query query = em.createQuery("SELECT e FROM Transaction e " +
        		"WHERE account.id = ? AND _date >= ? AND _date < ? " +
        		((showConfirmed&&showUnconfirmed) ? "" :
        		(showConfirmed ? "AND is_confirmed = 1" : "") +
        		(showUnconfirmed ? "AND is_confirmed = 0" : "")) +
        		"AND is_deleted = 0" +
        		" ORDER BY _date, insert_datetime");
        query.setParameter(1, account.getId());
        Calendar cal = Calendar.getInstance();
        cal.setTime( bDate );
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        query.setParameter(2, cal.getTimeInMillis()/1000*1000 );
        cal.setTime( eDate );
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        query.setParameter(3, cal.getTimeInMillis()/1000*1000 );
        return query.getResultList();
	}

	private HashMap<Account, List<Object[]>> getAmount(Integer id, Date from, Date to, String sql) {
		HashMap<Account, List<Object[]>> ret = new HashMap<Account, List<Object[]>>();
	
		List<Account> as = new AccountService().findAll();
		
		for ( Account a : as ) {
			Query query = em.createNativeQuery(sql);
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
//				LOG.debug(o);
			}
			ret.put(a, l); 
		}
	
		return ret;
	}

	/**
	 * 
	 * @return <code>List<Object[]></code> is a list of {Date, Long}
	 * from <= x < to
	 */
	public HashMap<Account, List<Object[]>> getDailyAmount(Integer id, Date from, Date to) {
		return getAmount(id, from, to,
				"SELECT datetime(_date/1000, 'unixepoch'), " +
				"sum(t.debit+t.credit) FROM t_transaction t " +
				"WHERE account_id = ? AND category_id = ? " +
				"AND _date >= ? AND _date <= ? " +
				"AND is_deleted = 0 " +
				"GROUP BY _date"
				);
	}

	public HashMap<Account,List<Object[]>> getMonthlyAmount(Integer id, Date from, Date to) {
		return getAmount(id, from, to,
				"SELECT strftime('%Y%m', _date/1000, 'unixepoch', 'localtime'), " +
				"sum(t.debit + t.credit) " +
				"FROM t_transaction t WHERE account_id = ? AND category_id = ? " +
				"AND _date >= ? AND _date <= ? " +
				"AND is_deleted = 0 " +
				"GROUP BY strftime('%Y%m', _date/1000, 'unixepoch', 'localtime')"
				);
	}

	/**
	 * 
	 * @param id
	 * @param from
	 * @param to
	 * @return List<{"YYYYWW", Long}>
	 */
	public HashMap<Account,List<Object[]>> getWeeklyAmount(Integer id, Date from, Date to) {
		// SQLite's week starts from Monday ends with Sunday, so:
		// if our locale starts from Monday, do nothing.
		// if starts from Sunday, move all Sunday's data to next week
		String week = Calendar.getInstance().getFirstDayOfWeek() == 1 ? 
				"_date/1000, 'unixepoch'" : "_date/1000, 'unixepoch', '1 day'";
		
		return getAmount(id, from, to,
				"SELECT strftime('%Y%W', " + week + ", 'localtime'), " +
				// TODO fix the plus/minus
				"sum(t.debit + t.credit) " +
				"FROM t_transaction t WHERE account_id = ? AND category_id = ? " +
				"AND _date >= ? AND _date <= ? " +
				"AND is_deleted = 0 " +
				"GROUP BY strftime('%Y%W', " + week + ", 'localtime') "
				);
	}

}
