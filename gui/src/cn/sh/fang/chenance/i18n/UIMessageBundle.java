package cn.sh.fang.chenance.i18n;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import org.eclipse.swt.widgets.Widget;

public class UIMessageBundle {

	 static ResourceBundle res;

	 static Map<Widget, String> widgets = new HashMap<Widget, String>();
	 
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
		try {
			for ( Entry<Widget, String> e : widgets.entrySet() ) {
				Widget w = e.getKey();
				Method m = w.getClass().getMethod( "setText", new Class[]{ String.class } );
				m.invoke( w, new Object[]{ _( e.getValue() ) } );
			 }
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }

	public static void add(Widget w) {
		Method m;
		try {
			m = w.getClass().getMethod( "getText", new Class[]{} );
			String s = (String) m.invoke( w, new Object[]{} );
			widgets.put( w, s );
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setText(Widget w, String s) {
		try {
			Method m = w.getClass().getMethod( "setText", new Class[]{ String.class } );
			widgets.put( w, s );
			m.invoke( w, new Object[]{ _( s ) } );
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
