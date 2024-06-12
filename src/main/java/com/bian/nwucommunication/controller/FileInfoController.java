package com.bian.nwucommunication.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bian.nwucommunication.common.errorcode.BaseErrorCode;
import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.common.result.Results;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dto.FileInfoDTO;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.mapper.FileInfoMapper;
import com.bian.nwucommunication.service.FileInfoService;
import com.bian.nwucommunication.util.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fs")
public class FileInfoController {

    @Resource
    private FileInfoService fileInfoService;


    @GetMapping("/getMyfile")
    private Result<?> getMyFile(){
        List<FileInfoDTO> fileInfoDTO = fileInfoService.queryMyFile();
        if (fileInfoDTO == null)
            return Results.failure(BaseErrorCode.FILE_LIST_EMPTY);
        return Results.success(fileInfoDTO);
    }

    @GetMapping("/myschool")
    private Result<?> getMySchool(){
        List<FileInfoDTO> fileInfoDTO = fileInfoService.querySchool();
        if (fileInfoDTO == null)
            return Results.failure(BaseErrorCode.FILE_LIST_EMPTY);
        return Results.success(fileInfoDTO);
    }

    @GetMapping("/allschool")
    private Result<?> getAllSchool(){
        List<FileInfoDTO> fileInfoDTO = fileInfoService.queryAllSchool();
        if (fileInfoDTO == null)
            return Results.failure(BaseErrorCode.FILE_LIST_EMPTY);
        return Results.success(fileInfoDTO);
    }
}
