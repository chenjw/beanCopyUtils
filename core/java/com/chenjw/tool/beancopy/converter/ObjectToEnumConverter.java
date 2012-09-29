/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tool.beancopy.converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.chenjw.tool.BeanCopyUtils;
import com.chenjw.tool.beancopy.ClassifiedConverter;
import com.chenjw.tool.beancopy.annotation.EnumParseMethod;

/**
 * 普通对象（支持String、int、long）到Enum的转换<br>
 * 会优先查找添加了注解“EnumParseMethod”的方法，如果没有找到会调用“Enum.valueOf()”或“查找枚举Ordinal”方法来转换<br>
 * 
 * @author chenjw 2012-1-10 下午7:29:56
 */
public class ObjectToEnumConverter implements ClassifiedConverter {

	private static ConcurrentMap<Class<?>, Map<Integer, Enum<?>>> integerEnumCache = new ConcurrentHashMap<Class<?>, Map<Integer, Enum<?>>>();
	/**
	 * 支持的类型
	 */
	private static final Class<?>[] ORIGIN_CLAZZES = new Class<?>[] {
			String.class, Integer.class, Long.class };

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz) {
		Object result = null;
		for (Method method : destClazz.getDeclaredMethods()) {
			// 优先使用注解中指定的解析方法
			boolean isAnnotation = method
					.isAnnotationPresent(EnumParseMethod.class);
			if (isAnnotation) {
				try {
					if (method.getParameterTypes().length != 1
							|| !BeanCopyUtils.getBoxClazz(origin.getClass())
									.isAssignableFrom(
											BeanCopyUtils.getBoxClazz(method
													.getParameterTypes()[0]))) {
						continue;
					}
					result = method.invoke(null, new Object[] { origin });
					if (result != null) {
						break;
					}
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}

		if (result == null) {
			// 如果原始类型是string,使用valueOf方法
			if (origin.getClass() == String.class) {
				result = Enum.valueOf((Class<? extends Enum>) destClazz,
						(String) origin);
			}
			// 如果原始类型时int，使用枚举的Ordinal解析
			else if (origin.getClass() == Integer.class) {
				result = getEnumByOrdinal((Integer) origin,
						(Class<? extends Enum>) destClazz);
			}
		}
		if (result == null) {
			return null;
		}
		if (result.getClass().isEnum()) {
			return result;
		}
		return null;
	}

	/**
	 * 根据枚举的ordinal获得枚举值
	 * 
	 * @param i
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Enum<?> getEnumByOrdinal(Integer i, Class<? extends Enum> clazz) {
		try {
			return this.getIntegerEnumMap(clazz).get(i);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 从缓存中取得枚举值缓存
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private Map<Integer, Enum<?>> getIntegerEnumMap(Class<? extends Enum> clazz)
			throws Exception {
		Map<Integer, Enum<?>> items = integerEnumCache.get(clazz);
		if (items == null) {
			items = new HashMap<Integer, Enum<?>>();
			Method method = clazz.getMethod("values");
			Object[] values = (Object[]) method.invoke(null);
			for (Object value : values) {
				Enum<?> e = (Enum<?>) value;
				items.put(e.ordinal(), e);
			}
			integerEnumCache.putIfAbsent(clazz, items);
		}
		return items;
	}

	/*
	 * 支持String、Long和Int
	 */
	public Class<?>[] getOriginClazzes() {
		return ORIGIN_CLAZZES;
	}

	/*
	 * 匹配所有枚举
	 */
	public Class<?>[] getDestClazzes() {
		return new Class<?>[] { Enum.class };
	}

}
