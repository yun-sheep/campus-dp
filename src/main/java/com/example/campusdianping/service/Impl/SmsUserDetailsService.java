package com.example.campusdianping.service.Impl;

import com.example.campusdianping.entity.user.SecurityUser;
import com.example.campusdianping.entity.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description 手机号查询用户信息
 * @auther j2-yizhiyang
 * @date 2023/4/3 19:24
 */
@Service
public class SmsUserDetailsService implements UserDetailsService {
    @Resource
    private UserServiceImpl userService;

    @Override
    public SecurityUser loadUserByUsername(String phone) throws UsernameNotFoundException {
        //TODO 数据库中根据电话号码查询到用户(然后存储）
        /*QueryWrapper<User> queryWrapper  = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        User user =  userService.getOne(queryWrapper);*/
        User user = new User("一只羊","玉贵狗",1l);


        /*int flag = 0;
        if(flag == 1){
            //TODO 直接使用当前手机号注册（默认用户名，用户id)
        }*/
        //TODO 扩展之后再写
       SecurityUser securityUser = new SecurityUser(user);
        return securityUser;
    }
}
