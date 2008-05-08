package cn.sh.fang.chenance.listener;

import org.apache.log4j.Logger;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
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
