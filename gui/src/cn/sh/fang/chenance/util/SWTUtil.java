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
package cn.sh.fang.chenance.util;



import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class SWTUtil {

	/**
	 * 
	 * @param c
	 * @param top
	 * @param ot
	 *            top offset
	 * @param at
	 *            top align
	 * @param left
	 * @param ol
	 *            left offset
	 * @param lt
	 *            left align
	 * @return
	 */
	public static FormData setFormLayoutData(Control c, Object top, int ot, int at,
			Object left, int ol, int lt) {
		FormData layoutData = new FormData();
		if (top instanceof Control) {
			layoutData.top = new FormAttachment((Control) top, ot, at);
		} else {
			layoutData.top = new FormAttachment((Integer) top, ol, at);
		}
		if (left instanceof Control) {
			layoutData.left = new FormAttachment((Control) left, ol, lt);
		} else {
			layoutData.left = new FormAttachment((Integer) left, ol, lt);
		}
		c.setLayoutData(layoutData);
		return layoutData;
	}

	public static FormData setFormLayoutData(Control c, Object top, int ot,
			Object left, int ol) {
		return setFormLayoutData(c, top, ot, SWT.NONE, left, ol, SWT.NONE);
	}

	public static FormData setFormLayoutDataRight(Control c, Object top, int ot,
			int at, Object right, int or, int ar) {
		FormData layoutData = new FormData();
		if (top instanceof Control) {
			layoutData.top = new FormAttachment((Control) top, ot, at);
		} else {
			layoutData.top = new FormAttachment((Integer) top, ot, at);
		}
		if (right instanceof Control) {
			layoutData.right = new FormAttachment((Control) right, or, ar);
		} else {
			layoutData.right = new FormAttachment((Integer) right, or, ar);
		}
		c.setLayoutData(layoutData);
		return layoutData;
	}

	public static void showErrorMessage(Shell shell, String msg) {
		MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
		mb.setText("Chenance");
		mb.setMessage(msg);
		mb.open();
	}

}
