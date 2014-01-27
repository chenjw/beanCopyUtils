package com.chenjw.tools.beancopy.copier;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.chenjw.tools.beancopy.Converter;

/**
 * 一些常量
 * 
 * @author chenjw 2012-6-14 上午12:57:16
 */
public class CopierConstants {

    // Converter.convert(Object,Class,Class)
    public final static Method CONVERTER_CONVERT_METHOD = findMethod(Converter.class, "convert",
                                                            new Class<?>[] { Object.class,
            Class.class, Class.class                       });
    // Map.get(Object);
    public final static Method MAP_GET_METHOD           = findMethod(Map.class, "get",
                                                            new Class<?>[] { Object.class });
    // Map.put(Object,Object);
    public final static Method MAP_PUT_METHOD           = findMethod(Map.class, "put",
                                                            new Class<?>[] { Object.class,
            Object.class                                   });

    // List.get(int);
    public final static Method LIST_GET_METHOD          = findMethod(List.class, "get",
                                                            new Class<?>[] { int.class });
    
    // List.set(int,Object);
    public final static Method LIST_ADD_METHOD          = findMethod(List.class, "add",
                                                            new Class<?>[] { 
            Object.class                                   });
    // Object.getClass();
    public final static Method OBJECT_GET_CLASS_METHOD  = findMethod(Object.class, "getClass",
                                                            new Class<?>[0]);

    private static Method findMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        try {
            Method method = clazz.getMethod(methodName, paramTypes);
            return method;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
