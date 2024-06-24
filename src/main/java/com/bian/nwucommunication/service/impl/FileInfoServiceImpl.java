package com.bian.nwucommunication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.common.errorcode.BaseErrorCode;
import com.bian.nwucommunication.common.execption.ClientException;
import com.bian.nwucommunication.common.execption.ServiceException;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dto.FileInfoDTO;
import com.bian.nwucommunication.dto.FileUploadDTO;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.mapper.FileInfoMapper;
import com.bian.nwucommunication.service.FileInfoService;
import com.bian.nwucommunication.util.*;
import com.bian.nwucommunication.util.constant.OssConstants;
import com.bian.nwucommunication.util.constant.RedisConstants;
import com.bian.nwucommunication.util.constant.UserConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper,FileInfo> implements FileInfoService {

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private RedisTemplate redisTemplate;
    
    @Resource
    private FileOperateUtil fileOperateUtil;

    @Override
    public List<FileInfoDTO> queryMyFile() {
        UserDTO user = UserHolder.getUser();
        List<FileInfo> fileList = fileInfoMapper.selectList(new QueryWrapper<FileInfo>().eq("user_id", user.getId()));
        if (CollUtil.isEmpty(fileList))
            throw new ServiceException(BaseErrorCode.FILE_LIST_EMPTY);
        return BeanUtil.copyToList(fileList, FileInfoDTO.class);
    }

    @Override
    public List<FileInfoDTO> querySchool() {
        UserDTO user = UserHolder.getUser();
        List<FileInfo> fileList = fileInfoMapper.selectList(new QueryWrapper<FileInfo>()
                .eq("school_id", user.getSchoolId())
                .eq("is_pass", UserConstants.FILE_HAVE_PASS));
        if (CollUtil.isEmpty(fileList))
            throw new ServiceException(BaseErrorCode.FILE_LIST_EMPTY);
        return BeanUtil.copyToList(fileList, FileInfoDTO.class);
    }

    @Override
    public List<FileInfoDTO> queryAllSchool() {
        Set keys = new RedisUtil().scanKeys(redisTemplate, RedisConstants.CACHE_All_School_KEY, RedisConstants.CACHE_SCANS_COUNT);
        if (!CollUtil.isEmpty(keys)){
            List<String> fileList = redisTemplate.opsForValue().multiGet(keys);
            if (CollUtil.isEmpty(fileList))
                throw new ServiceException(BaseErrorCode.FILE_LIST_EMPTY);
            List<FileInfoDTO> fileListDTO = new ArrayList<>();
            for(String item : fileList) {
                fileListDTO.add(JSONUtil.toBean(item, FileInfoDTO.class));
            }
            return fileListDTO;
        }
        List<FileInfo> fileList = fileInfoMapper.selectList(
                new QueryWrapper<FileInfo>().eq("is_pass", UserConstants.FILE_HAVE_PASS));
        for (FileInfo item : fileList) {
            redisTemplate.opsForValue().set(RedisConstants.CACHE_All_School_KEY+item.getId()+":",JSONUtil.toJsonStr(item));
        }
        return BeanUtil.copyToList(fileList, FileInfoDTO.class);
    }

    @Override
    public void uploadFile(FileUploadDTO fileUploadDTO, MultipartFile file) {
        UserDTO user = UserHolder.getUser();
        String imgStr = fileOperateUtil.upload(file, OssConstants.FILE_ADDRESS);
        FileInfo fileInfo = BeanUtil.toBeanIgnoreCase(fileUploadDTO, FileInfo.class, true);
        fileInfo.setDownNum(0);
        fileInfo.setGreatNum(0);
        fileInfo.setPushDate(LocalDateTimeUtil.parseDate(DateUtil.today()));
        fileInfo.setUserId(user.getId());
        fileInfo.setPath(imgStr);
        fileInfo.setIsScore(false);
        fileInfo.setIsPass(UserConstants.FILE_WAIT_CHECK);
        fileInfo.setSchoolId(user.getSchoolId());
        fileInfoMapper.insert(fileInfo);
    }

    @Override
    public List<FileInfoDTO> searchFileByKeyword(String search) {
        List<FileInfo> fileInfo = fileInfoMapper.selectList(new QueryWrapper<FileInfo>()
                .like("key_word", search)
                .eq("is_pass", UserConstants.FILE_HAVE_PASS));
        if(CollUtil.isEmpty(fileInfo))
            throw new ServiceException(BaseErrorCode.FILE_LIST_EMPTY);
        return BeanUtil.copyToList(fileInfo, FileInfoDTO.class);
    }
}
