package com.yb.fish.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * 集合类操作工具
 *
 * @author bing
 * @version 1.0
 * @create 2017/12/27
 **/
public class CollectionsUtil {
    private static final Logger logger = LoggerFactory.getLogger(CollectionsUtil.class);

    /**
     * 取出List对象中指定的属性，封装成新List
     *
     * @param list
     * @param propertyName
     * @return
     */
    public static List<Object> getPropertysFromList(List list, String propertyName) {
        List<Object> propertys = new ArrayList<>();
        if (list.isEmpty() || StringUtils.isBlank(propertyName)) {
            return propertys;
        }
        String methodName = "get" + captureName(propertyName);

        for (Object property : list) {
            Class clazz = property.getClass();
            try {
                Method method = clazz.getDeclaredMethod(methodName);
                Object ret = method.invoke(property);
                propertys.add(ret);
            } catch (Exception e) {
                logger.error("list' Object reflex Exception");
            }

        }
        return propertys;
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
}
