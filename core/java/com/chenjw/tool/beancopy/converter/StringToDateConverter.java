/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tool.beancopy.converter;

import java.sql.Timestamp;
import java.util.Date;

import com.chenjw.tool.beancopy.ClassifiedConverter;
import com.chenjw.tool.beancopy.util.DateUtils;

/**
 * String类型到Date类型和Timestamp类型的转换
 * 
 * @author chenjw 2012-1-10 下午3:23:43
 */
public class StringToDateConverter implements ClassifiedConverter {

	// 日期格式，如果还需要支持其他解析格式，添加在这里
	private static final String[] DATE_FORMATS = new String[] {
			"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };

	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz) {
		// 遍历所有格式列表，直到找到一种可以解析字符串的格式为止
		Date date = null;
		for (String dateFormat : DATE_FORMATS) {
			date = DateUtils.parseDate((String) origin, dateFormat);
			if (date != null) {
				break;
			}
		}
		if (date == null) {
			return null;
		}
		if (destClazz == Date.class) {
			return date;
		} else if (destClazz == Timestamp.class) {
			return new Timestamp(date.getTime());
		}
		return null;
	}

	public Class<?>[] getOriginClazzes() {
		return new Class<?>[] { String.class };
	}

	public Class<?>[] getDestClazzes() {
		return new Class<?>[] { Date.class, Timestamp.class };
	}

}
