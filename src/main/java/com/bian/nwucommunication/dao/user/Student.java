package com.bian.nwucommunication.dao.user;




import lombok.Data;

import java.io.Serializable;
@Data
public class Student implements Serializable{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String stu_no;
    private String class_no;
    //已完成的课程
    private String finished_course;
    private String major;
    private String college;
    private Integer is_deleted;


}
