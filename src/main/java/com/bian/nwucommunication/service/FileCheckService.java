package com.bian.nwucommunication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dto.FileInfoDTO;
import com.bian.nwucommunication.dto.req.CheckFileReqDTO;

import java.util.List;

public interface FileCheckService {

    Boolean checkNFSW(String fileUrl);
}
