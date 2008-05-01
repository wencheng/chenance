


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;


public class HistoryTableMouseDownListener implements Listener {

	private HistoryTable table;
	private TableEditor editor;

	public HistoryTableMouseDownListener(HistoryTable historyTable,
			TableEditor editor) {
		this.table = historyTable;
		this.editor = editor;
	}

	public void handleEvent(Event event) {
		Rectangle clientArea = table.getClientArea();
		Point pt = new Point(event.x, event.y);
		int index = table.getTopIndex();
		while (index < table.getItemCount()) {
			boolean visible = false;
			final TableItem item = table.getItem(index);
			for (int i = 0; i < table.getColumnCount(); i++) {
				Rectangle rect = item.getBounds(i);
				if (rect.contains(pt)) {
					final int column = i;

					// TODO handle 1-click

					// TODO handle 2-click

					// TODO handle double click

					if (column == 1) {
						handleCombo(item, column, editor);
					} else {
						handleText(item, column, editor);
					}
				}
				if (!visible && rect.intersects(clientArea)) {
					visible = true;
				}
			}
			if (!visible)
				return;
			index++;
		}
	}

	private void handleCombo(final TableItem item, int column,
			TableEditor editor) {
		String[] options = { "Option 1", "Option 2", "Option 3" };
		// Create the dropdown and add data to it
		final CCombo combo = new CCombo(table, SWT.READ_ONLY);
		for (int i = 0, n = options.length; i < n; i++) {
			combo.add(options[i]);
		}

		// Select the previously selected item from the cell
		combo.select(combo.indexOf(item.getText(column)));

		// Compute the width for the editor
		// Also, compute the column width, so that the dropdown fits
		editor.minimumWidth = combo.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		//table.getColumn(column).setWidth(editor.minimumWidth);

		// Set the focus on the dropdown and set into the editor
		combo.setFocus();
		editor.setEditor(combo, item, column);
		
		// Add a listener to set the selected item back into the cell
		final int col = column;
		combo.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent arg0) {
				// TODO 自動生成されたメソッド・スタブ
			}

			public void focusLost(FocusEvent arg0) {
				combo.dispose();
			}
			
		});
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				item.setText(col, combo.getText());

				// They selected an item; end the editing session
				combo.dispose();
			}
		});
	}

	private void handleText(final TableItem item, final int column,
			final TableEditor editor) {
		final Text text = new Text(table, SWT.NONE);
		Listener textListener = new Listener() {
			public void handleEvent(final Event e) {
				switch (e.type) {
				case SWT.FocusOut:
					item.setText(column, text.getText());
					text.dispose();
					break;
				case SWT.Traverse:
					switch (e.detail) {
					case SWT.TRAVERSE_RETURN:
						item.setText(column, text.getText());
						// FALL THROUGH
					case SWT.TRAVERSE_ESCAPE:
						text.dispose();
						e.doit = false;
					}
					break;
				}
			}
		};
		text.addListener(SWT.FocusOut, textListener);
		text.addListener(SWT.Traverse, textListener);
		editor.setEditor(text, item, column);
		text.setText(item.getText(column));
		text.selectAll();
		text.setFocus();
	}
}
