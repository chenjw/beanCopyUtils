/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tool.beancopy.copier.javassist.support;

/**
 * 工具类
 * 
 * @author chenjw 2012-6-14 上午12:19:48
 */
public class Helper {

	/**
	 * 生成表示某个字符串常量的表达式
	 * 
	 * @param str
	 * @return
	 */
	public static Expression createStringExpression(String str) {
		return new Expression("\"" + str + "\"", String.class);
	}

	/**
	 * 根据class调用默认构造函数创建实例
	 * 
	 * @param clazz
	 * @return
	 */
	public static Object newInstance(Class<?> clazz) {
		try {
			return clazz.getConstructor(new Class[0])
					.newInstance(new Object[0]);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据class获得表示类型的字符串
	 * 
	 * @param c
	 * @return
	 */
	public static String makeClassName(Class<?> c) {
		if (c.isArray()) {
			StringBuilder sb = new StringBuilder();
			do {
				sb.append("[]");
				c = c.getComponentType();
			} while (c.isArray());

			return c.getName() + sb.toString();
		}
		return c.getName();
	}

	/**
	 * 根据原始类型和目标类型判断是否需要解包
	 * 
	 * @param destClazz
	 * @return
	 */
	public static boolean isNeedUnbox(Class<?> originClazz, Class<?> destClazz) {
		if (destClazz.isPrimitive() && !originClazz.isPrimitive()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据原始类型和目标类型判断是否需要装包
	 * 
	 * @param originClazz
	 * @param destClazz
	 * @return
	 */
	public static boolean isNeedBox(Class<?> originClazz, Class<?> destClazz) {
		if (originClazz.isPrimitive() && !destClazz.isPrimitive()) {
			return true;
		} else {
			return false;
		}
	}
}
