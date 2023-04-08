package com.example.campusdianping.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @Description SecurityConfig配置类
 * @auther j2-yizhiyang
 * @date 2023/4/3 15:29
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private SmsSecurityConfigurerAdapter smsSecurityConfigurerAdapter;

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //TODO 怎么把
        // 关闭csrf，开启跨域支持
        http.csrf().disable().cors();
        http.authorizeRequests()
                .antMatchers("/login", "/sms/send/code", "/sms/login").permitAll()
                .anyRequest()
                .authenticated();
        //添加手机号短信登录
        http.apply(smsSecurityConfigurerAdapter);
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 设置自定义用户认证
        //auth.userDetailsService(myUserDetailsService);
        super.configure(auth);
    }

    /**
     * 将Provider添加到认证管理器中
     *
     * @return
     * @throws Exception
     */
    /*@Override
    protected AuthenticationManager authenticationManager() throws Exception {
        ProviderManager authenticationManager = new ProviderManager(Arrays.asList(smsAuthenticationProvider, daoAuthenticationProvider()));
        authenticationManager.setEraseCredentialsAfterAuthentication(false);
        return authenticationManager;
    }*/

    /**
     * 注入密码解析器到IOC中
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false); // 设置显示找不到用户异常
        return daoAuthenticationProvider;
    }*/
}
