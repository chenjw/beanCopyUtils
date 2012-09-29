package com.chenjw.tools.beancopy;

/**
 * 带适用范围的转换器接口，用于处理某一范围内的原始类型与某一范围内的目标类型之间的转换<br>
 * 注意： <br>
 * 1.只需要指定封装类型，自动会匹配该封装类型的基础类型，如：只需要指定Integer，不需要指定int类型就会自动匹配 <br>
 * 2.指定Object[].class会自动匹配所有数组类型 <br>
 * 3.指定Enum.class会自动匹配所有Enum的子类 <br>
 * 4.其他类型都只匹配该类型本身 <br>
 * 
 * @author chenjw 2012-1-10 下午3:33:23
 */
public interface ClassifiedConverter extends Converter {

	/**
	 * 返回转换器支持的原始类型数组
	 */
	public Class<?>[] getOriginClazzes();

	/**
	 * 返回转换器支持的目标类型数组
	 */
	public Class<?>[] getDestClazzes();
}
