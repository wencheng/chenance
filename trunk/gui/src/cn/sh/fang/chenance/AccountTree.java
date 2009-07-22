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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.internal.databinding.observable.EmptyObservableList;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import cn.sh.fang.chenance.data.entity.Account;

public class AccountTree {

	final static Logger LOG = Logger.getLogger(AccountTree.class);

	public Model model;

	public TreeViewer viewer;

	private IObservableValue hasSelected;

	public AccountTree(Model model) {
		this.model = model;
	}
	
	public AccountTree(List<Account> accounts) {
		List<Account> a = new ArrayList<Account>();
		Model a1 = new Model();
		a1.setAccounts(accounts);
		Model a2 = new Model();
		a.add(a1);
		a.add(a2);

		this.model = new Model();
		this.model.setAccounts(a);
	}

	public class Model extends Account {
		private static final long serialVersionUID = 1L;

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
			} else {
				return new EmptyObservableList(Realm.getDefault(), null);
			}
		}
	}

	public TreeViewer createControl(Composite composite) {
		Tree tree = new Tree(composite, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.SINGLE);
		tree.setLinesVisible(true);
		viewer = new TreeViewer(tree);

		TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
		column1.setAlignment(SWT.LEFT);
		//column1.setText("Land/Stadt");
		column1.setWidth(100);
		TreeColumn column2 = new TreeColumn(tree, SWT.RIGHT);
		column2.setAlignment(SWT.RIGHT);
		//column2.setText("Person");
		column2.setWidth(93);
		
		// Create a standard content provider
		ObservableListTreeContentProvider provider = new ObservableListTreeContentProvider(
				new ModelObservableFactory(), null);
		viewer.setContentProvider(provider);

		// And a standard label provider that maps columns
		IObservableMap[] attributeMaps = BeansObservables.observeMaps(provider
				.getKnownElements(), Account.class, new String[] { "name" });
		viewer.setLabelProvider(new TableLabelProvider(attributeMaps));

		// Now set the Viewer's input
		viewer.setInput(model);
		viewer.expandAll();

		final IObservableValue beanViewerSelection = ViewersObservables
			.observeSingleSelection(viewer);
		this.hasSelected = new ComputedValue(Boolean.TYPE) {
			protected Object calculate() {
				return Boolean.valueOf(beanViewerSelection.getValue() != null);
			}
		};

		return viewer;
	}

	public IObservableValue getHasSelected() {
		return hasSelected;
	}

	public Account getSelected() {
		IStructuredSelection selection = (IStructuredSelection)viewer
			.getSelection();
		if (selection.isEmpty())
			return null;
		return (Account) selection.getFirstElement();
	}

	/**
	 * 
	 * @param i
	 *            index in the tree
	 */
	public void selectAccount(int i) {
		TreeItem item = this.viewer.getTree().getItem(0);
		if ( i < 0 || i > item.getItemCount() ) {
			return;
		}
	
		this.viewer.getTree().setSelection( item.getItem(i) );
		this.viewer.getTree().notifyListeners(SWT.Selection, new Event());
	}
	
	public void addSelectionListener(SelectionAdapter a) {
		this.viewer.getTree().addSelectionListener(a);
	}

	class TableLabelProvider extends ObservableMapLabelProvider {

		public TableLabelProvider(IObservableMap[] attributeMaps) {
			super(attributeMaps);
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			switch (columnIndex) {
			// アカウント名
			case 0:
				if (element instanceof Model) {
					Model a = (Model)element;
					if ( a.getAccounts() != null ) {
						return _("Accounts");
					} else {
						return _("Balance Total");
					}
				} else {
					return super.getColumnText(element, 0);
				}
			// 残高
			case 1:
				if (element instanceof Account) {
					Integer i = ((Account) element).getCurrentBalance();
					if ( i == null ) {
						return "";
					} else {
						return NumberFormat.getCurrencyInstance().format(i);
					}
				}
			}
			return null;
		}
	}

}
