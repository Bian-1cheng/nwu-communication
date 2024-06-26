package com.bian.nwucommunication.common.result;

import com.bian.nwucommunication.common.errorcode.BaseErrorCode;
import com.bian.nwucommunication.common.execption.AbstractException;

import java.util.Date;
import java.util.Optional;

public final class Results {

    /**
     * 构造成功响应
     */
    public static Result<Void> success() {
        return new Result<Void>()
                .setCode(Result.SUCCESS_CODE)
                .setDataTime(new Date());
    }

    /**
     * 构造带返回数据的成功响应
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>()
                .setCode(Result.SUCCESS_CODE)
                .setData(data)
                .setDataTime(new Date());
    }

    /**
     * 构造带返回数据的成功响应
     */
    public static <T> Result<T> success(T data,String message) {
        return new Result<T>()
                .setCode(Result.SUCCESS_CODE)
                .setData(data)
                .setMessage(message)
                .setDataTime(new Date());
    }

    /**
     * 构造带返回数据的成功响应
     */
    public static <T> Result<T> success(String message) {
        return new Result<T>()
                .setCode(Result.SUCCESS_CODE)
                .setMessage(message)
                .setDataTime(new Date());
    }

    /**
     * 构建服务端失败响应
     */
    public static Result<Void> failure() {
        return new Result<Void>()
                .setCode(BaseErrorCode.SERVICE_ERROR.code())
                .setMessage(BaseErrorCode.SERVICE_ERROR.message())
                .setDataTime(new Date());
    }

    /**
     * 通过 {@link AbstractException} 构建失败响应
     */
    public static Result<Void> failure(AbstractException abstractException) {
        String errorCode = Optional.ofNullable(abstractException.getErrorCode())
                .orElse(BaseErrorCode.SERVICE_ERROR.code());
        String errorMessage = Optional.ofNullable(abstractException.getErrorMessage())
                .orElse(BaseErrorCode.SERVICE_ERROR.message());
        return new Result<Void>()
                .setCode(errorCode)
                .setMessage(errorMessage);
    }

    /**
     * 通过 errorCode、errorMessage 构建失败响应
     */


    /**
     * 通过 errorCode、errorMessage 构建失败响应
     */
    public static Result<Void> failure(String errorCode, String errorMessage) {
        return new Result<Void>()
                .setCode(errorCode)
                .setMessage(errorMessage)
                .setDataTime(new Date());
    }

    public static <T> Result<T> failure(BaseErrorCode baseErrorCode) {
        return new Result<T>()
                .setCode(baseErrorCode.code())
                .setMessage(baseErrorCode.message())
                .setDataTime(new Date());
    }
}
