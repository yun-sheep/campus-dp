package com.example.campusdianping.controller.sms;

import cn.hutool.core.util.RandomUtil;
import com.example.campusdianping.common.domian.Result;
import com.example.campusdianping.common.domian.vo.SmsCodeVO;
import com.example.campusdianping.common.utils.regex.RegexUtils;
import com.example.campusdianping.common.utils.redisutils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Description 生成并且获取验证码
 * @auther j2-yizhiyang
 * @date 2023/4/3 15:10
 */
@RestController
@RequestMapping("sms")
@Slf4j
public class SmsEndpoint {
    @Autowired
    private RedisUtils redisUtils;
    //生成验证码返回（在这里不查数据库)
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @GetMapping("/send/code")
    public Result msmCode(String phone){
        // 1.校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return Result.fail("手机号格式错误！");
        }
        log.info(phone + "请求获取验证码");
        // 2. 模拟调用短信平台获取验证码，以手机号为KEY，验证码为值，存入Redis，过期时间一分钟
        String code = RandomUtil.randomNumbers(6);
        //TODO 手机验证码+手机号存入redis（这个要写成工具类）
        stringRedisTemplate.opsForValue().set(phone, code, 60L, TimeUnit.SECONDS);
        //redisUtils.add(phone, code, 60L, TimeUnit.SECONDS);
        Long expire = 60L ;// 查询过期时间
        SmsCodeVO smsCodeVO = new SmsCodeVO(code,expire);
        return Result.ok(smsCodeVO);
    }

}
