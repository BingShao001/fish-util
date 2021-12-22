package com.yb.fish.ability.ext;


import com.yb.fish.ability.context.DataTempleContext;
import com.yb.fish.ability.ext.base.BaseExt;

public abstract class DataTempleExt extends BaseExt {

    public abstract void validateDataTemple(DataTempleContext dataTempleContext);

    public abstract void saveBindingDataTemple(DataTempleContext dataTempleContext);
}
