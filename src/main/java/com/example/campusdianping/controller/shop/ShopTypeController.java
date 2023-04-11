package com.example.campusdianping.controller.shop;

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
        return name;
    }


}
