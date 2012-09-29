package com.chenjw.tools.beancopy.converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.chenjw.tools.BeanCopyUtils;
import com.chenjw.tools.beancopy.ClassifiedConverter;
import com.chenjw.tools.beancopy.annotation.EnumValueMethod;

/**
 * 枚举类到普通对象（支持String、int、long）的转换<br>
 * 会优先查找添加了注解“EnumValueMethod”的方法，如果没有找到会调用“Enum.name()”或“Enum.ordinal()”来转换<br>
 * 如果转换后的结果不符合类型要求，会返回null<br>
 * 
 * @author chenjw 2012-1-10 下午1:42:15
 */
public class EnumToObjectConverter implements ClassifiedConverter {

	private static final Class<?>[] DEST_CLAZZES = new Class<?>[] {
			String.class, Integer.class, Long.class };

	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz) {
		Object result = null;
		// 查找有注解“EnumValueMethod”的方法
		for (Method method : origin.getClass().getDeclaredMethods()) {
			boolean isAnnotation = method
					.isAnnotationPresent(EnumValueMethod.class);
			if (isAnnotation) {
				try {
					if (!BeanCopyUtils.getBoxClazz(destClazz).isAssignableFrom(
							BeanCopyUtils.getBoxClazz(method.getReturnType()))) {
						continue;
					}
					result = method.invoke(origin, new Object[0]);
					if (result != null) {
						break;
					}
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}
		// 如果没有找到，就调用Enum.name()方法或Enum.ordinal()方法
		if (result == null) {
			if (destClazz == String.class) {
				result = ((Enum<?>) origin).name();
			} else if (destClazz == Integer.class) {
				result = ((Enum<?>) origin).ordinal();
			}
		}
		return result;
	}

	public Class<?>[] getOriginClazzes() {
		return new Class<?>[] { Enum.class };
	}

	public Class<?>[] getDestClazzes() {
		return DEST_CLAZZES;
	}

}
