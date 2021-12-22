package com.yb.fish.ability.impl;


import com.yb.fish.ability.annotition.ExtComponent;
import com.yb.fish.ability.context.ResponseDataConfigContext;
import com.yb.fish.ability.dto.ResponseDataConfigDTO;
import com.yb.fish.ability.ext.ResponseDataConfigExt;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtComponent(bizCode = "aPaaS")
public class APaaSResponseDataConfigExt extends ResponseDataConfigExt {
    @Override
    public void validateResponseDataConfig(ResponseDataConfigContext responseDataConfigContext) {
        log.info("APaaSResponseDataConfigExt-validateResponseDataConfig");

    }

    @Override
    public void saveResponseDataConfig(ResponseDataConfigContext responseDataConfigContext) {
        log.info("APaaSResponseDataConfigExt-saveResponseDataConfig");
    }

    @Override
    public ResponseDataConfigDTO getResponseDataConfigInfo(String resourceId) {
        return null;
    }
}
