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
package cn.sh.fang.chenance;

import static cn.sh.fang.chenance.i18n.UIMessageBundle._;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.provider.AccountListProvider;

public class AccountList {
	
	private AccountListProvider prov;
	
	// Account.id <-> TableTreeItem
	HashMap<Integer,TableTreeItem> items = null;
	
	private TableTreeItem sum;

	private TableTree tableTree;

	public AccountList(AccountListProvider prov) {
		this.prov = prov;
	}

	public TableTree createControl(Composite composite) {
		tableTree = new TableTree(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		Table table = tableTree.getTable();
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
//		table.addMouseListener(new AccountListMouseAdapter());

		TableColumn col1 = new TableColumn(table, SWT.LEFT);
		TableColumn col2 = new TableColumn(table, SWT.RIGHT);
		TableTreeItem parent = new TableTreeItem(tableTree, SWT.NONE);
		parent.setText(0, _("Accounts"));
		parent.setText(1, "");
		parent.setGrayed(true);
		items = new HashMap<Integer,TableTreeItem>();
		sum = new TableTreeItem(tableTree, SWT.NONE);
		sum.setText(0, _("Balance Total"));
		sum.setGrayed(true);

		updateList();

		parent.setExpanded(true);

		col1.pack();
		col1.setResizable(true);
		col1.setToolTipText("Double-click to view the balance");
		col1.setWidth(col1.getWidth() + 20);
//		col2.pack();
		col2.setWidth(80);
		col2.setResizable(false);

		FontData fd = parent.getFont().getFontData()[0];
		Font newFont = new Font(tableTree.getDisplay(), new FontData(fd.getName(),
				fd.getHeight(), fd.getStyle() | SWT.BOLD));
		parent.setFont(newFont);
		sum.setFont(newFont);
		
		return tableTree;
	}

	public void updateList() {
		TableTreeItem i;
		int balanceSum = 0;
		for ( Account a : this.prov.getAccounts() ) {
			i = items.get(a.getId());
			if ( i == null ) {
				i = new TableTreeItem(this.tableTree.getItem(0), SWT.NONE);
				balanceSum += a.getCurrentBalance();
				items.put(a.getId(), i);
			}
			i.setText(0, a.getName());
			i.setText(1, a.getCurrentBalance() + "");
			i.setData(a);
		}
		sum.setText(1, balanceSum + "");
	}
	
	public TableTreeItem getItem(int i) {
		return this.tableTree.getItem(i);
	}

	public TableTree getTableTree() {
		return tableTree;
	}
	
	/**
	 * 
	 * @param i index in the tree
	 */
	public void selectAccount(int i) {
		if ( i < 0 || i > items.size() - 1 ) {
			return;
		}

		this.tableTree.setSelection(new TableTreeItem[]{this.tableTree.getItem(0).getItem(i)});
		this.tableTree.notifyListeners(SWT.Selection, new Event());
	}
	
}
