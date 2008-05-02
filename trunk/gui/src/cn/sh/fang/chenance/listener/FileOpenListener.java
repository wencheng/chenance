package cn.sh.fang.chenance.listener;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;

public class FileOpenListener extends SelectionAdapter {

	public void widgetSelected(SelectionEvent event) {
		// TODO open a file
		MessageBox mb = new MessageBox(event.display.getActiveShell());
		mb.setMessage("File opened");
		mb.open();
	}

}
