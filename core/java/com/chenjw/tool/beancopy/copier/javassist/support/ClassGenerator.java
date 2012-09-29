/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tool.beancopy.copier.javassist.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

/**
 * 只实现了基本功能，定制父类，构造函数等功能未实现。
 * 
 * @author chenjw 2012-6-13 下午12:53:36
 */
public final class ClassGenerator {

	private static final AtomicLong CLASS_NAME_COUNTER = new AtomicLong(0);

	private final AtomicLong CLASS_VARIABLE_COUNTER = new AtomicLong(0);
	private ClassPool classPool;
	private CtClass ctClass;

	private Set<Class<?>> interfaces = new HashSet<Class<?>>();

	private List<String> fields = new ArrayList<String>();
	private List<MethodGenerator> methods = new ArrayList<MethodGenerator>();

	public CtClass getCtClass() {
		return ctClass;
	}

	public static ClassGenerator newInstance() {
		ClassPool pool = ClassPool.getDefault();
		return new ClassGenerator(pool);
	}

	private ClassGenerator(ClassPool pool) {
		this.classPool = pool;

	}

	/**
	 * 添加接口
	 * 
	 * @param clazz
	 */
	public void addInterface(Class<?> clazz) {
		interfaces.add(clazz);
	}

	/**
	 * 生成类变/常量名称
	 * 
	 * @return
	 */
	private String generateClassVariableName() {
		return "tmp_cv_" + CLASS_VARIABLE_COUNTER.getAndIncrement();
	}

	/**
	 * 生成类名称
	 * 
	 * @return
	 */
	private String generateClassName() {
		return ClassGenerator.class.getName() + "$$Javassist$$"
				+ CLASS_NAME_COUNTER.getAndIncrement();
	}

	/**
	 * 添加静态常量，默认是private statif final的
	 * 
	 * @param type
	 * @param expr
	 * @return
	 */
	public Field addStaticFinalField(Class<?> type, Expression expr) {
		String name = generateClassVariableName();
		this.fields.add(makeFieldStr(name, "private static final", type, expr));
		return new Field(name, type, true);
	}

	/**
	 * 添加方法，每个方法有相应的构造器
	 * 
	 * @param methodGenerator
	 */
	public void addMethod(MethodGenerator methodGenerator) {
		this.methods.add(methodGenerator);
	}

	/**
	 * 添加类属性，默认是private的
	 * 
	 * @param type
	 * @param expr
	 * @return
	 */
	public Field addField(Class<?> type, Expression expr) {
		String name = generateClassVariableName();
		this.fields.add(makeFieldStr(name, "private", type, expr));
		return new Field(name, type, false);
	}

	/**
	 * 生成属性定义的code
	 * 
	 * @param name
	 * @param desc
	 * @param type
	 * @param expr
	 * @return
	 */
	private String makeFieldStr(String name, String desc, Class<?> type,
			Expression expr) {
		StringBuilder sb = new StringBuilder();
		sb.append(desc);
		sb.append(' ');
		sb.append(Helper.makeClassName(type));
		sb.append(' ');
		sb.append(name);
		if (expr != null) {
			sb.append('=');
			sb.append(expr.cast(type).getCode());
		}
		sb.append(';');
		return sb.toString();
	}

	private CtClass findCtClass(String className) throws NotFoundException {
		CtClass targetCtClass = null;
		try {
			targetCtClass = classPool.getCtClass(className);
		} catch (NotFoundException e) {
			Class<?> clazz;
			try {
				clazz = Class.forName(className);
				classPool.appendClassPath(new ClassClassPath(clazz));
				return classPool.getCtClass(className);
			} catch (ClassNotFoundException e1) {
				throw new NotFoundException(className);
			}
		}
		return targetCtClass;

	}

	/**
	 * 根据定义生成需要的class
	 * 
	 * @return
	 */
	public Class<?> toClass() {
		String clazzName = generateClassName();
		ctClass = this.classPool.makeClass(clazzName);

		try {
			// 添加需要实现的接口
			for (Class<?> interf : interfaces) {
				ctClass.addInterface(findCtClass(interf.getName()));
			}
			// 添加属性
			for (String field : fields) {
				ctClass.addField(CtField.make(field, ctClass));

			}
			// 添加方法
			for (MethodGenerator method : methods) {
				CtMethod ctMethod = CtNewMethod.make(method.getMethodCode(),
						ctClass);
				// 方法可见性默认修改为public
				ctMethod.setModifiers(ctMethod.getModifiers()
						& ~Modifier.ABSTRACT);
				ctClass.addMethod(ctMethod);

			}
			// 添加默认构造函数
			ctClass.addConstructor(CtNewConstructor.defaultConstructor(ctClass));
			// 可见性修改为public
			ctClass.setModifiers(ctClass.getModifiers() & ~Modifier.ABSTRACT);

			// 要看生成的class文件，可以开打这个注释（需要自行反编译）
			// ctClass.writeFile("/home/chenjw/test/");
			return ctClass.toClass();
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
