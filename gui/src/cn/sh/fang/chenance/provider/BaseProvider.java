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

import java.util.ArrayList;
import java.util.Iterator;

import cn.sh.fang.chenance.listener.IDataAdapter;

public abstract class BaseProvider<T> {

	private ArrayList<IDataAdapter<T>> changeListeners = new ArrayList<IDataAdapter<T>>();

	/**
	 * @param lstn
	 */
	public void addChangeListener(IDataAdapter<T> lstn) {
		changeListeners.add(lstn);
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(IDataAdapter<T> lstn) {
		changeListeners.remove(lstn);
	}

	public void addItem() {
		T t = doAddItem();
		Iterator<IDataAdapter<T>> iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IDataAdapter<T>) iterator.next()).onAdded(t);
		doPostAddItem(t);
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

	protected T doPostAddItem(T t) {
		return t;
	}

}
