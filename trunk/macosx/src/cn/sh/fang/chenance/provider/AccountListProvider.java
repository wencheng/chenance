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
