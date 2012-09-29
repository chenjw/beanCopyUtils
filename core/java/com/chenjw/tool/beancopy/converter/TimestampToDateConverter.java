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
 * Timestamp类型到Date类型的转换
 * 
 * @author chenjw 2012-1-10 下午3:23:43
 */
public class TimestampToDateConverter implements ClassifiedConverter {

	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz) {
		return new Date(((Timestamp) origin).getTime());
	}

	public Class<?>[] getOriginClazzes() {
		return new Class<?>[] { Timestamp.class };
	}

	public Class<?>[] getDestClazzes() {
		return new Class<?>[] { Date.class };
	}

}
