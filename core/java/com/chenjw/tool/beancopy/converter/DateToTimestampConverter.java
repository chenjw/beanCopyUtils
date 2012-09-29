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
 * Date类型到Timestamp类型的转换
 * 
 * @author chenjw 2012-1-10 下午3:23:43
 */
public class DateToTimestampConverter implements ClassifiedConverter {

	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz) {
		return new Timestamp(((Date) origin).getTime());
	}

	public Class<?>[] getOriginClazzes() {
		return new Class<?>[] { Date.class };
	}

	public Class<?>[] getDestClazzes() {
		return new Class<?>[] { Timestamp.class };
	}

}
