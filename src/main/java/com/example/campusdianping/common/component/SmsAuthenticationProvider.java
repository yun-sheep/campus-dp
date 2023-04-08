package com.example.campusdianping.common.component;

import cn.hutool.core.util.StrUtil;
import com.example.campusdianping.common.config.SmsAuthenticationToken;
import com.example.campusdianping.common.utils.redisutils.RedisUtils;
import com.example.campusdianping.entity.SecurityUser;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class SmsAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private RedisUtils redisUtils;
    private UserDetailsService userDetailsServiceImpl;
    public SmsAuthenticationProvider(@Qualifier("smsUserDetailsService") UserDetailsService userDetailsServiceImpl){
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }
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
        //1、检查Redis手机号的验证码
        String redisCode = redisUtils.get(phone).toString();
        if(StrUtil.isEmpty(redisCode)){
            throw new BadCredentialsException("验证码已过期或者尚未发送，请重新输入");
        }
        if (!inputCode.equals(redisCode)) {
            throw new BadCredentialsException("输入的验证码不正确，请重新输入");
        }
        //认证成功之后才把用户信息查出来并且放在redis中
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(phone);
        //3、重新创建已经认证对象
        SmsAuthenticationToken authenticationToken1 = new SmsAuthenticationToken(principal,inputCode, userDetails.getAuthorities());
        authenticationToken1.setDetails(authenticationToken.getDetails());
        return authenticationToken1;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}