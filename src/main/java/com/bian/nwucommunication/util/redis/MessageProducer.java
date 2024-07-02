package com.bian.nwucommunication.util.redis;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

    private final RedisTemplate<String, String> redisTemplate;


    public void sendMessage(String streamKey, String messageKey, String message , Integer id) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("key", messageKey+":"+message+":"+id);
        RecordId recordId = redisTemplate.opsForStream().add(streamKey, messageMap);
        if (recordId != null) {
            log.info("消息id：{}，Stream：{}，消息体：{}",recordId,streamKey,messageMap);
        }
    }
}

