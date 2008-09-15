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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.internal.databinding.observable.EmptyObservableList;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.provider.AccountListProvider;

public class AccountList {

	final static Logger LOG = Logger.getLogger(AccountList.class);

	private AccountListProvider prov;

	// Account.id <-> TableTreeItem
	HashMap<Integer, TableTreeItem> items = null;

	private TableTreeItem sum;

	private TableTree tableTree;

	public AccountList(AccountListProvider prov) {
		this.prov = prov;

		List<Account> a = new ArrayList<Account>();
		Model a1 = new Model();
		a1.setName("Account");
		a1.setAccounts(prov.getAccounts());
		Model a2 = new Model();
		a2.setName("Balance");
		a.add(a1);
		a.add(a2);
		model.setAccounts(a);
	}

	Model model = new Model();

	private TreeViewer viewer;

	class Model extends Account {
		List<Account> accounts;

		public List<Account> getAccounts() {
			return accounts;
		}

		public void setAccounts(List<Account> accounts) {
			firePropertyChange("accounts", null,
					this.accounts = accounts);
		}

	}

	class ModelObservableFactory implements IObservableFactory {

		public ModelObservableFactory() {
			super();
		}

		public IObservable createObservable(Object target) {
			if (target instanceof Model) {
				return BeansObservables.observeList(Realm.getDefault(), target,
						"accounts");
			} else if (target instanceof Account) {
				return new EmptyObservableList(Realm.getDefault(), null);
//				return BeansObservables.observeValue(target, "name");
			} else {
				return new EmptyObservableList(Realm.getDefault(), null);
			}
		}
	}

	public Tree createControl(Composite composite) {
		Tree tree = new Tree(composite, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.SINGLE);
		tree.setLinesVisible(true);
		viewer = new TreeViewer(tree);

		TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
		column1.setAlignment(SWT.LEFT);
		column1.setText("Land/Stadt");
		column1.setWidth(160);
		TreeColumn column2 = new TreeColumn(tree, SWT.RIGHT);
		column2.setAlignment(SWT.RIGHT);
		column2.setText("Person");
		column2.setWidth(50);

		// Create a standard content provider
		ObservableListTreeContentProvider provider = new ObservableListTreeContentProvider(
				new ModelObservableFactory(), null);
		viewer.setContentProvider(provider);

		// And a standard label provider that maps columns
		IObservableMap[] attributeMaps = BeansObservables.observeMaps(provider
				.getKnownElements(), Account.class, new String[] { "name" });
		viewer.setLabelProvider(new ObservableMapLabelProvider(attributeMaps));
		viewer.setLabelProvider(new TableLabelProvider());

		// Now set the Viewer's input
		viewer.setInput(model);
		viewer.expandAll();

		// tableTree = new TableTree(composite, SWT.BORDER | SWT.FULL_SELECTION
		// | SWT.SINGLE);
		// Table table = tableTree.getTable();
		// table.setHeaderVisible(false);
		// table.setLinesVisible(false);
		// // table.addMouseListener(new AccountListMouseAdapter());
		//		
		// TableColumn col1 = new TableColumn(table, SWT.LEFT);
		// TableColumn col2 = new TableColumn(table, SWT.RIGHT);
		// TableTreeItem parent = new TableTreeItem(tableTree, SWT.NONE);
		// parent.setText(0, _("Accounts"));
		// parent.setText(1, "");
		// parent.setGrayed(true);
		// items = new HashMap<Integer,TableTreeItem>();
		// sum = new TableTreeItem(tableTree, SWT.NONE);
		// sum.setText(0, _("Balance Total"));
		// sum.setGrayed(true);
		//
		// updateList();
		//
		// parent.setExpanded(true);
		//
		// col1.pack();
		// col1.setResizable(true);
		// col1.setToolTipText("Double-click to view the balance");
		// col1.setWidth(col1.getWidth() + 20);
		// // col2.pack();
		// col2.setWidth(80);
		// col2.setResizable(false);
		//
		// FontData fd = parent.getFont().getFontData()[0];
		// Font newFont = new Font(tableTree.getDisplay(), new
		// FontData(fd.getName(),
		// fd.getHeight(), fd.getStyle() | SWT.BOLD));
		// parent.setFont(newFont);
		// sum.setFont(newFont);

		final IObservableValue beanViewerSelection = ViewersObservables
			.observeSingleSelection(viewer);
		IObservableValue beanSelected = new ComputedValue(Boolean.TYPE) {
			protected Object calculate() {
				return Boolean.valueOf(beanViewerSelection.getValue() != null);
			}
		};
		DataBindingContext bdc = new DataBindingContext();
//		dbc.bindValue(SWTObservables.observeEnabled(addBtn),
//				beanSelected, null, null);

		return viewer.getTree();
	}

	public void updateList() {
		TableTreeItem i;
		int balanceSum = 0;
		for (Account a : this.prov.getAccounts()) {
			LOG.debug(String.format("updating: %s %d", a.getName(), a
					.getCurrentBalance()));

			i = items.get(a.getId());
			if (i == null) {
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

	public void addSelectionListener(SelectionListener arg0) {
		// tableTree.addSelectionListener(arg0);
	}

	/**
	 * 
	 * @param i
	 *            index in the tree
	 */
	public void selectAccount(int i) {
		if (i < 0 || i > items.size() - 1) {
			return;
		}

		this.tableTree.setSelection(new TableTreeItem[] { this.tableTree
				.getItem(0).getItem(i) });
		this.tableTree.notifyListeners(SWT.Selection, new Event());
	}

	public Account getSelectedAccount() {
		if (this.tableTree.getSelection().length > 0) {
			return (Account) this.tableTree.getSelection()[0].getData();
		} else {
			return null;
		}
	}

	public void addAccount(Account item) {
		TableTreeItem parent = (TableTreeItem) this.tableTree.getItem(0);
		TableTreeItem ch = new TableTreeItem(parent, SWT.NONE);

		ch.setText(item.getName());
		ch.setData(item);
	}

	class TableLabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return ((Account) element).getName();
			case 1:
				if (element instanceof Account) {
					Integer i = ((Account) element).getCurrentBalance();
					if ( i == null ) {
						return "";
					} else {
						return "￥" + i;
					}
				}
			case 2:
				// if (element instanceof House)
				// return ((House)element).getSex();
			}
			return null;
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}
	}

	protected DataBindingContext initDataBindings() {
		IObservableValue treeViewerSelectionObserveSelection = ViewersObservables
				.observeSingleSelection(viewer);
//		IObservableValue textTextObserveWidget = SWTObservables.observeText(
//				name, SWT.Modify);
		IObservableValue treeViewerValueObserveDetailValue = BeansObservables
				.observeDetailValue(Realm.getDefault(),
						treeViewerSelectionObserveSelection, "text",
						java.lang.String.class);
		//
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
//		bindingContext.bindValue(textTextObserveWidget,
//				treeViewerValueObserveDetailValue, null, null);
		//
		return bindingContext;
	}

	public void addButton(Button btnAdd) {
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Model parent = (Model)model.getAccounts().get(0);
				List<Account> list = parent.getAccounts();

				Account a = new Account();
				a.setName("New Account");
				a.setDescription("");
				a.setCurrentBalance(0);
				a.setUpdater("USER");

				list.add(a);
				parent.setAccounts(list);

				viewer.setSelection(new StructuredSelection(a));
//				beanText.selectAll();
//				beanText.setFocus();
			}
		});
	}
}
