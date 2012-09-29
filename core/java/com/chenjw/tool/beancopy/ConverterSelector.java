package com.chenjw.tool.beancopy;

import java.util.HashMap;
import java.util.Map;

import com.chenjw.logger.Logger;
import com.chenjw.logger.LoggerFactory;
import com.chenjw.tool.BeanCopyUtils;

/**
 * 用于选择合适的转换方式转换对象
 * 
 * @author chenjw 2012-1-29 上午10:30:32
 */
public class ConverterSelector implements Converter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger("beanCopyLogger");
	// 字段值转换器映射
	private Map<ConverterKey, ClassifiedConverter> converterMap = new HashMap<ConverterKey, ClassifiedConverter>();

	/**
	 * 根据类型，从原始对象转换到目标对象
	 * 
	 * @param origin
	 * @param originClazz
	 * @param destClazz
	 * @return
	 */
	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz) {
		if (origin == null) {
			return null;
		}
		if (originClazz == null) {
			originClazz = origin.getClass();
		}
		// 如果原始类型和目标类型一致，无需转换
		if (isEquals(originClazz, destClazz)) {
			return origin;
		}
		// 找到合适的typeConverter
		Converter converter = findTypeConverter(originClazz, destClazz);
		if (converter == null) {
			if (destClazz.isInstance(origin)) {
				return origin;
			} else {
				return null;
			}
		}
		// 转换失败，返回null，错误不抛出
		try {
			Object r = null;
			// 如何时集合转换器，就调用集合转换的接口
			if (converter instanceof CollectionConverter) {
				return ((CollectionConverter) converter).convertCollection(
						origin, originClazz, destClazz, this);
			} else {
				r = converter.convert(origin, originClazz, destClazz);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("convert(" + origin + ", " + originClazz
							+ ", " + destClazz + ") returns " + r + " ("
							+ (r == null ? null : r.getClass()) + ")");
				}
				return r;
			}
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("convert error converter=" + converter
						+ ",originClazz=" + originClazz + ",destClazz="
						+ destClazz, e);
			}
			return null;
		}
	}

	/**
	 * 添加字段值转换器，当同一类原始类型和目标类型，已经映射到一个转换器时，再添加会抛出异常
	 * 
	 * @param typeConverter
	 */
	public void addTypeConverter(ClassifiedConverter typeConverter) {
		for (Class<?> originClazz : typeConverter.getOriginClazzes()) {
			for (Class<?> destClazz : typeConverter.getDestClazzes()) {
				ConverterKey converterKey = new ConverterKey(originClazz,
						destClazz);
				ClassifiedConverter oldConverter = converterMap.put(
						converterKey, typeConverter);
				// 如果转换器对应的转换类型已存在，抛错
				if (oldConverter != null) {
					throw new RuntimeException("converter init conflict key="
							+ converterKey + ",oldConverter=" + oldConverter
							+ ",newConverter=" + typeConverter);
				}
			}
		}
	}

	/**
	 * 根据原始类型和目标类型查找可以用于转换的转换器<br>
	 * 实现逻辑：<br>
	 * 目标类型是固定的 <br>
	 * 原始类型会先按照输入类型来找，然后按照输入类型的父类来找，直到找到object类型为止<br>
	 * 
	 * @param originClazz
	 * @param destClazz
	 * @return
	 */
	private Converter findTypeConverter(Class<?> originClazz, Class<?> destClazz) {
		ConverterKey key = new ConverterKey(originClazz, destClazz);
		for (;;) {
			ClassifiedConverter converter = converterMap.get(key);
			if (converter != null) {
				return converter;
			}
			// 如果没找到，就转换到下一个符合要求的类型来查找
			key = key.findNextConverterKey();
			if (key == null) {
				return null;
			}
		}
	}

	/**
	 * 判断两个类型是否相等，原始类型和封装类当作相等处理
	 * 
	 * @param originClazz
	 * @param destClazz
	 * @return
	 */
	private static boolean isEquals(Class<?> originClazz, Class<?> destClazz) {
		if (originClazz == destClazz) {
			return true;
		}
		if (originClazz.isPrimitive()) {
			if (BeanCopyUtils.getBoxClazz(originClazz) == destClazz) {
				return true;
			}
		} else if (destClazz.isPrimitive()) {
			if (BeanCopyUtils.getBoxClazz(destClazz) == originClazz) {
				return true;
			}
		}
		return false;
	}

}
