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
package cn.sh.fang.chenance.provider;

import static cn.sh.fang.chenance.i18n.UIMessageBundle._;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import cn.sh.fang.chenance.ChenanceDataException;
import cn.sh.fang.chenance.data.dao.CategoryService;
import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.util.SWTUtil;

public class CategoryListContentProvider extends BaseProvider<Category>
		implements ITreeContentProvider {

	List<Category> tops;
	private TreeViewer viewer;
	private Category root;

	CategoryService cs = new CategoryService();

	public CategoryListContentProvider() {
		this.initData();
	}

	private void initData() {
		CategoryService service = new CategoryService();
		tops = service.getTops();
		this.root = new Category();
		root.setChildren(tops);
	}

	public Category getRoot() {
		return root;
	}

	public Object[] getElements(Object arg0) {
		return getChildren(arg0);
	}

	public Object[] getChildren(Object obj) {
		if (obj == null) {
			return null;
		}

		if (obj instanceof Category) {
			Category c = (Category) obj;
			return c.getChildren().toArray();
		}
		return new Object[0];
	}

	public Object getParent(Object arg0) {
		return ((Category) arg0).getParent();
	}

	public boolean hasChildren(Object arg0) {
		List<Category> c = ((Category) arg0).getChildren();
		return c != null && c.size() > 0;
	}

	public void dispose() {
		// TODO Auto-generated method stub
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) viewer;
		// if (oldInput != null) {
		// removeListenerFrom((Category) oldInput);
		// }
		// if (newInput != null) {
		// addListenerTo((Category) newInput);
		// }
	}

	public class AddCategorySelectionAdapter extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			super.widgetSelected(e);
			if (viewer.getSelection().isEmpty()) {
				SWTUtil
						.showErrorMessage(viewer.getControl().getShell(),
								"You cannot add root category.  Please select a category.");
				return;
			}

			Category parent = (Category) ((IStructuredSelection) viewer
					.getSelection()).getFirstElement();
			int code;
			try {
				code = generateCode(parent);
			} catch (ChenanceDataException e1) {
				SWTUtil
						.showErrorMessage(viewer.getControl().getShell(),
								"You cannot add into this category any more.  Please select another category.");
				return;
			}

			CategoryListContentProvider.this.addItem();
		}

	}

	public static Integer generateCode(Category parent) throws ChenanceDataException {
		int pid = parent.getCode();
		int i = parent.getChildren().size() + 1;
		if (i >= 100) {
			throw new ChenanceDataException();
		}
		if (pid % 1000000 == 0) {
			return pid + i * 10000;
		} else if (pid % 10000 == 0) {
			return pid + i * 100;
		} else {
			throw new ChenanceDataException();
		}
	}

	public class DelCategorySelectionAdapter extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			super.widgetSelected(e);
		}
	}

	@Override
	protected Category doAddItem() {
		Category parent = (Category)((IStructuredSelection) viewer
				.getSelection()).getFirstElement();
		int code = 0;
		try {
			code = generateCode(parent);
		} catch (ChenanceDataException e1) {
			// impossible
		}

		Category c = new Category();
		c.setCode(code);
		c.setParent(parent);
		c.setName(_("New Category"));
		c.setDescription(_("Description of New Category"));
		c.setUpdater("USER");
		cs.save(c);
		return c;
	}

	@Override
	protected Category doRemoveItem(Category t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Category doUpdateItem(Category t) {
		cs.save(t);
		return t;
	}

}
