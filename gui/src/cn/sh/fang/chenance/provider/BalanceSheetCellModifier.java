package cn.sh.fang.chenance.provider;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import cn.sh.fang.chenance.provider.BalanceSheetContentProvider.Column;
import cn.sh.fang.chinance.data.entity.Transaction;

public class BalanceSheetCellModifier implements ICellModifier {

	TableViewer balanceSheet;

	public BalanceSheetCellModifier(TableViewer bs) {
		this.balanceSheet = bs;
	}

	public boolean canModify(Object arg0, String arg1) {
		return true;
	}

	public Object getValue(Object element, String property) {
		// Find the index of the column
		Column col = Column.valueOf(property);

		Object result = null;
		Transaction t = (Transaction) element;

		switch (col) {
		//case Column.DATE:
			//result = new Boolean(t.getIsRepeat());
			//break;
		case DATE:
			result = t.getDate();
			break;
		case CATEGORY:
			String stringValue = "";
			String[] choices = { "opt1", "opt2", "opt3" };
			int i = choices.length - 1;
			while (!stringValue.equals(choices[i]) && i > 0)
				--i;
			result = new Integer(1);
			break;
		case CREDIT:
			result = t.getCredit() + "";
			break;
		default:
			result = "";
		}
		return result;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object,
	 *      java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) {
		// Find the index of the column
		Column col = Column.valueOf(property);

		TableItem item = (TableItem) element;
		Transaction t = (Transaction) item.getData();
		String valueString;

		switch (col) {
		/*
		case 0:
			task.setIsRepeat(((Boolean) value).booleanValue());
			break;
			*/
		case DATE:
			t.setDate((Date) value);
			break;
		case CREDIT:
			t.setCredit(Integer.valueOf((String)value));
			break;
		/*
		 * case 2 : // OWNER_COLUMN valueString = new
		 * String[]{"opt1","opt2","opt3"}[((Integer) value).intValue()].trim();
		 * if (!task.getOwner().equals(valueString)) {
		 * task.setOwner(valueString); } break; case 3 : // PERCENT_COLUMN
		 * valueString = ((String) value).trim(); if (valueString.length() == 0)
		 * valueString = "0";
		 * task.setPercentComplete(Integer.parseInt(valueString)); break;
		 */
		default:
		}
		balanceSheet.update(t, null);
	}
}
