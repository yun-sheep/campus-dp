package com.example.campusdianping.common.domian;


import com.example.campusdianping.entity.SecurityUser;
import com.example.campusdianping.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
