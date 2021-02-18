package com.github.mrgzhen.core.exception;

/**
 * @author yanyu
 */
public class ParamException extends GeneralException {
    private static final String ERROR_CODE = "400";
    public ParamException() {
        super(ERROR_CODE);
    }

    public ParamException(String message) {
        super(ERROR_CODE, message);
    }

    public ParamException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }

    public ParamException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}