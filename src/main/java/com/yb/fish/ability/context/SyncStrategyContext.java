package com.yb.fish.ability.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncStrategyContext {
    private String contextId;
    private String resourceId;
    private String bizCode;
}
