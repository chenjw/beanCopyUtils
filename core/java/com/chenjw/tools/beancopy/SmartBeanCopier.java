package com.chenjw.tools.beancopy;

import java.util.Map;

import com.chenjw.logger.Logger;
import com.chenjw.logger.LoggerFactory;

/**
 * 实现通用的bean拷贝，可以存在多个实例，用于支持不同的拷贝规则
 * 
 * @see #copyProperties(Object, Object, Map, Class)
 * @author chenjw 2012-1-6 下午6:34:48
 */
public class SmartBeanCopier {

	private static final Logger LOGGER = LoggerFactory
			.getLogger("beanCopyLogger");
	// copier不可定制，因此多个SmartBeanCopier实例共用一个copierFactory
	private static final CopierFactory copierFactory = new CopierSelector();
	// converter可定制，因此各个实例使用各自的converter
	private final ConverterSelector converterSelector = new ConverterSelector();

	/**
	 * 从原始类转换到目标类
	 * <ol>
	 * <li>支持pojo和map之间的互相拷贝；</li>
	 * <li>支持pojo和pojo之间的互相拷贝；</li>
	 * <li>支持枚举和自定义枚举解析方法；</li>
	 * <li>支持date、timestamp与string、long之间的转换；</li>
	 * <li>支持基本类型和封装类之间的转换；</li>
	 * <li>支持数值型之间以及数值型与string之间的转换；</li>
	 * <li>支持数组的转换；</li>
	 * <li>可扩展其他自定义字段映射；</li>
	 * <li>不支持嵌套类的拷贝；</li>
	 * <li>单个字段无法拷贝不抛异常且不影响其他字段；</li>
	 * <li>基于预编译实现，比较BeanUtils性能好很多，功能上又较cglib的beanCopier工具类强；</li>
	 * </ol>
	 * 
	 * @param dest
	 * @param origin
	 * @param nameMapper
	 * @param destValueClazz
	 */
	public void copyProperties(Object dest, Object origin,
			Map<String, String> nameMap, Class<?> destValueClazz) {
		if (dest == null || origin == null) {
			LOGGER.error("dest or origin is null");
			return;
		}
		Copier copier = copierFactory.getCopier(dest.getClass(),
				origin.getClass(), nameMap, destValueClazz);
		// copy
		try {
			copier.copy(origin, dest, converterSelector);
		} catch (Throwable e) {
			LOGGER.error("copy fail ", e);
		}

	}

	public void addTypeConverter(ClassifiedConverter typeConverter) {
		converterSelector.addTypeConverter(typeConverter);
	}
}
