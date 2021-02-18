package com.github.mrgzhen.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yanyu
 */
@Data
@ConfigurationProperties(prefix = "yanyu.swagger")
public class SwaggerProperties {

    /** 标题 */
    private String title = "yanyu";

    /** 描述 */
    private String description = "yanyu平台";

    /** 版本号 */
    private String version = "1.0";

    /** 协议 */
    private String license = "Apache2.0";

    /** 协议地址 */
    private String licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0";

    /** 扫描的包名 */
    private String basePackage;

    /** 创建用户信息 */
    private Contact contact = new Contact();

    @Data
    public class Contact {
        /** 作者 */
        private String name = "yanyu";

        /** 地址 */
        private String url = "#";

        /** 邮箱 */
        private String email = "##@qq.com";

    }
}
