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
	
	public BalanceSheetDetailCellEditor(Composite table) {
		super(table);
	}

	@Override
	protected void updateContents(Object value) {
		super.updateContents(value);
		LOG.debug(value);
		Transaction t = (Transaction)value;
		getDefaultLabel().setText( BalanceSheetLabelProvider.getDetailLabel(t) );
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
