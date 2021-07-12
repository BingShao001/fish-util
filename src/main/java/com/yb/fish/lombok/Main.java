package com.yb.fish.lombok;

import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        LombokBean lombokBean = new LombokBean("bing", "person", "666".getBytes(StandardCharsets.UTF_8));
        LombokBean lombokBeanNull = new LombokBean();
        LombokBean lombokBeanBuildNull = LombokBean.builder().build();
        LombokBean lombokBeanBuild = LombokBean
                .builder()
                .className("bing_build")
                .classType("builder")
                .data("new".getBytes(StandardCharsets.UTF_8))
                .build();
        System.out.println(lombokBean);
        System.out.println(lombokBeanNull);
        System.out.println(lombokBeanBuild);

    }

}
