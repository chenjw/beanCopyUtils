package com.chenjw.tools.beancopy;

/**
 * 用于转换集合对象
 * 
 * @author chenjw 2012-1-10 下午3:33:23
 */
public interface CollectionConverter {

	/**
	 * 转换集合对象，其中集合里每个元素使用conponentConverter转换
	 * 
	 * @param origin
	 *            原始对象
	 * @param originClazz
	 *            原始类型
	 * @param destClazz
	 *            目标类型
	 * @param conponentConverter
	 *            子对象转换器
	 * @return 转换后的结果
	 */
	public Object convertCollection(Object origin, Class<?> originClazz,
			Class<?> destClazz, Converter componentConverter);
}
