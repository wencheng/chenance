package cn.sh.fang.gfp.swt.provider;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;

import cn.sh.fang.gfp.swt.MainWindow;
import cn.sh.fang.gfp.swt.provider.BalanceSheetContentProvider.Column;
import cn.sh.fang.gfp.swt.util.ITableLabelProviderEx;
import cn.sh.fang.gtp.entity.Transaction;


/**
 * Label provider for the TableViewerExample
 * 
 * @see org.eclipse.jface.viewers.LabelProvider 
 */
public class BalanceSheetLabelProvider 
	extends LabelProvider
	implements ITableLabelProviderEx {

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
		switch (col) {
			case DATE:
				result = new SimpleDateFormat("yyyy/MM/dd").format(t.getDate());
				break;
			case CATEGORY:
				result = t.getCategory() != null ? t.getCategory().getName() : "";
				break;
			case DEBIT:
				NumberFormat exValue = NumberFormat.getCurrencyInstance(Locale.JAPAN);
				result = exValue.format(t.getDebit());
				break;
			case CREDIT:
				exValue = NumberFormat.getCurrencyInstance(Locale.JAPAN);
				result = exValue.format(t.getCredit());
				break;
			case DETAIL:
				result = "’èŠú";
				break;
			default:
				break; 	
		}
		return result;
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

	public Button getColumnButton(Object element, int columnIndex) {
		if (columnIndex == Column.DETAIL.ordinal()) {
			Button btn = new Button(table, SWT.PUSH);
			btn.setText("...");
			return btn;
		} else {
			return null;
		}
	}

}
