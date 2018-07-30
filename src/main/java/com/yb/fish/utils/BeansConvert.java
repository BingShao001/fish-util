package com.yb.fish.utils;

import com.yb.venus.log.LoggerFactory;
import com.yb.venus.log.VenusLogger;
import org.springframework.beans.BeanUtils;

/**
* 复制实体工具
* @author bing
* @create 2018/1/26
* @version 1.0
**/
public class BeansConvert {

    private static final VenusLogger log = LoggerFactory.getLogger(BeansConvert.class);
    /**
     * 实体对象复制
     * @param source 被复制对象
     * @param target 目标对象
     * @param <T>
     * @return
     */
    public static <T> T beansCopy(Object source, T target) {
        // source(被复制对象), target(目标对象)
        if (source != null && target != null) {
            BeanUtils.copyProperties(source, target);
        }else {
            log.error("source(被复制对象), target(目标对象) : 不为空！");
        }
        return target;
    }

}
