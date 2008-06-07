package cn.sh.fang.chenance.listener;

import java.util.Locale;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import cn.sh.fang.chenance.i18n.UIMessageBundle;

public class ChangeLanguageListener extends SelectionAdapter {
	
	Locale locale;
	
	public ChangeLanguageListener(Locale locale) {
		this.locale = locale;
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		UIMessageBundle.reload(locale);
	}
}
