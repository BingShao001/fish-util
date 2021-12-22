package com.yb.fish.ability.impl;


import com.yb.fish.ability.annotition.ExtComponent;
import com.yb.fish.ability.context.SyncStrategyContext;
import com.yb.fish.ability.ext.SyncStrategyExt;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtComponent(bizCode = "aPaaS")
public class APaaSSyncStrategyExt extends SyncStrategyExt {


    @Override
    public void validateSyncStrategy(SyncStrategyContext syncStrategyContext) {
        log.info("APaaSSyncStrategyExt-validateSyncStrategy");

    }

    @Override
    public void saveSyncStrategy(SyncStrategyContext syncStrategyContext) {
        log.info("APaaSSyncStrategyExt-saveSyncStrategy");

    }
}
