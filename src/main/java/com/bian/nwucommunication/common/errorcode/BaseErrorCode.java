package com.bian.nwucommunication.common.errorcode;

public enum BaseErrorCode implements IErrorCode{
    // ========== 一级宏观错误码 客户端错误 ==========
    CLIENT_ERROR("A000001", "用户端错误"),

    // ========== 二级宏观错误码 用户注册错误 ==========
    USER_REGISTER_ERROR("A000100", "用户注册错误"),
    USER_NAME_VERIFY_ERROR("A000110", "用户名校验失败"),
    USER_NAME_EXIST_ERROR("A000111", "用户名已存在"),
    USER_NAME_SENSITIVE_ERROR("A000112", "用户名包含敏感词"),
    USER_NAME_SPECIAL_CHARACTER_ERROR("A000113", "用户名包含特殊字符"),


    EMAIL_NOT_EXIST_ERROR("A000113", "邮箱未注册"),
    EMAIL_NOT_EXIST("A000114", "邮箱不存在"),
    EMAIL_CODE_ERROR("A000115", "验证码错误"),
    PASSWORD_VERIFY_ERROR("A000120", "密码校验失败"),
    PASSWORD_SHORT_ERROR("A000121", "密码长度不够"),

    SENSITIVE_WORD_EXIST("A000130", "存在敏感词汇"),
    FILE_TYPE_ERROR("A000131", "文件格式不允许上传"),
    FILE_EMPTY_ERROR("A000131", "文件为空"),
    FILE_RESOLUTION_ERROR("A000132", "文件降低分辨率错误"),


    TOO_MANY_REQUESTS("A000130", "请求次数过多，请稍后访问"),

    // ========== 二级宏观错误码 系统请求缺少幂等Token ==========
    IDEMPOTENT_TOKEN_NULL_ERROR("A000200", "幂等Token为空"),
    IDEMPOTENT_TOKEN_DELETE_ERROR("A000201", "幂等Token已被使用或失效"),

    // ========== 一级宏观错误码 系统执行出错 ==========
    SERVICE_ERROR("B000001", "系统执行出错"),
    SERVICE_BOT_ERROR("B000002", "机器人未创建"),
    // ========== 二级宏观错误码 系统执行超时 ==========
    SERVICE_TIMEOUT_ERROR("B000100", "系统执行超时"),
    SERVICE_File_ERROR("B000101", "文件处理异常"),

    // ========== 一级宏观错误码 调用第三方服务出错 ==========
    REMOTE_ERROR("C000001", "调用第三方服务出错"),

    // ========== 非系统错误  ==========
    FILE_LIST_EMPTY("D000001","文件列表为空");


    private final String code;

    private final String message;

    BaseErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    ;

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
