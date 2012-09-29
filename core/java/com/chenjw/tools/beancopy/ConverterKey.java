package com.chenjw.tools.beancopy;

import com.chenjw.tools.BeanCopyUtils;

/**
 * 用于唯一标记某个转换器
 * 
 * @author chenjw 2012-1-12 下午4:16:02
 */
public class ConverterKey {

	private Class<?> originClazz;
	private Class<?> destClazz;

	public ConverterKey(Class<?> originClazz, Class<?> destClazz) {
		this.originClazz = classify(originClazz);
		this.destClazz = classify(destClazz);
	}

	/**
	 * 为原始类型或结果类型归类<br>
	 * 包括：<br>
	 * 1. 所有枚举类型统一成一种分类（用Enum.class表示） <br>
	 * 2. 所有数组类型统一成一种类型（用Object[].class表示） <br>
	 * 4. 基本类型转成对应的封装类型（如：int.class转换成Integer.class）<br>
	 * 5. 其他类型的分类仍旧用原有的类型。 <br>
	 * 
	 * @param clazz
	 * @return
	 */
	private Class<?> classify(Class<?> clazz) {
		if (clazz.isEnum()) {
			return Enum.class;
		} else if (clazz.isArray()) {
			return Object[].class;
		} else if (clazz.isPrimitive()) {
			return BeanCopyUtils.getBoxClazz(clazz);
		} else {
			return clazz;
		}
	}

	/**
	 * 查找下一个可以适用的ConverterKey<br>
	 * 实现逻辑：<br>
	 * 目标类型是固定的 <br>
	 * 原始类型会先按照输入类型来找，然后按照输入类型的父类来找，直到找到object类型为止<br>
	 * 
	 * @return
	 */
	public ConverterKey findNextConverterKey() {
		Class<?> superClazz = originClazz.getSuperclass();
		if (superClazz == null) {
			return null;
		}
		return new ConverterKey(superClazz, this.destClazz);
	}

	@Override
	public String toString() {
		return originClazz + " " + destClazz;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((destClazz == null) ? 0 : destClazz.hashCode());
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
		ConverterKey other = (ConverterKey) obj;
		if (destClazz == null) {
			if (other.destClazz != null)
				return false;
		} else if (!destClazz.equals(other.destClazz))
			return false;
		if (originClazz == null) {
			if (other.originClazz != null)
				return false;
		} else if (!originClazz.equals(other.originClazz))
			return false;
		return true;
	}

}
