package com.bian.nwucommunication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dto.FileInfoDTO;
import com.bian.nwucommunication.dto.FileUploadDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileInfoService extends IService<FileInfo> {
    List<FileInfoDTO> queryMyFile();

    List<FileInfoDTO> querySchool();

    List<FileInfoDTO> queryAllSchool();

    void uploadFile(FileUploadDTO fileUploadDTO,MultipartFile file);

    List<FileInfoDTO> searchFileByKeyword(String search);

    List<FileInfoDTO> updateRedisSchoolFile();
}
