package com.bian.nwucommunication.controller;


import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.common.result.Results;

import com.bian.nwucommunication.dto.FileInfoDTO;
import com.bian.nwucommunication.dto.req.CheckFileReqDTO;
import com.bian.nwucommunication.service.AdminService;
import com.bian.nwucommunication.service.FileCheckService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fs/admin")
public class FileAdminController {

    @Resource
    private AdminService adminService;

    @GetMapping("/allfile/{currentPage}/{pageSize}")
    private Result<List<FileInfoDTO>> getAllFile(@PathVariable("currentPage") int currentPage,
                                                 @PathVariable("pageSize") int pageSize){
        return Results.success(adminService.queryAllFilePage(currentPage,pageSize));
    }

    @PostMapping("/filestatus")
    private Result<?> updateFileStatus(@RequestBody CheckFileReqDTO checkFileReqDTO){
        adminService.checkFile(checkFileReqDTO);
        return Results.success("操作成功");
    }
}
