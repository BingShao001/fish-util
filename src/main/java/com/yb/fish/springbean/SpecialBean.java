package com.yb.fish.springbean;

import com.yb.fish.lombok.LombokBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
/**
* SpecialBean是一个特殊的bean，经过@Component等注解初始化后，
 * 相当于把getObject返回的对象，加入到bean池中，而id为specialBean;
 * 如果想获取SpecialBean本身的bean，要使用beanFactory.getBean("&specialBean")
 * eg:org.mybatis.spring.SqlSessionFactoryBean
* @author bing
* @create 16/09/2021
* @version 1.0
**/
@Component
public class SpecialBean implements FactoryBean<LombokBean> {

    @Override
    public LombokBean getObject() throws Exception {
        return LombokBean.builder()
                .className("LombokBeanFactoryBean")
                .classType("LombokBean")
                .data("data".getBytes())
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return LombokBean.class;
    }
}
