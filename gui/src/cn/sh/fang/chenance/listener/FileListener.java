/*
 * Copyright 2008 Wencheng FANG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sh.fang.chenance.listener;

import static cn.sh.fang.chenance.i18n.UIMessageBundle._;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;

import cn.sh.fang.chenance.data.dao.BaseService;

public class FileListener {

	public static class FileNewListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent event) {
			DirectoryDialog dlg = new DirectoryDialog(event.display.getActiveShell(),
					SWT.OPEN);
			dlg.setText(_("Please select Chenance file"));
			String fileName = dlg.open();

			if (fileName != null) {
				// TODO create a new db in that dir
			}
		}

	}

	public static class FileOpenListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent event) {
			FileDialog dlg = new FileDialog(event.display.getActiveShell(),
					SWT.OPEN);
	        String[] filterExt = { "*.che" };
	        dlg.setFilterExtensions(filterExt);
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
			BaseService.flushSession();
		}

	}
}
