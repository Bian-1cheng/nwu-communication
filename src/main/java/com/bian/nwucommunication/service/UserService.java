package com.bian.nwucommunication.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bian.nwucommunication.dao.UserInfo;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.dto.UserLoginDTO;
import org.springframework.stereotype.Service;


public interface UserService extends IService {

    UserDTO login(UserLoginDTO userLoginDTO);
}
