package com.yb.fish.dozer;

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
    private String classType;
    private byte[] data;
}