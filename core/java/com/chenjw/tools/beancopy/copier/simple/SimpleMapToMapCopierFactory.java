/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tools.beancopy.copier.simple;

import java.util.Map;
import java.util.Map.Entry;

import com.chenjw.tools.beancopy.Converter;
import com.chenjw.tools.beancopy.Copier;
import com.chenjw.tools.beancopy.CopierFactory;

/**
 * map到map的拷贝
 * 
 * @author chenjw 2012-1-17 下午3:53:14
 */
public class SimpleMapToMapCopierFactory implements CopierFactory {

	public Copier getCopier(Class<?> destClazz, Class<?> originClazz,
			Map<String, String> nameMap, Class<?> defaultDestValueClazz) {
		return new SimpleMapToMapCopier(nameMap, defaultDestValueClazz);
	}

	private static class SimpleMapToMapCopier implements Copier {

		private Map<String, String> nameMap;
		private Class<?> defaultDestValueClazz;

		public SimpleMapToMapCopier(Map<String, String> nameMap,
				Class<?> defaultDestValueClazz) {
			this.nameMap = nameMap;
			this.defaultDestValueClazz = defaultDestValueClazz;
		}

		@SuppressWarnings("unchecked")
		public void copy(Object origin, Object dest, Converter converter) {
			Map<String, Object> originMap = (Map<String, Object>) origin;
			Map<String, Object> destMap = (Map<String, Object>) dest;
			for (Entry<String, Object> entry : originMap.entrySet()) {
				String name = null;
				if (nameMap != null) {
					name = nameMap.get(entry.getKey());
				}
				if (name == null) {
					name = entry.getKey();
				}
				if (entry.getValue() != null) {
					Object value = converter.convert(originMap, entry
							.getValue().getClass(), defaultDestValueClazz);
					destMap.put(name, value);
				}
			}
		}
	}

}
