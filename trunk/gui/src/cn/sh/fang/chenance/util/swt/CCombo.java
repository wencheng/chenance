package cn.sh.fang.chenance.util.swt;

import org.aspencloud.widgets.ImageCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class CCombo extends ImageCombo {

	public CCombo(Composite parent, int style) {
		super(parent, style);
	}

	public Table getTable() {
		return this.table;
	}
	
	/*
	public TableItem getItem(int i) {
		return this.table.getItem(i);
	}
	*/
}
