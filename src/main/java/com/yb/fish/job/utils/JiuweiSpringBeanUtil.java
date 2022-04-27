package com.yb.fish.job.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * bean处理
 * 
 * @author huangwei
 * @date 2019/12/27
 */

public class JiuweiSpringBeanUtil implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(JiuweiSpringBeanUtil.class);

    private JiuweiSpringBeanUtil() {
    }

    private static ApplicationContext applicationContext;

    /**
     * 获取applicationContext
     * @return applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean
     * @param name bean 名称
     * @return bean
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean
     * @param clazz bean class object
     * @param <T> bean class
     * @return bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name bean 名称
     * @param clazz bean class object
     * @param <T> bean class
     * @return bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LOGGER.info("set applicationContext");
        if (JiuweiSpringBeanUtil.applicationContext == null) {
            LOGGER.info("applicationContext 赋值");
            JiuweiSpringBeanUtil.applicationContext = applicationContext;
        }
    }
}
