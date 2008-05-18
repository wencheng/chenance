package cn.sh.fang.chenance.listener;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import cn.sh.fang.chenance.data.entity.Category;

public class CategoryListListener implements IItemChangeListener<Category> {

	private TreeViewer tree;

	public CategoryListListener(TreeViewer tree) {
		this.tree = tree;
	}

	public void itemAdded(Category item) {
		Category parent = (Category) ((IStructuredSelection) tree
				.getSelection()).getFirstElement();

		parent.appendChild(item);
		tree.add(parent, item);
		tree.expandToLevel(parent, 1);
		tree.setSelection(new StructuredSelection(item), true);
	}

	public void itemRemoved(Category item) {
		tree.remove(item);
	}

	public void itemUpdated(Category item) {
		tree.refresh(item);
	}

}
