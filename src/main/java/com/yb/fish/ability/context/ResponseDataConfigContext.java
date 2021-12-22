package com.yb.fish.ability.context;

import com.yb.fish.ability.model.DataMapRuleModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDataConfigContext {
    private String contextId;
    private String resourceId;
    private String bizCode;
    private List<DataMapRuleModel> dataMapRuleModels;
}
