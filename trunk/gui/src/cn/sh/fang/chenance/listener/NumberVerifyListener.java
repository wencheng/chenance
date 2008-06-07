package cn.sh.fang.chenance.listener;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

public class NumberVerifyListener implements VerifyListener {

	public void verifyText(VerifyEvent e) {
		e.doit = e.text.matches("[\\-0-9]*");
	}

}
