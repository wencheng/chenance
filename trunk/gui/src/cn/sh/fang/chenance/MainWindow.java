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
package cn.sh.fang.chenance;

import static cn.sh.fang.chenance.i18n.UIMessageBundle.setText;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import cn.sh.fang.chenance.data.dao.BaseService;
import cn.sh.fang.chenance.data.dao.CategoryService;
import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.data.entity.Transaction;
import cn.sh.fang.chenance.listener.BalanceSheetTransactionListener;
import cn.sh.fang.chenance.listener.ChangeLanguageListener;
import cn.sh.fang.chenance.listener.NumberVerifyListener;
import cn.sh.fang.chenance.provider.AccountListProvider;
import cn.sh.fang.chenance.provider.BalanceSheetCellModifier;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider;
import cn.sh.fang.chenance.provider.BalanceSheetDetailCellEditor;
import cn.sh.fang.chenance.provider.BalanceSheetLabelProvider;
import cn.sh.fang.chenance.provider.CategoryComboCellEditor;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider.Column;
import cn.sh.fang.chenance.util.SWTUtil;
import cn.sh.fang.chenance.util.swt.CalendarCellEditor;

public class MainWindow {

	static Logger LOG = Logger.getLogger(MainWindow.class);

	static boolean MAC_OS_X;

	private Shell sShell = null; // @jve:decl-index=0:visual-constraint="91,5"
	private CoolBar coolBar;
	private Menu menuBar;
	private TabFolder tabFolder;
	private Table bsTable;

	private AccountListProvider accountListProv;

	BalanceSheetContentProvider bs;

	private CategoryEditForm categoryEditForm;

	private AccountTree bsAccountTree;

	private TableViewer bsTableViewer;

	private Display display;

	private BalanceSheetLabelProvider bslp;

	protected Calendar selectedCal = Calendar.getInstance();

	private Label currentBalance;

	private AccountTab accountTab;

	private CategoryTab categoryTab;

	public static DataBindingContext bindingContext;

	static {
		// set factory here due to a bug in max os x
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=211167
		MAC_OS_X = System.getProperty("os.name").toLowerCase().startsWith(
				"mac os x");
		if (MAC_OS_X) {
			BaseService.init();
		}
	}

	public MainWindow(Display display) {
		this.display = display;
		
//		Realm.setDefault(SWTObservables.getRealm(Display.getCurrent()));
	}

	private void init() {
		accountListProv = new AccountListProvider();
		bs = new BalanceSheetContentProvider();
	}

