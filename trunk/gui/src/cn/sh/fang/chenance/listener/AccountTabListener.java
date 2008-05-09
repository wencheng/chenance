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

import cn.sh.fang.chenance.data.dao.AccountService;
import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.provider.AccountEditorProvider;

public class AccountTabListener {

	final static Logger LOG = Logger.getLogger(AccountTabListener.class);

	public static class AccountListMouseAdapter extends MouseAdapter {
		public void mouseDoubleClick(MouseEvent e) {
			if (e.button == 1) {
				Table t = (Table) e.widget;
				TableItem i = t.getItem(new Point(e.x, e.y));
				System.out.println(i + " was d-clicked");
			}
		}
	}
	
	public static class AddAccountSelectionAdapter extends SelectionAdapter {
		
		TableTree tree;

		public AddAccountSelectionAdapter(TableTree t) {
			this.tree = t;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			TableTreeItem parent = (TableTreeItem)tree.getItem(0);
			TableTreeItem ch = new TableTreeItem(parent, SWT.NONE);
			Account a = new Account();
			a.setName(_("New Account"));
			a.setDescription("");
			ch.setText(a.getName());
			ch.setData(a);
			tree.setSelection(new TableTreeItem[]{ch});
		}
	}

	public static class DelAccountSelectionAdapter extends SelectionAdapter {
		
		TableTree tree;

		public DelAccountSelectionAdapter(TableTree t) {
			this.tree = t;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if ( tree.getSelection().length <= 0 ) {
				return;
			}
			MessageBox mb = new MessageBox(tree.getShell(),SWT.ICON_QUESTION|SWT.YES|SWT.NO);
			mb.setMessage(_("ARE YOU SURE TO *PERMENANTLY DELETE* YOUR ACCOUNT?"));
			if ( mb.open() == SWT.NO ) {
				return;
			}
			mb = new MessageBox(tree.getShell(),SWT.ICON_WARNING|SWT.YES|SWT.NO);
			mb.setMessage(_("ARE YOU *REALLY SURE*?"));
			mb.open();
			mb = new MessageBox(tree.getShell(),SWT.ICON_ERROR|SWT.YES|SWT.NO);
			mb.setMessage(_("DELETION FAILED\n  All your account data maybe broken. " +
					"Do you want to recover them?"));
			if ( mb.open() == SWT.NO) {
				return;
			}
			mb = new MessageBox(tree.getShell(),SWT.ICON_WORKING|SWT.OK);
			mb.setMessage(_("This is a JOKE. ^-^\n" +
					"Deleting function is still under construction."));
			mb.open();
		}
	}

	public static class AccountListSelectionAdapter extends SelectionAdapter {

		AccountEditorProvider prov;

		public AccountListSelectionAdapter(AccountEditorProvider prov) {
			this.prov = prov;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			super.widgetSelected(e);
			TableTreeItem i = ((TableTreeItem) e.item);
			if (i.getData() instanceof Account) {
				Account a = (Account) i.getData();

				prov.name.setText(a.getName());
				prov.memo.setText(a.getDescription());
				// TODO add rest items
				prov.save.setData(a);
				prov.save.setEnabled(true);
				prov.name.setFocus();
			} else {
				e.doit = true;
//				prov.save.setData(null);
//				prov.save.setEnabled(false);
			}
		}
	}

	public static class SaveAccountSelectionAdapter extends SelectionAdapter {

		AccountEditorProvider prov;

		public SaveAccountSelectionAdapter(AccountEditorProvider prov) {
			this.prov = prov;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			super.widgetSelected(e);
			LOG.debug(e);
			Account a = (Account) prov.save.getData();
			a.setName(prov.name.getText());
			a.setDescription(prov.memo.getText());
			// TODO add rest items

			try {
				AccountService s = new AccountService();
				s.save(a);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
