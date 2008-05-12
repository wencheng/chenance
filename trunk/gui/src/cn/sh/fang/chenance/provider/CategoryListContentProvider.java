package cn.sh.fang.chenance.provider;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import cn.sh.fang.chenance.data.dao.AccountService;
import cn.sh.fang.chenance.data.dao.CategoryService;
import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.listener.AccountTabListener.AccountListMouseAdapter;
import cn.sh.fang.chenance.util.swt.SWTUtil;

public class CategoryListContentProvider implements ITreeContentProvider {

	
	List<Category> tops;
	private TreeViewer viewer;
	private Category root;

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

	public Table createControl(TableTree tableTree) {
		Table table = tableTree.getTable();
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		table.addMouseListener(new AccountListMouseAdapter());

		AccountService service = new AccountService();
		List<Account> accounts = service.findAll();

		TableColumn col1 = new TableColumn(table, SWT.LEFT);
		TableColumn col2 = new TableColumn(table, SWT.RIGHT);
		TableTreeItem parent = new TableTreeItem(tableTree, SWT.NONE);
		parent.setText(0, "口座");
		parent.setText(1, "");
		int balanceSum = 0;
		for (Account a : accounts) {
			TableTreeItem child = new TableTreeItem(parent, SWT.NONE);
			child.setText(0, a.getName());
			child.setText(1, a.getCurrentBalance() + "");
			child.setData(a);
			balanceSum += a.getCurrentBalance();
		}
		parent.setExpanded(true);
		TableTreeItem sum = new TableTreeItem(tableTree, SWT.NONE);
		sum.setText(0, "残高の合計");
		sum.setText(1, balanceSum + "");
		col1.pack();
		col1.setResizable(false);
		// col1.setWidth(col1.getWidth() + 20);
		col2.pack();
		col2.setWidth(80);
		col2.setResizable(false);

		FontData fd = parent.getFont().getFontData()[0];
		Font newFont = new Font(tableTree.getDisplay(), new FontData(fd
				.getName(), fd.getHeight(), fd.getStyle() | SWT.BOLD));
		parent.setFont(newFont);
		sum.setFont(newFont);

		return table;
	}

	public Object[] getElements(Object arg0) {
		return getChildren(arg0);
	}

	public Object[] getChildren(Object obj) {
		if ( obj == null ) {
			return null;
		}
		
		if (obj instanceof Category) {
			Category c = (Category) obj;
			System.out.println(c);
			System.out.println(c.getChildren());
			return c.getChildren().toArray();
		}
		return new Object[0];
	}

	public Object getParent(Object arg0) {
		return ((Category)arg0).getParent();
	}

	public boolean hasChildren(Object arg0) {
		return ((Category)arg0).getChildren() != null;
	}

	public void dispose() {
		// TODO Auto-generated method stub
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) viewer;
//		if (oldInput != null) {
//			removeListenerFrom((Category) oldInput);
//		}
//		if (newInput != null) {
//			addListenerTo((Category) newInput);
//		}
	}

	public class AddCategorySelectionAdapter extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			super.widgetSelected(e);

			if ( viewer.getSelection().isEmpty() ) {
				SWTUtil.showErrorMessage(viewer.getControl().getShell(),
						"You cannot add root category.  Please select a category.");
				return;
			}
			
			Category parent = (Category)((IStructuredSelection)viewer.getSelection()).getFirstElement();
			Category c = new Category();
			c.setParent(parent);
			c.setName("New category");
			viewer.add(parent, c);
		}
	}
	
	public class DelCategorySelectionAdapter extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			super.widgetSelected(e);
		}
		
	}

}
