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
package cn.sh.fang.chenance.util.swt;

import static cn.sh.fang.chenance.i18n.UIMessageBundle._;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class SafeSaveDialog {
	  // The wrapped FileDialog
	  private FileDialog dlg;

	  /**
	   * SafeSaveDialog constructor
	   * 
	   * @param shell the parent shell
	   */
	  public SafeSaveDialog(Shell shell) {
	    dlg = new FileDialog(shell, SWT.SAVE);
	  }

	  public String open() {
	    // We store the selected file name in fileName
	    String fileName = null;

	    // The user has finished when one of the
	    // following happens:
	    // 1) The user dismisses the dialog by pressing Cancel
	    // 2) The selected file name does not exist
	    // 3) The user agrees to overwrite existing file
	    boolean done = false;

	    while (!done) {
	      // Open the File Dialog
	      fileName = dlg.open();
	      if (fileName == null) {
	        // User has cancelled, so quit and return
	        done = true;
	      } else {
	        // User has selected a file; see if it already exists
	        File file = new File(fileName);
	        if (file.exists()) {
	          // The file already exists; asks for confirmation
	          MessageBox mb = new MessageBox(dlg.getParent(), SWT.ICON_WARNING
	              | SWT.YES | SWT.NO);

	          // We really should read this string from a
	          // resource bundle
	          mb.setMessage(_("{0} already exists. Do you want to replace it?", fileName));

	          // If they click Yes, we're done and we drop out. If
	          // they click No, we redisplay the File Dialog
	          done = mb.open() == SWT.YES;
	        } else {
	          // File does not exist, so drop out
	          done = true;
	        }
	      }
	    }
	    return fileName;
	  }

	  public String getFileName() {
	    return dlg.getFileName();
	  }

	  public String[] getFileNames() {
	    return dlg.getFileNames();
	  }

	  public String[] getFilterExtensions() {
	    return dlg.getFilterExtensions();
	  }

	  public String[] getFilterNames() {
	    return dlg.getFilterNames();
	  }

	  public String getFilterPath() {
	    return dlg.getFilterPath();
	  }

	  public void setFileName(String string) {
	    dlg.setFileName(string);
	  }

	  public void setFilterExtensions(String[] extensions) {
	    dlg.setFilterExtensions(extensions);
	  }

	  public void setFilterNames(String[] names) {
	    dlg.setFilterNames(names);
	  }

	  public void setFilterPath(String string) {
	    dlg.setFilterPath(string);
	  }

	  public Shell getParent() {
	    return dlg.getParent();
	  }

	  public int getStyle() {
	    return dlg.getStyle();
	  }

	  public String getText() {
	    return dlg.getText();
	  }

	  public void setText(String string) {
	    dlg.setText(string);
	  }
	}