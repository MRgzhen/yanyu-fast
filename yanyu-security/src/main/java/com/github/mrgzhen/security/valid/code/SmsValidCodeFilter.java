package com.github.mrgzhen.security.valid.code;

import com.github.mrgzhen.security.SecurityProperties;
import com.github.mrgzhen.security.expection.handler.ValidCodeFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.service.GrantType;

/**
 * @author yanyu
 */
public class SmsValidCodeFilter extends AbstractValidCodeFilter{
    private static final ValidCodeType validCodeType = ValidCodeType.SMS;
    private final SecurityProperties properties;
    public SmsValidCodeFilter(SecurityProperties properties, ComposeValidCodeProcessor validCodeProcessor, ValidCodeFailureHandler validCodeFailureHandler) {
        super(properties.getAuth().getSmsLoginUri(), properties.getAuth().getSmsCodeUri(), validCodeProcessor.getInstance(validCodeType), validCodeFailureHandler);
        this.properties = properties;
    }

    @Override
    protected boolean enabled() {
        return properties.getAuth().isSmsCodeEnabled();
    }
}
