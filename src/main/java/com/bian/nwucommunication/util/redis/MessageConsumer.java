package com.bian.nwucommunication.util.redis;

import com.bian.nwucommunication.common.constant.RedisConstants;
import com.bian.nwucommunication.common.constant.UserConstants;


import com.bian.nwucommunication.dao.Requirement;
import com.bian.nwucommunication.dto.RequirementDTO;
import com.bian.nwucommunication.mapper.RequirementMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class MessageConsumer implements StreamListener<String, ObjectRecord<String, String>> {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RequirementMapper requirementMapper;

    @Override
    public void onMessage(ObjectRecord<String, String> message) {
        StreamOperations<String, String, String> streamOperations = redisTemplate.opsForStream();
        String stream = message.getStream();
        String messageId = message.getId().toString();
        String messageBody  = message.getValue();

        //messageBody 为  邮箱:关键词:id
        String[] split = messageBody.split(":");
        RequirementDTO requirementDTO = new RequirementDTO();
        requirementDTO.setEmail(split[0]);
        requirementDTO.setKeyWord(split[1]);
        requirementDTO.setId(Integer.parseInt(split[2]));

        try {
//            MailUtil.send(requirementDTO.getEmail(), EmailConstants.CODE_EMAIL_NAME, requirementDTO.getKeyWord(), false);
            Requirement requirement = requirementMapper.selectById(Integer.parseInt(split[2]));
            requirement.setIsNotice(UserConstants.is_NOTICE);
            requirementMapper.updateById(requirement);
            streamOperations.acknowledge(stream, RedisConstants.REDIS_STREAM_CONSUME_GROUP,message.getId());
            log.info("消息处理成功，消息id：{}",message.getId());
        }catch (RuntimeException e){
            log.error("需求文件，发送邮件失败：{}",requirementDTO.getEmail());
        }
    }

//    private void handlePendingList() {
//        while (true) {
//            try {
//                // 1.获取pending-list中的订单信息 XREADGROUP GROUP g1 c1 COUNT 1 BLOCK 2000 STREAMS s1 0
//                List<MapRecord<String, Object, Object>> list = redisTemplate.opsForStream().read(
//                        Consumer.from(RedisConstants.REDIS_STREAM_CONSUME_GROUP, RedisConstants.REDIS_STREAM_CONSUME_NAME),
//                        StreamReadOptions.empty().count(1),
//                        StreamOffset.create(RedisConstants.REDIS_STREAM_NAME, ReadOffset.from("0"))
//                );
//                if (list == null || list.isEmpty()) {
//                    break;
//                }
//                // 解析数据
//                MapRecord<String, Object, Object> record = list.get(0);
//                Map<Object, Object> value = record.getValue();
////                streamOperations.acknowledge(stream, RedisConstants.REDIS_STREAM_CONSUME_GROUP,message.getId() );
//                redisTemplate.opsForStream().acknowledge(RedisConstants.REDIS_STREAM_NAME, RedisConstants.REDIS_STREAM_CONSUME_GROUP, record.getId());
//                System.out.println("发送邮件"+record.getValue());
//
//            } catch (Exception e) {
//                log.error("处理pendding订单异常:", e);
////
//            }
//        }
//    }
}

