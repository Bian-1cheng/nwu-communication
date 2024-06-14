package com.bian.nwucommunication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dao.UserInfo;
import com.bian.nwucommunication.dto.FileInfoDTO;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.dto.UserInfoDTO;
import com.bian.nwucommunication.dto.UserLoginDTO;
import com.bian.nwucommunication.mapper.UserMapper;
import com.bian.nwucommunication.service.UserService;
import com.bian.nwucommunication.util.FileOperateUtil;
import com.bian.nwucommunication.util.OssConstants;
import com.bian.nwucommunication.util.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.bian.nwucommunication.util.RedisConstants.LOGIN_USER_KEY;

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
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("rank_forth",userLoginDTO.getUsername());
        queryWrapper.eq("rank_fifth",userLoginDTO.getPassword());
        UserInfo userInfo = userMapper.selectOne(queryWrapper);
        if(userInfo == null){
            return null;
        }
        String token = UUID.randomUUID().toString();
        UserDTO userDTO = BeanUtil.copyProperties(userInfo, UserDTO.class);
        userDTO.setToken(token);
        redisTemplate.opsForValue().set(LOGIN_USER_KEY+token, JSONUtil.toJsonStr(userDTO));
        return userDTO;
    }

    @Override
    public UserInfoDTO addInfo(UserInfoDTO userInfoDTO, MultipartFile file) {
        UserInfo userInfo = BeanUtil.toBean(userInfoDTO, UserInfo.class);
        String headImg = fileOperateUtil.upload(file, OssConstants.USER_HEAD_IMG);
        userInfo.setHeadImg(headImg);
        userInfo.setPhone(userInfoDTO.getPassword());
        userInfoDTO.setHeadImg(headImg);
        userMapper.insert(userInfo);

        return userInfoDTO;
    }
}
