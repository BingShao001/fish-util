package com.yb.fish.lombok;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LombokBean {
    private String className;
    private String classType;
    private String count;
    private byte[] data;
}
