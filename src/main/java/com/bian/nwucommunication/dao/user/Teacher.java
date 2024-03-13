package com.bian.nwucommunication.dao.user;




import lombok.Data;

import java.io.Serializable;
@Data
public class Teacher implements Serializable{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String teacher_no;
    //所属单位
    private String belong_department;
    //执教课程
    private String tutor_course;
    //执教班级
    private String tutor_class;
    //已完成教学班
    private String finished_class;
    //教师职称
    private String teacher_title;
    //个人介绍
    private String self_introduce;
    private Integer is_deleted;


}