	public static void main(String[] args) {
		final Display display = new Display();
		Realm.runWithDefault(SWTObservables.getRealm(display),
				new Runnable() {
			public void run() {
				final Splash s = new Splash(display);
				final MainWindow swt = new MainWindow(display);
				s.run(new Runnable() {
					public void run() {
						if (!MAC_OS_X) {
							BaseService.init();
						}
		
						swt.init();
						swt.createSShell();
						swt.sShell.open();
						s.close();
					}
				});
		
				// swt.sShell.setAlpha( 220 );
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
		});
	}

	public static void shutdown() {
		BaseService.shutdown();
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell(display);
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
		setText(fileMenuHeader, "&File");
		Menu fileMenu = new Menu(sShell, SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);
		MenuItem fileNewItem = new MenuItem(fileMenu, SWT.PUSH);
		setText(fileNewItem, "&New");
		// fileNewItem.addSelectionListener(new FileNewListener());
		MenuItem fileOpenItem = new MenuItem(fileMenu, SWT.PUSH);
		setText(fileOpenItem, "&Open");
		// fileOpenItem.addSelectionListener(new FileOpenListener());
		MenuItem fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
		setText(fileSaveItem, "&Save");
		// fileSaveItem.addSelectionListener(new FileSaveListener());

		// Edit
		MenuItem menuItem2 = new MenuItem(menuBar, SWT.CASCADE);
		setText(menuItem2, "&Edit");

		// View
		MenuItem viewMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		setText(viewMenuHeader, "&View");
		Menu viewMenu = new Menu(sShell, SWT.DROP_DOWN);
		viewMenuHeader.setMenu(viewMenu);
		MenuItem viewBsItem = new MenuItem(viewMenu, SWT.RADIO);
		setText(viewBsItem, "Balance Sheet");
		// viewBsItem.addSelectionListener(new
		// ChangeLanguageListener(Locale.ENGLISH));
		MenuItem viewInvItem = new MenuItem(viewMenu, SWT.RADIO);
		setText(viewInvItem, "Investment History");
		MenuItem viewAssetItem = new MenuItem(viewMenu, SWT.RADIO);
		setText(viewAssetItem, "Asset Management");

		// Lang
		MenuItem langMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		setText(langMenuHeader, "&Language");
		Menu langMenu = new Menu(sShell, SWT.DROP_DOWN);
		langMenuHeader.setMenu(langMenu);
		MenuItem langEnItem = new MenuItem(langMenu, SWT.RADIO);
		setText(langEnItem, "English");
		langEnItem.addSelectionListener(new ChangeLanguageListener(
				Locale.ENGLISH));
		MenuItem langJaItem = new MenuItem(langMenu, SWT.RADIO);
		setText(langJaItem, "日本語");
		langJaItem.addSelectionListener(new ChangeLanguageListener(
				Locale.JAPANESE));
		MenuItem langZhItem = new MenuItem(langMenu, SWT.RADIO);
		setText(langZhItem, "简体中文");
		langZhItem.addSelectionListener(new ChangeLanguageListener(
				Locale.CHINESE));

		// Help
		MenuItem menuItem4 = new MenuItem(menuBar, SWT.CASCADE);
		setText(menuItem4, "&Help");
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
//		Point size = toolBar.getSize();
		coolItem.setControl(toolBar);
//		Point preferred = coolItem.computeSize(size.x, size.y);
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
		setText(item1, "Balance");
		item1.setControl(getBalanceSheetTabControl(tabFolder));

		TabItem item2 = new TabItem(tabFolder, SWT.NULL);
		setText(item2, "Category");
		categoryTab = new CategoryTab();
		item2.setControl(categoryTab.getCategoryTabControl(tabFolder));

		TabItem item3 = new TabItem(tabFolder, SWT.NULL);
		setText(item3, "Accounts");
		accountTab = new AccountTab(accountListProv,bsAccountTree.model);
		item3.setControl(accountTab.getAccountTabControl(tabFolder) );

		tabFolder.setSize(sShell.getSize());
	}

	private Control getBalanceSheetTabControl(TabFolder tabFolder) {
		Composite composite = new Composite(tabFolder, SWT.NONE);

		// カレンダー
		DateTime listDate = new DateTime(composite, SWT.CALENDAR | SWT.SHORT);
		listDate.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				DateTime dt = (DateTime) e.widget;
				selectedCal.set(dt.getYear(), dt.getMonth(), dt.getDay());
				bs.setDate( selectedCal.getTime() );
				bsTableViewer.refresh();
			}
		});

		// 口座ツリー
		List<Account> accounts = accountListProv.getAccounts();
		bsAccountTree = new AccountTree(accounts);
		bsAccountTree.createControl(composite);

		Button oneDay = new Button(composite, SWT.RADIO);
		setText(oneDay, "Day");
		oneDay.setSelection(true);
		oneDay.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				bslp.setDateFormat( BalanceSheetLabelProvider.DD );
				bs.setDate( selectedCal.getTime() );
				bsTableViewer.refresh();
			}
		});
		Button oneWeek = new Button(composite, SWT.RADIO);
		setText(oneWeek, "Week");
		oneWeek.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				bslp.setDateFormat( BalanceSheetLabelProvider.MMDD );
				Calendar bgn = (Calendar) selectedCal.clone();
				Calendar end = (Calendar) selectedCal.clone();
				bgn.add( Calendar.DATE, -7 );
				bs.setDate( bgn.getTime(), end.getTime() );
				bsTableViewer.refresh();
			}
		});
		Button oneMonth = new Button(composite, SWT.RADIO);
		setText(oneMonth, "Month");
		oneMonth.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				bslp.setDateFormat( BalanceSheetLabelProvider.MMDD );
				Calendar bgn = (Calendar) selectedCal.clone();
				bgn.set( Calendar.DATE, bgn.getActualMinimum(Calendar.DATE) );
				Calendar end = (Calendar) bgn.clone();
				end.set( Calendar.DATE, bgn.getActualMaximum(Calendar.DATE) );
				bs.setDate( bgn.getTime(), end.getTime() );
				bsTableViewer.refresh();
			}
		});
		Button oneYear = new Button(composite, SWT.RADIO);
		setText(oneYear, "Year");
		oneYear.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				bslp.setDateFormat( BalanceSheetLabelProvider.YYYYMMDD );
				Calendar bgn = (Calendar) selectedCal.clone();
				bgn.set( Calendar.MONTH, bgn.getActualMinimum(Calendar.MONTH) );
				Calendar end = (Calendar) bgn.clone();
				end.set( Calendar.MONTH, bgn.getActualMaximum(Calendar.MONTH) );
				bs.setDate( bgn.getTime(), end.getTime() );
				bsTableViewer.refresh();
			}
		});

		// バランスシート
		bsTable = new Table(composite, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		bsTable.setLinesVisible(true);
		bsTable.setHeaderVisible(true);
		// カラム
		TableColumn[] cols = new TableColumn[6];
		for (int i = 0; i < 6; i++) {
			cols[i] = new TableColumn(bsTable, SWT.NONE);
			cols[i].setWidth(100);
		}
		cols[0].setText("日付");
		cols[1].setText("費目");
		cols[1].setWidth(150);
		cols[2].setText("支払");
		cols[2].setAlignment(SWT.RIGHT);
		cols[3].setText("預入");
		cols[3].setAlignment(SWT.RIGHT);
		cols[4].setText("残高");
		cols[4].setAlignment(SWT.RIGHT);
		cols[5].setText("詳細");
		// リスト
		createTableViewer();
		cols[0].pack();
		cols[0].setAlignment(SWT.CENTER);
		// ポップアップメニュー
		Menu menu = new Menu(sShell, SWT.POP_UP);
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText("Popup");
		bsTable.setMenu(menu);
		// <Add New>
		bsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (e.button == 1) {
					Transaction t = (Transaction) bsTable.getSelection()[0]
							.getData();
					if (t.getDate() == null) {
						bs.addItem();
						return;
					}
				}
				super.mouseDoubleClick(e);
			}
		});
		
		// default account selection
		// TODO select account last saved
