package cn.sh.fang.chenance.util;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.vafada.swtcalendar.SWTCalendar;
import org.vafada.swtcalendar.SWTCalendarEvent;
import org.vafada.swtcalendar.SWTCalendarListener;

public class CalendarCellEditor extends CellEditor implements Listener {

	Composite popup;
	SWTCalendar cal;
	Composite parent;

	public CalendarCellEditor(Composite parent, int style) {
		super(parent, style);
		this.parent = parent;
	}

	@Override
	protected Control createControl(Composite parent) {
		popup = new Shell(parent.getShell(),SWT.APPLICATION_MODAL|SWT.NO_TRIM|SWT.BORDER);
		popup.setLayout(new RowLayout());
		
		cal = new SWTCalendar(popup);

		//cal.addListener(SWT.Move, this);
		//cal.addListener(SWT.Resize, this);

		cal.addSWTCalendarListener(new SWTCalendarListener() {
			public void dateChanged(SWTCalendarEvent event) {
			}
			public void dateSelected(SWTCalendarEvent event) {
				applyEditorValueAndDeactivate();
			}
		});
		
		cal.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					fireCancelEditor();
					deactivate();
				}
				if ( e.detail == SWT.TRAVERSE_RETURN) {
					applyEditorValueAndDeactivate();
				}
			}
		});
		popup.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					fireCancelEditor();
					deactivate();
				}
				if ( e.detail == SWT.TRAVERSE_RETURN) {
					applyEditorValueAndDeactivate();
				}
			}
		});

		return cal;
	}

	@Override
	protected Object doGetValue() {
		return cal.getCalendar().getTime();
	}

	@Override
	protected void doSetFocus() {
		popup.setFocus();
		cal.setFocus();
	}

	public void handleEvent(Event e) {
		if ( e.widget == cal ) {
			Rectangle rect = cal.getBounds();
			System.out.println(rect);
        	cal.setBounds(0,0,222,171);
        	popup.setVisible(true);
		}
	}

	@Override
	public void activate(ColumnViewerEditorActivationEvent e) {
		super.activate(e);


        if ( popup.isVisible() == false ) {
        	Table table = (Table)parent;
//          MouseEvent me = (MouseEvent)e.sourceEvent;
//        	TableItem ti = table.getItem(new Point(me.x,me.y));
        	TableItem ti = 	table.getSelection()[0];
        	
        	Rectangle tiRect = ti.getBounds();
        	System.out.println("item "+tiRect);
        	Point p = table.toDisplay(tiRect.x,tiRect.y);
        	System.out.println("p "+p);
        	popup.setBounds(p.x+tiRect.width,p.y+tiRect.height,222,171);
        	cal.setBounds(0,0,222,171);
        	popup.setVisible(true);
		}
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		if ( popup.isVisible() ) {
			popup.setVisible(false);
		}
	}
	
	protected void applyEditorValueAndDeactivate() {
		markDirty();
		fireApplyEditorValue();
		deactivate();
	}

	@Override
	protected void doSetValue(Object value) {
		Calendar c = Calendar.getInstance();
		c.setTime((Date)value);
		cal.setCalendar(c);
	}

	@Override
	public LayoutData getLayoutData() {
		LayoutData layoutData = super.getLayoutData();
		layoutData.minimumWidth = 222;
		return layoutData;
	}

}
