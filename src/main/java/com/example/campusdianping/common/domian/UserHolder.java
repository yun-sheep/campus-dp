package com.example.campusdianping.common.domian;


import com.example.campusdianping.entity.user.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Description springsecurity权限认证的获取当前用户
 * @auther j2-yizhiyang
 * @date 2023/3/19 19:32
 */

public class UserHolder {
    private static final ThreadLocal<SecurityUser> tl = new ThreadLocal<>();
    private static final SecurityUser securityUser =  (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


    public static void saveUser(SecurityUser user){
        tl.set(user);
    }

    public static  SecurityUser getUser(){
        return securityUser;
    }

    public static void removeUser(){
        tl.remove();
    }
}
