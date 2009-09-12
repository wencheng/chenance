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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.sh.fang.chenance.data.dao.BreakdownService;
import cn.sh.fang.chenance.data.dao.RepeatPaymentService;
import cn.sh.fang.chenance.data.entity.Breakdown;
import cn.sh.fang.chenance.data.entity.RepeatPayment;
import cn.sh.fang.chenance.data.entity.Transaction;
import cn.sh.fang.chenance.i18n.UIMessageBundle;

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
	private DataBindingContext cnxt = new DataBindingContext();

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
		setText( lblBreakdown, "Breakdown:\n" +
				"(+) for Credit\n" +
				"(-) for Debit" );
		this.btnAdd = new Button( this.parent, SWT.PUSH);
		setText( btnAdd, "Add" );

		internalLayout();

		setDefaultValues();
		reLayoutWithoutPack();

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
		
		for ( Breakdown b : new BreakdownService().findAll(t) ) {
			addBreakdown(b);
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

				Breakdown bd = new Breakdown();
				bd.setTransaction(t);
				bd.setItemName("Unset");
				bd.setAmount(0);
				bd.setUpdater("");
				
				ComboText ct = addBreakdown(bd);
				reLayout();

				ct.amount.forceFocus();
				ct.amount.selectAll();
			}
		});
	}
	
	private ComboText addBreakdown(Breakdown breakdown) {
		ComboText ct = new ComboText();
		ct.breakdown = breakdown;
		ct.category = new Combo( parent, SWT.BORDER );
		ct.amount = new Text( parent, SWT.BORDER | SWT.RIGHT );
		ct.btnDelete = new Button( parent, SWT.BORDER );
		ct.btnDelete.setText("-");
		ct.btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ComboText c = (ComboText) e.widget.getData();
				c.category.dispose();
				c.amount.dispose();
				c.btnDelete.dispose();

				//breakdowns.remove(c);
				c.breakdown.setDeleted(true);
				
				reLayout();
			}
		});
		ct.btnDelete.setData(ct);

		// bind amount
        IObservableValue observeWidget = SWTObservables.observeText(ct.amount, SWT.Modify);
        IObservableValue observeValue = BeansObservables.observeValue(ct.breakdown, "amount");
        cnxt.bindValue(observeWidget, observeValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE), null);

		breakdowns.add(ct);
		return ct;
	}
	
	protected void reLayout() {
		reLayoutWithoutPack();
		TransactionDetailDialog.this.getShell().pack();
	}
	
	protected void reLayoutWithoutPack() {
		boolean isFirst = true;
		ComboText last = null;
		for ( ComboText ct : breakdowns ) {
			if (ct.breakdown.isDeleted()) {
				continue;
			}
			
			if ( isFirst ) {
				setFormLayoutData( ct.category, btnAdd, 10, SWT.NONE, btnAdd, 0, SWT.LEFT ).width = 100;
				isFirst = false;
			} else {
				setFormLayoutData( ct.category, last.category, 10, SWT.NONE, last.category, 0, SWT.LEFT ).width = 100;
			}
			setFormLayoutData( ct.amount, ct.category, 0, SWT.TOP, ct.category, 10, SWT.NONE ).width = 80;
			setFormLayoutData( ct.btnDelete, ct.amount, -5, SWT.TOP, ct.amount, 10, SWT.NONE );
			last = ct;
		}
//		parent.pack();
//		TransactionDetailDialog.this.getShell().pack();
	}

	@Override
	protected void okPressed() {
		if ( calcSum() == SWT.CANCEL ) {
			return;
		}

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
		
		BreakdownService bs = new BreakdownService();
		for ( ComboText ct : this.breakdowns ) {
			Breakdown bd = ct.breakdown;
			if ( bd.getId() != null || bd.getId() == null && bd.isDeleted() == false ) {
				bs.save(bd);
			}
		}

		super.okPressed();
	}

	private int calcSum() {
		int credit = 0;
		int debit = 0;
		for ( ComboText ct : this.breakdowns ) {
			Breakdown bd = ct.breakdown;
			if ( bd.getId() != null || bd.getId() == null && bd.isDeleted() == false ) {
				if ( bd.getAmount() > 0 ) {
					debit += bd.getAmount();
				} else {
					credit -= bd.getAmount();
				}
			}
		}
		
		if ( credit - debit != t.getCredit() - t.getDebit()) {
			MessageBox mb = new MessageBox(Display.getCurrent().getActiveShell(), SWT.YES | SWT.NO | SWT.CANCEL | SWT.ICON_WARNING );
			mb.setText(MainWindow.TITLE);
			mb.setMessage( UIMessageBundle._("Total amount of breakdowns you entered DOES NOT match the amount of transaction.\n\n" +
					"Would you like to update the amount of transaction?") );
			
			int ans = mb.open();
			if ( ans == SWT.YES ) {
				this.t.setDebit(debit);
				this.t.setCredit(credit);
			}
			
			return ans;
		} else {
			return SWT.OK;
		}
	}

	public Transaction getTransaction() {
		return this.t;
	}
	
	class ComboText {
		Combo category;
		Text amount;
		Button btnDelete;
		Breakdown breakdown;
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
