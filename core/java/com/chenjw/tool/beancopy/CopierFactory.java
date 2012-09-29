package com.chenjw.tool.beancopy;

import java.util.Map;

/**
 * 用于生成Copier实现类的抽象工厂
 * 
 * @author chenjw 2012-1-17 下午3:38:40
 */
public interface CopierFactory {

	/**
	 * 生成Copier实现类
	 * 
	 * @param destClazz
	 * @param originClazz
	 * @param nameMap
	 * @param defaultDestValueClazz
	 * @return
	 */
	public Copier getCopier(Class<?> destClazz, Class<?> originClazz,
			Map<String, String> nameMap, Class<?> defaultDestValueClazz);
}
