package com.github.mrgzhen.gateway;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yanyu
 */
@Data
@ConfigurationProperties(prefix = "yanyu.gateway")
public class YanyuGatewayProperties {

    private Swagger swagger = new Swagger();

    @Data
    public class Swagger {
        /** 当locator配置为true时，将扫描服务生成swagger路由规则 */
        private String[] serviceIds;
    }

}
