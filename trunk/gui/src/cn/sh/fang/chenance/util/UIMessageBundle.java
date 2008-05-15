package cn.sh.fang.chenance.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class UIMessageBundle {
	
	 static ResourceBundle res;

	 static {
		 try {
			 res = ResourceBundle.getBundle("cn.sh.fang.chenance.i18n.gui", Locale.getDefault());
		 } catch (java.util.MissingResourceException  e) {
			 res = ResourceBundle.getBundle("cn.sh.fang.chenance.i18n.gui", Locale.ENGLISH);
		 }
	 }
	 
	 public static String _(String key) {
		 try {
			 return res.getString(key);
		 } catch (java.util.MissingResourceException e) {
			 return key;
		 }
	 }
	 
	 public static String _(String key, Object... objs) {
		 return MessageFormat.format(_(key), objs);
	 }
	 
	 public static void reload(Locale locale) {
		 res = ResourceBundle.getBundle("cn.sh.fang.chenance.i18n.gui", locale); 
	 }
}
