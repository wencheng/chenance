package cn.sh.fang.chenance.provider;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import cn.sh.fang.chenance.data.dao.TransactionService;
import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.data.entity.Transaction;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider.Column;

public class BalanceSheetCellModifier implements ICellModifier {
	
	final static Logger LOG = Logger.getLogger(BalanceSheetCellModifier.class);

	TableViewer viewer;
	
	TransactionService ts = new TransactionService(); 

	public BalanceSheetCellModifier(TableViewer bs) {
		this.viewer = bs;
	}

	public boolean canModify(Object o, String prop) {
		if ( ((Transaction)o).getDate() == null ) {
			return false;
		}		
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object getValue(Object element, String property) {
		// Find the index of the column
		Column col = Column.valueOf(property);

		Object result = null;
		Transaction t = (Transaction) element;
//		LOG.debug(t);
		LOG.debug(col);
//		LOG.debug(t.getDebit());

		switch (col) {
		//case Column.DATE:
			//result = new Boolean(t.getIsRepeat());
			//break;
		case DATE:
			result = t.getDate();
			break;
		case CATEGORY:
			List<Category> l = (List<Category>)viewer.getData("categoryList");
			result = new Integer(l.indexOf(t.getCategory()));
			break;
		case DEBIT:
			result = t.getDebit() + "";
			break;
		case CREDIT:
			result = t.getCredit() + "";
			break;
		case DETAIL:
			result = t;
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
		LOG.debug("modified column: " + col);

		TableItem item = (TableItem) element;
		Transaction t = (Transaction) item.getData();

		switch (col) {
		/*
		case 0:
			task.setIsRepeat(((Boolean) value).booleanValue());
			break;
			*/
		case DATE:
			t.setDate((Date) value);
			break;
		case CATEGORY:
			int i = (Integer)value;
			if ( i < 0 ) break;
			List<Category> l = (List<Category>)viewer.getData("categoryList");
			t.setCategory(l.get(i));
			break;
		case DEBIT:
			t.setDebit(Integer.valueOf((String)value));
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

		ts.save(t);
		viewer.update(t, null);
	}
}
