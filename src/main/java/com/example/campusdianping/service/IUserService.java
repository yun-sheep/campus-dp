package com.example.campusdianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.campusdianping.common.domian.Result;
import com.example.campusdianping.entity.User;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IUserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

    //Result login(LoginFormDTO loginForm, HttpSession session);

    Result sign();

    Result signCount();

}
