package com.chenjw.tools.beancopy;

/**
 * 类型转换接口，用于把对象从一个类型转换到另外一个类型 <br>
 * 接口调用方已经做了参数类型校验、非空校验和异常捕获，实现类中可以无需再做<br>
 * 如果原始对象类型和目标对象类型相同，系统会直接自动返回原始对象，不会进入convert方法<br>
 * 如果无法转换，就返回null或抛错<br>
 * 
 * @author chenjw 2012-1-10 下午3:33:23
 */
public interface Converter {

	/**
	 * 把原始对象从原始类型转换到目标类型<br>
	 * 
	 * @param origin
	 *            原始对象
	 * @param originClazz
	 *            原始类型<br>
	 *            注意：入参中originClazz不一定等于origin.getClass()，
	 *            但origin肯定是originClazz或其子类创建的对象。选择转换器时会按照originClazz来选择<br>
	 *            比如：origin为Timestamp的对象，但在原始pojo的返回值是以Date类型返回的（
	 *            Timestamp继承自Date），这时originClazz为Date，在选取转换器时也会按照Date来选取<br>
	 * @param destClazz
	 *            目标类型
	 * @return 转换结果
	 */
	public Object convert(Object origin, Class<?> originClazz,
			Class<?> destClazz);
}
