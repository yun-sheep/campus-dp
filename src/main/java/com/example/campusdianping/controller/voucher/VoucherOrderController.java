package com.example.campusdianping.controller.voucher;

import com.example.campusdianping.common.domian.Result;
import com.example.campusdianping.entity.voucher.Voucher;
import com.example.campusdianping.service.IVoucherOrderService;
import com.example.campusdianping.service.IVoucherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

/**
 * @Description 优惠券订单业务
 * @auther j2-yizhiyang
 * @date 2023/4/4 15:24
 */
public class VoucherOrderController {
    @Resource
    private IVoucherOrderService voucherOrderService;

    @PostMapping("seckill/{id}")
    public Result seckillVoucher(@PathVariable("id") Long voucherId) {
        return voucherOrderService.seckillVoucher(voucherId);
    }

}
