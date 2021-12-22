package com.yb.fish.ability.impl;


import com.yb.fish.ability.annotition.Ext;
import com.yb.fish.ability.context.WestDataHttpSourceContext;
import com.yb.fish.ability.dto.WestDataHttpSourceDTO;
import com.yb.fish.ability.ext.WestDataHttpSourceExt;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@ExtComponent(bizCode = "aPaaS")
@Ext(bizCode = "aPaaS")
public class APaaSWestDataHttpSourceExt extends WestDataHttpSourceExt {

    @Override
    public void validateSource(WestDataHttpSourceContext westDataHttpSourceContext) {
        log.info("APaaSEastDataHttpSourceExt-validateSource");
    }

    @Override
    public void saveSource(WestDataHttpSourceContext westDataHttpSourceContext) {
        log.info("APaaSEastDataHttpSourceExt-saveSource");

    }

    @Override
    public WestDataHttpSourceDTO getHttpSource(String sourceId) {
        return null;
    }
}
