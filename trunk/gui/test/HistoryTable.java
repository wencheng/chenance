
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class HistoryTable extends Table {

	public HistoryTable(Composite parent) {
		this(parent, SWT.BORDER | SWT.FULL_SELECTION);
	}

	private HistoryTable(Composite parent, int style) {
		super(parent, style);
		create(parent);
	}

	@Override
	protected void checkSubclass() {
		// kill subclass check.
	}
	
	private void create(Composite composite) {
		this.setHeaderVisible(true);
		this.setLinesVisible(true);
		for (int i = 0; i < 3; i++) {
			TableColumn column = new TableColumn(this, SWT.NONE);
			column.setWidth(100);
		}
		for (int i = 0; i < 3; i++) {
			TableItem item = new TableItem(this, SWT.NONE);
			item.setText(new String[] { "" + i, "" + i, "" + i });
		}

		final TableEditor editor = new TableEditor(this);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		this.addListener(SWT.MouseDoubleClick, new HistoryTableMouseDownListener(this,editor));
	}
	
}
