package com.bian.nwucommunication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.common.constant.UserConstants;
import com.bian.nwucommunication.common.errorcode.BaseErrorCode;
import com.bian.nwucommunication.common.execption.ClientException;
import com.bian.nwucommunication.common.constant.SchoolEnum;
import com.bian.nwucommunication.common.execption.ServiceException;
import com.bian.nwucommunication.dao.UserInfo;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.dto.UserInfoDTO;
import com.bian.nwucommunication.dto.req.UserLoginReqDTO;
import com.bian.nwucommunication.mapper.UserMapper;
import com.bian.nwucommunication.service.UserService;
import com.bian.nwucommunication.util.*;
import com.bian.nwucommunication.common.constant.OssConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.bian.nwucommunication.common.constant.RedisConstants.*;
import static com.bian.nwucommunication.util.FileUtil.modifyResolution;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper,UserInfo> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private FileUtil fileUtil;

    @Override
    public UserDTO login(UserLoginReqDTO userLoginDTO) {
        Boolean isRight = checkCode(userLoginDTO.getEmail(), userLoginDTO.getCode());
        if(!isRight)
            throw new ClientException(BaseErrorCode.EMAIL_CODE_ERROR);
        UserInfo userInfo = checkUser(userLoginDTO.getEmail());
        if(userInfo == null)
            throw new ClientException(BaseErrorCode.EMAIL_NOT_EXIST_ERROR);
        String token = UUID.randomUUID().toString();
        UserDTO userDTO = BeanUtil.copyProperties(userInfo, UserDTO.class);
        userDTO.setToken(token);
        redisTemplate.opsForValue().set(LOGIN_USER_KEY+token, JSONUtil.toJsonStr(userDTO),LOGIN_USER_TTL,TimeUnit.MINUTES);
        return userDTO;
    }

    @Override
    @Transactional
    public UserDTO addInfo(UserInfoDTO userInfoDTO, MultipartFile file) {
        Boolean isRight = checkCode(userInfoDTO.getEmail(), userInfoDTO.getCode());
        if(!isRight)
            throw new ClientException(BaseErrorCode.EMAIL_CODE_ERROR);
        if(checkUser(userInfoDTO.getEmail()) != null)
            throw new ClientException(BaseErrorCode.USER_NAME_EXIST_ERROR);
        if(file == null)
            throw new ClientException(BaseErrorCode.FILE_EMPTY_ERROR);
        if(!checkUserHeadImgType(file))
            throw new ClientException(BaseErrorCode.FILE_TYPE_ERROR);
        int schoolId = SchoolEnum.getCodeByName(userInfoDTO.getSchoolName());
        try {
            String headImg = null;
            try {
                String modifiedPath = modifyResolution(
                        file, UserConstants.FILE_Resolution_PATH,
                        UserConstants.FILE_Resolution_WIDTH,
                        UserConstants.FILE_Resolution_HEIGHT);
                File localFile = new File(modifiedPath);
                headImg = fileUtil.upload(
                        new FileInputStream(localFile),
                        file.getOriginalFilename(),
                        OssConstants.USER_HEAD_IMG);
                localFile.delete();
            } catch (Exception e) {
                log.error("压缩图片或上传图片失败{}",e.getMessage());
                throw new ClientException(BaseErrorCode.FILE_RESOLUTION_ERROR);
            }
            UserInfo userInfo = BeanUtil.toBean(userInfoDTO, UserInfo.class);
            userInfo.setSchoolId(schoolId);
            userInfo.setHeadImg(headImg);
            userInfo.setPhone(userInfoDTO.getPassword());
            userInfoDTO.setHeadImg(headImg);
            userMapper.insert(userInfo);
        } catch (Exception e) {
            log.error("插入用户信息失败{}",e);
            throw new ServiceException(BaseErrorCode.SERVICE_File_ERROR);
        }
        return login(new UserLoginReqDTO( userInfoDTO.getCode(),userInfoDTO.getEmail()));
    }

    @Override
    public void getCode(String email) {
//        String code = RandomUtil.randomString(4);
        String code = "1111";
//        try {
//            MailUtil.send(email, EmailConstants.CODE_EMAIL_SUBJECT, code, false);
//        } catch (Exception e) {
//            throw new ClientException(BaseErrorCode.EMAIL_NOT_EXIST);
//        }
        redisTemplate.opsForValue().set(CACHE_CODE_KEY+email,code,CACHE_CODE_TTL, TimeUnit.MINUTES);
    }

    private Boolean checkCode(String email,String code){
        String cacheCode = (String) redisTemplate.opsForValue().get(CACHE_CODE_KEY + email);
        return code.equals(cacheCode);
    }

    private UserInfo checkUser(String email){
        LambdaQueryWrapper<UserInfo> queryWrapper = Wrappers.lambdaQuery(UserInfo.class)
                .eq(UserInfo::getEmail, email);
        UserInfo userInfo = userMapper.selectOne(queryWrapper);
        return userInfo;
    }

    private Boolean checkUserHeadImgType(MultipartFile file){
        String fileType = FileUtil.getFileType(file);
        return Arrays.stream(UserConstants.ALLOW_HEAD_IMG_TYPE).anyMatch(type -> type.equals(fileType));
    }

}
