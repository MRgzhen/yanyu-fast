package com.github.mrgzhen.gateway.exception;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.server.ServerRequest;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author yanyu
 */
@AllArgsConstructor
public class GeneralErrorAttributesResolver {

    private final ErrorAttributes errorAttributes;
    private final ErrorProperties errorProperties;

    public Map<String, Object> getErrorAttributes(ServerRequest request) {
        return this.errorAttributes.getErrorAttributes(request, getErrorAttributeOptions(request));
    }

    public Map<String, Object> getAllErrorAttributes(ServerRequest request) {
        return this.errorAttributes.getErrorAttributes(request, true);
    }

    protected ErrorAttributeOptions getErrorAttributeOptions(ServerRequest request) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        if (this.errorProperties.isIncludeException()) {
            options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
        }
        if (isIncludeStackTrace(request,  MediaType.ALL)) {
            options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        if (isIncludeMessage(request,  MediaType.ALL)) {
            options = options.including(ErrorAttributeOptions.Include.MESSAGE);
        }
        if (isIncludeBindingErrors(request,  MediaType.ALL)) {
            options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        }
        return options;
    }

    protected boolean isIncludeStackTrace(ServerRequest request, MediaType produces) {
        switch (this.errorProperties.getIncludeStacktrace()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
            case ON_TRACE_PARAM:
                return isTraceEnabled(request);
            default:
                return false;
        }
    }

    protected boolean isIncludeMessage(ServerRequest request, MediaType produces) {
        switch (this.errorProperties.getIncludeMessage()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return isMessageEnabled(request);
            default:
                return false;
        }
    }

    protected boolean isIncludeBindingErrors(ServerRequest request, MediaType produces) {
        switch (this.errorProperties.getIncludeBindingErrors()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return isBindingErrorsEnabled(request);
            default:
                return false;
        }
    }

    protected boolean isTraceEnabled(ServerRequest request) {
        return getBooleanParameter(request, "trace");
    }

    protected boolean isMessageEnabled(ServerRequest request) {
        return getBooleanParameter(request, "message");
    }

    protected boolean isBindingErrorsEnabled(ServerRequest request) {
        return getBooleanParameter(request, "errors");
    }

    private boolean getBooleanParameter(ServerRequest request, String parameterName) {
        String parameter = request.queryParam(parameterName).orElse("false");
        return !"false".equalsIgnoreCase(parameter);
    }
}
