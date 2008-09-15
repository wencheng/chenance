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
package cn.sh.fang.chenance;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import cn.sh.fang.chenance.data.entity.Category;

public class CategoryTree {

	final static Logger LOG = Logger.getLogger(CategoryTree.class);

	public TreeViewer viewer;

	private IObservableValue hasSelected;

	private Category category;

	public CategoryTree(Category root) {
		this.category = root;
	}

	public TreeViewer createControl(Composite composite) {
		viewer = new TreeViewer(composite, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.SINGLE);

		// Create a standard content provider
		ObservableListTreeContentProvider provider = new ObservableListTreeContentProvider(
				BeansObservables.listFactory(Realm.getDefault(), "children",
						Category.class), null);
		viewer.setContentProvider(provider);

		ColumnViewerToolTipSupport.enableFor(viewer);
		// And a standard label provider that maps columns
		IObservableMap[] attributeMaps = BeansObservables.observeMaps(provider
				.getKnownElements(), Category.class, new String[] { "name" });
		viewer.setLabelProvider( new ObservableMapLabelProvider(attributeMaps) {
			// TODO show tooltips 
			@SuppressWarnings("unused")
			public String getToolTipText(Object element) {
				return ((Category)element).getDescription();
			}
		});

		// Now set the Viewer's input
		viewer.setInput(this.category);
		viewer.expandAll();

		final IObservableValue beanViewerSelection = ViewersObservables
			.observeSingleSelection(viewer);
		this.hasSelected = new ComputedValue(Boolean.TYPE) {
			protected Object calculate() {
				return Boolean.valueOf(beanViewerSelection.getValue() != null);
			}
		};

		return viewer;
	}

	public IObservableValue getHasSelected() {
		return hasSelected;
	}

	public Category getSelected() {
		IStructuredSelection selection = (IStructuredSelection)viewer
			.getSelection();
		if (selection.isEmpty())
			return null;
		return (Category) selection.getFirstElement();
	}

}
