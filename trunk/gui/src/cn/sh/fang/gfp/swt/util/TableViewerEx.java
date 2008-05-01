package cn.sh.fang.gfp.swt.util;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

public class TableViewerEx extends TableViewer {
	
	public TableViewerEx(Composite parent) {
		super(parent);
	}

	public TableViewerEx(Composite parent, int style) {
		super(parent, style);
	}
	
	public TableViewerEx(Table table) {
		super(table);
	}

	@Override
	protected void doUpdateItem(Widget widget, Object element, boolean fullMap) {
		super.doUpdateItem(widget, element, fullMap);

		if (widget instanceof TableItem &&
				getLabelProvider() instanceof ITableLabelProviderEx ) {
		
			TableItem ti = (TableItem) widget;
			ITableLabelProviderEx prov = (ITableLabelProviderEx)getLabelProvider();
			Button btn;
			String text = "";//$NON-NLS-1$
			Image image = null;
			
			for (int i = 0; i < 6 || i == 0; i++) {
				text = prov.getColumnText(element, i);
				image = prov.getColumnImage(element, i);
				ti.setText(i, text);
				if (ti.getImage(i) != image) {
					ti.setImage(i, image);
				}
				 
				btn = prov.getColumnButton(element, i);
				if ( btn != null ) {
					TableEditor editor = new TableEditor(getTable());
					btn.setParent(getTable());
					btn.computeSize(SWT.DEFAULT, getTable().getItemHeight());
					System.out.println(btn.getSize());
					btn.setSize(new Point(20,getTable().getItemHeight()));
					System.out.println(btn.getSize());
					editor.grabHorizontal = false;
					editor.minimumHeight = btn.getSize().y;
					editor.minimumWidth = btn.getSize().x;
					editor.setEditor(btn, ti, i);
					editor.horizontalAlignment = SWT.RIGHT;
				}
			}
		}

	}
}
