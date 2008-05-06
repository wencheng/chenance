package cn.sh.fang.chenance;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeItem;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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

import cn.sh.fang.chenance.data.dao.AccountService;
import cn.sh.fang.chenance.data.dao.CategoryService;
import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.listener.FileOpenListener;
import cn.sh.fang.chenance.provider.BalanceSheetCellModifier;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider;
import cn.sh.fang.chenance.provider.BalanceSheetLabelProvider;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider.Column;
import cn.sh.fang.chenance.util.swt.CalendarCellEditor;
import cn.sh.fang.chenance.util.swt.TableViewerEx;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class MainWindow {

	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="91,5"
	private Menu menuBar = null;
	private CoolBar coolBar = null;
	private TabFolder tabFolder = null;

	private Table table;
	private TableViewer tableViewer;
	BalanceSheetContentProvider bs = new BalanceSheetContentProvider();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		/*
		 * Before this is run, be sure to set up the launch configuration
		 * (Arguments->VM Arguments) for the correct SWT library path in order
		 * to run with the SWT dlls. The dlls are located in the SWT plugin jar.
		 * For example, on Windows the Eclipse SWT 3.1 plugin jar is:
		 * installation_directory\plugins\org.eclipse.swt.win32_3.1.0.jar
		 */
		Display display = Display.getDefault();
		MainWindow thisClass = new MainWindow();
		thisClass.createSShell();
		thisClass.sShell.open();

		try { 
		while (!thisClass.sShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		} catch (SWTException e) {
			e.printStackTrace();
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
	    fileMenuHeader.setText("&File");
		Menu fileMenu = new Menu(sShell, SWT.DROP_DOWN);
	    fileMenuHeader.setMenu(fileMenu);
		MenuItem fileOpenItem = new MenuItem(fileMenu, SWT.PUSH);
		fileOpenItem.setText("&Open");
		fileOpenItem.addSelectionListener(new FileOpenListener());
		
		// Edit
		MenuItem menuItem2 = new MenuItem(menuBar, SWT.CASCADE);
		menuItem2.setText("Edit");
		
		// Help
		MenuItem menuItem3 = new MenuItem(menuBar, SWT.CASCADE);
		menuItem3.setText("&Help");
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
		item1.setText("履歴");
		item1.setControl(getBalanceSheetTabControl(tabFolder));
		TabItem item2 = new TabItem(tabFolder, SWT.NULL);
		item2.setText("投資");
		TabItem item3 = new TabItem(tabFolder, SWT.NULL);
		item3.setText("口座");
		item3.setControl(getAccountTabControl(tabFolder));
		tabFolder.setSize(sShell.getSize());
	}
	
	private Control getAccountTabControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		
		// 概要ツリー
		TableTree tableTree = new TableTree(composite, SWT.BORDER
				| SWT.FULL_SELECTION);
		Table tttable = tableTree.getTable();
		tttable.setHeaderVisible(false);
		tttable.setLinesVisible(false);
		tttable.addMouseListener(new MouseAdapter(){
			public void mouseDoubleClick(MouseEvent e) {
				if ( e.button == 1 ) {
					Table t = (Table)e.widget;
					TableItem i = t.getItem(new Point(e.x,e.y));
					System.out.println(i + " was d-clicked");
				}
			}
		});

		
		AccountService service = new AccountService();
		List<Account> accounts = service.findAll();

		TableColumn col1 = new TableColumn(tttable, SWT.LEFT);
		TableColumn col2 = new TableColumn(tttable, SWT.RIGHT);
		TableTreeItem parent = new TableTreeItem(tableTree, SWT.NONE);
		parent.setText(0, "口座");
		parent.setText(1, "");
		int balanceSum = 0;
		for ( Account a : accounts ) {
			TableTreeItem child = new TableTreeItem(parent, SWT.NONE);
			child.setText(0, a.getName());
			child.setText(1, a.getCurrentBalance()+"");
			balanceSum += a.getCurrentBalance();
		}
		parent.setExpanded(true);
		TableTreeItem sum = new TableTreeItem(tableTree, SWT.NONE);
		sum.setText(0, "残高の合計");
		sum.setText(1, balanceSum+"");
		col1.pack();
		col1.setResizable(false);
		//col1.setWidth(col1.getWidth() + 20);
		col2.pack();
		col2.setWidth(80);
		col2.setResizable(false);

		FontData fd = parent.getFont().getFontData()[0];
		Font newFont = new Font(sShell.getDisplay(), new FontData(fd.getName(),
				fd.getHeight(), fd.getStyle() | SWT.BOLD));
		parent.setFont(newFont);
		sum.setFont(newFont);

		// フォーム
		Group grp = new Group(composite, SWT.NONE);
		grp.setText("口座情報");

		Label lblName = new Label(grp, SWT.NONE);
		lblName.setText("口座名：");
		lblName.pack();
		Text name = new Text(grp, SWT.BORDER);
		
		Label lblNamePh = new Label(grp, SWT.NONE);
		lblNamePh.setText("口座名よみ：");
		lblNamePh.pack();
		Text namePh = new Text(grp, SWT.BORDER);

		Label lblType = new Label(grp, SWT.NONE);
		lblType.setText("口座種類：");
		lblType.pack();
		Combo type = new Combo(grp, SWT.READ_ONLY);
		type.setItems(new String[]{"現金","預金","カード","投資"});
		type.pack();
		type.select(0);

		Label lblCurrency = new Label(grp, SWT.NONE);
		lblCurrency.setText("通貨：");
		lblCurrency.pack();
		Combo currency = new Combo(grp, SWT.READ_ONLY);
		currency.setItems(new String[]{"USD","JPY","EUD","GBP","RMB"});
		currency.pack();
		currency.select(1);

		Label lblDay = new Label(grp, SWT.NONE);
		lblDay.setText("締切日：");
		lblDay.pack();
		Text day = new Text(grp, SWT.BORDER);

		Label lblBankName = new Label(grp, SWT.NONE);
		lblBankName.setText("銀行名：");
		lblBankName.pack();
		Text bankName = new Text(grp, SWT.BORDER);

		Label lblBranchName = new Label(grp, SWT.NONE);
		lblBranchName.setText("支店名：");
		lblBranchName.pack();
		Text branchName = new Text(grp, SWT.BORDER);

		Label lblBankNo = new Label(grp, SWT.NONE);
		lblBankNo.setText("口座番号：");
		lblBankNo.pack();
		Text bankNo = new Text(grp, SWT.BORDER);
		
		Label lblInterest = new Label(grp, SWT.NONE);
		lblInterest.setText("利息率：");
		lblInterest.pack();
		Text interest = new Text(grp, SWT.BORDER | SWT.RIGHT);
		interest.setText("00.00");
		interest.pack();
		Label lblInterestR = new Label(grp, SWT.NONE);
		lblInterestR.setText("%");
		lblInterestR.pack();
		Combo interestPer = new Combo(grp, SWT.READ_ONLY);
		interestPer.setItems(new String[]{"年","月"});
		interestPer.select(0);
		
		Label lblStart = new Label(grp, SWT.NONE);
		lblStart.setText("開始残高：");
		lblStart.pack();
		Text start = new Text(grp, SWT.BORDER);

		Label lblMemo = new Label(grp, SWT.NONE);
		lblMemo.setText("備考：");
		lblMemo.pack();
		Text memo = new Text(grp, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		
		Button save = new Button(grp, SWT.NONE);
		save.setText("保存");

		// レイアウト
		FormLayout formLayout = new FormLayout();
		composite.setLayout(formLayout);
		formLayout.marginHeight = 10;
		formLayout.marginWidth = 10;

		FormData layoutData = setFormLayoutData(tableTree, 0, 0, 0, 10);
		layoutData.height = 400;
		layoutData.width = 175;
		
		setFormLayoutData(grp, 0, 0, tableTree, 20).width = 400;

		formLayout = new FormLayout();
		grp.setLayout(formLayout);
		formLayout.marginHeight = 10;
		formLayout.marginWidth = 10;

		setFormLayoutData(lblName, 0, 20, 0, 20);
		setFormLayoutData(name, lblName, 0, SWT.TOP, lblName, 5, SWT.NONE).width = 80;
		setFormLayoutData(lblNamePh, lblName, 20, SWT.NONE, lblName, 0, SWT.LEFT);
		setFormLayoutData(namePh, lblNamePh, 0, SWT.TOP, lblNamePh, 5, SWT.NONE).width = 80;
		setFormLayoutData(lblType, lblNamePh, 20, SWT.NONE, lblName, 0, SWT.LEFT);
		setFormLayoutData(type, lblType, 0, SWT.TOP, lblType, 5, SWT.NONE);
		setFormLayoutData(lblCurrency, lblType, 20, SWT.NONE, lblName, 0, SWT.LEFT);
		setFormLayoutData(currency, lblCurrency, 0, SWT.TOP, lblCurrency, 5, SWT.NONE);
		setFormLayoutData(lblDay, lblCurrency, 20, SWT.NONE, lblName, 0, SWT.LEFT);
		setFormLayoutData(day, lblDay, 0, SWT.TOP, lblDay, 5, SWT.NONE).width = 80;
		
		setFormLayoutData(lblMemo, lblDay, 20, SWT.NONE, lblName, 0, SWT.LEFT);
		layoutData = setFormLayoutData(memo, lblMemo, 0, SWT.NONE, lblName, 0, SWT.LEFT);
		layoutData.width = 350;
		layoutData.height = 80;
		setFormLayoutDataRight(save, memo, 20, SWT.NONE, memo, 0, SWT.RIGHT).width = 80;

		setFormLayoutData(lblBankName, name, 0, SWT.TOP, name, 50, SWT.NONE);
		setFormLayoutData(bankName, lblBankName, 0, SWT.TOP, lblBankName, 5, SWT.NONE).width = 80;
		setFormLayoutData(lblBranchName, lblBankName, 20, SWT.NONE, name, 50, SWT.NONE);
		setFormLayoutData(branchName, lblBranchName, 0, SWT.TOP, lblBranchName, 5, SWT.NONE).width = 80;
		setFormLayoutData(lblBankNo, lblBranchName, 20, SWT.NONE, name, 50, SWT.NONE);
		setFormLayoutData(bankNo, lblBankNo, 0, SWT.TOP, lblBankNo, 5, SWT.NONE).width = 80;
		setFormLayoutData(lblInterest, lblBankNo, 20, SWT.NONE, name, 50, SWT.NONE);
		setFormLayoutData(interest, lblInterest, 0, SWT.TOP, lblInterest, 5, SWT.NONE);
		setFormLayoutData(lblInterestR, lblInterest, 0, SWT.TOP, interest, 5, SWT.NONE);
		setFormLayoutData(interestPer, lblInterestR, 0, SWT.TOP, lblInterestR, 5, SWT.NONE);
		setFormLayoutData(lblStart, lblInterest, 20, SWT.NONE, lblBankName, 0, SWT.LEFT);
		setFormLayoutData(start, lblStart, 0, SWT.TOP, lblStart, 5, SWT.NONE).width = 80;
		
		return composite;
	}

	private Control getBalanceSheetTabControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);

		// 概要ツリー
		TableTree tableTree = new TableTree(composite, SWT.BORDER
				| SWT.FULL_SELECTION);
		Table tttable = tableTree.getTable();
		tttable.setHeaderVisible(false);
		tttable.setLinesVisible(false);
		tttable.addMouseListener(new MouseAdapter(){
			public void mouseDoubleClick(MouseEvent e) {
				if ( e.button == 1 ) {
					Table t = (Table)e.widget;
					TableItem i = t.getItem(new Point(e.x,e.y));
					System.out.println(i + " was d-clicked");
				}
			}
		});

		TableColumn col1 = new TableColumn(tttable, SWT.LEFT);
		TableColumn col2 = new TableColumn(tttable, SWT.RIGHT);
		TableTreeItem parent = new TableTreeItem(tableTree, SWT.NONE);
		parent.setText(0, "口座");
		parent.setText(1, "");
		TableTreeItem child = new TableTreeItem(parent, SWT.NONE);
		child.setText(0, "生活費");
		child.setText(1, "30,000");
		child = new TableTreeItem(parent, SWT.NONE);
		child.setText(0, "娯楽");
		child.setText(1, "30,000");
		child = new TableTreeItem(parent, SWT.NONE);
		child.setText(0, "貯金");
		child.setText(1, "30,000");
		parent.setExpanded(true);
		TableTreeItem sum = new TableTreeItem(tableTree, SWT.NONE);
		sum.setText(0, "残高の合計");
		sum.setText(1, "90,000");
		col1.pack();
		col1.setResizable(false);
		col1.setWidth(col1.getWidth() + 20);
		col2.pack();
		col2.setWidth(80);
		col2.setResizable(false);

		FontData fd = parent.getFont().getFontData()[0];
		Font newFont = new Font(sShell.getDisplay(), new FontData(fd.getName(),
				fd.getHeight(), fd.getStyle() | SWT.BOLD));
		parent.setFont(newFont);
		sum.setFont(newFont);
		/*
		 * FontDialog fontDialog = new FontDialog(sShell);
		 * fontDialog.setFontList((sum.getFont()).getFontData()); FontData
		 * fontData = fontDialog.open(); if (fontData != null) { Font newFont =
		 * new Font(sShell.getDisplay(), fontData); sum.setFont(newFont); }
		 */

		// 日付
		Text listDate = new Text(composite, SWT.READ_ONLY|SWT.BORDER);
		listDate.setText("2008/01/01");
		newFont = new Font(sShell.getDisplay(), new FontData(fd.getName(),
				(int)(fd.getHeight()*1.5), fd.getStyle()));
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
	            bs.addTask();
	        }
	    });

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
		
		FormData layoutData = setFormLayoutData(tableTree, listDate, 10, SWT.NONE, listDate, 0, SWT.LEFT);
		layoutData.height = 400;
		layoutData.width = 175;

		setFormLayoutDataRight(customDur, listDate, 0, SWT.TOP, table, 0, SWT.RIGHT).width = 80;
		setFormLayoutDataRight(oneMonth, listDate, 0, SWT.TOP, customDur, -20, SWT.LEFT).width = 80;
		setFormLayoutDataRight(oneWeek, listDate, 0, SWT.TOP, oneMonth, -20, SWT.LEFT).width = 80;
		setFormLayoutDataRight(oneDay, listDate, 0, SWT.TOP, oneWeek, -20, SWT.LEFT).width = 80;

		setFormLayoutData(table, listDate, 10, tableTree, 20).height = 400;
		table.setSize(tabFolder.getSize());

		setFormLayoutData(btnAdd, table, 0, SWT.TOP, table, 10, SWT.NONE).width = 80;
		setFormLayoutDataRight(total, table, 10, SWT.NONE, table, -20, SWT.RIGHT).width = 80;
		setFormLayoutDataRight(label, table, 10, SWT.NONE, total, -100, SWT.RIGHT);

		return composite;
	}
	
	private FormData setFormLayoutData(Control c, Object top, int ot,
			Object left, int ol) {
		return setFormLayoutData(c,top,ot,SWT.NONE,left,ol,SWT.NONE);
	}
	
	/**
	 * 
	 * @param c
	 * @param top
	 * @param ot top offset
	 * @param at top align
	 * @param left
	 * @param ol left offset
	 * @param lt left align
	 * @return
	 */
	private FormData setFormLayoutData(Control c, Object top, int ot, int at,
			Object left, int ol, int lt) {
		FormData layoutData = new FormData();
		if ( top instanceof Control ) {
			layoutData.top = new FormAttachment((Control)top, ot, at);
		} else {
			layoutData.top = new FormAttachment((Integer)top, ol, at);
		}
		if ( left instanceof Control ) {
			layoutData.left = new FormAttachment((Control)left, ol, lt);
		} else {
			layoutData.left = new FormAttachment((Integer)left, ol, lt);
		}
		c.setLayoutData(layoutData);
		return layoutData;
	}

	private FormData setFormLayoutDataRight(Control c, Object top, int ot, int at,
			Object right, int or, int ar) {
		FormData layoutData = new FormData();
		if ( top instanceof Control ) {
			layoutData.top = new FormAttachment((Control)top, ot, at);
		} else {
			layoutData.top = new FormAttachment((Integer)top, ot, at);
		}
		if ( right instanceof Control ) {
			layoutData.right = new FormAttachment((Control)right, or, ar);
		} else {
			layoutData.right = new FormAttachment((Integer)right, or, ar);
		}
		c.setLayoutData(layoutData);
		return layoutData;
	}

	private void createTableViewer() {
		tableViewer = new TableViewerEx(table);
		bs.setTableViewer(tableViewer);
		tableViewer.setUseHashlookup(true);

		tableViewer.setColumnProperties(Column.stringValues());

		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
				tableViewer) {
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				if (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION) {
					MouseEvent e = ((MouseEvent) event.sourceEvent);
					return e.button == 1;
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

		//editors[0] = new CheckboxCellEditor(table);
		CalendarCellEditor dateEditor = new CalendarCellEditor(table,				
				SWT.NULL);
		editors[Column.DATE.ordinal()] = dateEditor;

		// TODO get categories
		CategoryService service = new CategoryService();
		ComboBoxCellEditor e = new ComboBoxCellEditor(table,
				toComboList(service.findAll()),
				SWT.READ_ONLY);
		e.getLayoutData().minimumWidth = 30;
		editors[Column.CATEGORY.ordinal()] = e;

		TextCellEditor textEditor = new TextCellEditor(table);
		((Text) textEditor.getControl()).setTextLimit(9);
		((Text) textEditor.getControl()).addVerifyListener(
		new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				e.doit = "0123456789".indexOf(e.text) >= 0;
			}
		});
		editors[Column.DEBIT.ordinal()] = textEditor;

		textEditor = new TextCellEditor(table);
		((Text) textEditor.getControl()).addVerifyListener(
		new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				// Here, we could use a RegExp such as the following
				// if using JRE1.4 such as e.doit = e.text.matches("[\\-0-9]*");
				e.doit = "0123456789".indexOf(e.text) >= 0;
			}
		});
		editors[Column.CREDIT.ordinal()] = textEditor;
		
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

	private String[] toComboList(List<Category> categories) {
		String[] ret = new String[categories.size()];
		Category cat;
		for ( int i = 0; i < categories.size(); i++ ) {
			cat = categories.get(i);
			if ( cat.getId()%10000 == 0 ) {
				ret[i] = " + " + cat.getName();
			} else {
				ret[i] = " |--- " + cat.getName();
			}
		}
		return ret;
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
