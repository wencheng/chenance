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
