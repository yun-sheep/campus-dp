package com.example.campusdianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.campusdianping.common.domian.Result;
import com.example.campusdianping.controller.user.LoginFormDTO;
import com.example.campusdianping.entity.user.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IUserService extends IService<User> {

    Result sendCode(String phone);

    Result login(LoginFormDTO loginForm);

    Result sign();

    Result signCount();

}
