package com.chenjw.tool.beancopy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.chenjw.tool.beancopy.copier.javassist.JavassistMapToPojoCopierFactory;
import com.chenjw.tool.beancopy.copier.javassist.JavassistPojoToMapCopierFactory;
import com.chenjw.tool.beancopy.copier.javassist.JavassistPojoToPojoCopierFactory;
import com.chenjw.tool.beancopy.copier.simple.SimpleMapToMapCopierFactory;

/**
 * 用于生成合适的Copier实例，并做缓存
 * 
 * @author chenjw 2012-1-18 上午9:56:11
 */
public class CopierSelector implements CopierFactory {

	// copier cache
	private ConcurrentMap<CopierKey, Copier> copierMapCache = new ConcurrentHashMap<CopierKey, Copier>();

	// map->map
	private CopierFactory mapToMapCopierFactory = new SimpleMapToMapCopierFactory();
	// map-pojo
	private CopierFactory mapToPojoCopierFactory = new JavassistMapToPojoCopierFactory();
	// pojo->map
	private CopierFactory pojoToMapCopierFactory = new JavassistPojoToMapCopierFactory();
	// pojo->pojo
	private CopierFactory pojoToPojoCopierFactory = new JavassistPojoToPojoCopierFactory();

	public Copier getCopier(Class<?> destClazz, Class<?> originClazz,
			Map<String, String> nameMap, Class<?> defaultDestValueClazz) {

		// get copier
		CopierKey key = new CopierKey(originClazz, destClazz, nameMap,
				defaultDestValueClazz);
		Copier copier = getFromCache(key);
		if (copier == null) {
			copier = createCopier(originClazz, destClazz, nameMap,
					defaultDestValueClazz);
			setToCache(key, copier);
		}
		return copier;
	}

	private Copier getFromCache(CopierKey key) {
		return copierMapCache.get(key);
	}

	private void setToCache(CopierKey key, Copier copier) {
		copierMapCache.putIfAbsent(key, copier);
	}

	/**
	 * 创建copier实例
	 * 
	 * @param originClazz
	 * @param destClazz
	 * @param nameMap
	 * @param destValueClazz
	 * @return
	 */
	private Copier createCopier(Class<?> originClazz, Class<?> destClazz,
			Map<String, String> nameMap, Class<?> defaultDestValueClazz) {
		if (Map.class.isAssignableFrom(originClazz)) {
			if (Map.class.isAssignableFrom(destClazz)) {
				// map->map
				return mapToMapCopierFactory.getCopier(destClazz, originClazz,
						nameMap, defaultDestValueClazz);
			} else {
				// map->pojo
				return mapToPojoCopierFactory.getCopier(destClazz, originClazz,
						nameMap, defaultDestValueClazz);
			}
		} else {
			if (Map.class.isAssignableFrom(destClazz)) {
				// pojo->map
				return pojoToMapCopierFactory.getCopier(destClazz, originClazz,
						nameMap, defaultDestValueClazz);
			} else {
				// pojo->pojo
				return pojoToPojoCopierFactory.getCopier(destClazz,
						originClazz, nameMap, defaultDestValueClazz);
			}
		}
	}

}
