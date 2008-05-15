package cn.sh.fang.chenance.provider;

import java.util.ArrayList;
import java.util.Iterator;

import cn.sh.fang.chenance.listener.IItemChangeListener;

public abstract class BaseProvider<T> {

	private ArrayList<IItemChangeListener<T>> changeListeners = new ArrayList<IItemChangeListener<T>>();

	/**
	 * @param viewer
	 */
	public void addChangeListener(IItemChangeListener<T> viewer) {
		changeListeners.add(viewer);
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(IItemChangeListener<T> viewer) {
		changeListeners.remove(viewer);
	}

	public void addItem() {
		T t = doAddItem();
		Iterator<IItemChangeListener<T>> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IItemChangeListener<T>) iterator.next()).itemAdded(t);
	}

	/**
	 * @param task
	 */
	public void itemChanged(T t) {
		doUpdateItem(t);
		Iterator<IItemChangeListener<T>> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IItemChangeListener<T>) iterator.next()).itemUpdated(t);
	}

	public void removeItem(T t) {
		doRemoveItem(t);
		Iterator<IItemChangeListener<T>> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IItemChangeListener<T>) iterator.next()).itemRemoved(t);
	}

	protected abstract T doAddItem();

	protected abstract T doRemoveItem(T t);
	
	protected abstract T doUpdateItem(T t);
}
