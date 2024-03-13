package com.bian.nwucommunication.dao.user;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer role_id;
    //学生信息
    private String stu_no;
    private String username;
    private String class_no;
    private String college;
    private String major;
    //教师信息
    private String belong_department;
    private String tutor_course;
    private String tutor_class;
    private String finished_class;
    private String teacher_title;
    private String self_introduce;
    //管理员信息
    private Integer level;
    private String work_no;

}
