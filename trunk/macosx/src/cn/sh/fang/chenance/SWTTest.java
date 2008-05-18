package cn.sh.fang.chenance;

import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutData;
import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutDataRight;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;

import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.listener.AccountEditFormListener;
import cn.sh.fang.chenance.listener.AccountListListener;
import cn.sh.fang.chenance.listener.BsAccountListListener;
import cn.sh.fang.chenance.listener.CategoryEditFormListener;
import cn.sh.fang.chenance.listener.CategoryListListener;
import cn.sh.fang.chenance.provider.AccountListProvider;
import cn.sh.fang.chenance.provider.CategoryListContentProvider;
import cn.sh.fang.chenance.provider.CategoryListLabelProvider;
import cn.sh.fang.chenance.util.SWTUtil;

public class SWTTest {

	static String filepath = System.getProperty("user.home") + "/chenance/db";

	public static EntityManagerFactory factory;

	static {
		HashMap<String, String> props = new HashMap<String, String>();
		props.put("hibernate.connection.url", "jdbc:h2:" + filepath);
		factory = Persistence
				.createEntityManagerFactory("chenance-data", props);
	}

	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="91,5"
	private CoolBar coolBar;
	private Menu menuBar;
	private TabFolder tabFolder;
	private Table table;

	private AccountListProvider accountListProv = new AccountListProvider();

	private CategoryEditForm categoryEditForm;

	private AccountList bsAccountList;

