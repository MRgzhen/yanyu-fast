package com.github.mrgzhen.core.exception.handler;

import com.github.mrgzhen.core.exception.support.ErrorResult;
import com.github.mrgzhen.core.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author yanyu
 */
@Controller
@RequestMapping("/error")
@Slf4j
public class GeneralErrorController implements ErrorController {

    private GeneralErrorAttributesResolver errorAttributesResolver;
    public GeneralErrorController(GeneralErrorAttributesResolver errorAttributesResolver) {
        this.errorAttributesResolver = errorAttributesResolver;
    }

    @RequestMapping
    public ResponseEntity<Result> error(HttpServletRequest request) {
        ErrorResult errorResult = errorAttributesResolver.getErrorAttributes(request);
        HttpStatus status = getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>(status);
        }
        log.warn("状态码：[{}], 异常明细:[{}]", status.value(), errorAttributesResolver.getAllErrorAttributes(request));
        return ResponseEntity.status(status).body(Result.fail(String.valueOf(status.value()), errorResult));
    }

    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        }
        catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
