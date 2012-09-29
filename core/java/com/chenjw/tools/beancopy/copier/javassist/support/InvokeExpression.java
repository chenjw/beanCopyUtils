/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tools.beancopy.copier.javassist.support;

import java.lang.reflect.Method;

/**
 * 调用表达式
 * 
 * @author chenjw 2012-6-14 上午12:18:44
 */
public class InvokeExpression extends Expression {

	/**
	 * 生成方法调用的表达式
	 * 
	 * @param method
	 *            调用的方法
	 * @param field
	 * @param param
	 */
	public InvokeExpression(Method method, Expression field, Expression[] param) {
		StringBuffer sb = new StringBuffer();
		sb.append("(" + field.getCode() + ")." + method.getName() + "(");
		for (int i = 0; i < method.getParameterTypes().length; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(param[i].cast(method.getParameterTypes()[i]).getCode());
		}

		sb.append(")");
		this.code = sb.toString();
		this.type = method.getReturnType();
	}

}
