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

import static cn.sh.fang.chenance.i18n.UIMessageBundle._;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import cn.sh.fang.chenance.data.entity.Transaction;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider.Column;


/**
 * Label provider for the TableViewerExample
 * 
 * @see org.eclipse.jface.viewers.LabelProvider 
 */
public class BalanceSheetLabelProvider extends StyledCellLabelProvider { 
	
	static final Logger LOG = Logger.getLogger( BalanceSheetLabelProvider.class );
	
	public static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy/MM/dd");
	
	public static final SimpleDateFormat MMDD = new SimpleDateFormat("MM/dd");
	
	public static final SimpleDateFormat DD = new SimpleDateFormat("dd");

	TableViewer tableViewer;

	private SimpleDateFormat dateFormat = YYYYMMDD;

	private Color DARK_GREEN;

	public BalanceSheetLabelProvider(TableViewer bsTableViewer) {
		DARK_GREEN = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);
		 
		this.tableViewer = bsTableViewer;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		Transaction t = (Transaction) element;
		Column col = Column.values()[columnIndex];
		
		if ( t.getDate() == null ) {
			if ( col == Column.DATE ) {
				return "<" + _("Add New") + ">";
			} else {
				return "";
			}
		}
		
		switch (col) {
			case DATE:
				result = dateFormat.format(t.getDate().getTime());
				break;
			case CATEGORY:
				result = t.getCategory() != null ? t.getCategory().getDisplayName() : "";
				break;
			case DEBIT:
				NumberFormat exValue = NumberFormat.getCurrencyInstance();
				result = exValue.format(t.getDebit());
				break;
			case CREDIT:
				exValue = NumberFormat.getCurrencyInstance();
				result = exValue.format(t.getCredit());
				break;
			case BALANCE:
				result = NumberFormat.getCurrencyInstance().format(t.getBalance());
				break;
			case DETAIL:
				result = getDetailLabel(t);
				break;
			default:
				break; 	
		}
		return result;
	}
	
	protected static String getDetailLabel(Transaction t) {
		if ( t == null ) return "";

		String ret = "";
		// TODO implement this
		if ( t.getRepeatPayment() != null ) {
			ret += "定期" + ",";
		}
		return ret;
	}
	
	@Override
	public void update(ViewerCell cell) {
		Transaction t = (Transaction) cell.getElement();
		int index = cell.getColumnIndex();
		String columnText = getColumnText(t, index);
		cell.setText(columnText);
		cell.setImage(getColumnImage(t, index));

		if ( ! t.getIsConfirmed() ) {
			cell.setForeground(DARK_GREEN);
		} else {
			cell.setForeground(null);
		}
	
		super.update(cell);
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public void setDateFormat(SimpleDateFormat f) {
		this.dateFormat = f;
		tableViewer.refresh();
	}
}
