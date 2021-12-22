package com.yb.fish.ability.ext;


import com.yb.fish.ability.context.ResponseDataConfigContext;
import com.yb.fish.ability.dto.ResponseDataConfigDTO;
import com.yb.fish.ability.ext.base.BaseExt;

public abstract class ResponseDataConfigExt extends BaseExt {

    public abstract void validateResponseDataConfig(ResponseDataConfigContext responseDataConfigContext);

    public abstract void saveResponseDataConfig(ResponseDataConfigContext responseDataConfigContext);

    public abstract ResponseDataConfigDTO getResponseDataConfigInfo(String resourceId);


}
