package com.bian.nwucommunication.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bian.nwucommunication.common.errorcode.BaseErrorCode;
import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.common.result.Results;


import com.bian.nwucommunication.dao.UserInfo;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.dto.UserLoginDTO;


import com.bian.nwucommunication.service.UserService;
import com.bian.nwucommunication.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/fs")
public class UserInfoController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    @PostMapping("/addInfo")
    private Result<String> bindUserInfo(HttpServletRequest request,
                                        @RequestParam(value = "nick_name") String nick_name,
                                        @RequestParam(value = "school_name") String school_name,
                                        @RequestParam(value = "phoneNum") String phoneNum,
                                        @RequestParam(value = "email") String phone,
                                        @RequestParam(value = "id_card") String idCard,
                                        @RequestParam(value = "identification") int identification,
                                        @RequestParam(value = "file") MultipartFile file){
        System.out.println(nick_name);

        return Results.success("绑定成功");
    }



    @PostMapping("/login")
    private Result<?> login(@RequestBody UserLoginDTO userLoginDTO){

        UserDTO userDTO = userService.login(userLoginDTO);
        if(userDTO == null)
            return Results.failure(BaseErrorCode.USER_NAME_VERIFY_ERROR);
    //        redisTemplate.opsForValue().set("userLoginDTO",userLoginDTO);
        return Results.success(userDTO);
    }

    @GetMapping("/getMessage")
    private Result<String> getMessage(){
        System.out.println("userLoginDTO");
        return Results.success("获取成功");
    }

    @GetMapping("/test")
    private Result<String> test(){
        System.out.println("userLoginDTO");
        return Results.success("绑定成功");
    }
}
