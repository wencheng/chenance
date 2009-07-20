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

import static cn.sh.fang.chenance.i18n.UIMessageBundle._;
import static cn.sh.fang.chenance.i18n.UIMessageBundle.setText;
import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutData;
import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutDataRight;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ExpandAdapter;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import cn.sh.fang.chenance.data.entity.Account;

public class AccountEditForm {
	
	final static Account EMPTY_ACCOUNT;

	static {
		EMPTY_ACCOUNT = new Account();
		EMPTY_ACCOUNT.setName("");
	}

//	private Account account;
	
	Group grp;
	Label lblName;
	public Text tName;
	Label lblNamePh;
	public Text namePh;
	public Button btnSave;
	public Text memo;
	private Label lblType;
	private Combo type;
	private Label lblCurrency;
	private Combo currency;
	private Label lblDay;
	private Text day;
	private Label lblBankName;
	private Text bankName;
	private Label lblBranchName;
	private Text branchName;
	private Label lblBankNo;
	private Text bankNo;
	private Label lblInterest;
	private Text interest;
	private Label lblInterestR;
	private Combo interestPer;
	private Label lblStart;
	public Text start;
	private Label lblMemo;
	private ExpandBar bar;

	public AccountEditForm() {
//		account = new Account();
//		try {
//			PropertyUtils.copyProperties(this.account, EMPTY_ACCOUNT);
//		} catch (IllegalAccessException e) {
//		} catch (InvocationTargetException e) {
//		} catch (NoSuchMethodException e) {
//		}
	}

//	public Account getAccount() {
//		return account;
//	}
//
//	public void setAccount(Account account) {
//		try {
//			PropertyUtils.copyProperties(this.account, account);
//		} catch (IllegalAccessException e) {
//		} catch (InvocationTargetException e) {
//		} catch (NoSuchMethodException e) {
//		}
//	}

	public Control createControl(Composite parent) {
		grp = new Group(parent, SWT.NONE);
		setText(grp, "Account Info");

		// left side
		lblName = new Label(grp, SWT.NONE);
		setText(lblName, "Account Name:");
		lblName.pack();
		tName = new Text(grp, SWT.BORDER);
//		account.setName("");
		
		// The bindValue method call binds the text element with the model
//		Realm.runWithDefault(SWTObservables.getRealm(Display.getCurrent()),
//				new Runnable() {
//			public void run() {
//				DataBindingContext dbc = new DataBindingContext();
//				dbc.bindValue(SWTObservables.observeText(tName, SWT.Modify),
//						BeansObservables.observeValue(account, "name"), null, null);
//			}
//		});
		
//		Binder.bind(this, "account.name", tName, "text");

		lblNamePh = new Label(grp, SWT.NONE);
		setText(lblNamePh, "Account Name Phonetic:");
		lblNamePh.pack();
		namePh = new Text(grp, SWT.BORDER);
//		Binder.bind(this, "account.name", tName, "text");

		this.lblType = new Label(grp, SWT.NONE);
		setText(lblType, "Account Type:");
		lblType.pack();
		this.type = new Combo(grp, SWT.READ_ONLY);
		type.setItems(new String[] { "現金", "預金", "カード", "投資" });
		type.pack();
		type.select(0);

		this.lblCurrency = new Label(grp, SWT.NONE);
		setText(lblCurrency, "Currency:");
		lblCurrency.pack();
		this.currency = new Combo(grp, SWT.READ_ONLY);
		currency.setItems(new String[] { "USD", "JPY", "EUD", "GBP", "RMB" });
		currency.pack();
		currency.select(1);

		this.lblDay = new Label(grp, SWT.NONE);
		setText(lblDay, "Closing Day:");
		lblDay.pack();
		this.day = new Text(grp, SWT.BORDER);

		// right side
		this.lblBankName = new Label(grp, SWT.NONE);
		setText(lblBankName, "Bank Name:");
		lblBankName.pack();
		this.bankName = new Text(grp, SWT.BORDER);

		this.lblBranchName = new Label(grp, SWT.NONE);
		setText(lblBranchName, "Branch Name:");
		lblBranchName.pack();
		this.branchName = new Text(grp, SWT.BORDER);

		this.lblBankNo = new Label(grp, SWT.NONE);
		setText(lblBankNo, "Account No:");
		lblBankNo.pack();
		this.bankNo = new Text(grp, SWT.BORDER);

		this.lblInterest = new Label(grp, SWT.NONE);
		setText(lblInterest, "Interest Rate:");
		lblInterest.pack();
		this.interest = new Text(grp, SWT.BORDER | SWT.RIGHT);
		setText(interest, "00.00");
		interest.pack();
		this.lblInterestR = new Label(grp, SWT.NONE);
		setText(lblInterestR, "%");
		lblInterestR.pack();
		this.interestPer = new Combo(grp, SWT.READ_ONLY);
		interestPer.setItems(new String[] { _("One Year"), _("One Month") });
		interestPer.select(0);

		this.lblStart = new Label(grp, SWT.NONE);
		setText(lblStart, "Start Balance:");
		lblStart.pack();
		this.start = new Text(grp, SWT.BORDER);

		this.lblMemo = new Label(grp, SWT.NONE);
		setText(lblMemo, "Memo:");
		lblMemo.pack();
		memo = new Text(grp, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);

		btnSave = new Button(grp, SWT.NONE);
		setText(btnSave, "&Save this account");

		createOptionControl();

		internalLayout();

		return grp;
	}

