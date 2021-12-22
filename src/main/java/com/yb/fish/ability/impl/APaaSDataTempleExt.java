package com.yb.fish.ability.impl;


import com.yb.fish.ability.annotition.Ext;
import com.yb.fish.ability.context.DataTempleContext;
import com.yb.fish.ability.ext.DataTempleExt;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@ExtComponent(bizCode = "aPaaS")
@Ext(bizCode = "aPaaS")
public class APaaSDataTempleExt extends DataTempleExt {
    @Override
    public void validateDataTemple(DataTempleContext dataTempleContext) {
        log.info("APaaSDataTempleExt-validateDataTemple");
    }

    @Override
    public void saveBindingDataTemple(DataTempleContext dataTempleContext) {
        log.info("APaaSDataTempleExt-saveBindingDataTemple");
    }
}
