package cn.sh.fang.chenance.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import cn.sh.fang.chenance.data.dao.AccountService;
import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.listener.AccountListListener.AccountListMouseAdapter;

public class AccountListProvider {
	
	Table table;
	
	// Account.id <-> TableTreeItem
	HashMap<Integer,TableTreeItem> items = null;

	private TableTree tableTree;

	private TableTreeItem sum;

	public AccountListProvider(TableTree tableTree) {
		this.tableTree = tableTree;
	}
	
	public Table createControl() {
		table = tableTree.getTable();
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		table.addMouseListener(new AccountListMouseAdapter());

		TableColumn col1 = new TableColumn(table, SWT.LEFT);
		TableColumn col2 = new TableColumn(table, SWT.RIGHT);
		TableTreeItem parent = new TableTreeItem(tableTree, SWT.NONE);
		parent.setText(0, "口座");
		parent.setText(1, "");
		items = new HashMap<Integer,TableTreeItem>();
		sum = new TableTreeItem(tableTree, SWT.NONE);
		sum.setText(0, "残高の合計");

		updateList();

		parent.setExpanded(true);

		col1.pack();
		col1.setResizable(false);
		// col1.setWidth(col1.getWidth() + 20);
		col2.pack();
		col2.setWidth(80);
		col2.setResizable(false);

		FontData fd = parent.getFont().getFontData()[0];
		Font newFont = new Font(tableTree.getDisplay(), new FontData(fd.getName(),
				fd.getHeight(), fd.getStyle() | SWT.BOLD));
		parent.setFont(newFont);
		sum.setFont(newFont);
		
		return table;
	}
	
	public void updateList() {
		AccountService service = new AccountService();
		List<Account> accounts = service.findAll();

		TableTreeItem i;
		int balanceSum = 0;
		for ( Account a : accounts ) {
			i = items.get(a.getId());
			if ( i == null ) {
				i = new TableTreeItem(this.tableTree.getItem(0), SWT.NONE);
				balanceSum += a.getCurrentBalance();
				items.put(a.getId(), i);
			}
			i.setText(0, a.getName());
			i.setText(1, a.getCurrentBalance() + "");
			i.setData(a);
		}
		sum.setText(1, balanceSum + "");
	}
	
}