	private void createOptionControl() {
		bar = new ExpandBar(grp, SWT.V_SCROLL);
		//bar.setBackgroundMode(SWT.INHERIT_FORCE);
		bar.setBackgroundMode(SWT.INHERIT_DEFAULT);

		Composite composite = new Composite(bar, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 0;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);

		Button button = new Button(composite, SWT.PUSH);
		button.setText("SWT.PUSH");
		button = new Button(composite, SWT.RADIO);
		button.setText("SWT.RADIO");
		button = new Button(composite, SWT.CHECK);
		button.setText("SWT.CHECK");
		button = new Button(composite, SWT.TOGGLE);
		button.setText("SWT.TOGGLE");

		ExpandItem item0 = new ExpandItem(bar, SWT.NONE, 0);
		setText(item0, "Options");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);

		bar.addExpandListener(new ExpandAdapter() {
			@Override
			public void itemCollapsed(ExpandEvent e) {
				Display.getCurrent().asyncExec(new Runnable() {
					public void run() {
						grp.pack();
						grp.layout();
					}
				});
			}

			@Override
			public void itemExpanded(ExpandEvent e) {
				Display.getCurrent().asyncExec(new Runnable() {
					public void run() {
						grp.pack();
						grp.layout();
					}
				});
			}
		});
	}

	int maxWidth, totalHeight;

	int SPACING = 20;

	/**
	 * 
	 * @param controls
	 * @return widest control
	 */
	private Control computeSize(Control[] controls) {
		Control ret = controls[0];
		maxWidth = 0;
		totalHeight = 0;

		for (Control c : controls) {
			if (maxWidth < c.getSize().x) {
				ret = c;
			}
			maxWidth = Math.max(maxWidth, c.getSize().x);
			totalHeight += c.getSize().y;
		}

		totalHeight += (controls.length - 1) * SPACING;

		return ret;
	}

	private void internalLayout() {
		FormLayout formLayout = new FormLayout();
		grp.setLayout(formLayout);
		formLayout.marginHeight = 10;
		formLayout.marginWidth = 10;

		// left side
		setFormLayoutData(lblName, 0, 20, 0, 20);
		setFormLayoutData(lblNamePh, lblName, 20, SWT.NONE, lblName, 0,
				SWT.LEFT);
		setFormLayoutData(lblType, lblNamePh, 20, SWT.NONE, lblName, 0,
				SWT.LEFT);
		setFormLayoutData(lblCurrency, lblType, 20, SWT.NONE, lblName, 0,
				SWT.LEFT);
		setFormLayoutData(lblDay, lblCurrency, 20, SWT.NONE, lblName, 0,
				SWT.LEFT);

		Control c = computeSize(new Control[] { lblName, lblNamePh, lblType,
				lblCurrency, lblDay });

		setFormLayoutData(tName, lblName, 0, SWT.TOP, c, 10, SWT.NONE).width = 80;
		setFormLayoutData(namePh, lblNamePh, 0, SWT.TOP, c, 10, SWT.NONE).width = 80;
		setFormLayoutData(type, lblType, 0, SWT.TOP, c, 10, SWT.NONE);
		setFormLayoutData(currency, lblCurrency, 0, SWT.TOP, c, 10, SWT.NONE);
		setFormLayoutData(day, lblDay, 0, SWT.TOP, c, 10, SWT.NONE).width = 80;

		// right side
		setFormLayoutData(lblBankName, tName, 0, SWT.TOP, tName, 50, SWT.NONE);
		setFormLayoutData(lblBranchName, lblBankName, 20, SWT.NONE, tName, 50,
				SWT.NONE);
		setFormLayoutData(lblBankNo, lblBranchName, 20, SWT.NONE, tName, 50,
				SWT.NONE);
		setFormLayoutData(lblInterest, lblBankNo, 20, SWT.NONE, tName, 50,
				SWT.NONE);
		setFormLayoutData(lblStart, lblInterest, 20, SWT.NONE, lblBankName, 0,
				SWT.LEFT);

		Control d = computeSize(new Control[] { lblBankName, lblBranchName,
				lblBankNo, lblInterest, lblStart });

		setFormLayoutData(bankName, lblBankName, 0, SWT.TOP, d, 5, SWT.NONE).width = 80;
		setFormLayoutData(branchName, lblBranchName, 0, SWT.TOP, d, 5, SWT.NONE).width = 80;
		setFormLayoutData(bankNo, lblBankNo, 0, SWT.TOP, d, 5, SWT.NONE).width = 80;
		setFormLayoutData(interest, lblInterest, 0, SWT.TOP, d, 5, SWT.NONE);
		setFormLayoutData(lblInterestR, lblInterest, 0, SWT.TOP, interest, 5,
				SWT.NONE);
		setFormLayoutData(interestPer, lblInterestR, 0, SWT.TOP, lblInterestR,
				5, SWT.NONE);
		setFormLayoutData(start, lblStart, 0, SWT.TOP, d, 5, SWT.NONE).width = 80;

		// bottom
		setFormLayoutData(lblMemo, lblDay, 20, SWT.NONE, lblName, 0, SWT.LEFT);
		FormData fd = setFormLayoutData(memo, lblMemo, 0, SWT.NONE, lblName, 0,
				SWT.LEFT);
		fd.width = grp.computeSize(SWT.DEFAULT, SWT.DEFAULT).x - 20;
		fd.height = 80;
		setFormLayoutDataRight(btnSave, memo, 10, SWT.NONE, memo, 0, SWT.RIGHT);

		fd = setFormLayoutData(bar, btnSave, 10, SWT.NONE, memo, 0, SWT.LEFT);
		fd.width = grp.computeSize(SWT.DEFAULT, SWT.DEFAULT).x - 20;
		// fd.height = 10;

		grp.pack();
	}
}
