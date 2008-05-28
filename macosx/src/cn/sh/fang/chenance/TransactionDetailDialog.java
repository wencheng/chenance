package cn.sh.fang.chenance;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import cn.sh.fang.chenance.data.dao.RepeatPaymentService;
import cn.sh.fang.chenance.data.entity.RepeatPayment;
import cn.sh.fang.chenance.data.entity.Transaction;

public class TransactionDetailDialog extends Dialog {
	
	private Transaction t;
	private Button chkRepeat;
	private Button chkAutoApprove;

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
		this.chkRepeat = new Button(parent, SWT.CHECK);
		chkRepeat.setText( "Repeatable" );
		
		this.chkAutoApprove = new Button(parent, SWT.CHECK);
		chkAutoApprove.setText( "Auto Approval" );

		setDefaultValues();
		addListeners();

		return super.createDialogArea(parent);
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