	public static void main(String[] args) throws InterruptedException {
		// TODO find a swt splash
		final Display display;
		SWTTest swt;
		try {
			display = new Display();
			swt = new SWTTest();
			swt.createSShell();
		} finally {
		}

		swt.sShell.open();

		try {
			while (!swt.sShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shutdown();
		}
		display.dispose();
	}

	public static void shutdown() {
		factory.close();
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell();
		sShell.setText("Chenance - Personal Finance Manager");
		sShell.setSize(1000, 400);

		createMenuBar();
		createToolBar();
		createControls();

		arrangeWidgetsLayout();
	}

	private void createMenuBar() {
		menuBar = new Menu(sShell, SWT.BAR);
		sShell.setMenuBar(menuBar);

		// File
		MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText(_("&File"));
		Menu fileMenu = new Menu(sShell, SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);
		MenuItem fileNewItem = new MenuItem(fileMenu, SWT.PUSH);
		fileNewItem.setText(_("&New"));
		// fileNewItem.addSelectionListener(new FileNewListener());
		MenuItem fileOpenItem = new MenuItem(fileMenu, SWT.PUSH);
		fileOpenItem.setText(_("&Open"));
		// fileOpenItem.addSelectionListener(new FileOpenListener());
		MenuItem fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
		fileSaveItem.setText(_("&Save"));
		// fileSaveItem.addSelectionListener(new FileSaveListener());

		// Edit
		MenuItem menuItem2 = new MenuItem(menuBar, SWT.CASCADE);
		menuItem2.setText(_("&Edit"));

		// View
		MenuItem viewMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		viewMenuHeader.setText(_("&View"));
		Menu viewMenu = new Menu(sShell, SWT.DROP_DOWN);
		viewMenuHeader.setMenu(viewMenu);
		MenuItem viewBsItem = new MenuItem(viewMenu, SWT.RADIO);
		viewBsItem.setText(_("Balance Sheet"));
		// viewBsItem.addSelectionListener(new
		// ChangeLanguageListener(Locale.ENGLISH));
		MenuItem viewInvItem = new MenuItem(viewMenu, SWT.RADIO);
		viewInvItem.setText(_("Investment History"));
		MenuItem viewAssetItem = new MenuItem(viewMenu, SWT.RADIO);
		viewAssetItem.setText(_("Asset Management"));

		// Lang
		MenuItem langMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		langMenuHeader.setText(_("&Language"));
		Menu langMenu = new Menu(sShell, SWT.DROP_DOWN);
		langMenuHeader.setMenu(langMenu);
		MenuItem langEnItem = new MenuItem(langMenu, SWT.RADIO);
		langEnItem.setText(_("English"));
		// langEnItem.addSelectionListener(new
		// ChangeLanguageListener(Locale.ENGLISH));
		MenuItem langJaItem = new MenuItem(langMenu, SWT.RADIO);
		langJaItem.setText(_("日本語"));
		// langJaItem.addSelectionListener(new
		// ChangeLanguageListener(Locale.JAPANESE));
		MenuItem langZhItem = new MenuItem(langMenu, SWT.RADIO);
		langZhItem.setText(_("简体中文"));
		// langZhItem.addSelectionListener(new
		// ChangeLanguageListener(Locale.CHINESE));

		// Help
		MenuItem menuItem4 = new MenuItem(menuBar, SWT.CASCADE);
		menuItem4.setText(_("&Help"));
	}

	/**
	 * This method initializes toolBar
	 * 
	 */
	private void createToolBar() {
		coolBar = new CoolBar(sShell, SWT.NONE);

		CoolItem coolItem = new CoolItem(coolBar, SWT.PUSH);
		ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT);
		coolItem.setControl(toolBar);
		ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);
		toolItem.setText("A");
		toolBar.pack();
		Point size = toolBar.getSize();
		coolItem.setControl(toolBar);
		Point preferred = coolItem.computeSize(size.x, size.y);
		coolItem.setPreferredSize(new org.eclipse.swt.graphics.Point(15, 26));
	}

	private void arrangeWidgetsLayout() {
		FormLayout layout = new FormLayout();
		layout.marginHeight = 0;
		sShell.setLayout(layout);

		FormData coolData = new FormData();
		coolData.left = new FormAttachment(0);
		coolData.right = new FormAttachment(100);
		coolData.top = new FormAttachment(0);
		coolBar.setLayoutData(coolData);
		coolBar.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				sShell.layout();
			}
		});

		FormData textData = new FormData();
		textData.left = new FormAttachment(0);
		textData.right = new FormAttachment(100);
		textData.top = new FormAttachment(coolBar);
		textData.bottom = new FormAttachment(100);
		tabFolder.setLayoutData(textData);

		sShell.setSize(this.tabFolder.computeSize(1000, 600));
	}

	private void createControls() {
		createTabFolder();
	}

	/**
	 * This method initializes tabFolder
	 * 
	 */
	private void createTabFolder() {
		tabFolder = new TabFolder(sShell, SWT.TOP | SWT.BORDER);
		TabItem item1 = new TabItem(tabFolder, SWT.NULL);
		item1.setText(_("Balance"));
		item1.setControl(getBalanceSheetTabControl(tabFolder));
		TabItem item2 = new TabItem(tabFolder, SWT.NULL);
		item2.setText(_("Category"));
		item2.setControl(getCategoryTabControl(tabFolder));
		TabItem item3 = new TabItem(tabFolder, SWT.NULL);
		item3.setText(_("Accounts"));
		 item3.setControl(getAccountTabControl(tabFolder));
		tabFolder.setSize(sShell.getSize());
	}

	private Control getBalanceSheetTabControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
	
		// 口座ツリー
		TableTree tableTree = new TableTree(composite, SWT.BORDER
				| SWT.FULL_SELECTION);
		bsAccountList = new AccountList(tableTree, accountListProv);
		bsAccountList.createControl();
		Table tttable = tableTree.getTable();
		tttable.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				if (e.button == 1) {
					Table t = (Table) e.widget;
					TableItem i = t.getItem(new Point(e.x, e.y));
					System.out.println(i + " was d-clicked");
				}
			}
		});
	
		// 日付
		Text listDate = new Text(composite, SWT.READ_ONLY | SWT.BORDER);
		listDate.setText("2008/01/01");
		FontData fd = composite.getFont().getFontData()[0];
		Font newFont = new Font(sShell.getDisplay(), new FontData(fd.getName(),
				(int) (fd.getHeight() * 1.5), fd.getStyle()));
		listDate.setFont(newFont);
	
		Button today = new Button(composite, SWT.NONE);
		today.setText("Today");
	
		Button oneDay = new Button(composite, SWT.FLAT);
		oneDay.setText("Day");
		Button oneWeek = new Button(composite, SWT.FLAT);
		oneWeek.setText("Week");
		Button oneMonth = new Button(composite, SWT.FLAT);
		oneMonth.setText("Month");
		Button customDur = new Button(composite, SWT.FLAT);
		customDur.setText("期間指定");
	
		// バランスシート
		table = new Table(composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		// カラム
		TableColumn[] cols = new TableColumn[6];
		for (int i = 0; i < 6; i++) {
			cols[i] = new TableColumn(table, SWT.NONE);
			cols[i].setWidth(100);
		}
		cols[0].setText("日付");
		cols[0].setAlignment(SWT.CENTER);
		cols[1].setText("費目");
		cols[1].setWidth(150);
		cols[2].setText("支払");
		cols[2].setAlignment(SWT.RIGHT);
		cols[3].setText("預入");
		cols[3].setAlignment(SWT.RIGHT);
		cols[4].setText("残高");
		cols[5].setText("詳細");
		// リスト
		// createTableViewer();
		cols[0].pack();
		// ポップアップメニュー
		Menu menu = new Menu(sShell, SWT.POP_UP);
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText("Popup");
		table.setMenu(menu);
	
		// ボタン
		Button btnAdd = new Button(composite, SWT.NULL);
		btnAdd.setText("追加");
		btnAdd.addSelectionListener(new SelectionAdapter() {
			// Add a task to the ExampleTaskList and refresh the view
			public void widgetSelected(SelectionEvent e) {
				// bs.addItem();
			}
		});
//		bs.addChangeListener(new BalanceSheetTransactionListener(tableViewer));
	
		// 残高ラベル
		Label total = new Label(composite, SWT.RIGHT);
		total.setText("￥0");
		Label label = new Label(composite, SWT.NONE);
		label.setText("残高: ");
	
		// レイアウト
		FormLayout formLayout = new FormLayout();
		composite.setLayout(formLayout);
		formLayout.marginHeight = 10;
		formLayout.marginWidth = 10;
	
		SWTUtil.setFormLayoutData(listDate, 0, 10, 0, 10).width = 105;
		SWTUtil.setFormLayoutData(today, listDate, 0, SWT.TOP, listDate, 20,
				SWT.NONE).width = 80;
	
		FormData layoutData = SWTUtil.setFormLayoutData(tableTree, listDate,
				10, SWT.NONE, listDate, 0, SWT.LEFT);
		layoutData.height = 400;
//		layoutData.width = 175;
	
		SWTUtil.setFormLayoutDataRight(customDur, listDate, 0, SWT.TOP, table,
				0, SWT.RIGHT).width = 80;
		SWTUtil.setFormLayoutDataRight(oneMonth, listDate, 0, SWT.TOP,
				customDur, -20, SWT.LEFT).width = 80;
		SWTUtil.setFormLayoutDataRight(oneWeek, listDate, 0, SWT.TOP, oneMonth,
				-20, SWT.LEFT).width = 80;
		SWTUtil.setFormLayoutDataRight(oneDay, listDate, 0, SWT.TOP, oneWeek,
				-20, SWT.LEFT).width = 80;
	
		SWTUtil.setFormLayoutData(table, listDate, 10, tableTree, 20).height = 400;
		table.setSize(tabFolder.getSize());
	
		SWTUtil.setFormLayoutData(btnAdd, table, 0, SWT.TOP, table, 10,
				SWT.NONE).width = 80;
		SWTUtil.setFormLayoutDataRight(total, table, 10, SWT.NONE, table, -20,
				SWT.RIGHT).width = 80;
		SWTUtil.setFormLayoutDataRight(label, table, 10, SWT.NONE, total, -100,
				SWT.RIGHT);
	
		return composite;
	}

	private Control getAccountTabControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);

		// 概要ツリー
		final TableTree tableTree = new TableTree(composite, SWT.BORDER
				| SWT.FULL_SELECTION);
		 AccountList accountList = new AccountList(tableTree,
		 this.accountListProv );
		 accountList.createControl();

		// 追加ボタン
		Button btnAdd = new Button(composite, SWT.PUSH);
		btnAdd.setText("＋");
		Button btnDel = new Button(composite, SWT.PUSH);
		btnDel.setText("－");

		// フォーム
		 AccountEditForm accountForm = new AccountEditForm();
		 Group grp = (Group)accountForm.createControl(composite);

		// イベント
		accountListProv.addChangeListener(new AccountEditFormListener(accountForm));
		accountListProv.addChangeListener(new AccountListListener(tableTree));
		accountListProv.addChangeListener(new BsAccountListListener(bsAccountList));
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				 accountListProv.addItem();
			}
		});
		// btnDel.addSelectionListener(new
		// DelAccountSelectionAdapter(tableTree));
		tableTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.item.getData() instanceof Account) {
					 accountListProv.itemChanged((Account) e.item.getData());
				} else {
					e.doit = true;
				}
			}
		});
		 accountForm.btnSave.addSelectionListener(new SelectionAdapter() {
		 @Override
		 public void widgetSelected(SelectionEvent e) {
			 accountListProv.itemChanged((Account)e.widget.getData());
		 }
		 });

		// レイアウト
		FormLayout formLayout = new FormLayout();
		composite.setLayout(formLayout);
		formLayout.marginHeight = 10;
		formLayout.marginWidth = 10;

		FormData fd = SWTUtil.setFormLayoutData(tableTree, 0, 0, 0, 10);
		fd.height = 400;
