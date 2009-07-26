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
	
	public void refresh() {
		TransactionService ts = new TransactionService();
		this.transactions = ts.find(account, bDate, eDate);
		calcBalance();
		this.transactions.add(EMPTY);
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
	 * Hour, minute, second will be ignored
	 * 
	 * @param date
	 */
	public void setDate(Date date) {
		Calendar ed = Calendar.getInstance();
		ed.setTime(date);
		ed.add( Calendar.DATE, 1 );
		setDate(date, date, ed.getTime());
	}
	
	public void setDate(Date cDate, Date bDate, Date eDate) {
		this.cDate = cDate;
		this.bDate = bDate;
		this.eDate = eDate;

		refresh();
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

}
