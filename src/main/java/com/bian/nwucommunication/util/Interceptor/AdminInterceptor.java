package com.bian.nwucommunication.util.Interceptor;

import com.bian.nwucommunication.common.execption.ClientException;
import com.bian.nwucommunication.util.UserHolder;
import com.bian.nwucommunication.common.constant.UserConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        if(!checkUserIdentify())
            throw new ClientException("用户身份不是管理员");
        return true;
    }

    private boolean checkUserIdentify(){
        return UserConstants.ADMIN_IDENTIFY.equals(UserHolder.getUser().getIdentification());
    }
}