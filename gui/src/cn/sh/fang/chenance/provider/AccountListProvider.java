package cn.sh.fang.chenance.provider;

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
import cn.sh.fang.chenance.listener.AccountTabListener.AccountListMouseAdapter;

public class AccountListProvider {

	public AccountListProvider() {
	}
	
	public Table createControl(TableTree tableTree) {
		Table accountListTable = tableTree.getTable();
		accountListTable.setHeaderVisible(false);
		accountListTable.setLinesVisible(false);
		accountListTable.addMouseListener(new AccountListMouseAdapter());

		AccountService service = new AccountService();
		List<Account> accounts = service.findAll();

		TableColumn col1 = new TableColumn(accountListTable, SWT.LEFT);
		TableColumn col2 = new TableColumn(accountListTable, SWT.RIGHT);
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
		Font newFont = new Font(tableTree.getDisplay(), new FontData(fd.getName(),
				fd.getHeight(), fd.getStyle() | SWT.BOLD));
		parent.setFont(newFont);
		sum.setFont(newFont);
		
		return accountListTable;
	}
}
