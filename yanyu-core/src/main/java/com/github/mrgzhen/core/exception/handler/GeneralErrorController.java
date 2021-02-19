package com.github.mrgzhen.core.exception.handler;

import com.github.mrgzhen.core.exception.GeneralException;
import com.github.mrgzhen.core.util.StringUtil;
import com.github.mrgzhen.core.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
        HttpStatus status = errorAttributesResolver.getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return ResponseEntity.ok(Result.fail(new GeneralException(String.valueOf(status.value()))));
        }
        String errorMsg = errorAttributesResolver.getMessage(request);
        log.warn("状态码：[{}], 当前请求路径：[{}], 异常明细:[{}]", status.value(), request.getRequestURI(), StringUtils.defaultString(errorMsg, ""));
        if(StringUtils.isBlank(errorMsg)) {
            return ResponseEntity.ok(Result.fail(new GeneralException(String.valueOf(status.value()), errorMsg)));
        } else {
            return ResponseEntity.ok(Result.fail(new GeneralException(String.valueOf(status.value()))));
        }
    }


    @Override
    public String getErrorPath() {
        return null;
    }
}
