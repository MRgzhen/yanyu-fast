package com.github.mrgzhen.core.exception;

public class MybatisParserException extends GeneralException {

    private static final String ERROR_CODE = "500";

    public MybatisParserException() {
        super(ERROR_CODE);
    }

    public MybatisParserException(String message) {
        super(ERROR_CODE, message);
    }

    public MybatisParserException(String message, Throwable cause) {
        super(ERROR_CODE, message, cause);
    }

    public MybatisParserException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}
