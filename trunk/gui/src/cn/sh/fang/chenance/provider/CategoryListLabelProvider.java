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
		cell.setText(((Category)cell.getElement()).getName());
	}

}
