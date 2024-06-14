package com.bian.nwucommunication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoDTO {

    private String nickName;

    private String schoolName;

    private String email;

    private Integer identification;

    private String headImg;

    private String password;

    private String code;
}
