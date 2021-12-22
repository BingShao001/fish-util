package com.yb.fish.ability.dto;

import com.yb.fish.ability.model.DataMapRuleModel;
import lombok.Data;

import java.util.List;

@Data
public class ResponseDataConfigDTO {
    private String resourceId;
    private List<DataMapRuleModel> dataMapRuleModels;
}
