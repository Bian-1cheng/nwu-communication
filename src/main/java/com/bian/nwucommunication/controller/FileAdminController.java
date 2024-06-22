package com.bian.nwucommunication.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.common.result.Results;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dto.FileInfoDTO;
import com.bian.nwucommunication.dto.req.CheckFileReqDTO;
import com.bian.nwucommunication.mapper.FileInfoMapper;
import com.bian.nwucommunication.service.FileCheckService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fs/admin")
public class FileAdminController {

    @Resource
    private FileCheckService fileCheckService;

    @GetMapping("/allfile/{currentPage}/{pageSize}")
    private Result<List<FileInfoDTO>> getAllFile(@PathVariable("currentPage") int currentPage,
                                                 @PathVariable("pageSize") int pageSize){
        return Results.success(fileCheckService.queryAllFilePage(currentPage,pageSize));
    }

    @PostMapping("/filestatus")
    private Result<?> updateFileStatus(@RequestBody CheckFileReqDTO checkFileReqDTO){
        fileCheckService.checkFile(checkFileReqDTO);
        return Results.success("操作成功");
    }
}
