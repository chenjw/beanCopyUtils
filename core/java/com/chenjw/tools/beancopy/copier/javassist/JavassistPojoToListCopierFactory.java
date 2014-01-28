/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.tools.beancopy.copier.javassist;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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
import com.chenjw.tools.beancopy.util.ReflectUtils;

/**
 * Javassist实现的pojo->list的copier,字段顺序为字段名的字母序
 * 
 * @author chenjw 2012-6-13 下午5:00:25
 */
public class JavassistPojoToListCopierFactory implements CopierFactory {

    @Override
    public Copier getCopier(Class<?> destClazz, Class<?> originClazz, Map<String, String> nameMap,
                            Class<?> defaultDestValueClazz) {
        PojoToListCopierBuilder build = new PojoToListCopierBuilder(destClazz, originClazz,
            nameMap, defaultDestValueClazz);
        return build.build();
    }

    private static class PojoToListCopierBuilder {

        private Class<?>             destClazz;
        private Class<?>             originClazz;

        private Class<?>             defaultDestValueClazz;
        private ClassGenerator       clazzGenerator;
        private Map<Class<?>, Field> finalFieldMap = new HashMap<Class<?>, Field>();

        public PojoToListCopierBuilder(Class<?> destClazz, Class<?> originClazz,
                                       Map<String, String> nameMap, Class<?> defaultDestValueClazz) {
            this.destClazz = destClazz;
            this.originClazz = originClazz;
            this.defaultDestValueClazz = defaultDestValueClazz;
        }

        public Copier build() {
            clazzGenerator = ClassGenerator.newInstance();
            clazzGenerator.addInterface(Copier.class);
            // 定义copy方法
            MethodGenerator methodGenerator = MethodGenerator
                .newInstance("public void copy(Object origin, Object dest, "
                             + Helper.makeClassName(Converter.class) + " converter)");
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

        private void addCopyCode(ClassGenerator clazzGenerator, MethodGenerator methodGenerator,
                                 Field originField, Field destField, Expression converter,
                                 PropertyDescriptor getter) {

            Class<?> getterClazz = getter.getReadMethod().getReturnType();
            // private static final class getterClazzField=getterClazz;
            Field getterClazzField = createStaticFinalField(getterClazz,
                new Expression(Helper.makeClassName(getterClazz) + ".class", Class.class));
            Field setterClazzField = createStaticFinalField(defaultDestValueClazz, new Expression(
                Helper.makeClassName(defaultDestValueClazz) + ".class", Class.class));

            // originField.getXXX()
            Expression getterInvoke = new InvokeExpression(getter.getReadMethod(), originField,
                new Expression[] {});
            getterInvoke = getterInvoke.box(Object.class);
            // Converter.convert(#getterInvoke,getterClazzField,defaultDestValueClazz)
            Expression convertInvoke = new InvokeExpression(
                CopierConstants.CONVERTER_CONVERT_METHOD, converter, new Expression[] {
                        getterInvoke, getterClazzField, setterClazzField });

            Expression setterInvoke = new InvokeExpression(CopierConstants.LIST_ADD_METHOD,
                destField, new Expression[] {convertInvoke });
            System.out.println(setterInvoke.getCode());
            methodGenerator.addExpression(setterInvoke);
        }

        private void initCopyMethodBody(MethodGenerator methodGenerator) {

            PropertyDescriptor[] getters = ReflectUtils.getBeanGetters(originClazz);
            // 原始对象
            Field originTmp = methodGenerator.addLocalVariableDefine(originClazz, new Expression(
                "$1", Object.class));
            // 目标对象
            Field destTmp = methodGenerator.addLocalVariableDefine(destClazz, new Expression("$2",
                Object.class));
            Arrays.sort(getters, new Comparator<PropertyDescriptor>() {

                @Override
                public int compare(PropertyDescriptor o1, PropertyDescriptor o2) {
                    return o1.getName().compareTo(o2.getName());
                }

            });
            for (PropertyDescriptor getter:getters) {
                addCopyCode(clazzGenerator, methodGenerator, originTmp, destTmp, new Expression(
                    "$3", Converter.class), getter);

            }
        }
    }
}
