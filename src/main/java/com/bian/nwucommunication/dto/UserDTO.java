package com.bian.nwucommunication.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class UserDTO {

    private File file;

    private Integer identification;

    private String nick_name;

    private String school_name;

    private String phoneNum;

    private String phone;

    private String email;

}
