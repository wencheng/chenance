package cn.sh.fang.chenance.util.swt;

import java.util.HashMap;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

public class TableViewerEx extends TableViewer {

	public TableViewerEx(Composite parent, int style) {
		this(new Table(parent, style));
	}

	public TableViewerEx(Table table) {
		super(table);
	}
	
	HashMap<Button,TableEditor> btnEditors = new HashMap<Button,TableEditor>();
	
	@Override
	protected void doUpdateItem(Widget widget, Object element, boolean fullMap) {
		System.err.println("doUpdateItem " + element);
		
		super.doUpdateItem(widget, element, fullMap);

		if (widget instanceof TableItem
				&& getLabelProvider() instanceof ITableLabelProviderEx) {
			TableItem ti = (TableItem) widget;
			ITableLabelProviderEx prov = (ITableLabelProviderEx) getLabelProvider();
			Button btn;

			for (int i = 0; i < getTable().getColumnCount() || i == 0; i++) {
				btn = prov.getColumnButton(element, i);
				TableEditor e = btnEditors.get(btn);
				if ( btn != null && e == null ) {
					e = new TableEditor(getTable());
					btn.setParent(getTable());
					btn.computeSize(SWT.DEFAULT, getTable().getItemHeight());
					btn.setSize(new Point(20, getTable().getItemHeight()));
					System.out.println("btn: "+ti);
					e.grabHorizontal = false;
					e.minimumHeight = btn.getSize().y;
					e.minimumWidth = btn.getSize().x;
					e.horizontalAlignment = SWT.RIGHT;
					e.setEditor(btn, ti, i);
					this.btnEditors.put(btn,e);
				}
			}
		}

	}
}
