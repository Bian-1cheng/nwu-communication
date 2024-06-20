package com.bian.nwucommunication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.common.execption.ClientException;
import com.bian.nwucommunication.common.school.SchoolEnum;
import com.bian.nwucommunication.dao.UserInfo;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.dto.UserInfoDTO;
import com.bian.nwucommunication.dto.UserLoginDTO;
import com.bian.nwucommunication.mapper.UserMapper;
import com.bian.nwucommunication.service.UserService;
import com.bian.nwucommunication.util.*;
import com.bian.nwucommunication.util.constant.EmailConstants;
import com.bian.nwucommunication.util.constant.OssConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.bian.nwucommunication.util.constant.RedisConstants.*;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,UserInfo> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private FileOperateUtil fileOperateUtil;

    @Override
    public UserDTO login(UserLoginDTO userLoginDTO) {
        Boolean isRight = checkCode(userLoginDTO.getEmail(), userLoginDTO.getCode());
        if(!isRight)
            throw new ClientException("验证码有误");
        QueryWrapper queryWrapper = new QueryWrapper<UserInfo>();
        queryWrapper.eq("email",userLoginDTO.getEmail());
        UserInfo userInfo= null;
        try {
            userInfo = userMapper.selectOne(queryWrapper);
        } catch (Exception e) {
            throw new ClientException("邮箱信息有误");
        }
        String token = UUID.randomUUID().toString();
        UserDTO userDTO = BeanUtil.copyProperties(userInfo, UserDTO.class);
        userDTO.setToken(token);
        redisTemplate.opsForValue().set(LOGIN_USER_KEY+token, JSONUtil.toJsonStr(userDTO),LOGIN_USER_TTL,TimeUnit.MINUTES);
        return userDTO;
    }

    @Override
    public UserInfoDTO addInfo(UserInfoDTO userInfoDTO, MultipartFile file) {
        Boolean isRight = checkCode(userInfoDTO.getEmail(), userInfoDTO.getCode());
        if(!isRight)
            return null;
        int schoolId = SchoolEnum.getCodeByName(userInfoDTO.getSchoolName());
        UserInfo userInfo = BeanUtil.toBean(userInfoDTO, UserInfo.class);
        userInfo.setSchoolId(schoolId);
        String headImg = fileOperateUtil.upload(file, OssConstants.USER_HEAD_IMG);
        userInfo.setHeadImg(headImg);
        userInfo.setPhone(userInfoDTO.getPassword());
        userInfoDTO.setHeadImg(headImg);
        userMapper.insert(userInfo);

        return userInfoDTO;
    }

    @Override
    public String getCode(String email) {
        // 优化邮件发送的过程
//        String code = RandomUtil.randomString(4);
        String code = "1111";
//        CompletableFuture<String> futureSum = CompletableFuture.supplyAsync(() ->{
//            return MailUtil.send(email, EmailConstants.CODE_EMAIL_SUBJECT, code, false);
//        });
        redisTemplate.opsForValue().set(CACHE_CODE_KEY+email,code,CACHE_CODE_TTL, TimeUnit.MINUTES);
        return "验证码发送成功";
    }

    private Boolean checkCode(String email,String code){
        String cacheCode = (String) redisTemplate.opsForValue().get(CACHE_CODE_KEY + email);
        return code.equals(cacheCode);
    }

}
