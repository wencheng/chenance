package cn.sh.fang.chenance.provider;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

import cn.sh.fang.chenance.data.entity.Category;

public class CategoryListLabelProvider extends LabelProvider implements
ILabelProvider {

	@Override
	public String getText(Object obj) {
		Category cat = (Category)obj;
		return cat.getName();		
	}

}
