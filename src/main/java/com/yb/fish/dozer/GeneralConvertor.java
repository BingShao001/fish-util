package com.yb.fish.dozer;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.yb.fish.lombok.LombokBean;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * GeneralConvertor
 *
 * @author bing
 * @version 1.0
 * @create 04/08/2021
 **/
public class GeneralConvertor {
    //持有Dozer单例, 避免重复创建DozerMapper消耗资源.
    public static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    /**
     * List  实体类 转换器
     *
     * @param source 原数据
     * @param clz    转换类型
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> List<T> convertor(List<S> source, Class<T> clz) {
        if (source == null) {
            return null;
        }
        List<T> map = new ArrayList<>();
        for (S s : source) {
            map.add(mapper.map(s, clz));
        }
        return map;
    }

    /**
     * Set 实体类 深度转换器
     *
     * @param source 原数据
     * @param clz    目标对象
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> Set<T> convertor(Set<S> source, Class<T> clz) {
        if (source == null) {
            return null;
        }
        Set<T> set = new TreeSet<>();
        for (S s : source) {
            set.add(mapper.map(s, clz));
        }
        return set;
    }

    /**
     * 实体类 深度转换器
     *
     * @param source
     * @param clz
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> T convertor(S source, Class<T> clz) {
        if (source == null) {
            return null;
        }
        return mapper.map(source, clz);
    }

    public static void convertor(Object source, Object object) {
        mapper.map(source, object);
    }

    public static <T> void copyConvertor(T source, Object object) {
        mapper.map(source, object);
    }

    public static void main(String[] args) {
        BingBean bingBean = BingBean.builder().classTypeAlias("int").count(3).className("Bing").data("xxx".getBytes(StandardCharsets.UTF_8)).build();
        LombokBean lombokBean = convertor(bingBean, LombokBean.class);
        System.out.println(lombokBean.toString());

    }

}
