package com.yb.fish.ability.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WestDataHttpSourceDTO {
    private String httpUri;
    private String httpMethod;
    private Map<String, String> httpParam;
}
