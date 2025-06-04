package com.yb.fish.limit;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author lijian
 * @date 2022/3/8
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtUser {

    private String guid;

    private Long userId;

    /**
     * 登陆标识
     */
    private String identifier;

    private String nickname;

    private String avatar;

    /**
     * 形象
     */
    private String face;

    /**
     * 是否匿名用户
     */
    private Boolean anonymous;

    /**
     * 用户语言(默认英语)
     */
    private Integer userType;

    /**
     * 国家
     */
    private String country;

    /**
     * 动态头像
     */
    private String dynamicAvatar;

}
