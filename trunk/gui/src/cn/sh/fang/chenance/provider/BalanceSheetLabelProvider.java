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
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;

import cn.sh.fang.chenance.MainWindow;
import cn.sh.fang.chenance.data.entity.Transaction;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider.Column;
import static cn.sh.fang.chenance.i18n.UIMessageBundle._;


/**
 * Label provider for the TableViewerExample
 * 
 * @see org.eclipse.jface.viewers.LabelProvider 
 */
public class BalanceSheetLabelProvider 
	extends LabelProvider
	implements ITableLabelProvider {

	// Names of images used to represent checkboxes
	public static final String CHECKED_IMAGE 	= "checked";
	public static final String UNCHECKED_IMAGE  = "unchecked";

	// For the checkbox images
	private static ImageRegistry imageRegistry = new ImageRegistry();

	/**
	 * Note: An image registry owns all of the image objects registered with it,
	 * and automatically disposes of them the SWT Display is disposed.
	 */ 
	static {
		String iconPath = "icons/"; 
		imageRegistry.put(CHECKED_IMAGE, ImageDescriptor.createFromFile(
				MainWindow.class, 
				iconPath + CHECKED_IMAGE + ".gif"
				)
			);
		imageRegistry.put(UNCHECKED_IMAGE, ImageDescriptor.createFromFile(
				MainWindow.class, 
				iconPath + UNCHECKED_IMAGE + ".gif"
				)
			);	
	}
	
	Table table;
	private HashMap<Transaction,Button> btns = new HashMap<Transaction,Button>();
	
	public BalanceSheetLabelProvider(Table table) {
		this.table = table;
	}

	/**
	 * Returns the image with the given key, or <code>null</code> if not found.
	 */
	private Image getImage(Boolean isSelected) {
		String key = isSelected ? CHECKED_IMAGE : UNCHECKED_IMAGE;
		return  imageRegistry.get(key);
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
				result = new SimpleDateFormat("yyyy/MM/dd").format(t.getDate());
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

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return 
//			(columnIndex == 0) ?
//			getImage(((Transaction) element).getIsRepeat()) :
			null;
	}

}
