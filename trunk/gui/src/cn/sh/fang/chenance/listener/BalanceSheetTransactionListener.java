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

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import cn.sh.fang.chenance.data.dao.TransactionService;
import cn.sh.fang.chenance.data.entity.Transaction;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider;
import cn.sh.fang.chenance.provider.BalanceSheetContentProvider.Column;

public class BalanceSheetTransactionListener implements
		IDataAdapter<Transaction> {

	private TableViewer viewer;
	
	TransactionService ts = new TransactionService();

	private BalanceSheetContentProvider prov;

	public BalanceSheetTransactionListener(TableViewer tableViewer, BalanceSheetContentProvider prov) {
		this.viewer = tableViewer;
		this.prov = prov;
	}

	public void onAdded(Transaction t) {
		// TODO detail buttons would not be updated, fix it.
		this.viewer.insert(t, this.viewer.getTable().getItemCount()-1);
		this.viewer.setSelection(new StructuredSelection(t));
		this.viewer.getTable().showSelection();
		this.viewer.editElement(t, Column.CATEGORY.ordinal());
	}

	public void onRemoved(Transaction t) {
		ts.remove(t.getId(), t.getUpdater());
		this.viewer.remove(t);
		this.prov.refresh();
	}

	public void onUpdated(Transaction t) {
		ts.save(t);
		this.prov.refresh();
	}

	public void onLoaded(Transaction item) {
		// TODO Auto-generated method stub
	}

}
