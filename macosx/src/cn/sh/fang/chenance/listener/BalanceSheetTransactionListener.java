package cn.sh.fang.chenance.listener;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import cn.sh.fang.chenance.data.dao.TransactionService;
import cn.sh.fang.chenance.data.entity.Transaction;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider.Column;

public class BalanceSheetTransactionListener implements
		IItemChangeListener<Transaction> {

	private TableViewer viewer;
	
	TransactionService ts = new TransactionService();

	public BalanceSheetTransactionListener(TableViewer tableViewer) {
		this.viewer = tableViewer;
	}
	
	public void onAdded(Transaction t) {
		// TODO detail buttons would not be updated, fix it.
		this.viewer.insert(t, this.viewer.getTable().getItemCount()-1);
		this.viewer.setSelection(new StructuredSelection(t));
		this.viewer.getTable().showSelection();
		this.viewer.editElement(t, Column.CATEGORY.ordinal());
	}

	public void onRemoved(Transaction t) {
		this.viewer.remove(t);
	}

	public void onUpdated(Transaction t) {
		ts.save(t);
	}

	public void onLoaded(Transaction item) {
		// TODO Auto-generated method stub
		
	}

}
