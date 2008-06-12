package cn.sh.fang.chenance;

import static cn.sh.fang.chenance.i18n.UIMessageBundle.setText;
import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutData;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import cn.sh.fang.chenance.data.dao.RepeatPaymentService;
import cn.sh.fang.chenance.data.entity.RepeatPayment;
import cn.sh.fang.chenance.data.entity.Transaction;

public class TransactionDetailDialog extends Dialog {
	
	private Transaction t;
	private Button chkRepeat;
	private Button chkAutoApprove;
	private Button btnAdd;
	private Group grp1;
	private Composite parent;
	private Group grp2;

	public TransactionDetailDialog(Shell shell, Transaction t) {
		super(shell);
		this.t = t;
	}

	protected TransactionDetailDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		return super.createContents(parent);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.parent = parent;
		
		grp1 = new Group( parent, SWT.NONE );
		setText( grp1, "Repeat" );
		this.chkRepeat = new Button(grp1, SWT.CHECK);
		setText( chkRepeat, "Repeatable" );
		this.chkAutoApprove = new Button(grp1, SWT.CHECK);
		setText( chkAutoApprove, "Auto Approval" );

		grp2 = new Group( parent, SWT.NONE );
		setText( grp2, "Breakdown" );
		this.btnAdd = new Button(grp2, SWT.PUSH);
		setText( btnAdd, "Add" );

		internalLayout();
		setDefaultValues();
		addListeners();

		return super.createDialogArea(parent);
	}

	private void internalLayout() {
//		FormLayout formLayout = new FormLayout();
//		parent.setLayout(formLayout);
//		formLayout.marginHeight = 10;
//		formLayout.marginWidth = 10;
//		
//		setFormLayoutData( grp1, 0, 20, 0, 20 );
//		setFormLayoutData( grp2, grp1, 0, SWT.NONE, grp1, 0, SWT.LEFT );
		
		// repeat
		grp1.setLayout( new FormLayout() );
		setFormLayoutData( chkRepeat, 0, 20, 0, 20 );
		setFormLayoutData( chkAutoApprove, chkRepeat, 20, SWT.NONE, chkRepeat, 0,
				SWT.LEFT);

		// breakdown
		grp2.setLayout( new FormLayout() );
		setFormLayoutData( btnAdd, 0, 20, 0, 20 );
	}
	
	private void setDefaultValues() {
		if ( t.getRepeatPayment() != null ) {
			chkRepeat.setSelection( true );
			chkAutoApprove.setSelection( t.getRepeatPayment().getAutoApprove() );
		} else {
			chkRepeat.setSelection( false );
			chkAutoApprove.setEnabled( false );
		}
	}

	private void addListeners() {
		chkRepeat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				super.widgetSelected(arg0);
				chkAutoApprove.setEnabled( chkRepeat.getSelection() );
			}
		});
	}

	@Override
	protected void okPressed() {
		if ( chkRepeat.getSelection() ) {
			RepeatPayment r = null;
			if ( t.getRepeatPayment() == null ) {
				r = new RepeatPayment();
				t.setRepeatPayment( r );
			}
			r.setPeriod( 0 );
			r.setPeriodUnit( 0 );
			r.setUpdater( "USER" );
			r.setAmount( t.getDebit() );
			r.setAutoApprove( chkAutoApprove.getSelection() );
			new RepeatPaymentService().save( r );
		}
		
		super.okPressed();
	}

	public Transaction getTransaction() {
		return this.t;
	}

}
