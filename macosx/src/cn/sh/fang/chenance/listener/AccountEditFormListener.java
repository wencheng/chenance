package cn.sh.fang.chenance.listener;

import cn.sh.fang.chenance.AccountEditForm;
import cn.sh.fang.chenance.data.dao.AccountService;
import cn.sh.fang.chenance.data.entity.Account;

public class AccountEditFormListener implements IItemChangeListener<Account> {
	
	AccountEditForm form;
	
	public AccountEditFormListener(AccountEditForm form) {
		this.form = form;
	}

	public void itemAdded(Account item) {
		form.btnSave.setEnabled(true);

		form.name.setText(item.getName());
		form.memo.setText(item.getDescription());
		form.btnSave.setData(item);

		form.name.setFocus();
		form.name.selectAll();
	}

	public void itemRemoved(Account item) {
		form.name.setText("");
		form.memo.setText("");
		form.btnSave.setEnabled(false);
	}

	public void itemUpdated(Account item) {
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
