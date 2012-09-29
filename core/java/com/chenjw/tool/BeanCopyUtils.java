package com.chenjw.tool;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.chenjw.tool.beancopy.ClassifiedConverter;
import com.chenjw.tool.beancopy.SmartBeanCopier;
import com.chenjw.tool.beancopy.converter.ArrayToArrayConverter;
import com.chenjw.tool.beancopy.converter.DateToLongConverter;
import com.chenjw.tool.beancopy.converter.DateToStringConverter;
import com.chenjw.tool.beancopy.converter.DateToTimestampConverter;
import com.chenjw.tool.beancopy.converter.EnumToObjectConverter;
import com.chenjw.tool.beancopy.converter.LongToDateConverter;
import com.chenjw.tool.beancopy.converter.NumberToNumberConverter;
import com.chenjw.tool.beancopy.converter.ObjectToEnumConverter;
import com.chenjw.tool.beancopy.converter.ObjectToStringConverter;
import com.chenjw.tool.beancopy.converter.StringToDateConverter;
import com.chenjw.tool.beancopy.converter.StringToPrimativeConverter;
import com.chenjw.tool.beancopy.converter.TimestampToDateConverter;

/**
 * 基于预编译，实现通用且高性能的bean拷贝工具
 * 
 * @author chenjw 2012-1-6 下午6:34:48
 */
public class BeanCopyUtils {

	// 系统会初始化一个默认的beancopier，无需自定义参数的场景只需要使用这个默认的beancopier就行了
	private static SmartBeanCopier DEFAULT_BEAN_COPIER;
	static {
		DEFAULT_BEAN_COPIER = new SmartBeanCopier();
		initDefaultTypeConverters(DEFAULT_BEAN_COPIER);
	}

	/**
	 * 初始化默认的字段值转换器列表
	 * 
	 * @param beanCopier
	 */
	public static void initDefaultTypeConverters(SmartBeanCopier beanCopier) {
		for (ClassifiedConverter typeConverter : BeanCopyUtils
				.getDefaultTypeConverters()) {
			beanCopier.addTypeConverter(typeConverter);
		}
	}

	/**
	 * @see #copyProperties(Object, Object, Map, Class)
	 * @param dest
	 *            原始对象
	 * @param origin
	 *            目标对象
	 * @param nameMapper
	 *            从原始对象字段名到目标对象字段名的映射
	 */
	public static void copyProperties(Object dest, Object origin,
			Map<String, String> nameMapper) {
		copyProperties(dest, origin, nameMapper, Object.class);
	}

	/**
	 * @see #copyProperties(Object, Object, Map, Class)
	 * @param dest
	 * @param origin
	 * @param destValueClazz
	 */
	public static void copyProperties(Object dest, Object origin,
			Class<?> destValueClazz) {
		copyProperties(dest, origin, null, destValueClazz);
	}

	/**
	 * 实现通用的bean拷贝
	 * 
	 * @see SmartBeanCopier#copyProperties(Object, Object, Map, Class)
	 * @param dest
	 *            目标对象
	 * @param origin
	 *            原始对象
	 * @param nameMapper
	 *            从原始对象字段名到目标对象字段名的映射
	 * @param destValueClazz
	 *            目标对象值的默认字段类型，当复制到map等值对象类型不定的目标时起作用，当从目标中可以获得某个值的类型时该参数不起作用
	 */
	public static void copyProperties(Object dest, Object origin,
			Map<String, String> nameMapper, Class<?> destValueClazz) {
		DEFAULT_BEAN_COPIER.copyProperties(dest, origin, nameMapper,
				destValueClazz);
	}

	/**
	 * @see #copyProperties(Object, Object, Map, Class)
	 * @param dest
	 * @param origin
	 */
	public static void copyProperties(Object dest, Object origin) {
		copyProperties(dest, origin, null, Object.class);
	}

	/**
	 * 默认配置<br>
	 * 
	 * @author chenjw 2012-1-11 下午5:46:23
	 */
	private static class DefaultConfig {

		public static ClassifiedConverter[] DEFAULT_TYPE_CONVERTERS;
		static {
			// converter
			List<ClassifiedConverter> typeConverterList = new ArrayList<ClassifiedConverter>();
			typeConverterList.add(new LongToDateConverter());
			typeConverterList.add(new ObjectToStringConverter());
			typeConverterList.add(new StringToPrimativeConverter());
			typeConverterList.add(new TimestampToDateConverter());
			typeConverterList.add(new StringToDateConverter());
			typeConverterList.add(new DateToLongConverter());
			typeConverterList.add(new ObjectToEnumConverter());
			typeConverterList.add(new DateToStringConverter());
			typeConverterList.add(new NumberToNumberConverter());
			typeConverterList.add(new ArrayToArrayConverter());
			typeConverterList.add(new DateToTimestampConverter());
			typeConverterList.add(new EnumToObjectConverter());
			DEFAULT_TYPE_CONVERTERS = typeConverterList
					.toArray(new ClassifiedConverter[typeConverterList.size()]);
		}

	}

	/**
	 * 获得默认的字段转换器列表
	 */
	public static ClassifiedConverter[] getDefaultTypeConverters() {
		return DefaultConfig.DEFAULT_TYPE_CONVERTERS;
	}

	/**
	 * 从基础类型获得封装类型
	 * 
	 * @param clazz
	 * @return 封装类型
	 */
	public static Class<?> getBoxClazz(Class<?> clazz) {
		if (!clazz.isPrimitive()) {
			return clazz;
		}
		if (clazz == int.class) {
			return Integer.class;
		} else if (clazz == long.class) {
			return Long.class;
		} else if (clazz == float.class) {
			return Float.class;
		} else if (clazz == double.class) {
			return Double.class;
		} else if (clazz == short.class) {
			return Short.class;
		} else if (clazz == boolean.class) {
			return Boolean.class;
		} else if (clazz == byte.class) {
			return Byte.class;
		} else if (clazz == char.class) {
			return Character.class;
		} else {
			return clazz;
		}
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
