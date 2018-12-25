package com.yb.fish.utils;

import java.lang.reflect.Method;

public class ReflectUtils {
    static String[] types = {"java.lang.Integer",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.Byte",
            "java.lang.Boolean",
            "java.lang.Character",
            "java.lang.String",
            "int", "double", "long", "short", "byte", "boolean", "char", "float"};

    public static Object getValueByFieldName(String propertyName, Object object) {
        Class clazz = object.getClass();
        String methodName = "get" + captureName(propertyName);
        Object ret = null;
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            ret = method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 首字母大写
     *
     * @param name
     * @return
     */
    private static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);

    }

    public static boolean isBasicType(String fieldType) {
        for (String type : types) {
            if (type.equals(fieldType)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static boolean isBasicType(Object arg) {
        return  arg instanceof Integer ||
                arg instanceof Byte ||
                arg instanceof Long ||
                arg instanceof Double ||
                arg instanceof Float ||
                arg instanceof Character ||
                arg instanceof Short ||
                arg instanceof Boolean;
    }

    public static boolean isModelType(Object arg) {
        return !isJavaUtilType(arg)&&!isBasicType(arg);
    }

    public static boolean isJavaUtilType(Object arg) {
        return  arg instanceof String ||
                arg instanceof java.util.List ||
                arg instanceof java.util.Map ||
                arg instanceof java.util.Set ||
                arg instanceof java.util.Date;
    }
}
