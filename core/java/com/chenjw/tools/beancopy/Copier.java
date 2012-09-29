package com.chenjw.tools.beancopy;

/**
 * 用于把原始对象中的属性拷贝到目标对象中，必要时使用转换器做属性值转换
 * 
 * @author chenjw 2012-1-17 下午3:39:05
 */
public interface Copier {

	/**
	 * 把origin对象中的属性拷贝到dest对象中，必要时使用converter做属性值转换
	 * 
	 * @param origin
	 * @param dest
	 * @param converter
	 */
	public void copy(Object origin, Object dest, Converter converter);
}
