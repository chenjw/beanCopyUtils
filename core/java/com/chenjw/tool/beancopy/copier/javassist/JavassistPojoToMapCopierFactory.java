/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tool.beancopy.copier.javassist;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

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
import com.chenjw.tool.beancopy.util.ReflectUtils;

/**
 * Javassist实现的pojo->map的copier
 * 
 * @author chenjw 2012-6-13 下午5:00:25
 */
public class JavassistPojoToMapCopierFactory implements CopierFactory {

	@Override
	public Copier getCopier(Class<?> destClazz, Class<?> originClazz,
			Map<String, String> nameMap, Class<?> defaultDestValueClazz) {
		PojoToMapCopierBuilder build = new PojoToMapCopierBuilder(destClazz,
				originClazz, nameMap, defaultDestValueClazz);
		return build.build();
	}

	private static class PojoToMapCopierBuilder {

		private Class<?> destClazz;
		private Class<?> originClazz;
		private Map<String, String> nameMap;

		private Class<?> defaultDestValueClazz;
		private ClassGenerator clazzGenerator;
		private Map<Class<?>, Field> finalFieldMap = new HashMap<Class<?>, Field>();

		public PojoToMapCopierBuilder(Class<?> destClazz, Class<?> originClazz,
				Map<String, String> nameMap, Class<?> defaultDestValueClazz) {
			this.destClazz = destClazz;
			this.originClazz = originClazz;
			this.nameMap = nameMap;
			this.defaultDestValueClazz = defaultDestValueClazz;
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
				PropertyDescriptor getter, String setterName) {

			Class<?> getterClazz = getter.getReadMethod().getReturnType();
			// private static final class getterClazzField=getterClazz;
			Field getterClazzField = createStaticFinalField(getterClazz,
					new Expression(
							Helper.makeClassName(getterClazz) + ".class",
							Class.class));
			Field setterClazzField = createStaticFinalField(
					defaultDestValueClazz,
					new Expression(Helper.makeClassName(defaultDestValueClazz)
							+ ".class", Class.class));

			// originField.getXXX()
			Expression getterInvoke = new InvokeExpression(
					getter.getReadMethod(), originField, new Expression[] {});
			getterInvoke = getterInvoke.box(Object.class);
			// Converter.convert(#getterInvoke,getterClazzField,defaultDestValueClazz)
			Expression convertInvoke = new InvokeExpression(
					CopierConstants.CONVERTER_CONVERT_METHOD, converter,
					new Expression[] { getterInvoke, getterClazzField,
							setterClazzField });

			Expression setterInvoke = new InvokeExpression(
					CopierConstants.MAP_PUT_METHOD, destField,
					new Expression[] {
							Helper.createStringExpression(setterName),
							convertInvoke });
			methodGenerator.addExpression(setterInvoke);
		}

		private void initCopyMethodBody(MethodGenerator methodGenerator) {

			PropertyDescriptor[] getters = ReflectUtils
					.getBeanGetters(originClazz);
			// 原始对象
			Field originTmp = methodGenerator.addLocalVariableDefine(
					originClazz, new Expression("$1", Object.class));
			// 目标对象
			Field destTmp = methodGenerator.addLocalVariableDefine(destClazz,
					new Expression("$2", Object.class));

			for (PropertyDescriptor getter : getters) {
				String setterName = null;
				if (nameMap != null) {
					setterName = nameMap.get(getter.getName());
				}
				if (setterName == null) {
					setterName = getter.getName();
				}
				addCopyCode(clazzGenerator, methodGenerator, originTmp,
						destTmp, new Expression("$3", Converter.class), getter,
						setterName);

			}
		}
	}
}
