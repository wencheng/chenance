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

import cn.sh.fang.chenance.AccountTab;
import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider;

public class BsAccountListListener implements IDataAdapter<Account> {

	final static Logger LOG = Logger.getLogger(BsAccountListListener.class);

	AccountTab list;

	public BsAccountListListener(AccountTab t) {
		this.list = t;
	}

	public void onAdded(Account item) {
//		this.list.addAccount( item );
	}

	public void onRemoved(Account item) {
		// not supported
	}

	public void onUpdated(Account item) {
//		this.list.updateList();
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
			bs.setAccount(a);
			tv.refresh();
		}
	}

	public void onLoaded(Account item) {
		// TODO Auto-generated method stub
		
	}

}
