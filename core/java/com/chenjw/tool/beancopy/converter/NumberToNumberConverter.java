/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tool.beancopy.converter;

import java.math.BigDecimal;

import com.chenjw.tool.beancopy.ClassifiedConverter;

/**
 * 数值型到数值型的转换，浮点型会做处理避免精度丢失的情况<br>
 * 
 * @author chenjw 2012-1-10 下午7:29:12
 */

public class NumberToNumberConverter implements ClassifiedConverter {

	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz) {
		// number->int
		// number->long
		// number->double
		// number->float
		// number->short
		// number->byte
		Number number = (Number) origin;
		if (destClazz == Integer.class || destClazz == int.class) {
			return number.intValue();
		} else if (destClazz == Double.class || destClazz == double.class) {
			return new BigDecimal(number.toString()).doubleValue();
		} else if (destClazz == Long.class || destClazz == long.class) {
			return number.longValue();
		} else if (destClazz == Float.class || destClazz == float.class) {
			return new BigDecimal(number.toString()).floatValue();
		} else if (destClazz == Short.class || destClazz == short.class) {
			return number.shortValue();
		} else if (destClazz == Byte.class || destClazz == byte.class) {
			return number.byteValue();
		}
		return null;
	}

	public Class<?>[] getOriginClazzes() {
		return new Class<?>[] { Number.class };
	}

	public Class<?>[] getDestClazzes() {
		return new Class<?>[] { Integer.class, Double.class, Long.class,
				Float.class, Short.class, Byte.class };
	}

}
