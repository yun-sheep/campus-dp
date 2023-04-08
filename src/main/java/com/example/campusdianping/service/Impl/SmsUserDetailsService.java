package com.example.campusdianping.service.Impl;

import com.example.campusdianping.entity.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Description 手机号查询用户信息
 * @auther j2-yizhiyang
 * @date 2023/4/3 19:24
 */
@Service
public class SmsUserDetailsService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        //TODO 数据库中根据电话号码查询到用户
        int flag = 0;
        if(flag == 1){
            //TODO 直接使用当前手机号注册（默认用户名，用户id)
        }
        //TODO 扩展之后再写
        SecurityUser securityUser = new SecurityUser();
        return securityUser;
    }
}
