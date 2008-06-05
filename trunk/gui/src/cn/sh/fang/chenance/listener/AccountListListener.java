package cn.sh.fang.chenance.listener;

import static cn.sh.fang.chenance.util.UIMessageBundle._;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import cn.sh.fang.chenance.AccountEditForm;
import cn.sh.fang.chenance.data.dao.AccountService;
import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.util.swt.SWTUtil;

public class AccountListListener implements IItemChangeListener<Account> {

	final static Logger LOG = Logger.getLogger(AccountListListener.class);

	TableTree tree;

	public AccountListListener(TableTree t) {
		this.tree = t;
	}

	public static class DelAccountSelectionAdapter extends SelectionAdapter {

		TableTree tree;

		public DelAccountSelectionAdapter(TableTree t) {
			this.tree = t;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (tree.getSelection().length <= 0) {
				return;
			}
			MessageBox mb = new MessageBox(tree.getShell(), SWT.ICON_QUESTION
					| SWT.YES | SWT.NO);
			mb
					.setMessage(_("ARE YOU SURE TO *PERMENANTLY DELETE* YOUR ACCOUNT?"));
			if (mb.open() == SWT.NO) {
				return;
			}
			mb = new MessageBox(tree.getShell(), SWT.ICON_WARNING | SWT.YES
					| SWT.NO);
			mb.setMessage(_("ARE YOU *REALLY SURE*?"));
			mb.open();
			mb = new MessageBox(tree.getShell(), SWT.ICON_ERROR | SWT.YES
					| SWT.NO);
			mb
					.setMessage(_("DELETION FAILED\n  All your account data maybe broken. "
							+ "Do you want to recover them?"));
			if (mb.open() == SWT.NO) {
				return;
			}
			mb = new MessageBox(tree.getShell(), SWT.ICON_WORKING | SWT.OK);
			mb.setMessage(_("This is a JOKE. ^-^\n"
					+ "Deleting function is still under construction."));
			mb.open();
		}
	}

	public void itemAdded(Account item) {
		TableTreeItem parent = (TableTreeItem) this.tree.getItem(0);
		TableTreeItem ch = new TableTreeItem(parent, SWT.NONE);

		ch.setText(item.getName());
		ch.setData(item);
		tree.deselectAll();
		tree.setSelection(new TableTreeItem[] { ch });
	}

	public void itemRemoved(Account item) {
		// TODO Auto-generated method stub
	}

	public void itemUpdated(Account item) {
		tree.getSelection()[0].setText(item.getName());
	}

}