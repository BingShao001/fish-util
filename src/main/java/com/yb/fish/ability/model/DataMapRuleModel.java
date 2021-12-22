package com.yb.fish.ability.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataMapRuleModel {
    private String westKey;
    private String eastKey;
}
