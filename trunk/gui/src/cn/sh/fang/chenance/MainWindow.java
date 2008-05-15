package cn.sh.fang.chenance;

import static cn.sh.fang.chenance.util.UIMessageBundle._;
import static cn.sh.fang.chenance.util.swt.SWTUtil.setFormLayoutData;
import static cn.sh.fang.chenance.util.swt.SWTUtil.setFormLayoutDataRight;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
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
import org.eclipse.swt.widgets.MessageBox;
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

import cn.sh.fang.chenance.data.dao.BaseService;
import cn.sh.fang.chenance.data.dao.CategoryService;
import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.listener.AccountEditFormListener;
import cn.sh.fang.chenance.listener.AccountListListener;
import cn.sh.fang.chenance.listener.BalanceSheetTransactionListener;
import cn.sh.fang.chenance.listener.BsAccountListListener;
import cn.sh.fang.chenance.listener.CategoryEditFormListener;
import cn.sh.fang.chenance.listener.CategoryListListener;
import cn.sh.fang.chenance.listener.ChangeLanguageListener;
import cn.sh.fang.chenance.listener.AccountListListener.DelAccountSelectionAdapter;
import cn.sh.fang.chenance.listener.FileListener.FileNewListener;
import cn.sh.fang.chenance.listener.FileListener.FileOpenListener;
import cn.sh.fang.chenance.listener.FileListener.FileSaveListener;
import cn.sh.fang.chenance.provider.AccountListProvider;
import cn.sh.fang.chenance.provider.BalanceSheetCellModifier;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider;
import cn.sh.fang.chenance.provider.BalanceSheetDetailCellEditor;
import cn.sh.fang.chenance.provider.BalanceSheetLabelProvider;
import cn.sh.fang.chenance.provider.CategoryComboCellEditor;
import cn.sh.fang.chenance.provider.CategoryListContentProvider;
import cn.sh.fang.chenance.provider.CategoryListLabelProvider;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider.Column;
import cn.sh.fang.chenance.util.swt.CalendarCellEditor;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class MainWindow {

	final static Logger LOG = Logger.getLogger(MainWindow.class.getName());

	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="91,5"
	private Menu menuBar = null;
	private CoolBar coolBar = null;
	private TabFolder tabFolder = null;

	private Table table;
	private TableViewer tableViewer;
	Table accountListTable;

	BalanceSheetContentProvider bs = new BalanceSheetContentProvider();
	final AccountListProvider accountListProv = new AccountListProvider();

	private CategoryEditForm categoryEditForm;

	private AccountList bsAccountList;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//SplashScreen splash= new SplashScreen("cn/sh/fang/chenance/splash.gif");
		Display display = Display.getDefault();
		MainWindow thisClass = new MainWindow();
		try {
			BaseService.createTable();
			thisClass.createSShell();
		} catch (Exception e) {
			e.printStackTrace();
			Shell shell = new Shell(display);
			MessageBox mb = new MessageBox(shell);
			mb.setMessage(e.getMessage());
			mb.open();
			display.dispose();
			return;
		} finally {
			//splash.close();
		}

		thisClass.sShell.open();

		try {
			while (!thisClass.sShell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BaseService.shutdown();
		}
		display.dispose();
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
		fileNewItem.addSelectionListener(new FileNewListener());
		MenuItem fileOpenItem = new MenuItem(fileMenu, SWT.PUSH);
		fileOpenItem.setText(_("&Open"));
		fileOpenItem.addSelectionListener(new FileOpenListener());
		MenuItem fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
		fileSaveItem.setText(_("&Save"));
		fileSaveItem.addSelectionListener(new FileSaveListener());

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
//		viewBsItem.addSelectionListener(new ChangeLanguageListener(Locale.ENGLISH));
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
		langEnItem.addSelectionListener(new ChangeLanguageListener(Locale.ENGLISH));
		MenuItem langJaItem = new MenuItem(langMenu, SWT.RADIO);
		langJaItem.setText(_("日本語"));
		langJaItem.addSelectionListener(new ChangeLanguageListener(Locale.JAPANESE));
		MenuItem langZhItem = new MenuItem(langMenu, SWT.RADIO);
		langZhItem.setText(_("简体中文"));
		langZhItem.addSelectionListener(new ChangeLanguageListener(Locale.CHINESE));

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
		for (int i = 0; i < Column.LAST.ordinal(); i++) {
			cols[i] = new TableColumn(table, SWT.NONE);
			cols[i].setWidth(100);
		}
		cols[Column.DATE.ordinal()].setText("日付");
		cols[Column.DATE.ordinal()].setAlignment(SWT.CENTER);
		cols[Column.CATEGORY.ordinal()].setText("費目");
		cols[Column.CATEGORY.ordinal()].setWidth(150);
		cols[Column.DEBIT.ordinal()].setText("支払");
		cols[Column.DEBIT.ordinal()].setAlignment(SWT.RIGHT);
		cols[Column.CREDIT.ordinal()].setText("預入");
		cols[Column.CREDIT.ordinal()].setAlignment(SWT.RIGHT);
		cols[Column.BALANCE.ordinal()].setText("残高");
		cols[Column.DETAIL.ordinal()].setText("詳細");
		// リスト
		createTableViewer();
		cols[Column.DATE.ordinal()].pack();
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
				bs.addItem();
			}
		});
		bs.addChangeListener(new BalanceSheetTransactionListener(tableViewer));

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
	
		setFormLayoutData(listDate, 0, 10, 0, 10).width = 105;
		setFormLayoutData(today, listDate, 0, SWT.TOP, listDate, 20, SWT.NONE).width = 80;
	
		FormData layoutData = setFormLayoutData(tableTree, listDate, 10,
				SWT.NONE, listDate, 0, SWT.LEFT);
		layoutData.height = 400;
		layoutData.width = 175;
	
		setFormLayoutDataRight(customDur, listDate, 0, SWT.TOP, table, 0,
				SWT.RIGHT).width = 80;
		setFormLayoutDataRight(oneMonth, listDate, 0, SWT.TOP, customDur, -20,
				SWT.LEFT).width = 80;
		setFormLayoutDataRight(oneWeek, listDate, 0, SWT.TOP, oneMonth, -20,
				SWT.LEFT).width = 80;
		setFormLayoutDataRight(oneDay, listDate, 0, SWT.TOP, oneWeek, -20,
				SWT.LEFT).width = 80;
	
		setFormLayoutData(table, listDate, 10, tableTree, 20).height = 400;
		table.setSize(tabFolder.getSize());
	
		setFormLayoutData(btnAdd, table, 0, SWT.TOP, table, 10, SWT.NONE).width = 80;
		setFormLayoutDataRight(total, table, 10, SWT.NONE, table, -20,
				SWT.RIGHT).width = 80;
		setFormLayoutDataRight(label, table, 10, SWT.NONE, total, -100,
				SWT.RIGHT);
	
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

	private Control getAccountTabControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);

		// 概要ツリー
		final TableTree tableTree = new TableTree(composite, SWT.BORDER
				| SWT.FULL_SELECTION);
		AccountList accountList = new AccountList(tableTree, accountListProv);
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
		btnAdd.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				accountListProv.addItem();
			}
		});
		btnDel.addSelectionListener(new DelAccountSelectionAdapter(tableTree));
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

		FormData fd = setFormLayoutData(tableTree, 0, 0, 0, 10);
		fd.height = 400;
		fd.width = 175;

		fd = setFormLayoutDataRight(btnDel, tableTree, 2, SWT.NONE, tableTree, 0, SWT.RIGHT);
		fd.width = fd.height;
		fd = setFormLayoutDataRight(btnAdd, tableTree, 2, SWT.NONE, btnDel, 0, SWT.NONE);
		fd.width = fd.height;
		
		setFormLayoutData(grp, 0, 0, tableTree, 20);

		return composite;
	}

	private void createTableViewer() {
		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);

		tableViewer.setColumnProperties(Column.stringValues());

		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
				tableViewer) {
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				if (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION) {
					MouseEvent e = ((MouseEvent) event.sourceEvent);
					return e.button == 1;
				} else if (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION) {
					ViewerCell cell = (ViewerCell) event.getSource();
					return cell.getColumnIndex() == Column.DETAIL.ordinal();
				} else {
					return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
							|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
				}
			}
		};

		TableViewerEditor.create(tableViewer, actSupport,
				ColumnViewerEditor.TABBING_HORIZONTAL
						// | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_CYCLE_IN_ROW
						| ColumnViewerEditor.TABBING_VERTICAL
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);

		// Create the cell editors
		CellEditor[] editors = new CellEditor[Column.values().length];

		// editors[0] = new CheckboxCellEditor(table);
		CalendarCellEditor dateEditor = new CalendarCellEditor(table, SWT.NULL);
		editors[Column.DATE.ordinal()] = dateEditor;

		CategoryService service = new CategoryService();
		List<Category> categoryList = service.findAll();
		tableViewer.setData("categoryList", categoryList);
		CategoryComboCellEditor e = new CategoryComboCellEditor(table);
		e.setItems(categoryList);
		editors[Column.CATEGORY.ordinal()] = e;

		TextCellEditor textEditor = new TextCellEditor(table);
		((Text) textEditor.getControl()).setTextLimit(9);
		((Text) textEditor.getControl())
				.addVerifyListener(new VerifyListener() {
					public void verifyText(VerifyEvent e) {
						e.doit = "0123456789".indexOf(e.text) >= 0;
					}
				});
		editors[Column.DEBIT.ordinal()] = textEditor;

		textEditor = new TextCellEditor(table);
		((Text) textEditor.getControl())
				.addVerifyListener(new VerifyListener() {
					public void verifyText(VerifyEvent e) {
						// Here, we could use a RegExp such as the following
						// if using JRE1.4 such as e.doit =
						// e.text.matches("[\\-0-9]*");
						e.doit = "0123456789".indexOf(e.text) >= 0;
					}
				});
		editors[Column.CREDIT.ordinal()] = textEditor;

		editors[Column.DETAIL.ordinal()] = new BalanceSheetDetailCellEditor(table);

		// Assign the cell editors to the viewer
		tableViewer.setCellEditors(editors);
		// Set the cell modifier for the viewer
		tableViewer.setCellModifier(new BalanceSheetCellModifier(tableViewer));
		// Set the default sorter for the viewer
		// tableViewer.setSorter(new ExampleTaskSorter(
		// ExampleTaskSorter.DESCRIPTION));

		tableViewer.setContentProvider(bs);
		tableViewer.setLabelProvider(new BalanceSheetLabelProvider(table));
		tableViewer.setInput(bs);
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

}
