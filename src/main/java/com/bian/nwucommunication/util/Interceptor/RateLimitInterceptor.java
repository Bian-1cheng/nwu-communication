package com.bian.nwucommunication.util.Interceptor;


import com.bian.nwucommunication.common.constant.RedisConstants;
import com.bian.nwucommunication.common.execption.ClientException;
import com.bian.nwucommunication.util.IpUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import java.util.Collections;

@Slf4j
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String key = RedisConstants.CACHE_LIMIT_KEY + IpUtil.getIpAddr(request) + request.getRequestURI();
        if(!RateLimited(key,RedisConstants.CACHE_LIMIT_COUNT,RedisConstants.CACHE_LIMIT_TIME))
            return true;
        log.error("[limit] 限制请求数'{}',缓存key'{}'", RedisConstants.CACHE_LIMIT_COUNT, key);
        throw new ClientException("请求次数过多，请稍后再访问");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    public boolean RateLimited(String key, int limit, int expiryInSeconds){
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();

        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(RedisConstants.CACHE_LIMIT_SCRIPT)));
        redisScript.setResultType(Boolean.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        return (Boolean) redisTemplate.execute(redisScript, Collections.singletonList(key),limit, expiryInSeconds);
    }


}