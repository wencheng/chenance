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

	@Override
	public void applyEditorValue() {
		v.editElement(((StructuredSelection) v.getSelection())
				.getFirstElement(), Column.DEBIT.ordinal());
	}

	@Override
	public void cancelEditor() {
	}

	@Override
	public void editorValueChanged(boolean arg0, boolean arg1) {
	}
}
