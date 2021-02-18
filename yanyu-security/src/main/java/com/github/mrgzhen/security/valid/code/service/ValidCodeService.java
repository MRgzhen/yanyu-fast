package com.github.mrgzhen.security.valid.code.service;

import com.github.mrgzhen.security.valid.code.ValidCodeType;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yanyu
 */
public interface ValidCodeService {

    void generatorAndSend(ServletWebRequest request);

    void validCode(ServletWebRequest request);

    ValidCodeType name();
}
