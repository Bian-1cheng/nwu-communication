package com.bian.nwucommunication.common.constant;

public class UserConstants {

    // 用户身份的常量
    public static final Integer ADMIN_IDENTIFY = 3;
    public static final Integer STUDENT_IDENTIFY = 1;

    // 用户上传的文件的状态
    // 0 --待审核  1--审核通过 3-- 审核不通过
    public static final Integer FILE_WAIT_CHECK = 0;
    public static final Integer FILE_HAVE_PASS = 1;
    public static final Integer FILE_NOT_PASS = 3;

    public static final Integer FILE_INIT_GREAT_NUM = 0;

    // 用户上传的头像压缩后的存储位置
    public static final String FILE_Resolution_PATH = "F:\\桌面\\";
    public static final Integer FILE_Resolution_HEIGHT = 100;
    public static final Integer FILE_Resolution_WIDTH = 100;


    // 消息中心
    public static final Integer is_NOTICE = 1;//已通知
    public static final Integer NOT_NOTICE = 0;// 未通知

    // 允许的文件格式
    public static final String[] ALLOW_TYPE = {"jpg", "jpeg", "png","pdf","mp3","mp4","word"};
    public static final String[] ALLOW_HEAD_IMG_TYPE = {"jpg", "jpeg", "png"};
}
