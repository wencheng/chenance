package cn.sh.fang.gfp.swt.provider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cn.sh.fang.gtp.entity.Transaction;

/**
 * Class that plays the role of the domain model in the TableViewerExample In
 * real life, this class would access a persistent store of some kind.
 * 
 */
public class BalanceSheetContentProvider implements IStructuredContentProvider,
		IBalanceSheetListener {
	
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		if (newInput != null)
			((BalanceSheetContentProvider) newInput).addChangeListener(this);
		if (oldInput != null)
			((BalanceSheetContentProvider) oldInput).removeChangeListener(this);
	}

	public enum Column {
		DATE, CATEGORY, DEBIT, CREDIT, BALANCE, DETAIL, LAST;

		public static String[] stringValues() {
			Column[] v = Column.values();
			String[] ret = new String[v.length];
			for ( int i = 0; i < ret.length; i++ ) {
				ret[i] = v[i].toString();
			}
			return ret;
		}
	}

	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	private Set<IBalanceSheetListener> changeListeners = new HashSet<IBalanceSheetListener>();

	/**
	 * Constructor
	 */
	public BalanceSheetContentProvider() {
		super();
		this.initData();
	}
	
	/*
	 * Initialize the table data. Create COUNT tasks and add them them to the
	 * collection of tasks
	 */
	private void initData() {
		Transaction t;
		for (int i = 0; i < 14; i++) {
			t = new Transaction();
			this.transactions.add(t);
		}
	};

	/**
	 * Return the tasks as an array of Objects
	 */
	public Object[] getElements(Object parent) {
		return this.transactions.toArray();
	}

	public void dispose() {
		this.removeChangeListener(this);
	}

	/**
	 * Return the collection of tasks
	 */
	public List<Transaction> getTransactions() {
		return this.transactions;
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(IBalanceSheetListener viewer) {
		changeListeners.add(viewer);
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(IBalanceSheetListener viewer) {
		changeListeners.remove(viewer);
	}

	public void addTask(Transaction t) {
		this.transactions.add(t);
		Iterator<IBalanceSheetListener> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IBalanceSheetListener) iterator.next()).addTask(t);
	}

	/**
	 * @param task
	 */
	public void removeTask(Transaction t) {
		this.transactions.remove(t);
		Iterator<IBalanceSheetListener> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IBalanceSheetListener) iterator.next()).removeTask(t);
	}

	public void updateTask(Transaction task) {
		// TODO 自動生成されたメソッド・スタブ
		 
	}

	/**
	 * Add a new task to the collection of tasks
	 */
	public void addTask() {
		Transaction t = new Transaction();
		this.transactions.add(t);
		Iterator<IBalanceSheetListener> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IBalanceSheetListener) iterator.next()).addTask(t);
	}

	/**
	 * @param task
	 */
	public void taskChanged(Transaction t) {
		Iterator<IBalanceSheetListener> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IBalanceSheetListener) iterator.next()).updateTask(t);
	}

}
