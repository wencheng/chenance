package cn.sh.fang.chenance.provider;

import cn.sh.fang.chenance.data.entity.Transaction;

public interface IBalanceSheetListener {
	
	public void addRecord(Transaction task);
	
	public void removeRecord(Transaction task);
	
	/**
	 * Update the view to reflect the fact that one of the tasks
	 * was modified 
	 * 
	 * @param task
	 */
	public void updateRecord(Transaction task);
}
