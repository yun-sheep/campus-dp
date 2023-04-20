package com.example.campusdianping.controller.shop;

import com.example.campusdianping.common.domian.Result;
import com.example.campusdianping.entity.shop.ShopType;
import com.example.campusdianping.service.IShopTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 处理商户类型
 * @auther j2-yizhiyang
 * @date 2023/4/4 15:21
 */
@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {
    @Resource
    private IShopTypeService typeService;

    @GetMapping("list")
    public Result queryTypeList() {
        List<ShopType> typeList = typeService
                .query().orderByAsc("sort").list();
        System.out.println("访问类型");
        return Result.ok(typeList);
    }


}
