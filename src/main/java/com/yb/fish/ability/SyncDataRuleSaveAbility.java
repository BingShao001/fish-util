package com.yb.fish.ability;

import com.yb.fish.ability.component.ExtContainer;
import com.yb.fish.ability.context.DataTempleContext;
import com.yb.fish.ability.context.ResponseDataConfigContext;
import com.yb.fish.ability.context.SyncStrategyContext;
import com.yb.fish.ability.context.WestDataHttpSourceContext;
import com.yb.fish.ability.ext.DataTempleExt;
import com.yb.fish.ability.ext.ResponseDataConfigExt;
import com.yb.fish.ability.ext.SyncStrategyExt;
import com.yb.fish.ability.ext.WestDataHttpSourceExt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SyncDataRuleSaveAbility {
    @Resource
    private ExtContainer extContainer;

    public void saveRequestSource(WestDataHttpSourceContext westDataHttpSourceContext) {
        WestDataHttpSourceExt westDataHttpSourceExt = extContainer.getExt(westDataHttpSourceContext.getBizCode(), WestDataHttpSourceExt.class);
        westDataHttpSourceExt.validateSource(westDataHttpSourceContext);
        westDataHttpSourceExt.saveSource(westDataHttpSourceContext);
    }

    public void bindingDataTemple(DataTempleContext dataTempleContext) {
        DataTempleExt dataTempleExt = extContainer.getExt(dataTempleContext.getBizCode(), DataTempleExt.class);
        dataTempleExt.validateDataTemple(dataTempleContext);
        dataTempleExt.saveBindingDataTemple(dataTempleContext);
    }

    public void saveResponseData(ResponseDataConfigContext responseDataConfigContext) {
        ResponseDataConfigExt responseDataConfigExt = extContainer.getExt(responseDataConfigContext.getBizCode(), ResponseDataConfigExt.class);
        responseDataConfigExt.validateResponseDataConfig(responseDataConfigContext);
        responseDataConfigExt.saveResponseDataConfig(responseDataConfigContext);
    }

    public void saveSyncStrategy(SyncStrategyContext syncStrategyContext) {
        SyncStrategyExt syncStrategyExt = extContainer.getExt(syncStrategyContext.getBizCode(),SyncStrategyExt.class);
        syncStrategyExt.validateSyncStrategy(syncStrategyContext);
        syncStrategyExt.saveSyncStrategy(syncStrategyContext);
    }

}
