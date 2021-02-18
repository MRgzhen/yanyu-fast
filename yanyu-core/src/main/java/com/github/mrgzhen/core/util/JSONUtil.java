package com.github.mrgzhen.core.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mrgzhen.core.spring.AppContext;

/**
 * @author yanyu
 */
public class JSONUtil {

    private static final ObjectMapper defaultObjectMapper = AppContext.getBean(ObjectMapper.class);
    public static ObjectMapper instant() {
        return instant(false);
    }
    public static ObjectMapper instant(boolean isNew) {
        if(isNew || defaultObjectMapper == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            /** 忽略未知的属性 */
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
            return objectMapper;
        } else {
            return defaultObjectMapper;
        }
    }

}