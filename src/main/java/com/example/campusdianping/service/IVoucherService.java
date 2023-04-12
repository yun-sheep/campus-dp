package com.example.campusdianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.campusdianping.common.domian.Result;
import com.example.campusdianping.entity.voucher.Voucher;


/**
 * <p>
 *  服务类
 * </p>

 */
public interface IVoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    void addSeckillVoucher(Voucher voucher);
}
