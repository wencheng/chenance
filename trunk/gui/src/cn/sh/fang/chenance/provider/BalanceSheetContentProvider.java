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
package cn.sh.fang.chenance.provider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cn.sh.fang.chenance.data.dao.TransactionService;
import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.data.entity.Transaction;

/**
 * Class that plays the role of the domain model in the TableViewerExample In
 * real life, this class would access a persistent store of some kind.
 * 
 */
public class BalanceSheetContentProvider extends BaseProvider<Transaction>
		implements IStructuredContentProvider {

	static final Logger LOG = Logger.getLogger( BalanceSheetContentProvider.class );

	public enum Column {
		DATE, CATEGORY, DEBIT, CREDIT, BALANCE, DETAIL;

		public static String[] stringValues() {
			Column[] v = Column.values();
			String[] ret = new String[v.length];
			for (int i = 0; i < ret.length; i++) {
				ret[i] = v[i].toString();
			}
			return ret;
		}
	}

	private Account account = null;

	private List<Transaction> transactions = new ArrayList<Transaction>();

	private Date cDate = Calendar.getInstance().getTime();

	private Date bDate = cDate;

	private Date eDate = bDate;

	private int balance;

	private int dateRange;

	private boolean isUnconfirmedOnly = false;

	static final Transaction EMPTY = new Transaction();

	/**
	 * Constructor
	 */
	public BalanceSheetContentProvider() {
		this.initData();
	}

	/*
	 * Initialize the table data. Create COUNT tasks and add them them to the
	 * collection of tasks
	 */
	private void initData() {
		this.transactions.add(EMPTY);
	};
	
	public void setAccount(Account account) {
		this.account = account;
		refresh();
	}
	
	public void setDate(Date date, int range) {
		this.cDate = date;
		this.dateRange = range;
		refresh();
	}

	public void refresh() {
		resetDateRange();
		
		TransactionService ts = new TransactionService();
		this.transactions = ts.find(account, bDate, eDate, isUnconfirmedOnly);
		calcBalance();
		this.transactions.add(EMPTY);
	}

	/**
	 * Hour, minute, second will be ignored
	 * 
	 * @param date
	 */
	private void resetDateRange() {
		Calendar bgn;
		Calendar end;

		if ( this.dateRange == Calendar.DATE ) {
			end = Calendar.getInstance();
			end.setTime(cDate);
			end.add( Calendar.DATE, 1 );

			this.bDate = cDate;
			this.eDate = end.getTime();
		} else if ( this.dateRange == Calendar.MONTH ) {
			bgn = Calendar.getInstance();
			bgn.setTime(cDate);
			end = (Calendar) bgn.clone();

			if (account.getClosingDay() == 31) {
				bgn.set( Calendar.DATE, bgn.getActualMinimum(Calendar.DATE) );
				end.set( Calendar.DATE, bgn.getActualMaximum(Calendar.DATE) );
				end.add( Calendar.DATE, 1 );
			} else if ( bgn.get( Calendar.DATE ) > account.getClosingDay() ) {
				bgn.set( Calendar.DATE, account.getClosingDay()+1 );
				end.add( Calendar.MONTH, 1 );
				end.set( Calendar.DATE, account.getClosingDay()+1 );
			} else {
				bgn.add( Calendar.MONTH, -1 );
				bgn.set( Calendar.DATE, account.getClosingDay()+1 );
				end.set( Calendar.DATE, account.getClosingDay()+1 );
			}
			
			this.bDate = bgn.getTime();
			this.eDate = end.getTime();
		} else if ( this.dateRange == Calendar.WEEK_OF_YEAR ) {
			bgn = Calendar.getInstance();
			bgn.setTime(cDate);
			bgn.add( Calendar.DATE, -(bgn.get(Calendar.DAY_OF_WEEK)-bgn.getMinimalDaysInFirstWeek()) );
			end = (Calendar) bgn.clone();
			end.add( Calendar.DATE, 7 );

			this.bDate = bgn.getTime();
			this.eDate = end.getTime();
		} else if ( this.dateRange == Calendar.YEAR ) {
			bgn = Calendar.getInstance();
			bgn.setTime(cDate);
			bgn.set( Calendar.MONTH, 0 );
			bgn.set( Calendar.DATE, 1 );
			end = (Calendar) bgn.clone();
			end.set( Calendar.MONTH, 11 );
			end.set( Calendar.DATE, 31 );
			end.add( Calendar.DATE, 1 );

			this.bDate = bgn.getTime();
			this.eDate = end.getTime();
		}

	}

	private void calcBalance() {
		this.balance = 0;
		for (Transaction t : transactions) {
			this.balance -= t.getDebit();
			this.balance += t.getCredit();
		}
	}
	
	public int getBalance() {
		return this.balance;
	}

	/**
	 * Return the tasks as an array of Objects
	 */
	public Object[] getElements(Object element) {
		return this.transactions.toArray();
	}

	/**
	 * Return the collection of tasks
	 */
	public List<Transaction> getTransactions() {
		return this.transactions;
	}

	@Override
	protected Transaction doAddItem() {
		Transaction t = new Transaction();
		t.setDate(cDate);
		t.setDebit(0);
		t.setCredit(0);
		t.setBalance(0);
		t.setAccount(this.account);
		t.setIsConfirmed(true);
		t.setUpdater("USER");

		this.transactions.add(this.transactions.size()-1, t);
		return t;
	}

	@Override
	protected Transaction doRemoveItem(Transaction t) {
		this.transactions.remove(t);
		calcBalance();
		return t;
	}

	@Override
	protected Transaction doUpdateItem(Transaction t) {
		calcBalance();
		return t;
	}

	public void dispose() {
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

	public void setIsUnconfirmedOnly(boolean b) {
		this.isUnconfirmedOnly  = b;
		refresh();
	}

}
