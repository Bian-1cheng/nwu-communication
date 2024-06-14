package com.bian.nwucommunication.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class UserDTO {

    private Integer id;

    private Integer schoolId;

    private String headImg;

    private Integer identification;

    private String nickName;

    private String schoolName;

    private String phone;

    private String email;

    private String token;

}
