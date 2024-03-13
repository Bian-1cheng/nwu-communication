package com.bian.nwucommunication.service;

import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.dto.UserDTO;

public interface UserService {
    Result login(UserDTO userDTO);
}
