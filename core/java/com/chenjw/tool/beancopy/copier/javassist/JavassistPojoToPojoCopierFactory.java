/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tool.beancopy.copier.javassist;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.chenjw.tool.BeanCopyUtils;
import com.chenjw.tool.beancopy.Converter;
import com.chenjw.tool.beancopy.Copier;
import com.chenjw.tool.beancopy.CopierFactory;
import com.chenjw.tool.beancopy.copier.CopierConstants;
import com.chenjw.tool.beancopy.copier.javassist.support.ClassGenerator;
import com.chenjw.tool.beancopy.copier.javassist.support.Expression;
import com.chenjw.tool.beancopy.copier.javassist.support.Field;
import com.chenjw.tool.beancopy.copier.javassist.support.Helper;
import com.chenjw.tool.beancopy.copier.javassist.support.InvokeExpression;
import com.chenjw.tool.beancopy.copier.javassist.support.MethodGenerator;

/**
 * Javassist实现的pojo->pojo的copier
 * 
 * @author chenjw 2012-6-13 下午5:00:25
 */
public class JavassistPojoToPojoCopierFactory implements CopierFactory {

	@Override
	public Copier getCopier(Class<?> destClazz, Class<?> originClazz,
			Map<String, String> nameMap, Class<?> defaultDestValueClazz) {
		PojoToPojoCopierBuilder build = new PojoToPojoCopierBuilder(destClazz,
				originClazz, nameMap);
		return build.build();
	}

	private static class PojoToPojoCopierBuilder {

		private Class<?> destClazz;
		private Class<?> originClazz;
		private Map<String, String> nameMap;
		private ClassGenerator clazzGenerator;
		private Map<Class<?>, Field> finalFieldMap = new HashMap<Class<?>, Field>();

		public PojoToPojoCopierBuilder(Class<?> destClazz,
				Class<?> originClazz, Map<String, String> nameMap) {
			this.destClazz = destClazz;
			this.originClazz = originClazz;
			this.nameMap = nameMap;
		}

		public Copier build() {
			clazzGenerator = ClassGenerator.newInstance();
			clazzGenerator.addInterface(Copier.class);
			// 定义copy方法
			MethodGenerator methodGenerator = MethodGenerator
					.newInstance("public void copy(Object origin, Object dest, "
							+ Helper.makeClassName(Converter.class)
							+ " converter)");
			clazzGenerator.addMethod(methodGenerator);
			// 定义方法体
			initCopyMethodBody(methodGenerator);
			Copier copier = null;
			Class<?> clazz = clazzGenerator.toClass();
			copier = (Copier) Helper.newInstance(clazz);
			return copier;
		}

		private Field createStaticFinalField(Class<?> clazz, Expression expr) {
			Field field = finalFieldMap.get(clazz);
			if (field == null) {
				field = clazzGenerator.addStaticFinalField(Class.class, expr);
				finalFieldMap.put(clazz, field);
			}
			return field;
		}

		private void addCopyCode(ClassGenerator clazzGenerator,
				MethodGenerator methodGenerator, Field originField,
				Field destField, Expression converter,
				PropertyDescriptor getter, PropertyDescriptor setter) {

			Class<?> getterClazz = getter.getReadMethod().getReturnType();
			Class<?> setterClazz = setter.getWriteMethod().getParameterTypes()[0];
			// 把使用到class类作为类静态常量，这点改动提高了30%左右性能。
			Field getterClazzField = createStaticFinalField(getterClazz,
					new Expression(
							Helper.makeClassName(getterClazz) + ".class",
							Class.class));
			Field setterClazzField = createStaticFinalField(setterClazz,
					new Expression(
							Helper.makeClassName(setterClazz) + ".class",
							Class.class));

			Expression getterInvoke = new InvokeExpression(
					getter.getReadMethod(), originField, new Expression[0]);

			getterInvoke = getterInvoke.box(Object.class);

			Expression convertInvoke = new InvokeExpression(
					CopierConstants.CONVERTER_CONVERT_METHOD, converter,
					new Expression[] { getterInvoke, getterClazzField,
							setterClazzField });
			Expression setterInvoke = null;
			if (Helper.isNeedUnbox(convertInvoke.getType(), setterClazz)) {
				Field tmp = methodGenerator.addLocalVariableDefine(
						Object.class, convertInvoke);
				setterInvoke = new InvokeExpression(setter.getWriteMethod(),
						destField,
						new Expression[] { tmp.unboxOrZero(setterClazz) });

			} else {

				setterInvoke = new InvokeExpression(setter.getWriteMethod(),
						destField, new Expression[] { convertInvoke });
			}
			methodGenerator.addExpression(setterInvoke);
		}

		private void initCopyMethodBody(MethodGenerator methodGenerator) {
			PropertyDescriptor[] getters = BeanCopyUtils
					.getBeanGetters(originClazz);
			PropertyDescriptor[] setters = BeanCopyUtils
					.getBeanSetters(destClazz);

			// 反转
			Map<String, String> setterNameToGetterNameMap = null;
			if (nameMap != null) {
				setterNameToGetterNameMap = new HashMap<String, String>();
				for (Entry<String, String> entry : nameMap.entrySet()) {
					setterNameToGetterNameMap.put(entry.getValue(),
							entry.getKey());
				}
			}
			// 属性名到Get方法的映射
			Map<String, PropertyDescriptor> getterMap = new HashMap<String, PropertyDescriptor>();
			for (PropertyDescriptor getter : getters) {
				getterMap.put(getter.getName(), getter);
			}
			// 原始对象
			Field originTmp = methodGenerator.addLocalVariableDefine(
					originClazz, new Expression("$1", Object.class));
			// 目标对象
			Field destTmp = methodGenerator.addLocalVariableDefine(destClazz,
					new Expression("$2", Object.class));

			for (PropertyDescriptor setter : setters) {
				String getterName = null;
				if (setterNameToGetterNameMap != null) {
					getterName = setterNameToGetterNameMap
							.get(setter.getName());
				}
				if (getterName == null) {
					getterName = setter.getName();
				}
				PropertyDescriptor getter = getterMap.get(getterName);
				if (getter != null) {
					// 添加拷贝代码
					addCopyCode(clazzGenerator, methodGenerator, originTmp,
							destTmp, new Expression("$3", Converter.class),
							getter, setter);
				}
			}
		}
	}
}
