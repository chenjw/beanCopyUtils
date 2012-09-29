/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tools.beancopy.copier.javassist.support;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 方法构建器
 * 
 * @author chenjw 2012-6-13 上午11:46:07
 */
public final class MethodGenerator {

	private final AtomicLong METHOD_VARIABLE_COUNTER = new AtomicLong(0);

	private String signature;
	private List<String> methodLines = new ArrayList<String>();

	public String getMethodCode() {
		StringBuffer sb = new StringBuffer();
		sb.append(signature);
		sb.append('{');
		for (String line : methodLines) {
			sb.append(line);
		}
		sb.append('}');
		return sb.toString();
	}

	public static MethodGenerator newInstance(String signature) {
		return new MethodGenerator(signature);
	}

	private MethodGenerator(String signature) {
		this.signature = signature;
	}

	public List<String> getMethodLines() {
		return methodLines;
	}

	public String getSignature() {
		return signature;
	}

	/**
	 * 方法变量名称
	 * 
	 * @return
	 */
	private String generateMethodVariableName() {
		return "tmp_mv_" + METHOD_VARIABLE_COUNTER.getAndIncrement();
	}

	/**
	 * 增加方法本地变量的定义
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	public Field addLocalVariableDefine(Class<?> type, Expression expr) {
		String name = generateMethodVariableName();
		StringBuilder sb = new StringBuilder();
		sb.append(Helper.makeClassName(type)).append(' ');
		sb.append(name);
		if (expr != null) {
			sb.append('=');
			sb.append(expr.cast(type).getCode());
		}
		sb.append(';');
		methodLines.add(sb.toString());
		return new Field(name, type, false);
	}

	/**
	 * 增加变量的赋值
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	public void addVariableAssign(Field field, Expression expr) {
		if (field.isFinal()) {
			throw new IllegalStateException("final field(" + field.getCode()
					+ ") cant be assign");
		}
		StringBuilder sb = new StringBuilder();
		sb.append(field.getCode());
		sb.append('=');
		sb.append(expr.cast(field.getType()).getCode());
		sb.append(';');
		methodLines.add(sb.toString());
	}

	/**
	 * 表达式语句
	 * 
	 * @param expr
	 */
	public void addExpression(Expression expr) {
		methodLines.add(expr.getCode() + ';');
	}
}
