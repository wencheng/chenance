package cn.sh.fang.chenance;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;

public class GroceriesTab {
	
	public Control getTabControl(TabFolder parent) {
		Composite comp = new Composite(parent, SWT.None);
		
		return comp;
	}

}
