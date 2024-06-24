package com.bian.nwucommunication.util.constant;

public class UserConstants {

    // 用户身份的常量
    public static final Integer ADMIN_IDENTIFY = 3;
    public static final Integer STUDENT_IDENTIFY = 3;

    // 用户上传的文件的状态
    // 0 --待审核  1--审核通过 3-- 审核不通过
    public static final Integer FILE_WAIT_CHECK = 0;
    public static final Integer FILE_HAVE_PASS = 1;
    public static final Integer FILE_NOT_PASS = 3;
}
