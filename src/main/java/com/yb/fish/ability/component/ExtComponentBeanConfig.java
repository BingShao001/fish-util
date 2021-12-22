package com.yb.fish.ability.component;


import com.yb.fish.ability.annotition.ExtComponent;
import com.yb.fish.ability.ext.base.BaseExt;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ExtComponentBeanConfig implements BeanPostProcessor {
    @Resource
    private ExtContainer extContainer;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> aClass = bean.getClass();
        boolean hasExtComponent = aClass.isAnnotationPresent(ExtComponent.class);
        if (hasExtComponent) {
            Class<?> superclass = aClass.getSuperclass();
            String simpleName = superclass.getSimpleName();
            ExtComponent extComponent = aClass.getAnnotation(ExtComponent.class);
            String extKey = simpleName + "_" + extComponent.bizCode();
            extContainer.addExt(extKey, (BaseExt) bean);
        }
        return bean;
    }
}
