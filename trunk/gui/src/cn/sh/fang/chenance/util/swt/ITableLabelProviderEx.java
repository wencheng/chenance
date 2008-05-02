package cn.sh.fang.chenance.util.swt;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.widgets.Button;

public interface ITableLabelProviderEx extends ITableLabelProvider {

	public Button getColumnButton(Object element, int columnIndex);
	
}
