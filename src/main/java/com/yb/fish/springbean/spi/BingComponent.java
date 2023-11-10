package com.bing.component;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;

/**
 * @ClassName BingComponent
 * @Description TODO
 * @Author Jiangnan Cui
 * @Date 2023/11/10 10:21
 * @Version 1.0
 */
@Data
public class BingComponent implements InitializingBean {
    private int poolSize;
    private String componentName;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("BingComponent print.......");
    }

    public void doProcess() {
        System.out.println("******" + poolSize + "******" + componentName);
    }
}
