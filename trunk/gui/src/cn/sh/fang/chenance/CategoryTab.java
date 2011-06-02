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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.nebula.widgets.pshelf.PShelf;
import org.eclipse.nebula.widgets.pshelf.PShelfItem;
import org.eclipse.nebula.widgets.pshelf.RedmondShelfRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.listener.CategoryListListener;
import cn.sh.fang.chenance.provider.CategoryContentProvider;
import cn.sh.fang.chenance.util.SWTUtil;
import cn.sh.fang.chenance.util.swt.StyledTextObservableValue;

public class CategoryTab {
	
	private CategoryContentProvider prov;

	private CategoryTree tree;

	private CategoryEditForm infoForm;
	
	private CategoryStatsForm statsForm;

	private Category root;

	private Button btnAdd;

	private Button btnDel;

	public CategoryTab(CategoryContentProvider prov) {
		this.prov = prov;
		this.root = prov.getRoot();
	}

	public Control getCategoryTabControl(TabFolder tabFolder) {
		Composite comp = new Composite(tabFolder, SWT.NONE);

		// ツリー
		tree = new CategoryTree(root);
		tree.createControl(comp);
		ColumnViewerToolTipSupport.enableFor(tree.viewer);
		
		// 追加削除ボタン
		btnAdd = new Button(comp, SWT.PUSH);
		btnAdd.setText("＋");
		btnDel = new Button(comp, SWT.PUSH);
		btnDel.setText("－");

		addButton(btnAdd);
		removeButton(btnDel);
		
		prov.addChangeListener(new CategoryListListener(tree));

		PShelf shelf = new PShelf(comp, SWT.BORDER);
		shelf.setRenderer(new RedmondShelfRenderer());
		
		// 編集フォーム
		PShelfItem item0 = new PShelfItem(shelf,SWT.NONE);
		Composite group = item0.getBody();
		setText(item0, "Category Info");
		FormLayout formLayout = new FormLayout();
		group.setLayout(formLayout);
		formLayout.marginHeight = 10;
		formLayout.marginWidth = 10;
		formLayout.spacing = 10;
		infoForm = new CategoryEditForm(group, comp.getStyle());
		infoForm.btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				prov.itemChanged(tree.getSelected());
			}
		});

		// 集計フォーム
		item0 = new PShelfItem(shelf,SWT.NONE);
		group = item0.getBody();
		setText(item0, "Category Statistics");
		formLayout = new FormLayout();
		group.setLayout(formLayout);
		formLayout.marginHeight = 10;
		formLayout.marginWidth = 10;
		formLayout.spacing = 10;

		statsForm = new CategoryStatsForm(group, comp.getStyle());

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
		fd = setFormLayoutData(shelf, t, 0, SWT.TOP, t, 20, SWT.NONE);
		fd.width = 780;
		fd.height = 420;
	
		initDataBindings();

		return comp;
	}

	/**
	 * Binds form fields with selected item in the tree
	 * @return
	 */
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		final IObservableValue observeSelection = ViewersObservables
				.observeSingleSelection(tree.viewer);

		// "name" field
		IObservableValue w1 = SWTObservables.observeText(
				this.infoForm.name, SWT.Modify);
		IObservableValue v1 = BeansObservables
				.observeDetailValue(Realm.getDefault(),
						observeSelection, "name",
						java.lang.String.class);
		bindingContext.bindValue(w1, v1, null, null);

		// "description" field
		IObservableValue w2 = new StyledTextObservableValue(this.infoForm.desc, SWT.Modify);
		IObservableValue v2 = BeansObservables
				.observeDetailValue(Realm.getDefault(),
						observeSelection, "description",
						java.lang.String.class);
		bindingContext.bindValue(w2, v2, null, null);

		// "save" button
		IObservableValue isSavable = new ComputedValue(Boolean.TYPE) {
			protected Object calculate() {
				if ( observeSelection.getValue() == null ) {
					return Boolean.FALSE;
				}
				return Boolean.valueOf(((Category)observeSelection.getValue()).getParent() != null);
			}
		};
		bindingContext.bindValue(SWTObservables.observeEnabled(this.infoForm.btnSave),
				isSavable, null, null);

		// "+" button
		IObservableValue isAppendable = new ComputedValue(Boolean.TYPE) {
			protected Object calculate() {
				if ( observeSelection.getValue() == null ) {
					return Boolean.FALSE;
				}
				try {
					CategoryContentProvider.generateCode((Category)observeSelection.getValue());
				} catch (ChenanceDataException e1) {
					return Boolean.FALSE;
				}
				return Boolean.TRUE;
			}
		};
		bindingContext.bindValue(SWTObservables.observeEnabled(this.btnAdd),
				isAppendable, null, null);

		// "-" button
		IObservableValue isDeletable = new ComputedValue(Boolean.TYPE) {
			protected Object calculate() {
				if ( observeSelection.getValue() == null ) {
					return Boolean.FALSE;
				}
				return Boolean.valueOf(((Category)observeSelection.getValue()).getParent() != null);
			}
		};
		bindingContext.bindValue(SWTObservables.observeEnabled(this.btnDel),
				isDeletable, null, null);

		// stats id field
		IObservableValue v3 = BeansObservables
				.observeDetailValue(Realm.getDefault(),
						observeSelection, "id",
						java.lang.Integer.class);
		bindingContext.bindValue(this.statsForm.id, v3, null, null);

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
				prov.addItem();
				infoForm.name.selectAll();
				infoForm.name.setFocus();
			}
		});
	}
	
	private void removeButton(Button btn) {
		btn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				prov.removeItem(tree.getSelected());
			}
		});
	}
}
