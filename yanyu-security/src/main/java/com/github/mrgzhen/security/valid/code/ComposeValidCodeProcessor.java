package com.github.mrgzhen.security.valid.code;

import com.github.mrgzhen.security.valid.code.service.ValidCodeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author yanyu
 */
@Slf4j
@AllArgsConstructor
public class ComposeValidCodeProcessor{

    private List<ValidCodeService> validCodeProcessors;

    public ValidCodeService getInstance(ValidCodeType type) {
        for(ValidCodeService validCodeProcessor : validCodeProcessors) {
            if(validCodeProcessor.name().equals(type)) {
                return validCodeProcessor;
            }
        }
        return null;
    }

}
