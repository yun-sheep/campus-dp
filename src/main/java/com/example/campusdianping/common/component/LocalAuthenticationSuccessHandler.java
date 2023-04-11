package com.example.campusdianping.common.component;

import cn.hutool.core.bean.BeanUtil;
import com.example.campusdianping.common.domian.user.UserVO;
import com.example.campusdianping.common.utils.redisutils.RedisUtils;
import com.example.campusdianping.common.utils.token.JwtUtils;
import com.example.campusdianping.entity.SecurityUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.example.campusdianping.common.constant.RedisConstants.LOGIN_USER_KEY;

/**
 * @Description 登录成功处理类
 * @auther j2-yizhiyang
 * @date 2023/4/10 15:39
 */
@Component
public class LocalAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    @Resource
    private RedisUtils redisUtils;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //TODO 验证成功，存入redis（key:value = Token:UserDTO(只存id,nickName,icon))
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        String token = JwtUtils.generateToken(securityUser.id);
        String tokenKey = LOGIN_USER_KEY + securityUser.getId();
        //为什么这个东西转换不成功

        UserVO userVO = BeanUtil.copyProperties(securityUser,UserVO.class);
        int flag = redisUtils.add(tokenKey,userVO);


        //TODO 把Token和UserVO传回去给前端
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        Map<String,Object> map  = BeanUtil.beanToMap(userVO);
        map.put("token",token);
        response.getWriter().write(gson.toJson(map));
    }
}
