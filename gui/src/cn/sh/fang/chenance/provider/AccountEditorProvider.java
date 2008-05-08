package cn.sh.fang.chenance.provider;

import static cn.sh.fang.chenance.util.UIMessageBundle._;
import static cn.sh.fang.chenance.util.swt.SWTUtil.setFormLayoutData;
import static cn.sh.fang.chenance.util.swt.SWTUtil.setFormLayoutDataRight;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class AccountEditorProvider {

	Group grp;
	Label lblName;
	public Text name;
	Label lblNamePh;
	public Text namePh;
	public Button save;
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
	private Text start;
	private Label lblMemo;

	public AccountEditorProvider() {
	}

	public Control createControl(Composite parent) {
		grp = new Group(parent, SWT.NONE);
		grp.setText(_("Account Info"));

		// left side
		lblName = new Label(grp, SWT.NONE);
		lblName.setText(_("Account Name:"));
		lblName.pack();
		name = new Text(grp, SWT.BORDER);

		lblNamePh = new Label(grp, SWT.NONE);
		lblNamePh.setText(_("Account Name Phonetic:"));
		lblNamePh.pack();
		namePh = new Text(grp, SWT.BORDER);

		this.lblType = new Label(grp, SWT.NONE);
		lblType.setText(_("Account Type:"));
		lblType.pack();
		this.type = new Combo(grp, SWT.READ_ONLY);
		type.setItems(new String[] { "現金", "預金", "カード", "投資" });
		type.pack();
		type.select(0);

		this.lblCurrency = new Label(grp, SWT.NONE);
		lblCurrency.setText(_("Currency:"));
		lblCurrency.pack();
		this.currency = new Combo(grp, SWT.READ_ONLY);
		currency.setItems(new String[] { "USD", "JPY", "EUD", "GBP", "RMB" });
		currency.pack();
		currency.select(1);

		this.lblDay = new Label(grp, SWT.NONE);
		lblDay.setText(_("締切日："));
		lblDay.pack();
		this.day = new Text(grp, SWT.BORDER);

		// right side
		this.lblBankName = new Label(grp, SWT.NONE);
		lblBankName.setText(_("Bank Name:"));
		lblBankName.pack();
		this.bankName = new Text(grp, SWT.BORDER);

		this.lblBranchName = new Label(grp, SWT.NONE);
		lblBranchName.setText(_("Branch Name:"));
		lblBranchName.pack();
		this.branchName = new Text(grp, SWT.BORDER);

		this.lblBankNo = new Label(grp, SWT.NONE);
		lblBankNo.setText(_("Account No:"));
		lblBankNo.pack();
		this.bankNo = new Text(grp, SWT.BORDER);

		this.lblInterest = new Label(grp, SWT.NONE);
		lblInterest.setText(_("Interest Rate:"));
		lblInterest.pack();
		this.interest = new Text(grp, SWT.BORDER | SWT.RIGHT);
		interest.setText(_("00.00"));
		interest.pack();
		this.lblInterestR = new Label(grp, SWT.NONE);
		lblInterestR.setText(_("%"));
		lblInterestR.pack();
		this.interestPer = new Combo(grp, SWT.READ_ONLY);
		interestPer.setItems(new String[] { _("One Year"), _("One Month") });
		interestPer.select(0);

		this.lblStart = new Label(grp, SWT.NONE);
		lblStart.setText(_("Start Balance:"));
		lblStart.pack();
		this.start = new Text(grp, SWT.BORDER);

		this.lblMemo = new Label(grp, SWT.NONE);
		lblMemo.setText(_("Memo:"));
		lblMemo.pack();
		memo = new Text(grp, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);

		save = new Button(grp, SWT.NONE);
		save.setText(_("Save this account"));

		internalLayout();

		return grp;
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

		setFormLayoutData(name, lblName, 0, SWT.TOP, c, 10, SWT.NONE).width = 80;
		setFormLayoutData(namePh, lblNamePh, 0, SWT.TOP, c, 10, SWT.NONE).width = 80;
		setFormLayoutData(type, lblType, 0, SWT.TOP, c, 10, SWT.NONE);
		setFormLayoutData(currency, lblCurrency, 0, SWT.TOP, c, 10, SWT.NONE);
		setFormLayoutData(day, lblDay, 0, SWT.TOP, c, 10, SWT.NONE).width = 80;

		// right side
		setFormLayoutData(lblBankName, name, 0, SWT.TOP, name, 50, SWT.NONE);
		setFormLayoutData(lblBranchName, lblBankName, 20, SWT.NONE, name, 50,
				SWT.NONE);
		setFormLayoutData(lblBankNo, lblBranchName, 20, SWT.NONE, name, 50,
				SWT.NONE);
		setFormLayoutData(lblInterest, lblBankNo, 20, SWT.NONE, name, 50,
				SWT.NONE);
		setFormLayoutData(lblStart, lblInterest, 20, SWT.NONE, lblBankName, 0,
				SWT.LEFT);

		Control d = computeSize(new Control[] { lblBankName, lblBranchName, lblBankNo,
				lblInterest, lblStart});

		setFormLayoutData(bankName, lblBankName, 0, SWT.TOP, d, 5,
				SWT.NONE).width = 80;
		setFormLayoutData(branchName, lblBranchName, 0, SWT.TOP, d,
				5, SWT.NONE).width = 80;
		setFormLayoutData(bankNo, lblBankNo, 0, SWT.TOP, d, 5, SWT.NONE).width = 80;
		setFormLayoutData(interest, lblInterest, 0, SWT.TOP, d, 5,
				SWT.NONE);
		setFormLayoutData(lblInterestR, lblInterest, 0, SWT.TOP, interest, 5,
				SWT.NONE);
		setFormLayoutData(interestPer, lblInterestR, 0, SWT.TOP, lblInterestR,
				5, SWT.NONE);
		setFormLayoutData(start, lblStart, 0, SWT.TOP, d, 5, SWT.NONE).width = 80;

		
		// bottom
		setFormLayoutData(lblMemo, lblDay, 20, SWT.NONE, lblName, 0, SWT.LEFT);
		FormData fd = setFormLayoutData(memo, lblMemo, 0, SWT.NONE, lblName, 0,
				SWT.LEFT);
		fd.width = 350;
		fd.height = 80;
		setFormLayoutDataRight(save, memo, 20, SWT.NONE, memo, 0, SWT.RIGHT).width = 80;
	}
}
