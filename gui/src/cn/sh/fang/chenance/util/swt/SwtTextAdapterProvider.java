/*
 * Copyright (C) 2006-2007 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package cn.sh.fang.chenance.util.swt;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;
import org.jdesktop.beansbinding.ext.BeanAdapterProvider;
import org.jdesktop.swingbinding.adapters.BeanAdapterBase;
/**
 * @author Wencheng FANG
 */
public final class SwtTextAdapterProvider implements BeanAdapterProvider {

	static Logger LOG = Logger.getLogger(SwtTextAdapterProvider.class);

	private static final String PROPERTY_BASE = "text";
    private static final String ON_ACTION_OR_FOCUS_LOST = PROPERTY_BASE + "_ON_ACTION_OR_FOCUS_LOST";
    private static final String ON_FOCUS_LOST = PROPERTY_BASE + "_ON_FOCUS_LOST";

    public final class Adapter extends BeanAdapterBase {
        private Text component;
        private String cachedText;
        private Handler handler;

        private Adapter(Text component, String property) {
            super(property);
            this.component = component;
        }

        public String getText() {
            return component.getText();
        }

        public String getText_ON_ACTION_OR_FOCUS_LOST() {
            return getText();
        }

        public String getText_ON_FOCUS_LOST() {
            return getText();
        }

        public void setText(String text) {
            component.setText(text);
            component.setSelection(0,0);
            cachedText = text;
        }

        public void setText_ON_ACTION_OR_FOCUS_LOST(String text) {
            setText(text);
        }

        public void setText_ON_FOCUS_LOST(String text) {
            setText(text);
        }
        
        protected void listeningStarted() {
            cachedText = component.getText();
            handler = new Handler();
            component.addModifyListener(handler);

            if (property != PROPERTY_BASE) {
                component.addFocusListener(handler);
            }

//            if (property == ON_ACTION_OR_FOCUS_LOST) {
//                component.addActionListener(handler);
//            }
        }
        
        protected void listeningStopped() {
            cachedText = null;
            component.removeModifyListener(handler);
            
            if (property != PROPERTY_BASE) {
                component.removeFocusListener(handler);
            }
            
//            if (property == ON_ACTION_OR_FOCUS_LOST && (component instanceof JTextField)) {
//                ((JTextField)component).removeActionListener(handler);
//            }

            handler = null;
        }

        private class Handler implements FocusListener, ModifyListener {

            private void updateText() {
                Object oldText = cachedText;
                cachedText = getText();
                LOG.debug( "update text: " + cachedText);
                firePropertyChange(oldText, cachedText);
            }

//            public void propertyChange(PropertyChangeEvent pce) {
//                uninstallDocumentListener();
//                document = component.getDocument();
//                installDocumentListener();
//                updateText();
//            }
//            
//            public void actionPerformed(ActionEvent e) {
//                updateText();
//            }

			public void modifyText(ModifyEvent arg0) {
				updateText();
			}

			public void focusGained(FocusEvent e) {}

			public void focusLost(FocusEvent e) {
//				if (!e.isTemporary()) {
					updateText();
			}
        }
        
    }

    public boolean providesAdapter(Class<?> type, String property) {
        if (!Text.class.isAssignableFrom(type)) {
            return false;
        }

        property = property.intern();
        
        return property == PROPERTY_BASE ||
               property == ON_ACTION_OR_FOCUS_LOST ||
               property == ON_FOCUS_LOST;
                 
    }
    
    public Object createAdapter(Object source, String property) {
        if (!providesAdapter(source.getClass(), property)) {
            throw new IllegalArgumentException();
        }
        
        return new Adapter((Text)source, property);
    }
    
    public Class<?> getAdapterClass(Class<?> type) {
        return Text.class.isAssignableFrom(type) ?
            SwtTextAdapterProvider.Adapter.class :
            null;
    }

}