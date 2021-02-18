package com.github.mrgzhen.core.util;

import com.github.mrgzhen.core.exception.GeneralException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.asm.ClassReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author yanyu
 */
public class ClassUtil {

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    public static Set<Class<?>> scanPackage(List<String> packageNames) {
        Set<Class<?>> packages = new LinkedHashSet<>();
        for(String packageName:packageNames) {
            packages.addAll(scanPackage(packageName));
        }
        return packages;
    }

    public static Set<Class<?>> scanPackage(String packageName) {

        Set<Class<?>> classes = new LinkedHashSet<>();
        try {
            String packageSearchPath = "classpath*:" + ClassUtils.convertClassNameToResourcePath(packageName) + '/' + DEFAULT_RESOURCE_PATTERN;
            PathMatchingResourcePatternResolver pmrp = new PathMatchingResourcePatternResolver();
            Resource[] resources = pmrp.getResources(StringUtils.join("classpath:",
                    StringUtils.replace(packageName,".","/"),"/**/*.class"));
            for(Resource resource: resources) {
                ClassReader red  = new ClassReader(resource.getInputStream());
                String className = ClassUtils.convertResourcePathToClassName(red.getClassName());
                Class<?> clazz = Class.forName(className);
                classes.add(clazz);
            }
        } catch (Exception e) {
            throw new GeneralException("通过包名获取类信息异常",e);
        }
        return classes;
    }

    public static String methodName(Method method) {
        return StringUtils.join(method.getDeclaringClass().getName(),".",method.getName());
    }
}
