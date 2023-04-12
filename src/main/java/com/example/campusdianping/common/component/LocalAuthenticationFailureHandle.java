package com.example.campusdianping.common.component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 登录失败处理类
 * @auther j2-yizhiyang
 * @date 2023/4/10 15:39
 */
@Component
public class LocalAuthenticationFailureHandle extends SimpleUrlAuthenticationFailureHandler {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Map<String,String> map = new HashMap<>();
        map.put("code","500");
        map.put("messege","验证失败");
        response.getWriter().write(gson.toJson(map));
    }
}
