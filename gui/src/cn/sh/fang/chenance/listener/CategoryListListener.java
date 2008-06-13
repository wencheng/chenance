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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import cn.sh.fang.chenance.data.entity.Category;

public class CategoryListListener implements IDataAdapter<Category> {

	private TreeViewer tree;

	public CategoryListListener(TreeViewer tree) {
		this.tree = tree;
	}

	public void onAdded(Category item) {
		Category parent = (Category) ((IStructuredSelection) tree
				.getSelection()).getFirstElement();

		parent.appendChild(item);
		tree.add(parent, item);
		tree.expandToLevel(parent, 1);
		tree.setSelection(new StructuredSelection(item), true);
	}

	public void onRemoved(Category item) {
		tree.remove(item);
	}

	public void onUpdated(Category item) {
		tree.refresh(item);
	}

	public void onLoaded(Category item) {
		// TODO Auto-generated method stub
		
	}

}
