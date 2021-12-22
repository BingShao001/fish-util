package com.yb.fish.ability.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WestDataHttpSourceContext {
    private String contextId;
    private String resourceId;
    private String bizCode;
    private String httpUri;
    private String httpMethod;
    private Map<String, Object> httpParam;
}
