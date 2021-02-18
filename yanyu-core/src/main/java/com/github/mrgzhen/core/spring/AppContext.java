package com.github.mrgzhen.core.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * 获取ApplicationContext对象
 * @author yanyu
 */
@Configuration
public class AppContext implements ApplicationContextAware {
    private static ApplicationContext _applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        AppContext._applicationContext = applicationContext;
        AppEnv._sharedEnv = applicationContext.getEnvironment();
    }

    public static <T> T getBean(Class<T> clazz) {
        return _applicationContext.getBean(clazz);
    }

    public static Object getBean(String beanName) {
        return _applicationContext.getBean(beanName);
    }
}