//		bsAccountList.addSelectionListener(
//				new AccountListSelectionAdapter(bs, this.bsTableViewer));
//		bsAccountList.selectAccount(0);

		// 残高ラベル
		currentBalance = new Label(composite, SWT.RIGHT);
//		currentBalance.setText(NumberFormat.getCurrencyInstance().format(bsAccountList.getSelectedAccount().getCurrentBalance()));
		Label label = new Label(composite, SWT.NONE);
		setText( label, "Balance:" );

		// レイアウト
		FormLayout formLayout = new FormLayout();
		composite.setLayout(formLayout);
		formLayout.marginHeight = 10;
		formLayout.marginWidth = 10;

//		SWTUtil.setFormLayoutData(listDate, 0, 10, 0, 10).width = 105;
//		SWTUtil.setFormLayoutData(today, listDate, 0, SWT.TOP, listDate, 20,
//		SWT.NONE).width = 80;

		FormData layoutData = SWTUtil.setFormLayoutData(bsAccountTree.viewer.getTree(),
				listDate, 10, SWT.NONE, listDate, 0, SWT.LEFT);
		layoutData.height = 300;
//		layoutData.width = listDate.getSize().x;

		SWTUtil.setFormLayoutData(bsTable, listDate, 0, SWT.TOP, listDate, 20,
				SWT.NONE).height = 400;
		bsTable.setSize(tabFolder.getSize());

		SWTUtil.setFormLayoutData(oneDay, bsTable, 0, SWT.TOP, bsTable, 10,
				SWT.NONE).width = 80;
		SWTUtil.setFormLayoutData(oneWeek, oneDay, 10, SWT.NONE, oneDay, 0,
				SWT.LEFT).width = 80;
		SWTUtil.setFormLayoutData(oneMonth, oneWeek, 10, SWT.NONE, oneWeek, 0,
				SWT.LEFT).width = 80;
		SWTUtil.setFormLayoutData(oneYear, oneMonth, 10, SWT.NONE, oneMonth, 0,
				SWT.LEFT).width = 80;

		SWTUtil.setFormLayoutDataRight(currentBalance, bsTable, 10, SWT.NONE, bsTable, -20,
				SWT.RIGHT).width = 80;
		SWTUtil.setFormLayoutDataRight(label, bsTable, 10, SWT.NONE, currentBalance, -100,
				SWT.RIGHT);

		return composite;
	}

	private void createTableViewer() {
		bsTableViewer = new TableViewer(bsTable);
		bsTableViewer.setUseHashlookup(true);

		bsTableViewer.setColumnProperties(Column.stringValues());

		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
				bsTableViewer) {
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				ViewerCell cell = (ViewerCell) event.getSource();
				// Transaction t =
				// (Transaction)((TableItem)cell.getItem()).getData();

				if (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION) {
					MouseEvent e = ((MouseEvent) event.sourceEvent);
					return e.button == 1;
				} else if (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION) {
					return cell.getColumnIndex() == Column.DETAIL.ordinal();
				} else {
					return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
							|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
				}
			}
		};

		TableViewerEditor.create(bsTableViewer, actSupport,
				ColumnViewerEditor.TABBING_HORIZONTAL
						// | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_CYCLE_IN_ROW
						| ColumnViewerEditor.TABBING_VERTICAL
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);

		// Create the cell editors
		final CellEditor[] editors = new CellEditor[Column.values().length];

		// editors[0] = new CheckboxCellEditor(table);
		CalendarCellEditor dateEditor = new CalendarCellEditor(bsTable, SWT.NULL);
		editors[Column.DATE.ordinal()] = dateEditor;

		CategoryService service = new CategoryService();
		List<Category> categoryList = service.findAll();
		bsTableViewer.setData("categoryList", categoryList);
		CategoryComboCellEditor e = new CategoryComboCellEditor(bsTable);
		e.setItems(categoryList);
		// e.addListener(new ActivateNextCellEditorListener(tableViewer));
		editors[Column.CATEGORY.ordinal()] = e;

		TextCellEditor debitEditor = new TextCellEditor(bsTable);
		((Text) debitEditor.getControl()).setTextLimit(9);
		((Text) debitEditor.getControl())
				.addVerifyListener(new NumberVerifyListener());
		editors[Column.DEBIT.ordinal()] = debitEditor;

		TextCellEditor creditEditor = new TextCellEditor(bsTable);
		((Text) creditEditor.getControl()).setTextLimit(9);
		((Text) creditEditor.getControl())
				.addVerifyListener(new NumberVerifyListener());
		editors[Column.CREDIT.ordinal()] = creditEditor;

		editors[Column.DETAIL.ordinal()] = new BalanceSheetDetailCellEditor(
				bsTable);

		bsTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj = ((StructuredSelection) bsTableViewer
						.getSelection()).getFirstElement();

				// Clean up detail column editor
				CellEditor oldEditor = editors[Column.DETAIL.ordinal()];
				if (oldEditor.isActivated() && oldEditor.getValue() != obj) {
					oldEditor.deactivate();
				}
			}
		});

		// Assign the cell editors to the viewer
		bsTableViewer.setCellEditors(editors);
		// Set the cell modifier for the viewer
		bsTableViewer.setCellModifier(new BalanceSheetCellModifier(
				bs, bsTableViewer, accountListProv));
		// Set the default sorter for the viewer
		// tableViewer.setSorter(new ExampleTaskSorter(
		// ExampleTaskSorter.DESCRIPTION));

		ICellEditorListener propChange = new ICellEditorListener(){
			public void applyEditorValue() {
			}

			public void cancelEditor() {
			}

			public void editorValueChanged(boolean arg0, boolean arg1) {
			}
		};
		for ( CellEditor ed : editors ) {
			if ( ed != null ) {
				ed.addListener(propChange);
			}
		}

		bslp = new BalanceSheetLabelProvider(bsTableViewer);
		bslp.setDateFormat( BalanceSheetLabelProvider.DD );

		bsTableViewer.setContentProvider(bs);
		bsTableViewer.setLabelProvider(bslp);
		bsTableViewer.setInput(bs);

		BalanceSheetTransactionListener bstl = new BalanceSheetTransactionListener(
				bsTableViewer);
		bs.addChangeListener(bstl);
	}

}
