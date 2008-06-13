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
package cn.sh.fang.chenance.provider;

import static cn.sh.fang.chenance.i18n.UIMessageBundle._;

import java.util.List;

import cn.sh.fang.chenance.data.dao.AccountService;
import cn.sh.fang.chenance.data.entity.Account;

public class AccountListProvider extends BaseProvider<Account> {

	AccountService service = new AccountService();

	List<Account> accounts;
	
	public AccountListProvider() {
		initData();
	}
	
	private void initData() {
		this.accounts = service.findAll();
	}
	
	public List<Account> getAccounts() {
		return this.accounts;
	}
	
	@Override
	protected Account doAddItem() {
		Account a = new Account();
		a.setDescription("");
		a.setCurrentBalance(0);
		a.setUpdater("USER");
		// TODO make this more clever(if i can)
		int i;
		for ( i = 1; i < 5000; i++ ) {
			String name = _("New Account {0}",i);
			if ( this.service.isUsableName(name) ) {
				a.setName(name);
				break;
			}
		}
		if ( i == 50 ) {
			return null;
		}
		this.service.save(a);
		return a;
	}

	@Override
	protected Account doRemoveItem(Account t) {
		service.remove(t.getId(), "USER");
		accounts.remove(t);
		return t;
	}
	
	@Override
	protected Account doUpdateItem(Account t) {
		service.save(t);
		return t;
	}
}
