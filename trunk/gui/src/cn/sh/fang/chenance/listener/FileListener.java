package cn.sh.fang.chenance.listener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;

import cn.sh.fang.chenance.data.dao.BaseService;

public class FileListener {

	public static class FileOpenListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent event) {
			FileDialog dlg = new FileDialog(event.display.getActiveShell(),
					SWT.OPEN);
			String fileName = dlg.open();

			if (fileName != null) {
				// TODO open the file
				MessageBox mb = new MessageBox(event.display.getActiveShell());
				mb.setMessage("File opened: " + fileName);
				mb.open();
			}
		}

	}

	public static class FileSaveListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent event) {
			BaseService.commit();
		}

	}
}
