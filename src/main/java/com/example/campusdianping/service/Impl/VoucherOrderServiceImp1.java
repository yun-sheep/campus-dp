package com.example.campusdianping.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campusdianping.common.domian.Result;
import com.example.campusdianping.common.domian.UserHolder;
import com.example.campusdianping.common.utils.redisutils.RedisIdWorker;
import com.example.campusdianping.entity.voucher.SeckillVoucher;
import com.example.campusdianping.entity.voucher.Voucher;
import com.example.campusdianping.entity.voucher.VoucherOrder;
import com.example.campusdianping.mapper.VoucherMapper;
import com.example.campusdianping.mapper.VoucherOrderMapper;
import com.example.campusdianping.service.ISeckillVoucherService;
import com.example.campusdianping.service.IVoucherOrderService;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @Description 使用锁的方式解决超卖和一人一单（仅学习测试）
 * @auther j2-yizhiyang
 * @date 2023/4/12 16:56
 */
public class VoucherOrderServiceImp1 extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {
    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private VoucherOrderServiceImp1 voucherOrderServiceImp1;
    /**
     * 秒杀订单
     *
     * */
    @Override
    public Result seckillVoucher(Long voucherId) {
        // 1.查询优惠券
        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
        // 2.判断秒杀是否开始
        if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
            // 尚未开始
            return Result.fail("秒杀尚未开始！");
        }
        // 3.判断秒杀是否已经结束
        if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
            // 尚未开始
            return Result.fail("秒杀已经结束！");
        }
        // 5.一人一单
        Long userId = UserHolder.getUser().getId();
        synchronized (userId.toString().intern()){
            //为什么要锁方法，以及字符串做加锁对象的要点(这样写会失效）
            //return createVoucherOrder(voucherId);
            //两种解决方法：1、自己注入自己，2、获取当前对象的代理对象，用代理对象的
            //return voucherOrderServiceImp1.createVoucherOrder(userId);
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);

        }
    }


    @Transactional
    public Result createVoucherOrder(Long voucherId) {

         Long userId = UserHolder.getUser().getId();

            // 5.1.查询订单
            Long count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
            // 5.2.判断是否存在
            if (count > 0) {
                // 用户已经购买过了
                return Result.fail("用户已经购买过一次！");
            }

            // 6.扣减库存（使用是sql默认的行锁来解决超卖问题）
            boolean success = seckillVoucherService.update()
                    .setSql("stock = stock - 1") // set stock = stock - 1
                    .eq("voucher_id", voucherId).gt("stock", 0) // where id = ? and stock > 0
                    .update();
            if (!success) {
                // 扣减失败
                return Result.fail("库存不足！");
            }

            // 7.创建订单
            VoucherOrder voucherOrder = new VoucherOrder();
            // 7.1.订单id
            long orderId = redisIdWorker.nextId("order");
            voucherOrder.setId(orderId);
            // 7.2.用户id
            voucherOrder.setUserId(userId);
            // 7.3.代金券id
            voucherOrder.setVoucherId(voucherId);
            save(voucherOrder);

            // 7.返回订单id
            return Result.ok(orderId);
        }

}
