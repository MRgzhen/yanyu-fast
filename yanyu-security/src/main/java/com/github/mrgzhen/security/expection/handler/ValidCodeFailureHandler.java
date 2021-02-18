package com.github.mrgzhen.security.expection.handler;

import com.github.mrgzhen.core.exception.GeneralException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yanyu
 */
public interface ValidCodeFailureHandler {

    void onAuthenticationFailure(HttpServletRequest request,
                                 HttpServletResponse response, GeneralException exception)
            throws IOException, ServletException;
}
