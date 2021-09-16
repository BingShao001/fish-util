package com.yb.fish.springbean;

import com.alibaba.fastjson.JSON;
import com.yb.fish.lombok.LombokBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class BeanLifeCycle implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, BeanPostProcessor, InitializingBean, DisposableBean {
    {
        System.out.println("1.初始化;");
        System.out.println("2.注入属性值");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("3.获取bean name：" + name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("4. 获取beanFactory，可以直接用来获取bean");
        if (beanFactory.containsBean("bingBean")) {
            //*重点*：某些bean想要被BeanPostProcessor钩子方法纳入管理，就必须不能提前被实例化
            // BingBean bingBean = (BingBean) beanFactory.getBean("bingBean");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //在beanFactory基础上扩展出了，很多属性，比如环境参数
        System.out.println("5.获取spring上下文");
        System.out.println(applicationContext.toString());
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        //在每个bean被初始化到bean池前，可以修改bean的属性值相关
        boolean equalsName = "lombokBean".equals(beanName);
        boolean isType = bean instanceof LombokBean;
        System.out.println("6.每一个bean刚刚初始化完成后执行 : " + beanName);
        if (isType && equalsName) {
            LombokBean lombokBean = (LombokBean) bean;
            lombokBean.setClassName("LombokBeanSpring");
        }
        return bean;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("7.一般用于容器启动后，要初始化的逻辑，比如本地缓存初始化");
    }

    @PostConstruct
    public void init() {
        System.out.println("8.自定义初始化");
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        //在每个bean被初始化装进bean池后，可以在后置处理器中把他们加到监听器中做特殊处理
        System.out.println("9.每个bean初始化的流程结束后执行: " + beanName);
        boolean equalsName = "lombokBean".equals(beanName);
        boolean isType = bean instanceof LombokBean;
        if (isType && equalsName) {
            LombokBean lombokBean = (LombokBean) bean;
            System.out.println("lombokBean值：" + JSON.toJSONString(lombokBean));
        }
        return bean;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("10.标准流程销毁");
    }

    @PreDestroy
    public void close() {
        System.out.println("11.自定义destroy方法");
    }
}
