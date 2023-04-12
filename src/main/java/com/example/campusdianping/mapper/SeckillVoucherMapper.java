package com.example.campusdianping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.campusdianping.entity.voucher.SeckillVoucher;
import org.apache.ibatis.annotations.Mapper;


/**
 *
 * 秒杀优惠券表，与优惠券是一对一关系 Mapper 接口
 *
 */
@Mapper
public interface SeckillVoucherMapper extends BaseMapper<SeckillVoucher> {

}
