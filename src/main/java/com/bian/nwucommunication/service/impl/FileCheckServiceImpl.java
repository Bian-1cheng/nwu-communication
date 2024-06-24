package com.bian.nwucommunication.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bian.nwucommunication.common.execption.ClientException;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dto.FileInfoDTO;
import com.bian.nwucommunication.dto.req.CheckFileReqDTO;
import com.bian.nwucommunication.mapper.FileInfoMapper;
import com.bian.nwucommunication.service.FileCheckService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileCheckServiceImpl  extends ServiceImpl<FileInfoMapper, FileInfo> implements FileCheckService {

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Override
    public List<FileInfoDTO> queryAllFilePage(int currentPage, int pageSize) {
        Page<FileInfo> page = new Page<>(currentPage, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper<FileInfo>();
        queryWrapper.orderByDesc("push_date");
        Page<FileInfo> fileInfoPage = fileInfoMapper.selectPage(page, queryWrapper);
        return BeanUtil.copyToList(fileInfoPage.getRecords(), FileInfoDTO.class);
    }

    @Override
    public void checkFile(CheckFileReqDTO checkFileReqDTO) {
        FileInfo fileInfo = fileInfoMapper.selectById(checkFileReqDTO.getFileId());
        if(fileInfo == null)
            throw new ClientException("文件不存在");
        fileInfoMapper.updateById(fileInfo.setIsPass(checkFileReqDTO.getNewStatus()));
    }


}
