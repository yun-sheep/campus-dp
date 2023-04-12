package com.example.campusdianping.common.filter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;

import com.example.campusdianping.common.config.SmsAuthenticationToken;
import com.example.campusdianping.common.constant.RedisConstants;
import com.example.campusdianping.common.constant.TokenConstant;
import com.example.campusdianping.common.domian.user.UserVO;
import com.example.campusdianping.common.utils.redisutils.RedisUtils;
import com.example.campusdianping.common.utils.token.JwtUtils;
import com.example.campusdianping.entity.SecurityUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/**
 * @Description 处理所有的请求（如果没有token就放行到登录认证过滤器）
 * @auther j2-yizhiyang
 * @date 2023/4/3 14:13
 */
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Resource
    private RedisUtils redisUtils;
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        //获取请求头的中的token
        String token = httpServletRequest.getParameter(TokenConstant.TOKEN_HEAD);
        //如果请求头中没有token
        if(!StringUtils.hasText(token)){
            //不加这个就不行（这个的一个运行顺序到底是怎么样的啊）
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            //返回到登录界面
            return;

        }
        //1、解析token
        String userid;
        try{
            Claims claims = JwtUtils.getClaimsByToken(token);
            userid = claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        //2、获取用户信息key:value = Token:UserDTO
        String redisKey = RedisConstants.LOGIN_USER_KEY+userid;
        //UserVO userVO = (UserVO) redisUtils.get(redisKey);
        SecurityUser securityUser  = (SecurityUser)  redisUtils.get(redisKey);
        //TODO 刷新Token时间（只有小于两分钟的tokne再进行刷新）
        SmsAuthenticationToken smsAuthenticationToken =
                new SmsAuthenticationToken(securityUser,null);
        SecurityContextHolder.getContext().setAuthentication(smsAuthenticationToken);
        //放行
        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }
}
