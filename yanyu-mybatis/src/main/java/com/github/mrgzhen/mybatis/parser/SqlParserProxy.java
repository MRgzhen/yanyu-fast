package com.github.mrgzhen.mybatis.parser;

import com.github.mrgzhen.core.util.ClassUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

@Aspect
public class SqlParserProxy {

    @Pointcut("@annotation(com.github.mrgzhen.mybatis.parser.SqlParser)")
    public void sqlParserPointcut() {
    }

    @Around(value = "sqlParserPointcut()")
    public Object arround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object target = joinPoint.getTarget();
        SqlParser sqlParser = AnnotationUtils.findAnnotation(target.getClass(), SqlParser.class);
        if(sqlParser == null) {
            Method method = signature.getMethod();
            sqlParser = AnnotationUtils.findAnnotation(method, SqlParser.class);
            SqlParserHelper.setSqlParserAttributeHolder(sqlParser, ClassUtil.methodName(method));
        }
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            throw e;
        } finally {
            // 清空缓存
            SqlParserHelper.removeSqlParserAttributeHolder();
        }
        return result;
    }

}
