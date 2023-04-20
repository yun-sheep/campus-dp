package com.example.campusdianping.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * @Description SecurityConfig配置类
 * @auther j2-yizhiyang
 * @date 2023/4/3 15:29
 */

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
        /*http.csrf().disable().cors();
        http.authorizeRequests()
                .antMatchers("/login", "/sms/send/code", "/sms/login").permitAll()
                .anyRequest()
                .authenticated();
        //添加手机号短信登录
        http.apply(smsSecurityConfigurerAdapter);*/
        http
                .formLogin()
                .loginPage("/login")
                .and()
                .apply(smsSecurityConfigurerAdapter)
                .and()
                // 设置URL的授权
                .authorizeRequests()
                // 这里需要将登录页面放行
                .antMatchers(  "/sms/login").permitAll()
                // anyRequest() 所有请求   authenticated() 必须被认证
                .anyRequest()
                .authenticated()
                .and()
                // 关闭csrf
                .csrf().disable();
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
