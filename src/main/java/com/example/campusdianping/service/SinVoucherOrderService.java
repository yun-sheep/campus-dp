package com.example.campusdianping.service;

import com.example.campusdianping.common.domian.Result;

/**
 * @Description 单体体应用使用redis和MQ实现秒杀优惠券
 * @auther j2-yizhiyang
 * @date 2023/4/17 17:04
 */
public interface SinVoucherOrderService {
    Result seckillVoucher(Long voucherId);

    Result createVoucherOrder(Long voucherId);
}
