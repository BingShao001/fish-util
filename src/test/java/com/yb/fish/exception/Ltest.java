package com.yb.fish.exception;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Ltest {
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "a");
        map.put(2, "b");
        List<Integer> list = Lists.newArrayList(1, 2, 3);
        Set<Integer> set = list.stream().filter(integer -> !map.containsKey(integer)).collect(Collectors.toSet());
        System.out.println(JSON.toJSONString(set));

    }
}
