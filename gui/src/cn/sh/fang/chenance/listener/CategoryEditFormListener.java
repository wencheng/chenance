package cn.sh.fang.chenance.listener;

import cn.sh.fang.chenance.CategoryEditForm;
import cn.sh.fang.chenance.data.entity.Category;

public class CategoryEditFormListener implements IDataAdapter<Category> {
	
	CategoryEditForm form;
	
	public CategoryEditFormListener(CategoryEditForm form) {
		this.form = form;
	}

	public void onAdded(Category item) {
		form.name.setEditable(true);
		form.desc.setEditable(true);
		form.btnSave.setEnabled(true);

		form.name.setText(item.getName());
		form.desc.setText(item.getDescription());
		form.btnSave.setData(item);

		form.name.setFocus();
		form.name.selectAll();
	}

	public void onRemoved(Category item) {
		form.name.setText("");
		form.desc.setText("");
		form.btnSave.setEnabled(false);
	}

	public void onUpdated(Category item) {
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

	public void onLoaded(Category item) {
		// TODO Auto-generated method stub
		
	}

}
