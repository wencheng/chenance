package cn.sh.fang.chenance.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import cn.sh.fang.chenance.data.dao.AccountService;
import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.listener.AccountTabListener.AccountListMouseAdapter;

public class CategoryListContentProvider implements ITreeContentProvider {

	ArrayList<Category> cats = new ArrayList<Category>();

	public CategoryListContentProvider() {
		this.initData();
	}

	private void initData() {
		O
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

	@Override
	public Object[] getElements(Object arg0) {
		return getChildren(arg0);
	}

	@Override
	public Object[] getChildren(Object obj) {
		if (obj instanceof Category) {
			Category c = (Category) obj;
			return c.getChildren().toArray();
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object arg0) {
		return ((Category)arg0).getParent();
	}

	@Override
	public boolean hasChildren(Object arg0) {
		return ((Category)arg0).getChildren() != null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) viewer;
		if (oldInput != null) {
			removeListenerFrom((Category) oldInput);
		}
		if (newInput != null) {
			addListenerTo((Category) newInput);
		}
	}

}
