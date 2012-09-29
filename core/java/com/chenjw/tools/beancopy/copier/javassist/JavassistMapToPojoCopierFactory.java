package com.chenjw.tools.beancopy.copier.javassist;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.chenjw.tools.BeanCopyUtils;
import com.chenjw.tools.beancopy.Converter;
import com.chenjw.tools.beancopy.Copier;
import com.chenjw.tools.beancopy.CopierFactory;
import com.chenjw.tools.beancopy.copier.CopierConstants;
import com.chenjw.tools.beancopy.copier.javassist.support.ClassGenerator;
import com.chenjw.tools.beancopy.copier.javassist.support.Expression;
import com.chenjw.tools.beancopy.copier.javassist.support.Field;
import com.chenjw.tools.beancopy.copier.javassist.support.Helper;
import com.chenjw.tools.beancopy.copier.javassist.support.InvokeExpression;
import com.chenjw.tools.beancopy.copier.javassist.support.MethodGenerator;
import com.chenjw.tools.beancopy.copier.javassist.support.TrinocularExpression;

/**
 * Javassist实现的map->pojo的copier
 * 
 * @author chenjw 2012-6-13 下午5:00:25
 */
public class JavassistMapToPojoCopierFactory implements CopierFactory {

	@Override
	public Copier getCopier(Class<?> destClazz, Class<?> originClazz,
			Map<String, String> nameMap, Class<?> defaultDestValueClazz) {
		MapToPojoCopierBuilder build = new MapToPojoCopierBuilder(destClazz,
				originClazz, nameMap);
		return build.build();
	}

	private static class MapToPojoCopierBuilder {

		private Class<?> destClazz;
		private Class<?> originClazz;
		private Map<String, String> nameMap;
		private ClassGenerator clazzGenerator;
		private Map<Class<?>, Field> finalFieldMap = new HashMap<Class<?>, Field>();

		public MapToPojoCopierBuilder(Class<?> destClazz, Class<?> originClazz,
				Map<String, String> nameMap) {
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
				Field destField, Expression converter, String getterName,
				PropertyDescriptor setter) {

			Class<?> setterClazz = setter.getWriteMethod().getParameterTypes()[0];
			// private static final class setterClazzField=setterClazz;
			Field setterClazzField = createStaticFinalField(setterClazz,
					new Expression(
							Helper.makeClassName(setterClazz) + ".class",
							Class.class));
			// Map.get(Object)
			Expression getterInvoke = new InvokeExpression(
					CopierConstants.MAP_GET_METHOD, originField,
					new Expression[] { Helper
							.createStringExpression(getterName) });
			// Object tmp1=Map.get(Object);
			Field tmp1 = methodGenerator.addLocalVariableDefine(Object.class,
					getterInvoke);
			// Converter.convert(tmp1,tmp1.getClass(),#setterClazzField)
			Expression convertInvoke = new InvokeExpression(
					CopierConstants.CONVERTER_CONVERT_METHOD, converter,
					new Expression[] {
							new Expression(tmp1.getCode(), tmp1.getType()),
							new InvokeExpression(
									CopierConstants.OBJECT_GET_CLASS_METHOD,
									tmp1, new Expression[0]),
							new Expression(setterClazzField.getCode(),
									Class.class) });
			// tmp1==null?null:#convertInvoke
			Expression convertIfNotNullInvoke = new TrinocularExpression(
					new Expression(tmp1.getCode() + "==null", boolean.class),
					new Expression("null", Object.class), convertInvoke,
					Object.class);

			Expression setterInvoke = null;
			if (Helper.isNeedUnbox(convertIfNotNullInvoke.getType(),
					setterClazz)) {
				// Object tmp2=(tmp1==null?null:#convertInvoke);
				Field tmp2 = methodGenerator.addLocalVariableDefine(
						Object.class, convertIfNotNullInvoke);
				// destField.setXXX(tmp2);
				setterInvoke = new InvokeExpression(setter.getWriteMethod(),
						destField,
						new Expression[] { tmp2.unboxOrZero(setterClazz) });

			} else {
				// destField.setXXX(tmp2);
				setterInvoke = new InvokeExpression(setter.getWriteMethod(),
						destField, new Expression[] { convertIfNotNullInvoke });
			}
			methodGenerator.addExpression(setterInvoke);
		}

		private void initCopyMethodBody(MethodGenerator methodGenerator) {
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
				addCopyCode(clazzGenerator, methodGenerator, originTmp,
						destTmp, new Expression("$3", Converter.class),
						getterName, setter);

			}
		}
	}
}
