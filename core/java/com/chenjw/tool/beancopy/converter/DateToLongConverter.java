/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tool.beancopy.converter;

import java.sql.Timestamp;
import java.util.Date;

import com.chenjw.tool.beancopy.ClassifiedConverter;

/**
 * Date类型和Timestamp类型到Long类型的转换
 * 
 * @author chenjw 2012-1-10 下午3:23:43
 */
public class DateToLongConverter implements ClassifiedConverter {

	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz) {
		if (originClazz == Date.class) {
			return ((Date) origin).getTime();
		} else if (originClazz == Timestamp.class) {
			return ((Timestamp) origin).getTime();
		}
		return null;
	}

	public Class<?>[] getOriginClazzes() {
		return new Class<?>[] { Date.class, Timestamp.class };
	}

	public Class<?>[] getDestClazzes() {
		return new Class<?>[] { Long.class };
	}

}
