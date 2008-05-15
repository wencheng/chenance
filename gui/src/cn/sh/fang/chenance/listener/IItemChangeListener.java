package cn.sh.fang.chenance.listener;


public interface IItemChangeListener<T> {
	
	public void itemAdded(T item);
	
	public void itemRemoved(T item);
	
	public void itemUpdated(T item);
}
