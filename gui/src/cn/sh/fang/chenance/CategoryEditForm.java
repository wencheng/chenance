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
package cn.sh.fang.chenance;

import static cn.sh.fang.chenance.i18n.UIMessageBundle._;
import static cn.sh.fang.chenance.i18n.UIMessageBundle.setText;
import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutData;
import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutDataRight;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.listener.IDataAdapter;
import cn.sh.fang.chenance.listener.AbstractDataAdapter;

// 編集欄
public class CategoryEditForm implements Listener {

	private static final int MAX_HEIGHT = 260;
	private static final int MIN_HEIGHT = 50;
	private static final int RESIZE_STEP = 15;

	Label lblName;
	public Text name;
	Label lblDesc;
	public StyledText desc;
	public Button btnSave;
	private boolean isDescResizing = false;
	private Composite parent;

	public CategoryEditForm(Composite parent, int style) {
		this.parent = parent;
		createControl(parent);
	}

	private void createControl(final Composite parent) {
		lblName = new Label(parent, SWT.NONE);
		setText(lblName, "Display Name:");
		name = new Text(parent, SWT.BORDER);
		lblDesc = new Label(parent, SWT.NONE);
		setText(lblDesc, "Description:");
		desc = new StyledText(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL);
		btnSave = new Button(parent, SWT.PUSH);
		setText(btnSave, "Save");
		btnSave.setEnabled(false);

		setFormLayoutData(lblName, 0, 0, 0, 10);
		setFormLayoutData(name, lblName, 0, SWT.TOP, lblName, 20, SWT.NONE).width = 100;
		setFormLayoutData(lblDesc, lblName, 20, SWT.NONE, lblName, 0, SWT.LEFT);
		FormData fd = setFormLayoutData(desc, lblDesc, 10, SWT.NONE, lblDesc,
				0, SWT.LEFT);
		fd.width = 200;
		fd.height = 80;
		setFormLayoutDataRight(btnSave, desc, 10, SWT.NONE, desc, 0, SWT.RIGHT).width = 120;

		desc.addListener(SWT.MouseMove, this);
		desc.addListener(SWT.MouseDown, this);
		desc.addListener(SWT.MouseUp, this);
	}

	class CategoryEditFormListener extends AbstractDataAdapter<Category> {

		public void onAdded(Category item) {
			name.setText(item.getName());
			desc.setText(item.getDescription());
		}

		public void onRemoved(Category item) {
			name.setText("");
			desc.setText("");
		}

	}

	public void handleEvent(Event e) {
		if (e.type == SWT.MouseMove && e.widget == desc) {
			int height = desc.getSize().y - 6;

			if (isDescResizing) {
				if (e.y - height >= RESIZE_STEP && height < MAX_HEIGHT) {
					// incresing
					FormData fd = (FormData) desc.getLayoutData();
					fd.height += RESIZE_STEP;
					desc.layout();
					Point p = desc.getSize();
					p.y += RESIZE_STEP;
					desc.setSize(p);
					fd = (FormData) parent.getLayoutData();
					fd.height += RESIZE_STEP;
					parent.layout();
					p = parent.getSize();
					p.y += RESIZE_STEP;
					parent.setSize(p);
				}

				if (e.y - height <= -RESIZE_STEP && height > MIN_HEIGHT) {
					// decresing
					FormData fd = (FormData) desc.getLayoutData();
					fd.height -= RESIZE_STEP;
					desc.layout();
					Point p = desc.getSize();
					p.y -= RESIZE_STEP;
					desc.setSize(p);
					fd = (FormData) parent.getLayoutData();
					fd.height -= RESIZE_STEP;
					parent.layout();
					p = parent.getSize();
					p.y -= RESIZE_STEP;
					parent.setSize(p);
				}
			} else {
				if (Math.abs(e.y - height) <= 9) {
					desc.setCursor(desc.getDisplay().getSystemCursor(
							SWT.CURSOR_SIZENS));
				} else {
					desc.setCursor(null);
				}
			}
		} else if (e.type == SWT.MouseDown) {
			isDescResizing = true;
		} else if (e.type == SWT.MouseUp) {
			isDescResizing = false;
		}
	}

}
