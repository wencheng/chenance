package cn.sh.fang.chenance.listener;

import cn.sh.fang.chenance.CategoryEditForm;
import cn.sh.fang.chenance.data.entity.Category;

public class CategoryEditFormListener implements IItemChangeListener<Category> {
	
	CategoryEditForm form;
	
	public CategoryEditFormListener(CategoryEditForm form) {
		this.form = form;
	}

	public void itemAdded(Category item) {
		form.name.setEditable(true);
		form.desc.setEditable(true);
		form.btnSave.setEnabled(true);

		form.name.setText(item.getName());
		form.desc.setText(item.getDescription());
		form.btnSave.setData(item);

		form.name.setFocus();
		form.name.selectAll();
	}

	public void itemRemoved(Category item) {
		form.name.setText("");
		form.desc.setText("");
		form.btnSave.setEnabled(false);
	}

	public void itemUpdated(Category item) {
		boolean editable = item.getParent() != null;
		form.name.setEditable(editable);
		form.desc.setEditable(editable);
		form.btnSave.setEnabled(editable);

		if ( item == form.btnSave.getData() ) {
			// save
			item.setName(form.name.getText());
			item.setDescription(form.desc.getText());
		} else {
			form.name.setText(item.getName());
			form.desc.setText(item.getDescription());
			form.btnSave.setData(item);
		}
	}

}
