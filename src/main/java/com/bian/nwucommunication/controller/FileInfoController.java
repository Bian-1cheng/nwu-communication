package com.bian.nwucommunication.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.common.result.Results;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.mapper.FileInfoMapper;
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
    private FileInfoMapper fileInfoMapper;

    @GetMapping("/getMyfile")
    private Result<?> getMyFile(){
        UserDTO user = UserHolder.getUser();
        List<FileInfo> fileList = fileInfoMapper.selectList(new QueryWrapper<FileInfo>().eq("userId_id", user.getId()));
        if(CollUtil.isEmpty(fileList))
            return Results.failure("402","文件列表为空");
        return Results.success(fileList);
    }
}
