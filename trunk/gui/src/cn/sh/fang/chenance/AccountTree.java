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
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import cn.sh.fang.chenance.data.entity.Account;

public class AccountTree {

	final static Logger LOG = Logger.getLogger(AccountTree.class);

	private TreeViewer viewer;

	Model model;

	private IObservableValue hasSelected;

	public AccountTree(List<Account> accounts) {

		List<Account> a = new ArrayList<Account>();
		Model a1 = new Model();
		a1.setName("Account");
		a1.setAccounts(accounts);
		Model a2 = new Model();
		a2.setName("Balance");
		a.add(a1);
		a.add(a2);

		this.model = new Model();
		this.model.setAccounts(a);
	}

	class Model extends Account {
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
//				return BeansObservables.observeValue(target, "name");
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

	public Account getSelectedAccount() {
		IStructuredSelection selection = (IStructuredSelection)viewer
			.getSelection();
		if (selection.isEmpty())
			return null;
		return (Account) selection.getFirstElement();
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

}
