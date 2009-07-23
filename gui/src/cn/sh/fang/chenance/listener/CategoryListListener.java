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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.TreeItem;

import cn.sh.fang.chenance.CategoryTree;
import cn.sh.fang.chenance.ChenanceDataException;
import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.provider.CategoryListContentProvider;

public class CategoryListListener implements IDataAdapter<Category> {

	final static Logger LOG = Logger.getLogger(CategoryListListener.class);

	CategoryTree tree;

	public CategoryListListener(CategoryTree t) {
		this.tree = t;
	}

	public void onAdded(Category item) {
		Category parent = tree.getSelected();

		int code = 0;
		try {
			code = CategoryListContentProvider.generateCode(parent);
		} catch (ChenanceDataException e1) {
			// impossible
		}
		item.setCode(code);
		item.setParent(parent);
		
		List<Category> list = parent.getChildren();
		list.add(item);
		parent.setChildren(list);
//		parent.appendChildren(item);

		tree.viewer.setSelection(new StructuredSelection(item));
	}

	public void onLoaded(Category item) {
		// TODO Auto-generated method stub
		
	}

	public void onRemoved(Category item) {
		TreeItem selectedItem = tree.viewer.getTree().getSelection()[0];
		TreeItem parentItem = selectedItem.getParentItem();
		if (parentItem == null) {
//			SWTUtil
//			.showErrorMessage(tree.viewer.getControl().getShell(),
//					"You cannot delete root category.");
//			return;
		}

		Category parent = (Category) parentItem.getData();
		List<Category> list = parent.getChildren();
		list.remove(item);
		LOG.debug("remove category: " + item);
		parent.setChildren(list);
	}

	public void onUpdated(Category item) {
		tree.viewer.refresh();
	}

}
