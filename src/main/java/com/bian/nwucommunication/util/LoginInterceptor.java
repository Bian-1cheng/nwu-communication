package com.bian.nwucommunication.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bian.nwucommunication.dto.UserDTO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

import static com.bian.nwucommunication.util.constant.RedisConstants.LOGIN_USER_KEY;
import static com.bian.nwucommunication.util.constant.RedisConstants.LOGIN_USER_TTL;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        String token = request.getHeader("token");
        if(StrUtil.isBlank(token)){
            response.setStatus(401);
            return false;
        }
        String userJson = (String)redisTemplate.opsForValue().get(LOGIN_USER_KEY + token);
        if(JSONUtil.isNull(userJson))
            return false;
        UserHolder.saveUser(JSONUtil.toBean(userJson,UserDTO.class));
        redisTemplate.expire(LOGIN_USER_KEY+token,LOGIN_USER_TTL, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果不删 ThreadLocal中用完的信息会有内存泄漏的风险
//        UserThreadLocal.remove();
    }

}