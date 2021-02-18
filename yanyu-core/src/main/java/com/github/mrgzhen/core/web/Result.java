package com.github.mrgzhen.core.web;

import com.github.mrgzhen.core.constant.AppConstant;
import com.github.mrgzhen.core.exception.GeneralException;
import com.github.mrgzhen.core.spring.AppEnv;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 统一响应
 * @author yanyu
 */
@Data
@ToString
public class Result<T> implements Serializable {

    private String code;

    private String msg;

    private T bean;

    private Result() {

    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T bean) {
        Result<T> result = new Result<>();
        result.code = AppConstant.SUCCESS_RESPONSE;
        result.msg = "操作成功";
        result.bean = bean;
        return result;
    }

    public static <T> Result<T> fail(String errorCode) {
        return fail(errorCode, null);
    }

    public static <T> Result<T> fail(String errorCode, T bean) {
        String message = AppEnv.getString(errorCode,"未知异常");
        return fail(errorCode, message, bean);
    }

    private static <T> Result<T> fail(String errorCode, String message, T bean) {
        Result<T> result = new Result<>();
        result.code = errorCode;
        result.msg = message;
        result.bean = bean;
        return result;
    }

    public static <T> Result<T> fail(GeneralException e) {
        return fail(e, null);
    }
    public static <T> Result<T> fail(GeneralException e, T bean) {
        Result<T> result = new Result<>();
        result.code = e.getErrorCode();
        result.msg = e.getMessage();
        result.bean = bean;
        return result;
    }
}
