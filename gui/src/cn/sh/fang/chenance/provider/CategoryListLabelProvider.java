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

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import cn.sh.fang.chenance.data.entity.Category;

public class CategoryListLabelProvider extends CellLabelProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerLabelProvider#getTooltipText(java.lang.Object)
	 */
	public String getToolTipText(Object element) {
		return ((Category)element).getDescription();
	}

	@Override
	public void update(ViewerCell cell) {
		if (cell == null)
			return;
		
		cell.setText(((Category)cell.getElement()).getName());
	}

}
