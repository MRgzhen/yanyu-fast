package com.github.mrgzhen.security.expection;

import com.github.mrgzhen.core.exception.AuthenticationException;
import com.github.mrgzhen.core.exception.GeneralException;

/**
 * @author yanyu
 */
public class ValidCodeException extends AuthenticationException {

    public ValidCodeException() {
        super();
    }

    public ValidCodeException(String message) {
        super(message);
    }

    public ValidCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidCodeException(Throwable cause) {
        super(cause);
    }
}