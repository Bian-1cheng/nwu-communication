package com.bian.nwucommunication.controller;


import com.bian.nwucommunication.common.result.Result;
import com.bian.nwucommunication.common.result.Results;


import com.bian.nwucommunication.dto.UserLoginDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/fs")
public class UserInfoController {

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
        System.out.println(file);

        return Results.success("绑定成功");
    }



    @PostMapping("/login")
    private Result<String> login(@RequestBody UserLoginDTO userLoginDTO){
        System.out.println(userLoginDTO);
        return Results.success("绑定成功");
    }



    @GetMapping("/test")
    private Result<String> test(){
        System.out.println("userLoginDTO");
        return Results.success("绑定成功");
    }
}
