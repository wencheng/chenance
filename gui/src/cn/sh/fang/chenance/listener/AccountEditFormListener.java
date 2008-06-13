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

import cn.sh.fang.chenance.AccountEditForm;
import cn.sh.fang.chenance.data.entity.Account;

public class AccountEditFormListener extends AbstractDataAdapter<Account> {
	
	AccountEditForm form;
	
	public AccountEditFormListener(AccountEditForm form) {
		this.form = form;
	}

	public void onAdded(Account item) {
		form.btnSave.setEnabled(true);

		form.name.setText(item.getName());
		form.memo.setText(item.getDescription());
		form.btnSave.setData(item);

		form.name.setFocus();
		form.name.selectAll();
	}

	public void onRemoved(Account item) {
		form.name.setText("");
		form.memo.setText("");
		form.btnSave.setEnabled(false);
	}

	public void onUpdated(Account item) {
		form.btnSave.setEnabled(true);

		if ( item == form.btnSave.getData() ) {
			// save
			item.setName(form.name.getText());
			item.setDescription(form.memo.getText());
			if (form.start.getText().length() == 0) {
				item.setStartBalance(0);
			} else {
				item.setStartBalance(Integer.parseInt(form.start.getText()));
			}
		} else {
			form.name.setText(item.getName());
			form.memo.setText(item.getDescription());
			form.btnSave.setData(item);
		}
	}

}
