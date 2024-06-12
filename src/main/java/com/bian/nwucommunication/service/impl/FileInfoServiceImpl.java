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
import com.bian.nwucommunication.mapper.FileInfoMapper;
import com.bian.nwucommunication.mapper.UserMapper;
import com.bian.nwucommunication.service.FileInfoService;
import com.bian.nwucommunication.util.RedisConstants;
import com.bian.nwucommunication.util.UserHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class FileInfoServiceImpl extends ServiceImpl<UserMapper,UserInfo> implements FileInfoService {

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public List<FileInfoDTO> queryMyFile() {
        UserDTO user = UserHolder.getUser();
        List<FileInfo> fileList = fileInfoMapper.selectList(new QueryWrapper<FileInfo>().eq("userId_id", user.getId()
        ));
        if (CollUtil.isEmpty(fileList))
            return null;
        return BeanUtil.copyToList(fileList, FileInfoDTO.class);
    }

    @Override
    public List<FileInfoDTO> querySchool() {
        UserDTO user = UserHolder.getUser();
        List<FileInfo> fileList = fileInfoMapper.selectList(new QueryWrapper<FileInfo>()
                .eq("school_id_id", user.getSchoolId())
                .eq("is_pass", "1"));
        if (CollUtil.isEmpty(fileList))
            return null;
        return BeanUtil.copyToList(fileList, FileInfoDTO.class);
    }

    @Override
    public List<FileInfoDTO> queryAllSchool() {
        String json = (String) redisTemplate.opsForValue().get(RedisConstants.CACHE_All_School_KEY+"*");
        // TODO  根据前缀查出所有的文件数据
//        Set keys = RedisTemplate.keys(RedisConstants.CACHE_All_School_KEY + "*");
        if (json != null){
            return Collections.singletonList(JSONUtil.toBean(json, FileInfoDTO.class));
        }
        List<FileInfo> fileList = fileInfoMapper.selectList(new QueryWrapper<FileInfo>().eq("is_pass", "1"));
        if (CollUtil.isEmpty(fileList))
            return null;
        for (FileInfo item : fileList) {
            redisTemplate.opsForValue().set(RedisConstants.CACHE_All_School_KEY+item.getId()+":",JSONUtil.toJsonStr(item));
        }
        return BeanUtil.copyToList(fileList, FileInfoDTO.class);
    }

}
