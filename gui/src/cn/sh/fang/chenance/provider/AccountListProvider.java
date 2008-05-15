package cn.sh.fang.chenance.provider;

import static cn.sh.fang.chenance.util.UIMessageBundle._;

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
import cn.sh.fang.chenance.util.swt.SWTUtil;

public class AccountListProvider extends BaseProvider<Account> {

	AccountService service = new AccountService();

	List<Account> accounts;
	
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
		parent.setGrayed(true);
		items = new HashMap<Integer,TableTreeItem>();
		sum = new TableTreeItem(tableTree, SWT.NONE);
		sum.setText(0, "残高の合計");
		sum.setGrayed(true);
		
		updateList();

		parent.setExpanded(true);

		col1.pack();
		col1.setResizable(true);
		col1.setToolTipText("Double-click to view the balance");
		col1.setWidth(col1.getWidth() + 20);
//		col2.pack();
		col2.setWidth(80);
		col2.setResizable(false);

		FontData fd = parent.getFont().getFontData()[0];
		Font newFont = new Font(tableTree.getDisplay(), new FontData(fd.getName(),
				fd.getHeight(), fd.getStyle() | SWT.BOLD));
		parent.setFont(newFont);
		sum.setFont(newFont);
		
		return table;
	}

	private void initData() {
		this.accounts = service.findAll();
	}
	
	public void updateList() {
		initData();
		
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
	
	@Override
	protected Account doAddItem() {
		Account a = new Account();
		a.setDescription("");
		a.setCurrentBalance(0);
		a.setUpdater("USER");
		// TODO make this more clever(if i can)
		int i;
		for ( i = 1; i < 5000; i++ ) {
			String name = _("New Account {0}",i);
			if ( this.service.isUsableName(name) ) {
				a.setName(name);
				break;
			}
		}
		if ( i == 50 ) {
			return null;
		}
		this.service.save(a);
		return a;
	}

	@Override
	protected Account doRemoveItem(Account t) {
		service.remove(t.getId(), "USER");
		accounts.remove(t);
		return t;
	}
	
	@Override
	protected Account doUpdateItem(Account t) {
		service.save(t);
		return t;
	}
}
