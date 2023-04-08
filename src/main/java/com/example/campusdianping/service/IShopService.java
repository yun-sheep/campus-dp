package com.example.campusdianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campusdianping.common.domian.Result;
import com.example.campusdianping.entity.shop.Shop;

/**
 * @Description
 * @auther j2-yizhiyang
 * @date 2023/4/5 11:27
 */
public interface IShopService extends IService<Shop> {
    Result queryById(Long id);

    Result update(Shop shop);

    Result queryShopByType(Integer typeId, Integer current, Double x, Double y);
}
