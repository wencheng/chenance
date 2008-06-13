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

import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import cn.sh.fang.chenance.provider.BalanceSheetContentProvider.Column;

public class ActivateNextCellEditorListener implements ICellEditorListener {
	TableViewer v;

	public ActivateNextCellEditorListener(TableViewer v) {
		this.v = v;
	}

	public void applyEditorValue() {
		v.editElement(((StructuredSelection) v.getSelection())
				.getFirstElement(), Column.DEBIT.ordinal());
	}

	public void cancelEditor() {
	}

	public void editorValueChanged(boolean arg0, boolean arg1) {
	}
}
