/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tools.beancopy.converter;

import com.chenjw.tools.beancopy.ClassifiedConverter;

/**
 * object类到String类型的转换
 * 
 * @author chenjw 2012-1-10 下午3:50:27
 */
public class ObjectToStringConverter implements ClassifiedConverter {

	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz) {
		return origin.toString();
	}

	public Class<?>[] getOriginClazzes() {
		return new Class<?>[] { Object.class };
	}

	public Class<?>[] getDestClazzes() {
		return new Class<?>[] { String.class };
	}

}
