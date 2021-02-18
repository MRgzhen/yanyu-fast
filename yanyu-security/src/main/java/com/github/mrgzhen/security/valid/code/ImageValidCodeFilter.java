package com.github.mrgzhen.security.valid.code;

import com.github.mrgzhen.security.SecurityProperties;
import com.github.mrgzhen.security.expection.handler.ValidCodeFailureHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author yanyu
 */
public class ImageValidCodeFilter extends AbstractValidCodeFilter{
    private static final ValidCodeType validCodeType = ValidCodeType.IMAGE;
    private final SecurityProperties properties;
    public ImageValidCodeFilter(SecurityProperties properties, ComposeValidCodeProcessor validCodeProcessor, ValidCodeFailureHandler validCodeFailureHandler) {
        super(properties.getAuth().getLoginUri(), properties.getAuth().getImageCodeUri(), validCodeProcessor.getInstance(validCodeType), validCodeFailureHandler);
        this.properties = properties;
    }

    @Override
    protected boolean enabled() {
        return properties.getAuth().isImageCodeEnabled();
    }
}