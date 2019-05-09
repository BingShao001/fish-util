package com.yb.fish.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


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
    /**
     * 只添加不为null的元素
     * @param list
     * @param t
     * @return
     */
    public static <T> List<T> addListNotNull(List<T> list, T t){
        if (null == list){
            return new ArrayList<>();
        }
        if (null != t){
            list.add(t);
        }
        return list;
    }

    /**
     * 按条件筛选出List集合
     * @param predicate
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> generateListBycondition(Predicate<T> predicate, List<T> list){
        if (null == list){
            return new ArrayList<>();
        }
        return list.stream().filter(predicate).collect(Collectors.toList());
    }
}
