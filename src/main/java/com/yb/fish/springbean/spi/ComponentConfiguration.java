package com.yb.fish.springbean.spi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName demo
 * @Description TODO
 * @Author Jiangnan Cui
 * @Date 2023/11/10 10:20
 * @Version 1.0
 */
@Configuration
public class ComponentConfiguration {

    @Bean
    public BingComponent bingComponent(){
        BingComponent bingComponent = new BingComponent();
        bingComponent.setComponentName("bing-component");
        bingComponent.setPoolSize(12);
        return bingComponent;
    }
}
