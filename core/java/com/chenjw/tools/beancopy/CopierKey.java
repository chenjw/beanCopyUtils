package com.chenjw.tools.beancopy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 用于唯一标记某个Copier实现类，作为Copier缓存的key
 * 
 * @author chenjw 2012-1-12 下午4:16:02
 */
public final class CopierKey {

	// 原始类型
	private Class<?> originClazz;
	// 目标类型
	private Class<?> destClazz;
	// 字段名映射
	private String mapString;
	// 目标对象默认值类型（只有当目标对象无法确定值类型时会用到，比如map，可以通过这个参数指定是转换到Map<String,Object>还是Map<String,String>）
	private Class<?> defaultDestValueClazz;

	/**
	 * map转换到string，用于唯一标记map
	 * 
	 * @param nameMap
	 * @return
	 */
	private static String map2String(Map<String, String> nameMap) {
		if (nameMap == null) {
			return null;
		}
		List<String> list = new ArrayList<String>(nameMap.size());
		for (Entry<String, String> entry : nameMap.entrySet()) {
			list.add(entry.getKey() + "=" + entry.getValue());
		}
		Collections.sort(list);
		StringBuffer sb = new StringBuffer();
		for (String s : list) {
			sb.append(s + "&");
		}
		return sb.toString();
	}

	public CopierKey(Class<?> originClazz, Class<?> destClazz,
			Map<String, String> nameMap, Class<?> defaultDestValueClazz) {
		this.originClazz = originClazz;
		this.destClazz = destClazz;
		this.mapString = map2String(nameMap);
		this.defaultDestValueClazz = defaultDestValueClazz;
		// 预先计算hash
		// hash();
	}

	@Override
	public String toString() {
		return originClazz + " " + destClazz + " " + mapString + " "
				+ defaultDestValueClazz;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((defaultDestValueClazz == null) ? 0 : defaultDestValueClazz
						.hashCode());
		result = prime * result
				+ ((destClazz == null) ? 0 : destClazz.hashCode());
		result = prime * result
				+ ((mapString == null) ? 0 : mapString.hashCode());
		result = prime * result
				+ ((originClazz == null) ? 0 : originClazz.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CopierKey other = (CopierKey) obj;
		if (defaultDestValueClazz == null) {
			if (other.defaultDestValueClazz != null)
				return false;
		} else if (!defaultDestValueClazz.equals(other.defaultDestValueClazz))
			return false;
		if (destClazz == null) {
			if (other.destClazz != null)
				return false;
		} else if (!destClazz.equals(other.destClazz))
			return false;
		if (mapString == null) {
			if (other.mapString != null)
				return false;
		} else if (!mapString.equals(other.mapString))
			return false;
		if (originClazz == null) {
			if (other.originClazz != null)
				return false;
		} else if (!originClazz.equals(other.originClazz))
			return false;
		return true;
	}

}
