package com.example.campusdianping.common.filter;

import javax.servlet.FilterChain;

import com.example.campusdianping.common.constant.TokenConstant;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
/**
 * @Description 处理所有（除了/login之外的hhtp请求）认证token的Filter
 * @auther j2-yizhiyang
 * @date 2023/4/3 14:13
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        //获取请求头的中的token
        String token = httpServletRequest.getParameter(TokenConstant.TOKEN_HEAD);
        //如果请求头中没有token
        if(!StringUtils.hasText(token)){
            //TODO 是否在访问白名单中（在的话，就放行）
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            //给前端，前端又发起登录的请求
            httpServletResponse.getWriter().write("未经授权访问！");
            return;
        }
        //解析token
        String userid;
        //TODO 查询redis中token的信息并且进行认证
        //TODO 刷新Token时间（只有小于两分钟的tokne再进行刷新）

    }
}
