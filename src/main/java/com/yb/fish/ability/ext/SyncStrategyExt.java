package com.yb.fish.ability.ext;


import com.yb.fish.ability.context.SyncStrategyContext;
import com.yb.fish.ability.ext.base.BaseExt;

public abstract class SyncStrategyExt extends BaseExt {

    public abstract void validateSyncStrategy(SyncStrategyContext syncStrategyContext);

    public abstract void saveSyncStrategy(SyncStrategyContext syncStrategyContext);
}
