package com.bian.nwucommunication.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bian.nwucommunication.common.errorcode.BaseErrorCode;
import com.bian.nwucommunication.common.execption.ServiceException;
import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.common.result.Results;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dao.UserInfo;
import com.bian.nwucommunication.dto.FileInfoDTO;
import com.bian.nwucommunication.dto.FileUploadDTO;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.mapper.FileInfoMapper;
import com.bian.nwucommunication.service.FileInfoService;
import com.bian.nwucommunication.service.UserService;
import com.bian.nwucommunication.util.UserHolder;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/fs")
public class FileInfoController {

    @Resource
    private FileInfoService fileInfoService;

    @Resource
    private UserService userService;


    @GetMapping("/getMyfile")
    private Result<?> getMyFile(){
        List<FileInfoDTO> fileInfoDTO = fileInfoService.queryMyFile();
        return Results.success(fileInfoDTO);
    }

    @GetMapping("/myschool")
    private Result<?> getMySchool(){
        List<FileInfoDTO> fileInfoDTO = fileInfoService.querySchool();
        return Results.success(fileInfoDTO);
    }

    @GetMapping("/allschool")
    private Result<?> getAllSchool(){
        List<FileInfoDTO> fileInfoDTO = fileInfoService.queryAllSchool();
        return Results.success(fileInfoDTO);
    }

    @GetMapping("/hotfile")
    private Result<?> getHotFile(){
        List<FileInfo> fileInfoList = fileInfoService.getBaseMapper().selectList(
                new QueryWrapper<FileInfo>().orderByAsc("greatNum"));
        if (CollUtil.isEmpty(fileInfoList))
            throw new ServiceException(BaseErrorCode.FILE_LIST_EMPTY);
        List<FileInfoDTO> fileInfoDTO = BeanUtil.copyToList(fileInfoList, FileInfoDTO.class);
        return Results.success(fileInfoDTO);
    }

    @PostMapping("/file")
    private Result<String> uploadFile(HttpServletRequest request,
                                        @RequestParam(value = "title") String title,
                                        @RequestParam(value = "key_word") String keyWord,
                                        @RequestParam(value = "isPublic") Boolean isPublic,
                                        @RequestParam(value = "school_name") String schoolName,
                                        @RequestParam(value = "desc") String intro,
                                        @RequestParam(value = "file") MultipartFile file){
        FileUploadDTO fileUploadDTO = new FileUploadDTO(title,intro,isPublic,schoolName,keyWord);
        fileInfoService.uploadFile(fileUploadDTO,file);
        return Results.success("文件上传成功");
    }

    @GetMapping("/filebyname/{search}")
    private Result<?> searchFileByKeyword(@PathVariable("search") String search){
        List<FileInfoDTO> fileInfoDTO = fileInfoService.searchFileByKeyword(search);
        return Results.success(fileInfoDTO);
    }

}
