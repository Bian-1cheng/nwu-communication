package com.bian.nwucommunication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bian.nwucommunication.dao.UserInfo;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.dto.UserInfoDTO;
import com.bian.nwucommunication.dto.req.UserLoginReqDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService extends IService<UserInfo> {

    UserDTO login(UserLoginReqDTO userLoginDTO);

    UserDTO addInfo(UserInfoDTO userInfoDTO, MultipartFile file);

    void getCode(String email);

    UserDTO getUserByName(String name);
}
