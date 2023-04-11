package com.example.campusdianping.common.domian.user;

import lombok.Data;

/**
 * @Description user显示类(封装到返回的地方）
 * @auther j2-yizhiyang
 * @date 2023/4/6 19:33
 */
@Data
public class UserVO {
    /**
     * 用户ID
     * */
    private Long id;
    /**
     * 用户昵称
     * */
    private String nickname;
    /**
     * 用户头像
     * */
    private String icon;
}
