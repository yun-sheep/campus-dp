package com.example.campusdianping.common.config;

import com.example.campusdianping.common.component.LocalAuthenticationFailureHandle;
import com.example.campusdianping.common.component.LocalAuthenticationSuccessHandler;
import com.example.campusdianping.common.component.SmsAuthenticationProvider;
import com.example.campusdianping.common.filter.SmsAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @Description 短信认证配置类
 * @auther j2-yizhiyang
 * @date 2023/4/3 20:38
 */
@Configuration
public class SmsSecurityConfigurerAdapter
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{
    @Resource
    private LocalAuthenticationSuccessHandler localAuthenticationSuccessHandler;
    @Resource
    private LocalAuthenticationFailureHandle localAuthenticationFailureHandle;
    @Resource
    SmsAuthenticationProvider smsAuthenticationProvider;
    public void configure(HttpSecurity http) throws Exception{
        SmsAuthenticationFilter smsAuthenticationFilter = new SmsAuthenticationFilter();
        smsAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsAuthenticationFilter.setAuthenticationSuccessHandler(localAuthenticationSuccessHandler);
        smsAuthenticationFilter.setAuthenticationFailureHandler(localAuthenticationFailureHandle);
        http.authenticationProvider(smsAuthenticationProvider)
                .addFilterBefore(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
