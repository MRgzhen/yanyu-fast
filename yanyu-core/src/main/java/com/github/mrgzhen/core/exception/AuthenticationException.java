package com.github.mrgzhen.core.exception;

/**
 * @author yanyu
 */
public class AuthenticationException extends GeneralException {
    private static final String ERROR_CODE = "401";
    public AuthenticationException() {
        super(ERROR_CODE);
    }

    public AuthenticationException(String message) {
        super(ERROR_CODE, message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}
