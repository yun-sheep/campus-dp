package com.example.campusdianping.controller.shop;

import com.example.campusdianping.common.domian.UserHolder;
import com.example.campusdianping.common.domian.user.UserVO;
import com.example.campusdianping.entity.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 处理商户类型
 * @auther j2-yizhiyang
 * @date 2023/4/4 15:21
 */
@RestController
@RequestMapping("/shopType")
public class ShopTypeController {
    @GetMapping("/test")
    public String test(String name){
        //Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        SecurityUser userVO = UserHolder.getUser();
        System.out.println(userVO.getNickname());
        return name;
    }


}
