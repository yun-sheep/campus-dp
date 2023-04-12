package com.example.campusdianping.common.component;

import cn.hutool.core.util.StrUtil;
import com.example.campusdianping.common.config.SmsAuthenticationToken;
import com.example.campusdianping.common.utils.redisutils.RedisUtils;
import com.example.campusdianping.entity.SecurityUser;
import com.example.campusdianping.service.Impl.SmsUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 对认证码进行认证
 * @auther j2-yizhiyang
 * @date 2023/4/3 20:20
 */
@Component
@Slf4j
public class SmsAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private SmsUserDetailsService smsUserDetailsService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    //进行认证
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        // 获取凭证也就是用户的手机号
        Object principal = authentication.getPrincipal();
        String phone = "";
        if (principal instanceof String) {
            phone = (String) principal;
        }
        // 获取输入的验证码
        String inputCode = (String)  authentication.getCredentials();
        //1、检查Redis手机号的验证码(redis
        String redisCode = stringRedisTemplate.opsForValue().get(phone);
        log.info(redisCode+"验证码");
        if(StrUtil.isEmpty(redisCode)){
            log.info("验证码已过期或者尚未发送，请重新输入");
            throw new BadCredentialsException("验证码已过期或者尚未发送，请重新输入");
        }
        if (!inputCode.equals(redisCode)) {
            log.info("输入的验证码不正确，请重新输入");
            throw new BadCredentialsException("输入的验证码不正确，请重新输入");
        }
        //认证成功之后才把用户信息查出来并且放在redis中（执行的是一个查用户的操作和存在redis操作，以及查该用户验证的操作）
        SecurityUser securityUser = smsUserDetailsService.loadUserByUsername(phone);
        //3、重新创建已经认证对象
        SmsAuthenticationToken authenticationToken1 = new SmsAuthenticationToken(securityUser,inputCode, securityUser.getAuthorities());
        authenticationToken1.setDetails(authenticationToken.getDetails());
        return authenticationToken1;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
