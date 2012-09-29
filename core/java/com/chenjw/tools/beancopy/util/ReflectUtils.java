package com.chenjw.tools.beancopy.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtils {
	public static PropertyDescriptor[] getBeanProperties(Class<?> type) {
		return getPropertiesHelper(type, true, true);
	}

	public static PropertyDescriptor[] getBeanGetters(Class<?> type) {
		return getPropertiesHelper(type, true, false);
	}

	public static PropertyDescriptor[] getBeanSetters(Class<?> type) {
		return getPropertiesHelper(type, false, true);
	}

	private static PropertyDescriptor[] getPropertiesHelper(Class<?> type,
			boolean read, boolean write) {
		try {
			BeanInfo info = Introspector.getBeanInfo(type, Object.class);
			PropertyDescriptor[] all = info.getPropertyDescriptors();
			if (read && write) {
				return all;
			}
			List<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>(
					all.length);
			for (int i = 0; i < all.length; i++) {
				PropertyDescriptor pd = all[i];
				if ((read && pd.getReadMethod() != null)
						|| (write && pd.getWriteMethod() != null)) {
					properties.add(pd);
				}
			}
			return (PropertyDescriptor[]) properties
					.toArray(new PropertyDescriptor[properties.size()]);
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}
}
