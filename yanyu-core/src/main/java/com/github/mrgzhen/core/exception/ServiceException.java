package com.github.mrgzhen.core.exception;

/**
 * @author yanyu
 */
public class ServiceException extends GeneralException {

    private static final String ERROR_CODE = "500";

    public ServiceException() {
        super(ERROR_CODE);
    }

    public ServiceException(String message) {
        super(ERROR_CODE, message);
    }

    public ServiceException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }

    public ServiceException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}
