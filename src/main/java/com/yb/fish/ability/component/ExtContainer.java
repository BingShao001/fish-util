package com.yb.fish.ability.component;

import com.google.common.collect.Maps;
import com.yb.fish.ability.ext.base.BaseExt;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;


@Component
public class ExtContainer {
    private Map<String, BaseExt> extContainerMap = Maps.newConcurrentMap();

    public Map<String, BaseExt> addExt(String key, BaseExt baseExt) {
        extContainerMap.put(key, baseExt);
        return extContainerMap;
    }

    public <T> T getExt(String bizCode, Class<T> tClass) {
        String simpleName = tClass.getSimpleName();
        String key = simpleName + "_" + bizCode;
        T t = (T) extContainerMap.get(key);
        Assert.notNull(t, "has no ext ,the key is : " + key);
        return t;
    }

    public <T> T getDefExt(Class<T> tClass) {
        return this.getExt("def", tClass);
    }
}

