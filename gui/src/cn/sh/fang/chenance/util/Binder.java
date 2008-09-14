package cn.sh.fang.chenance.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

public class Binder {

	// source, targets( EL, EL )
	static Map<Entry, List<Entry>> map = new HashMap<Entry, List<Entry>>();

	public static void bind(Object src, String el, Object targ, String prop2) {
		Property prop = BeanProperty.create(el);
		BeanProperty textP = BeanProperty.create(prop2);
		Binding binding =
			Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, 
					src, prop, targ, textP);
		binding.bind();
		
//		List<Entry> list = Binder.map.get(src);
//		if ( list == null ) {
//			list = new ArrayList<Entry>();
//		}
//		list.add( new Entry(targ,prop2) );
//		Binder.map.put(new Entry(src,prop), list);
    }

	static class Entry {
		Object obj;
		String prop;

		public Entry(Object o, String p) {
			this.obj = o;
			this.prop = p;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((obj == null) ? 0 : obj.hashCode());
			result = prime * result + ((prop == null) ? 0 : prop.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Entry other = (Entry) obj;
			if (this.obj == null) {
				if (other.obj != null)
					return false;
			} else if (!this.obj.equals(other.obj))
				return false;
			if (prop == null) {
				if (other.prop != null)
					return false;
			} else if (!prop.equals(other.prop))
				return false;
			return true;
		}
	}
}
