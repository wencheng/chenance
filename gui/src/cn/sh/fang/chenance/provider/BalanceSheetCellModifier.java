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

import java.text.NumberFormat;
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

	private BalanceSheetContentProvider bs;

	private AccountListProvider accountListProv; 

	public BalanceSheetCellModifier(BalanceSheetContentProvider bs2, TableViewer bs, AccountListProvider accountListProv) {
		this.viewer = bs;
		this.bs = bs2;
		this.accountListProv = accountListProv;
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
		LOG.debug(col);

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
			List<Category> l = (List<Category>)viewer.getData("categoryList");
			if ( i < 0 ) {
				i = 0;
			}
			t.setCategory(l.get(i));
			break;
		case DEBIT:
			t.setDebit(Integer.valueOf((String)value));
			break;
		case CREDIT:
			t.setCredit(Integer.valueOf((String)value));
			break;
		default:
		}

		ts.save(t);
		viewer.refresh();
//		viewer.update(t, null);
		
		bs.itemChanged( t );
		accountListProv.itemChanged( t.getAccount() );
	}
}
