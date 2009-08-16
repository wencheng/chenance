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
import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutData;
import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutDataRight;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.sh.fang.chenance.data.dao.RepeatPaymentService;
import cn.sh.fang.chenance.data.entity.RepeatPayment;
import cn.sh.fang.chenance.data.entity.Transaction;

public class TransactionDetailDialog extends Dialog {
	
	private Transaction t;
	private Button chkRepeat;
	private Button chkAutoConfirm;
	private Button btnAdd;
	private Label lblRepeat;
	private Composite parent;
	private Label lblBreakdown;
	private List<ComboText> breakdowns = new ArrayList<ComboText>();
	private Button chkConfirmed;

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
		this.parent = new Composite( parent, SWT.NONE );

		this.chkConfirmed = new Button( this.parent, SWT.CHECK);
		setText( chkConfirmed, "Confirmed" );

		lblRepeat = new Label( this.parent, SWT.NONE );
		setText( lblRepeat, "Repeatability:" );
		this.chkRepeat = new Button( this.parent, SWT.CHECK);
		setText( chkRepeat, "Repeatable" );
		this.chkAutoConfirm = new Button( this.parent, SWT.CHECK);
		setText( chkAutoConfirm, "Auto Confirm" );

		lblBreakdown = new Label( this.parent, SWT.NONE );
		setText( lblBreakdown, "Breakdown:" );
		this.btnAdd = new Button( this.parent, SWT.PUSH);
		setText( btnAdd, "Add" );

		internalLayout();
		setDefaultValues();
		addListeners();

		return parent;
//		return super.createDialogArea(parent);
	}

	private void internalLayout() {
		FormLayout formLayout = new FormLayout();
		parent.setLayout(formLayout);
		formLayout.marginHeight = 10;
		formLayout.marginWidth = 10;

		// repeat
		setFormLayoutData( lblRepeat, 0, 20, 0, 20 );
		setFormLayoutData( chkRepeat, lblRepeat, -2,
				SWT.TOP, lblRepeat, 10, SWT.NONE );
		setFormLayoutData( chkAutoConfirm, chkRepeat, 10, SWT.NONE, lblRepeat, 10, SWT.NONE );
		setFormLayoutData( chkConfirmed, chkAutoConfirm, 10, SWT.NONE, lblRepeat, 10, SWT.NONE );

		// separator
		Label sep = new Label( this.parent, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.BORDER );
		FormData fd = new FormData();
		sep.setLayoutData( fd );
		fd.top = new FormAttachment( chkConfirmed, 20 );
		fd.left = new FormAttachment( 0, 0);
		fd.right= new FormAttachment( 100, 0 );

		// breakdown
		setFormLayoutDataRight( lblBreakdown, sep, 20, SWT.NONE, lblRepeat, 0, SWT.RIGHT );
		setFormLayoutData( btnAdd, lblBreakdown, -(btnAdd.getSize().y-lblBreakdown.getSize().y)/2,
				SWT.TOP, lblBreakdown, 10, SWT.NONE ).width = 80;
	}
	
	private void setDefaultValues() {
		chkConfirmed.setSelection( t.getIsConfirmed() );
		
		if ( t.getRepeatPayment() != null ) {
			chkRepeat.setSelection( true );
			chkAutoConfirm.setSelection( t.getRepeatPayment().getAutoConfirm() );
			chkConfirmed.setEnabled( chkRepeat.getSelection() );
		} else {
			chkRepeat.setSelection( false );
			chkAutoConfirm.setEnabled( false );
			chkConfirmed.setEnabled( false );
		}
	}

	private void addListeners() {
		chkRepeat.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				super.widgetSelected(arg0);
				chkAutoConfirm.setEnabled( chkRepeat.getSelection() );
				chkConfirmed.setEnabled( chkRepeat.getSelection() );
			}
		});

		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				super.widgetSelected(arg0);
				ComboText ct = new ComboText();
				ct.combo = new Combo( parent, SWT.BORDER );
				ct.text = new Text( parent, SWT.BORDER );
				ct.btn = new Button( parent, SWT.BORDER );
				ct.btn.setText("-");
				ct.btn.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						ComboText c = (ComboText) e.widget.getData();
						c.combo.dispose();
						c.text.dispose();
						breakdowns.remove(c);
						e.widget.dispose();
						reLayout();
					}
				});
				ct.btn.setData(ct);
				breakdowns.add( ct );

				reLayout();
			}
		});
	}
	
	protected void reLayout() {
		boolean isFirst = true;
		ComboText last = null;
		for ( ComboText ct : breakdowns ) {
			if ( isFirst ) {
				setFormLayoutData( ct.combo, btnAdd, 10, SWT.NONE, btnAdd, 0, SWT.LEFT ).width = 100;
				isFirst = false;
			} else {
				setFormLayoutData( ct.combo, last.combo, 10, SWT.NONE, last.combo, 0, SWT.LEFT ).width = 100;
			}
			setFormLayoutData( ct.text, ct.combo, 0, SWT.TOP, ct.combo, 10, SWT.NONE ).width = 80;
			setFormLayoutData( ct.btn, ct.text, -5, SWT.TOP, ct.text, 10, SWT.NONE );
			last = ct;
		}
		parent.pack();
		TransactionDetailDialog.this.getShell().pack();
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
			r.setAutoConfirm( chkAutoConfirm.getSelection() );
			new RepeatPaymentService().save( r );
		}
		
		super.okPressed();
	}

	public Transaction getTransaction() {
		return this.t;
	}
	
	class ComboText {
		Combo combo;
		Text text;
		Button btn;
	}
	
	public final static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);

		Transaction t =  new Transaction();
		t.setIsConfirmed( true );
		
		TransactionDetailDialog diag = new TransactionDetailDialog(shell, t);
		try {
			diag.open();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

}
