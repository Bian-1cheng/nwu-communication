package com.bian.nwucommunication.service.impl;

import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.common.result.Results;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public Result login(UserDTO userDTO) {
        System.out.println(userDTO.getIdentity_no());
        return Results.success(userDTO);
    }
}


