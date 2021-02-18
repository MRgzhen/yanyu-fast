package com.github.mrgzhen.core.exception.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author yanyu
 */
@Slf4j
public class GeneralExceptionPropertySource extends PropertySource {
    public static final String EXCEPTION_DEFAULT_RESOURCE_PATH = "/exception/default-error.properties";
    private static final String EXCEPTION_CUSTOM_RESOURCE_PATH = "classpath*:exception/error.properties";
    private Properties properties = new Properties();
    private PathMatchingResourcePatternResolver pmrpResolver = new PathMatchingResourcePatternResolver();
    public GeneralExceptionPropertySource(String name) throws IOException {
        super(name);
        // 加载系统默认错误
        loadDefault();
        // 加载自定义错误
        loadCustom();
    }

    private void loadDefault() throws IOException {
        tryResourceSafely(getClass().getResourceAsStream(EXCEPTION_DEFAULT_RESOURCE_PATH));
    }

    private void loadCustom() throws IOException {
        Resource[] resources = pmrpResolver.getResources(EXCEPTION_CUSTOM_RESOURCE_PATH);
        for(Resource resource : resources) {
            tryResourceSafely(resource.getInputStream());
        }
    }

    private void tryResourceSafely(InputStream inputStream) throws IOException {
        try {
            if (inputStream != null) {
                this.properties.load(new InputStreamReader(inputStream,  "gbk"));
            }
        } catch (IOException e) {
            log.warn("加载错误码映射文件错误", e);
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
        }
    }

    @Override
    public Object getProperty(String name) {
        return this.properties.get(name);
    }
}
