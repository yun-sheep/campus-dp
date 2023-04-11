package com.example.campusdianping.common.filter;

import com.example.campusdianping.common.config.SmsAuthenticationToken;
import com.example.campusdianping.common.constant.PoneConstant;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description 手机验证码登录的过滤
 * @auther j2-yizhiyang
 * @date 2023/4/3 14:27
 */
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private boolean postOnly = true;
    private String phoneParameter = PoneConstant.phoneParameter;
    private String smsCodeParameter = PoneConstant.smsCodeParameter;
    // 设置拦截/sms/login短信登录接口
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/sms/login", "POST");

    //设置短信登录接口
    public SmsAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    /*public SmsAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }*/

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
                                                throws AuthenticationException, IOException, ServletException {
        //验证请求方法
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }else {
            String phone = this.obtainPhone(request);
            phone = phone != null ? phone : "";
            phone = phone.trim();
            String smsCode = this.obtainSmsCode(request);
            smsCode = smsCode != null ? smsCode : "";
            SmsAuthenticationToken authRequest = new SmsAuthenticationToken(phone, smsCode);
            this.setDetails(request, authRequest);
            // 认证
            return this.getAuthenticationManager().authenticate(authRequest);
        }

    }
    @Nullable
    protected String obtainSmsCode(HttpServletRequest request) {
        return request.getParameter(this.smsCodeParameter);
    }

    @Nullable
    protected String obtainPhone(HttpServletRequest request) {
        return request.getParameter(this.phoneParameter);
    }

    protected void setDetails(HttpServletRequest request, SmsAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setPhoneParameter(String phoneParameter) {
        Assert.hasText(phoneParameter, "Phone parameter must not be empty or null");
        this.phoneParameter = phoneParameter;
    }

    public void setSmsCodeParameter(String smsCodeParameter) {
        Assert.hasText(smsCodeParameter, "SmsCode parameter must not be empty or null");
        this.smsCodeParameter = smsCodeParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getUsernameParameter() {
        return this.phoneParameter;
    }

    public final String getPasswordParameter() {
        return this.smsCodeParameter;
    }

}
