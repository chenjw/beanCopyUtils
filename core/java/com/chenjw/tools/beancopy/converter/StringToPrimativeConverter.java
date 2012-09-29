/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tools.beancopy.converter;

import com.chenjw.tools.beancopy.ClassifiedConverter;

/**
 * 字符串到java原始类型的转换
 * 
 * @author chenjw 2012-1-10 下午7:29:12
 */
public class StringToPrimativeConverter implements ClassifiedConverter {

	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz) {
		String str = (String) origin;
		if (destClazz == Integer.class || destClazz == int.class) {
			return Integer.parseInt(str);
		} else if (destClazz == Double.class || destClazz == double.class) {
			return Double.parseDouble(str);
		} else if (destClazz == Long.class || destClazz == long.class) {
			return Long.parseLong(str);
		} else if (destClazz == Float.class || destClazz == float.class) {
			return Float.parseFloat(str);
		} else if (destClazz == Short.class || destClazz == short.class) {
			return Short.parseShort(str);
		} else if (destClazz == Boolean.class || destClazz == boolean.class) {
			return Boolean.parseBoolean(str);
		} else if (destClazz == Byte.class || destClazz == byte.class) {
			return Byte.parseByte(str);
		} else if (destClazz == Character.class || destClazz == char.class) {
			if (str.length() == 1) {
				return str.charAt(0);
			}
		}
		return null;
	}

	public Class<?>[] getOriginClazzes() {
		return new Class<?>[] { String.class };
	}

	public Class<?>[] getDestClazzes() {
		return new Class<?>[] { Integer.class, Double.class, Long.class,
				Float.class, Short.class, Boolean.class, Byte.class,
				Character.class };
	}

}
