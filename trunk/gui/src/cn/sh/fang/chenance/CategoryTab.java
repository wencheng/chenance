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

import static cn.sh.fang.chenance.i18n.UIMessageBundle.setText;
import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutData;
import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutDataRight;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
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

import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.provider.CategoryListContentProvider;
import cn.sh.fang.chenance.util.SWTUtil;
import cn.sh.fang.chenance.util.swt.StyledTextObservableValue;

public class CategoryTab {

	private final static Logger LOG = Logger.getLogger(CategoryTab.class);

	private CategoryListContentProvider prov;

	private CategoryTree tree;

	private CategoryEditForm form;

	private Category root;

	public CategoryTab() {
		prov = new CategoryListContentProvider();
		this.root = prov.getRoot();
	}

	public Control getCategoryTabControl(TabFolder tabFolder) {
		Composite comp = new Composite(tabFolder, SWT.NONE);

		// ツリー
		tree = new CategoryTree(root);
		tree.createControl(comp);
		ColumnViewerToolTipSupport.enableFor(tree.viewer);
		
		// 追加削除ボタン
		Button btnAdd = new Button(comp, SWT.PUSH);
		btnAdd.setText("＋");
		Button btnDel = new Button(comp, SWT.PUSH);
		btnDel.setText("－");

		addButton(btnAdd);
		removeButton(btnDel);

		// 編集フォーム
		Group group = new Group(comp, SWT.RESIZE);
		setText(group, "Cagetory Info");
		FormLayout formLayout = new FormLayout();
		group.setLayout(formLayout);
		formLayout.marginHeight = 10;
		formLayout.marginWidth = 10;
		form = new CategoryEditForm(group, comp.getStyle());
		form.btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				prov.save(tree.getSelected());
			}
		});
		// group.pack();
	
		// レイアウト
		formLayout = new FormLayout();
		comp.setLayout(formLayout);
		formLayout.marginHeight = 10;
		formLayout.marginWidth = 10;
	
		Tree t = tree.viewer.getTree();
		FormData fd = SWTUtil.setFormLayoutData(t, 0, 0, 0, 10);
		fd.height = 400;
		fd.width = 175;
		fd = setFormLayoutDataRight(btnDel, t, 2, SWT.NONE, t, 0,
				SWT.RIGHT);
		fd.width = fd.height;
		fd = setFormLayoutDataRight(btnAdd, t, 2, SWT.NONE, btnDel, 0,
				SWT.NONE);
		fd.width = fd.height;
		setFormLayoutData(group, t, 0, SWT.TOP, t, 20, SWT.NONE);
	
		initDataBindings();

		return comp;
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
				this.form.name, SWT.Modify);
		IObservableValue v1 = BeansObservables
				.observeDetailValue(Realm.getDefault(),
						treeViewerSelectionObserveSelection, "name",
						java.lang.String.class);
		bindingContext.bindValue(w1, v1, null, null);

		// "description" field
		IObservableValue w2 = new StyledTextObservableValue(this.form.desc, SWT.Modify);
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
				Category parent = tree.getSelected();
				List<Category> list = new ArrayList<Category>(parent.getChildren());
				Category child = prov.doAddItem( parent );
				list.add(child);
				parent.setChildren(list);

//				try {
//					code = generateCode(parent);
//				} catch (ChenanceDataException e1) {
//					SWTUtil
//							.showErrorMessage(viewer.getControl().getShell(),
//									"You cannot add into this category any more.  Please select another category.");
//					return;
//				}

				tree.viewer.setSelection(new StructuredSelection(child));
				form.name.selectAll();
				form.name.setFocus();
			}
		});
	}
	
	private void removeButton(Button btn) {
		btn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				TreeItem selectedItem = tree.viewer.getTree().getSelection()[0];
				TreeItem parentItem = selectedItem.getParentItem();
				Category parent;
				int index;
				if (parentItem == null) {
					SWTUtil
					.showErrorMessage(tree.viewer.getControl().getShell(),
							"You cannot delete root category.");
					return;
				} else {
					parent = (Category) parentItem.getData();
					index = parentItem.indexOf(selectedItem);
				}
	
				List<Category> list = new ArrayList<Category>(parent.getChildren());
				Category i = list.remove(index);
				LOG.debug("remove category: " + i);
				parent.setChildren(list);
			}
		});
	}
}
