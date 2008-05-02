package cn.sh.fang.chenance.provider;

import cn.sh.fang.chinance.data.entity.Transaction;

public interface IBalanceSheetListener {
	
	/**
	 * Update the view to reflect the fact that a task was added 
	 * to the task list
	 * 
	 * @param task
	 */
	public void addTask(Transaction task);
	
	/**
	 * Update the view to reflect the fact that a task was removed 
	 * from the task list
	 * 
	 * @param task
	 */
	public void removeTask(Transaction task);
	
	/**
	 * Update the view to reflect the fact that one of the tasks
	 * was modified 
	 * 
	 * @param task
	 */
	public void updateTask(Transaction task);
}
