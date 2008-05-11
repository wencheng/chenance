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
		switch (col) {
			case DATE:
				result = new SimpleDateFormat("yyyy/MM/dd").format(t.getDate());
				break;
			case CATEGORY:
				result = t.getCategory() != null ? t.getCategory().getName() : "";
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

	public Button getColumnButton(Object element, int columnIndex) {
		if (columnIndex == Column.DETAIL.ordinal()) {
			Transaction t = (Transaction)element;
			Button btn = this.btns.get(t);
			if ( btn == null ) {
				final Button b = new Button(table, SWT.PUSH);
				b.setText(btns.size()+"");
				b.setText("...");
				b.addMouseListener(new MouseAdapter(){
					@Override
					public void mouseUp(MouseEvent e) {
						super.mouseUp(e);
						if ( e.button == 1 ) {
							// TODO show detail dialog
							System.out.println(b);
						}
					}
				});
				b.addListener(SWT.Move, new Listener(){
					public void handleEvent(Event e) {
						if ( e.type == SWT.Move) {
							System.out.println(b+" moving");
						}
					}
				});
				btn = b;
				this.btns.put(t, btn);
			}
			
			return btn; 
		} else {
			return null;
		}
	}

}
