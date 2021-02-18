package com.github.mrgzhen.security.config;


import com.github.mrgzhen.security.tokenService.CustomUserInfoTokenServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author yanyu
 */
@Configuration
public class CustomResourceServerTokenServiceConfiguration {

    @Configuration
    @AllArgsConstructor
    public static class UserInfoTokenServicesBeanPostProcessor implements InstantiationAwareBeanPostProcessor, ApplicationContextAware {
        private ApplicationContext applicationContext;
        @Override
        public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
            if(beanClass == UserInfoTokenServices.class){
                ResourceServerProperties sso = applicationContext.getBean(ResourceServerProperties.class);
                return new CustomUserInfoTokenServices(sso.getUserInfoUri(),"") ;
            }
          return null ;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }
    }
}
