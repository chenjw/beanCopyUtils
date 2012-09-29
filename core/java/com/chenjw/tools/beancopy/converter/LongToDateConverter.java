/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tools.beancopy.converter;

import java.sql.Timestamp;
import java.util.Date;

import com.chenjw.tools.beancopy.ClassifiedConverter;

/**
 * Long类型到Date类型和Timestamp类型的转换
 * 
 * @author chenjw 2012-1-10 下午3:23:43
 */
public class LongToDateConverter implements ClassifiedConverter {

	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz) {
		if (destClazz == Date.class) {
			return new Date((Long) origin);
		} else if (destClazz == Timestamp.class) {
			return new Timestamp((Long) origin);
		}
		return null;
	}

	public Class<?>[] getOriginClazzes() {
		return new Class<?>[] { Long.class };
	}

	public Class<?>[] getDestClazzes() {
		return new Class<?>[] { Date.class, Timestamp.class };
	}

}
