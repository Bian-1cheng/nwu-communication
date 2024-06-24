package com.bian.nwucommunication.controller;



import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bian.nwucommunication.common.errorcode.BaseErrorCode;
import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.common.result.Results;


import com.bian.nwucommunication.dao.FileInfo;
import com.bian.nwucommunication.dao.Notice;
import com.bian.nwucommunication.dao.UserInfo;
import com.bian.nwucommunication.dto.UserDTO;
import com.bian.nwucommunication.dto.UserInfoDTO;
import com.bian.nwucommunication.dto.UserLoginDTO;


import com.bian.nwucommunication.mapper.NoticeMapper;
import com.bian.nwucommunication.mapper.UserMapper;
import com.bian.nwucommunication.service.FileInfoService;
import com.bian.nwucommunication.service.UserService;
import com.bian.nwucommunication.service.impl.UserServiceImpl;
import com.bian.nwucommunication.util.UserHolder;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/fs")
public class UserInfoController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private FileInfoService fileInfoService;

    @Resource
    private NoticeMapper noticeMapper;

    @PostMapping("/addinfo")
    private Result<?> bindUserInfo(HttpServletRequest request,
                                        @RequestParam(value = "nick_name") String nickName,
                                        @RequestParam(value = "school_name") String schoolName,
                                        @RequestParam(value = "email") String email,
                                        @RequestParam(value = "identification") int identification,
                                        @RequestParam(value = "code") String code,
                                        @RequestParam(value = "file") MultipartFile file){
        UserInfoDTO userInfoDTO = new UserInfoDTO(nickName, schoolName, email, identification,null, null,code);
        UserDTO userDTO = userService.addInfo(userInfoDTO,file);
        return Results.success(userDTO);
    }



    @PostMapping("/login")
    private Result<?> login(@RequestBody UserLoginDTO userLoginDTO){
        UserDTO userDTO = userService.login(userLoginDTO);
        return Results.success(userDTO,"登录成功");
    }

    @GetMapping("/message")
    private Result<?> getMessage(){
        UserDTO user = UserHolder.getUser();
        List<Notice> notices = noticeMapper.selectList(new QueryWrapper<Notice>().eq("user_id", user.getId()));
        if(CollUtil.isEmpty(notices))
            return Results.success("没有新消息");
        return Results.success(notices);
    }

    @GetMapping("/fileuser/{id}")
    private Result<?> getFileUser(@PathVariable("id") long id){
        FileInfo fileInfo = fileInfoService.getById(id);
        UserInfo userInfo = userMapper.selectOne(new QueryWrapper<UserInfo>().eq("id", fileInfo.getUserId()));
        UserDTO userDTO = BeanUtil.toBean(userInfo, UserDTO.class);
        return Results.success(userDTO);
    }

    @GetMapping("/user")
    private Result<?> getUser(){
        return Results.success(UserHolder.getUser());
    }

    @PostMapping("/code")
    private Result<String> getCode(@RequestParam(value = "email") String email){
        return Results.success(userService.getCode(email));
    }

    @GetMapping("/test")
    private Result<String> test(){
        System.out.println("userLoginDTO");
        return Results.success("绑定成功");
    }
}
