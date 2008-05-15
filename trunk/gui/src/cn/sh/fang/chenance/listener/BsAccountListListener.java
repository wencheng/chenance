package cn.sh.fang.chenance.listener;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import cn.sh.fang.chenance.AccountList;
import cn.sh.fang.chenance.data.entity.Account;

public class BsAccountListListener implements IItemChangeListener<Account> {

	final static Logger LOG = Logger.getLogger(BsAccountListListener.class);

	AccountList list;

	public BsAccountListListener(AccountList t) {
		this.list = t;
	}

	public static class AccountListMouseAdapter extends MouseAdapter {
		public void mouseDoubleClick(MouseEvent e) {
			if (e.button == 1) {
				Table t = (Table) e.widget;
				TableItem i = t.getItem(new Point(e.x, e.y));
				System.out.println(i + " was d-clicked");
			}
		}
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

}
