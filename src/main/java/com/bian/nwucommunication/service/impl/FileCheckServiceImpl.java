package com.bian.nwucommunication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.common.constant.RedisConstants;
import com.bian.nwucommunication.common.execption.ClientException;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dao.Notice;
import com.bian.nwucommunication.dto.FileInfoDTO;
import com.bian.nwucommunication.dto.req.CheckFileReqDTO;
import com.bian.nwucommunication.dto.resp.RequirementRespDTO;
import com.bian.nwucommunication.mapper.FileInfoMapper;
import com.bian.nwucommunication.service.FileCheckService;
import com.bian.nwucommunication.service.FileInfoService;
import com.bian.nwucommunication.service.NoticeService;
import com.bian.nwucommunication.service.RequirementService;
import com.bian.nwucommunication.common.constant.UserConstants;
import com.bian.nwucommunication.util.redis.RedisUtil;
import com.bian.nwucommunication.util.redis.MessageProducer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class FileCheckServiceImpl  extends ServiceImpl<FileInfoMapper, FileInfo> implements FileCheckService {

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Resource
    private RequirementService requirementService;

    @Resource
    private NoticeService noticeService;

    @Resource
    private FileInfoService fileInfoService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MessageProducer messageProducer;

    @Override
    public List<FileInfoDTO> queryAllFilePage(int currentPage, int pageSize) {
        Page<FileInfo> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<FileInfo> queryWrapper = Wrappers.lambdaQuery(FileInfo.class)
                .orderByDesc(FileInfo::getPushDate);
        Page<FileInfo> fileInfoPage = fileInfoMapper.selectPage(page, queryWrapper);
        return BeanUtil.copyToList(fileInfoPage.getRecords(), FileInfoDTO.class);
    }

    @Override
    public void checkFile(CheckFileReqDTO checkFileReqDTO) {
        FileInfo fileInfo = null;
        try {
            fileInfo = fileInfoMapper.selectById(checkFileReqDTO.getFileId());
        } catch (Exception e) {
            log.error("文件不存在{}",e.getMessage());
            throw new ClientException("文件不存在");
        }
        fileInfoMapper.updateById(fileInfo.setIsPass(checkFileReqDTO.getNewStatus()));
        if(Objects.equals(checkFileReqDTO.getNewStatus(), UserConstants.FILE_HAVE_PASS)){
            List<RequirementRespDTO> requirementList = requirementService.searchRequirementByKeyWord(fileInfo.getKeyWord());
            for(RequirementRespDTO item : requirementList){
                messageProducer.sendMessage(RedisConstants.REDIS_STREAM_NAME,item.getEmail(),item.getKeyWord(),item.getId());
                Notice notice = Notice.builder()
                        .isNotice(UserConstants.NOT_NOTICE)
                        .fileId(fileInfo.getId())
                        .date(LocalDateTimeUtil.parseDate(DateUtil.today()))
                        .keyWord(item.getKeyWord())
                        .userId(item.getUserId())
                        .build();
                boolean saved = noticeService.save(notice);
            }
        }

        Set keys = new RedisUtil().scanKeys(redisTemplate, RedisConstants.CACHE_All_School_KEY, RedisConstants.CACHE_SCANS_COUNT);
        redisTemplate.delete(keys);
        fileInfoService.updateRedisSchoolFile();



    }


}
