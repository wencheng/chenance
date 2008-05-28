package cn.sh.fang.chenance.provider;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import cn.sh.fang.chenance.TransactionDetailDialog;
import cn.sh.fang.chenance.data.entity.Transaction;

public class BalanceSheetDetailCellEditor extends DialogCellEditor {

	final static Logger LOG = Logger.getLogger(BalanceSheetDetailCellEditor.class);
	
	Label label;

	public BalanceSheetDetailCellEditor(Composite table) {
		super(table);
	}

	@Override
	protected Control createContents(Composite cell) {
		Color bg = cell.getBackground();
		Composite composite = new Composite(cell, getStyle());
		composite.setBackground(bg);
		FormLayout f = new FormLayout();
		f.marginLeft = 5;
		f.marginTop = 2;
		composite.setLayout(f);
		label = new Label(composite, getStyle());
		label.setBackground(bg);
		label.setFont(cell.getFont());
		return composite;
	}

	@Override
	protected void updateContents(Object value) {
		super.updateContents(value);
		LOG.debug(value);
		Transaction t = (Transaction)value;
		label.setText( BalanceSheetLabelProvider.getDetailLabel(t) );
	}

	@Override
	protected Label getDefaultLabel() {
		// TODO Auto-generated method stub
		return super.getDefaultLabel();
	}

	@Override
	protected Object doGetValue() {
		// TODO Auto-generated method stub
		return super.doGetValue();
	}

	@Override
	protected void doSetValue(Object value) {
		// TODO Auto-generated method stub
		super.doSetValue(value);
	}

	@Override
	protected Object openDialogBox(Control c) {
		TransactionDetailDialog diag = new TransactionDetailDialog( c.getShell(), (Transaction)getValue() );
		int ret = diag.open();
		if ( ret == SWT.OK ) {
			return diag.getTransaction();
		} else {
			return getValue();
		}
	}

}
