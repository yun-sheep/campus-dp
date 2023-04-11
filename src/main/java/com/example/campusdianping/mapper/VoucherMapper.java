package com.example.campusdianping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.campusdianping.entity.voucher.Voucher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>

 */
@Mapper
public interface VoucherMapper extends BaseMapper<Voucher> {

    List<Voucher> queryVoucherOfShop(@Param("shopId") Long shopId);
}
