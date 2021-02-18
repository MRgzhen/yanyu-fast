package com.github.mrgzhen.core.exception.support;

import lombok.Data;

import java.util.Date;

/**
 * @author yanyu
 */
@Data
public class ErrorResult {

    /** 错误路径 */
    private String path;

    /** 错误消息 */
    private String msg;

    /** 错误异常 */
    private String exception;

    /** 错误异常栈 */
    private String trace;

    /** 错误时间 */
    private Date timestamp;
}
