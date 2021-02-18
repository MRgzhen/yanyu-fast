package com.github.mrgzhen.core.exception;


import com.github.mrgzhen.core.spring.AppEnv;
import org.apache.commons.lang3.StringUtils;

/**
 * 基础异常类
 * @author yanyu
 */
public class GeneralException extends RuntimeException{

    private String errorCode;

    public GeneralException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public GeneralException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public GeneralException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public GeneralException(String errorCode, Throwable cause) {
        this(errorCode, errorCode, cause);
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getMessage() {
        String message = super.getMessage();
        if (StringUtils.isNoneBlank(message) &&  StringUtils.isNoneBlank(errorCode) && !this.errorCode.equals(message)) {
            return message;
        } else if (AppEnv.isLoaded()) {
            message = AppEnv.getString(getErrorCode(), null);
        }
        return message;
    }
}
