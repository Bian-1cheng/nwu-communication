package com.bian.nwucommunication.controller;

import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.dao.user.UserInfo;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
public class UserController {
    @Resource
    private UserService userService;

    // TODO 实现单点登录
    @PostMapping("/login")
    public Result<UserInfo> Login(@RequestBody UserDTO userDTO){
        return userService.login(userDTO);
    }
}
