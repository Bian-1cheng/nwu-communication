package com.bian.nwucommunication.dao.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class User implements Serializable {

    private Integer id;
    //父编号
    private Integer pid;
    //身份编号
    private Integer role_id;
    private String username;
    private Integer sex;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmt_create;
    private String is_blocked;
    private String phone;
    private String email;
    //头像
    private String headDir;
    private byte[] head;
    //身份识别码
    private String identity_no;
    private String password;
    private Integer is_online;
    private Integer is_deleted;

    private Serializable token;

    private String major;
    private String class_no;

    private Integer online_devices;

    private String belong_department;

}
