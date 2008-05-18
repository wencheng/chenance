package cn.sh.fang.chenance;

import static cn.sh.fang.chenance.i18n.UIMessageBundle._;
import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutData;
import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutDataRight;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.listener.IItemChangeListener;

// 編集欄
public class CategoryEditForm {
	
	Label lblName;
	public Text name;
	Label lblDesc;
	public Text desc;
	public Button btnSave;

	public CategoryEditForm(Composite parent, int style) {
		createControl(parent);
	}

	private void createControl(Composite parent) {
		lblName = new Label(parent, SWT.NONE);
		lblName.setText(_("Display Name:"));
		name = new Text(parent, SWT.BORDER);
		lblDesc = new Label(parent, SWT.NONE);
		lblDesc.setText(_("Description:"));
		desc = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		btnSave= new Button(parent, SWT.PUSH);
		btnSave.setText(_("Save"));
		btnSave.setEnabled(false);

		setFormLayoutData(lblName, 0, 0, 0, 10);
		setFormLayoutData(name, lblName, 0, SWT.TOP, lblName, 20, SWT.NONE).width = 100;
		setFormLayoutData(lblDesc, lblName, 20, SWT.NONE, lblName, 0, SWT.LEFT);
		FormData fd = setFormLayoutData(desc, lblDesc, 10, SWT.NONE, lblDesc, 0, SWT.LEFT);
		fd.width = 200;
		fd.height = 80;
		setFormLayoutDataRight(btnSave, desc, 10, SWT.NONE, desc, 0, SWT.RIGHT).width = 120;
	}
	
	class CategoryEditFormListener implements IItemChangeListener<Category> {

		public void itemAdded(Category item) {
			name.setText(item.getName());
			desc.setText(item.getDescription());
		}

		public void itemRemoved(Category item) {
			name.setText("");
			desc.setText("");
		}

		public void itemUpdated(Category item) {
			// do nothing
		}
		
	}

}