//		fd.width = 175;

		fd = SWTUtil.setFormLayoutDataRight(btnDel, tableTree, 2, SWT.NONE,
				tableTree, 0, SWT.RIGHT);
		fd.width = fd.height;
		fd = SWTUtil.setFormLayoutDataRight(btnAdd, tableTree, 2, SWT.NONE,
				btnDel, 0, SWT.NONE);
		fd.width = fd.height;

		setFormLayoutData(grp, 0, 0, tableTree, 20);

		return composite;
	}

	private Control getCategoryTabControl(TabFolder tabFolder) {
			Composite comp = new Composite(tabFolder, SWT.NONE);
	
			// ツリー
			TreeViewer treeViewer = new TreeViewer(comp, SWT.BORDER | SWT.SINGLE);
			Tree tree = treeViewer.getTree();
			final CategoryListContentProvider prov = new CategoryListContentProvider();
			treeViewer.setContentProvider(prov);
			treeViewer.setLabelProvider(new CategoryListLabelProvider());
			treeViewer.setInput(prov.getRoot());
			treeViewer.expandAll();
			ColumnViewerToolTipSupport.enableFor(treeViewer);
	
			// 追加削除ボタン
			Button btnAdd = new Button(comp, SWT.PUSH);
			btnAdd.setText("＋");
			Button btnDel = new Button(comp, SWT.PUSH);
			btnDel.setText("－");
	
			btnAdd.addSelectionListener(prov.new AddCategorySelectionAdapter());
			btnDel.addSelectionListener(prov.new DelCategorySelectionAdapter());
			treeViewer.addSelectionChangedListener(new ISelectionChangedListener(){
				public void selectionChanged(SelectionChangedEvent e) {
					if (e.getSelection() != null) {
						Category c = (Category) ((IStructuredSelection) e.getSelection())
								.getFirstElement();
						if (c != null) {
							prov.itemChanged(c);
						}
					}
				}
			});
	
			// 編集フォーム
			Group group = new Group(comp, SWT.NONE);
			group.setText(_("Cagetory Info"));
			FormLayout formLayout = new FormLayout();
			group.setLayout(formLayout);
			formLayout.marginHeight = 10;
			formLayout.marginWidth = 10;
			categoryEditForm = new CategoryEditForm(group, comp.getStyle());
			categoryEditForm.btnSave.addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					super.widgetSelected(e);
					prov.itemChanged((Category)e.widget.getData());
				}
			});
	//		group.pack();
	
			prov.addChangeListener(new CategoryEditFormListener(categoryEditForm));
			prov.addChangeListener(new CategoryListListener(treeViewer));
			
			// レイアウト
			formLayout = new FormLayout();
			comp.setLayout(formLayout);
			formLayout.marginHeight = 10;
			formLayout.marginWidth = 10;
	 
			FormData fd = setFormLayoutData(tree, 0, 0, 0, 10);
			fd.height = 400;
			fd.width = 175;
			fd = setFormLayoutDataRight(btnDel, tree, 2, SWT.NONE, tree, 0, SWT.RIGHT);
			fd.width = fd.height;
			fd = setFormLayoutDataRight(btnAdd, tree, 2, SWT.NONE, btnDel, 0, SWT.NONE);
			fd.width = fd.height;
			setFormLayoutData(group, tree, 0, SWT.TOP, tree, 20, SWT.NONE);
	
			return comp;
		}

	public static String _(String s) {
		return s;
	}

}
