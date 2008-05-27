package cn.sh.fang.chenance.listener;


public interface IItemChangeListener<T> {
	
	public void onAdded(T item);
	
	public void onRemoved(T item);
	
	public void onUpdated(T item);
	
	public void onLoaded(T item);
}
