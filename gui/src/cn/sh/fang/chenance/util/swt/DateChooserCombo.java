package cn.sh.fang.chenance.util.swt;

import java.util.Date;

import org.eclipse.nebula.widgets.formattedtext.DefaultFormatterFactory;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class DateChooserCombo extends
		org.eclipse.nebula.widgets.datechooser.DateChooserCombo {

	public DateChooserCombo(Composite parent, int style) {
		super(parent, style);

		GridData gridData = new GridData();
		gridData.verticalAlignment = SWT.CENTER;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;
		this.text.setLayoutData(gridData);
	}
	
	@Override
	protected Text createTextControl(int style) {
		formattedText = new FormattedText(this, SWT.NONE|SWT.READ_ONLY|SWT.BORDER);
		formattedText.setFormatter(DefaultFormatterFactory.createFormatter(Date.class));
		return formattedText.getControl();
	}

}
