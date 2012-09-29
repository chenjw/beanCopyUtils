/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tools.beancopy.converter;

import java.lang.reflect.Array;

import com.chenjw.tools.beancopy.ClassifiedConverter;
import com.chenjw.tools.beancopy.CollectionConverter;
import com.chenjw.tools.beancopy.Converter;

/**
 * 数组与数组之间的转换，匹配所有数组类型<br>
 * 主要流程： <br>
 * 遍历传入数据的所有元素，选择合适的转换器分别转换成目标类型，并组装成目标数组类型返回。<br>
 * 只要有一个数组元素转换执行失败，整个数据转换就标记为失败。<br>
 * 
 * @author chenjw 2012-1-11 下午7:54:19
 */
public class ArrayToArrayConverter implements ClassifiedConverter,
		CollectionConverter {

	public Object convertCollection(Object origin, Class<?> originClazz,
			Class<?> destClazz, Converter componentConverter) {
		Class<?> originComponentClazz = originClazz.getComponentType();
		Class<?> destComponentClazz = destClazz.getComponentType();

		Object destArray = Array.newInstance(destComponentClazz,
				Array.getLength(origin));
		for (int i = 0; i < Array.getLength(origin); i++) {
			Object originComponent = Array.get(origin, i);
			if (originComponent == null) {
				Array.set(destArray, i, null);
			}
			Object destComponent = componentConverter.convert(originComponent,
					originComponentClazz, destComponentClazz);
			if (destComponent != null) {
				Array.set(destArray, i, destComponent);
				continue;
			}
			return null;
		}
		return destArray;
	}

	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz) {
		throw new RuntimeException(
				"CollectionConverter not implement 'convert' method, use 'convertCollection' instead");
	}

	/*
	 * 匹配所有数组类型
	 */
	public Class<?>[] getOriginClazzes() {
		return new Class[] { Object[].class };
	}

	/*
	 * 匹配所有数组类型
	 */
	public Class<?>[] getDestClazzes() {
		return new Class[] { Object[].class };
	}

}
