package cn.sh.fang.chenance.provider;

import java.util.ArrayList;
import java.util.Iterator;

import cn.sh.fang.chenance.listener.IDataAdapter;

public abstract class BaseProvider<T> {

	private ArrayList<IDataAdapter<T>> changeListeners = new ArrayList<IDataAdapter<T>>();

	/**
	 * @param viewer
	 */
	public void addChangeListener(IDataAdapter<T> viewer) {
		changeListeners.add(viewer);
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(IDataAdapter<T> viewer) {
		changeListeners.remove(viewer);
	}

	public void addItem() {
		T t = doAddItem();
		Iterator<IDataAdapter<T>> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IDataAdapter<T>) iterator.next()).onAdded(t);
	}

	/**
	 * @param task
	 */
	public void itemChanged(T t) {
		doUpdateItem(t);
		Iterator<IDataAdapter<T>> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IDataAdapter<T>) iterator.next()).onUpdated(t);
	}

	public void removeItem(T t) {
		doRemoveItem(t);
		Iterator<IDataAdapter<T>> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IDataAdapter<T>) iterator.next()).onRemoved(t);
	}

	protected abstract T doAddItem();

	protected abstract T doRemoveItem(T t);
	
	protected abstract T doUpdateItem(T t);
}
