package com.github.mrgzhen.security.switchUser.configure;

import com.github.mrgzhen.security.config.CustomAuthorizationServerSecurityConfiguration;
import com.github.mrgzhen.security.config.EnableYanyuAuthorizationServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.type.AnnotationMetadata;

import java.util.LinkedHashMap;

/**
 * @author yanyu
 */
@Configuration
@ConditionalOnBean(CustomAuthorizationServerSecurityConfiguration.class)
public class EnableYanyuSwitchSecurityImportSelector implements DeferredImportSelector, EnvironmentAware {

    private Class annotationClass = EnableYanyuAuthorizationServer.class;
    private Environment env;

    @Override
    public String[] selectImports(AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                metadata.getAnnotationAttributes(this.annotationClass.getName(), true));
        if(attributes.getBoolean("switchUser")) {
            setSwitUserEnabled(true);
            return new String[]{"com.github.mrgzhen.security.switchUser.configure.SkySwitchSecurityConfigurer"};
        }
        setSwitUserEnabled(false);
        return new String[]{};
    }

    private void setSwitUserEnabled(Boolean enabled) {
        if (ConfigurableEnvironment.class.isInstance(env)) {
            ConfigurableEnvironment configEnv = (ConfigurableEnvironment) env;
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("yanyu.security.switch-user.enabled", enabled);
            MapPropertySource propertySource = new MapPropertySource(
                    "skySecurityProperties", map);
            configEnv.getPropertySources().addLast(propertySource);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}