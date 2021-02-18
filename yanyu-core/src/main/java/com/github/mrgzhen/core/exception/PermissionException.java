package com.github.mrgzhen.core.exception;

/**
 * @author yanyu
 */
public class PermissionException extends GeneralException {
    private static final String ERROR_CODE = "403";
    public PermissionException() {
        super(ERROR_CODE);
    }

    public PermissionException(String message) {
        super(ERROR_CODE, message);
    }

    public PermissionException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }

    public PermissionException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}