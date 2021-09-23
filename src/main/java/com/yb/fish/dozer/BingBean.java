package com.yb.fish.dozer;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BingBean {
    private String className;
    @Mapping("classType")
    private String classTypeAlias;
    private Integer count;
    private byte[] data;
}