package cn.sh.fang.chenance.listener;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import cn.sh.fang.chenance.AccountList;
import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider;

public class BsAccountListListener implements IItemChangeListener<Account> {

	final static Logger LOG = Logger.getLogger(BsAccountListListener.class);

	AccountList list;

	public BsAccountListListener(AccountList t) {
		this.list = t;
	}

	public void itemAdded(Account item) {
		TableTreeItem parent = (TableTreeItem) this.list.getItem(0);
		TableTreeItem ch = new TableTreeItem(parent, SWT.NONE);

		ch.setText(item.getName());
		ch.setData(item);
	}

	public void itemRemoved(Account item) {
		// not supported
	}

	public void itemUpdated(Account item) {
		this.list.updateList();
	}

	public static class AccountListSelectionAdapter extends SelectionAdapter {

		private BalanceSheetContentProvider bs;
		private TableViewer tv;

		public AccountListSelectionAdapter(BalanceSheetContentProvider bs, TableViewer tv) {
			this.bs = bs;
			this.tv = tv;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			TableTree t = (TableTree) e.widget;
			if ( t.getSelection() == null || t.getSelection()[0].getData() == null ) {
				return;
			}

			super.widgetSelected(e);
			
			Account a = (Account)t.getSelection()[0].getData();
			System.out.println("refreshing " + a);
			bs.setAccount(a);
			tv.refresh();
		}
	}

}
