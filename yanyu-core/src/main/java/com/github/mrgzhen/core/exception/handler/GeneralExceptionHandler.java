package com.github.mrgzhen.core.exception.handler;

import cn.hutool.core.util.ObjectUtil;
import com.github.mrgzhen.core.exception.*;
import com.github.mrgzhen.core.web.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 统一异常处理
 * @author yanyu
 */
@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class GeneralExceptionHandler {

    @Autowired
    private GeneralErrorAttributesResolver errorAttributesResolver;

    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出该异常 public Result test3( @RequestParam @NotEmpty String name)
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result handlerConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        Map<String,String> errMap = new HashMap<>();
        constraintViolations.forEach(constraintViolation -> {
            errMap.put(ObjectUtil.toString(constraintViolation.getPropertyPath()), constraintViolation.getMessage());
        });
        log.error("[{}异常：{}]",e.getClass(), e.getMessage(),e);
        return Result.fail(new ParamException(), errMap);
    }

    /**
     * BindException异常：处理form表单提交 在实体类使用前@validate 验证路径中请求实体校验失败后抛出该异常 public Result test2( @Validated Student student)
     * MethodArgumentNotValidException异常：处理请求参数格式错误 @RequestBody上validate失败后抛出该异常。public Result test1(@RequestBody @Validated Student student)
     */
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public Result handleBindException(Exception e, HttpServletRequest request) {
        log.error("[{}异常：{}]",e.getClass(),e.getMessage(),e);
        String errorResult = errorAttributesResolver.getBindingResultErrorMessage(e);
        return Result.fail(new ParamException());
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(GeneralException.class)
    public Result handleGeneralException(GeneralException e, HttpServletRequest request) {
        log.error("[{}异常:{}]",e.getClass(),e.getMessage(),e);
        String errorResult = errorAttributesResolver.getBindingResultErrorMessage(e);
        return Result.fail(e, errorResult);
    }

    /**
     * 其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e, HttpServletRequest request) {
        log.error("[{}异常:{}]",e.getClass(),e.getMessage(),e);
        return Result.fail(new GeneralException("500"));
    }
}
