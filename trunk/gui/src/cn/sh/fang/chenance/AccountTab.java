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

import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutData;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import cn.sh.fang.chenance.AccountTree.Model;
import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.provider.AccountListProvider;
import cn.sh.fang.chenance.util.SWTUtil;

public class AccountTab {

	private final static Logger LOG = Logger.getLogger(AccountTab.class);

	private AccountListProvider prov;

	private AccountTree tree;

	private AccountEditForm form;

	private Model model;

	public AccountTab(AccountListProvider prov, Model model) {
		this.prov = prov;
		this.model = model;
	}

	public Control getAccountTabControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);

		// 概要ツリー
		tree = new AccountTree(model);
		tree.createControl(composite);
//		this.selectAccount(0);

		// 追加ボタン
		Button btnAdd = new Button(composite, SWT.PUSH);
		btnAdd.setText("＋");
		Button btnDel = new Button(composite, SWT.PUSH);
		btnDel.setText("－");

		// フォーム
		form = new AccountEditForm();
		Group grp = (Group) form.createControl(composite);
	
		// イベント
	//		accountListProv.addChangeListener(new AccountEditFormListener(
	//				accountForm));
	//		accountListProv.addChangeListener(new AccountListListener(tableTree));
	//		accountListProv.addChangeListener(new BsAccountListListener(
	//				bsAccountList));
	//		accountListProv.addChangeListener(new AbstractDataAdapter<Account>(){
	//			@Override
	//			public void onUpdated(Account item) {
	//				currentBalance.setText(NumberFormat.getCurrencyInstance().format(bsAccountList.getSelectedAccount().getCurrentBalance()));
	//			}
	//		});
		this.addButton(btnAdd);
		this.removeButton(btnDel);
//		btnDel.addSelectionListener(new DelAccountSelectionAdapter(tableTree));

		initDataBindings();

		form.btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prov.itemChanged(tree.getSelectedAccount());
			}
		});

		// レイアウト
		FormLayout formLayout = new FormLayout();
		composite.setLayout(formLayout);
		formLayout.marginHeight = 10;
		formLayout.marginWidth = 10;

		Tree tableTree = tree.viewer.getTree();
		FormData fd = SWTUtil.setFormLayoutData(tableTree, 0, 0, 0, 10);
		fd.height = 400;
//		fd.width = 175;

		fd = SWTUtil.setFormLayoutDataRight(btnDel, tableTree, 2, SWT.NONE,
				tableTree, 0, SWT.RIGHT);
		fd.width = fd.height;
		fd = SWTUtil.setFormLayoutDataRight(btnAdd, tableTree, 2, SWT.NONE,
				btnDel, 0, SWT.NONE);
		fd.width = fd.height;

		setFormLayoutData(grp, 0, 0, tableTree, 20);

		return composite;
	}

	/**
	 * Binds form fields with selected item in the tree
	 * @return
	 */
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		IObservableValue treeViewerSelectionObserveSelection = ViewersObservables
				.observeSingleSelection(tree.viewer);

		// "name" field
		IObservableValue w1 = SWTObservables.observeText(
				this.form.tName, SWT.Modify);
		IObservableValue v1 = BeansObservables
				.observeDetailValue(Realm.getDefault(),
						treeViewerSelectionObserveSelection, "name",
						java.lang.String.class);
		bindingContext.bindValue(w1, v1, null, null);

		// "description" field
		IObservableValue w2 = SWTObservables.observeText(
				this.form.memo, SWT.Modify);
		IObservableValue v2 = BeansObservables
				.observeDetailValue(Realm.getDefault(),
						treeViewerSelectionObserveSelection, "description",
						java.lang.String.class);
		bindingContext.bindValue(w2, v2, null, null);

		//
		return bindingContext;
	}

	/**
	 * 
	 * @param i
	 *            index in the tree
	 */
	public void selectAccount(int i) {
		TreeItem item = this.tree.viewer.getTree().getItem(0);
		if ( i < 0 || i > item.getItemCount() ) {
			return;
		}

		this.tree.viewer.getTree().setSelection( item.getItem(i) );
		this.tree.viewer.getTree().notifyListeners(SWT.Selection, new Event());
	}

	private void addButton(Button btnAdd) {
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Model parent = (Model)tree.model.getAccounts().get(0);
				List<Account> list = parent.getAccounts();

				Account a = new Account();
				a.setName("New Account");
				a.setDescription("");
				a.setCurrentBalance(0);
				a.setUpdater("USER");

				list.add(a);
				parent.setAccounts(list);

				tree.viewer.setSelection(new StructuredSelection(a));
				form.tName.selectAll();
				form.tName.setFocus();
			}
		});
	}
	
	private void removeButton(Button btn) {
		btn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				TreeItem selectedItem = tree.viewer.getTree().getSelection()[0];
				TreeItem parentItem = selectedItem.getParentItem();
				Model parent;
				int index;
				if (parentItem == null) {
					parent = tree.model;
					index = tree.viewer.getTree().indexOf(selectedItem);
				} else {
					parent = (Model) parentItem.getData();
					index = parentItem.indexOf(selectedItem);
				}
	
				List<Account> list = new ArrayList<Account>(parent.getAccounts());
				Account i = list.remove(index);
				LOG.debug("remove account: " + i);
				parent.setAccounts(list);
			}
		});
	}
}
