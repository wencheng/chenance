package cn.sh.fang.chenance.provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cn.sh.fang.chenance.data.entity.Transaction;
import cn.sh.fang.chenance.listener.IItemChangeListener;

/**
 * Class that plays the role of the domain model in the TableViewerExample In
 * real life, this class would access a persistent store of some kind.
 * 
 */
public class BalanceSheetContentProvider extends BaseProvider<Transaction>
		implements IStructuredContentProvider {

	public enum Column {
		DATE, CATEGORY, DEBIT, CREDIT, BALANCE, DETAIL, LAST;

		public static String[] stringValues() {
			Column[] v = Column.values();
			String[] ret = new String[v.length];
			for (int i = 0; i < ret.length; i++) {
				ret[i] = v[i].toString();
			}
			return ret;
		}
	}

	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

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
		Transaction t;
		for (int i = 0; i < 5; i++) {
			t = new Transaction();
			t.setId(i);
			t.setDate(new Date());
			t.setDebit(i*1000);
			t.setCredit(0);
			this.transactions.add(t);
		}
		
		// for adding
		t = new Transaction();
		this.transactions.add(t);
	};

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

	/*
	 * TableViewerオブジェクトのsetInputメソッドでドメインオブジェクトが渡されたときに呼び出されるメソッドである。
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
//		if (newInput != null)
//			((BalanceSheetContentProvider) newInput).addChangeListener(this);
//		if (oldInput != null)
//			((BalanceSheetContentProvider) oldInput).removeChangeListener(this);
	}

	@Override
	protected Transaction doAddItem() {
		Transaction t = new Transaction();
		t.setDate(new Date());
		t.setDebit(0);
		t.setCredit(0);
		this.transactions.add(t);
		return t;
	}

	@Override
	protected Transaction doRemoveItem(Transaction t) {
		this.transactions.remove(t);
		return t;
	}

	@Override
	protected Transaction doUpdateItem(Transaction t) {
		return t;
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

}
