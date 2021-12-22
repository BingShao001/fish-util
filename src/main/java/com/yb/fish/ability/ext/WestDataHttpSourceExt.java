package com.yb.fish.ability.ext;


import com.yb.fish.ability.context.WestDataHttpSourceContext;
import com.yb.fish.ability.dto.WestDataHttpSourceDTO;
import com.yb.fish.ability.ext.base.BaseExt;

public abstract class WestDataHttpSourceExt extends BaseExt {

    public abstract void validateSource(WestDataHttpSourceContext westDataHttpSourceContext);
    public abstract void saveSource(WestDataHttpSourceContext westDataHttpSourceContext);
    public abstract WestDataHttpSourceDTO getHttpSource(String sourceId);




}
