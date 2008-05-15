package cn.sh.fang.chenance.listener;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import cn.sh.fang.chenance.data.entity.Transaction;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider.Column;

public class BalanceSheetTransactionListener implements
		IItemChangeListener<Transaction> {

	private TableViewer viewer;

	public BalanceSheetTransactionListener(TableViewer tableViewer) {
		this.viewer = tableViewer;
	}

	public void itemAdded(Transaction t) {
		// TODO detail buttons would not be updated, fix it.
		this.viewer.add(t);
		this.viewer.setSelection(new StructuredSelection(t));
		this.viewer.getTable().showSelection();
		this.viewer.editElement(t, Column.CATEGORY.ordinal());
	}

	public void itemRemoved(Transaction t) {
		this.viewer.remove(t);
	}

	public void itemUpdated(Transaction task) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
