package com.github.mrgzhen.security.switchUser.filter;

import com.github.mrgzhen.core.util.JSONUtil;
import com.github.mrgzhen.security.switchUser.matcher.SwitchUserRequestMatcher;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 切换用户
 * @author yanyu
 */
@Setter
@Slf4j
public class SwitchFormContentFilter extends OncePerRequestFilter implements InitializingBean {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private String usernameParameter = SwitchUserFilter.SPRING_SECURITY_SWITCH_USERNAME_KEY;
    private String tenantCodeParameter = SwitchUserFilter.SPRING_SECURITY_SWITCH_TENANT_KEY;
    private Charset charset = DEFAULT_CHARSET;
    private SwitchUserRequestMatcher switchUserRequestMatcher;

    public SwitchFormContentFilter(SwitchUserRequestMatcher switchUserRequestMatcher) {
        this.switchUserRequestMatcher = switchUserRequestMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (!requiresConvert(request, response)) {
            chain.doFilter(request, response);
            return;
        }

        HttpInputMessage inputMessage = new ServletServerHttpRequest(request) {
            @Override
            public InputStream getBody() throws IOException {
                return request.getInputStream();
            }
        };
        MultiValueMap<String, String> formParameters = read(inputMessage);
        if (!formParameters.isEmpty()) {
            HttpServletRequest wrapper = new SwitchFormContentFilter.FormContentRequestWrapper(request, formParameters);
            chain.doFilter(wrapper, response);
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * 是否要求转换
     */
    protected boolean requiresConvert(HttpServletRequest request,
                                             HttpServletResponse response) {
        String contentType = request.getContentType();
        return (switchUserRequestMatcher.requiresSwitchUser(request) &&
                org.apache.commons.lang3.StringUtils.containsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE, contentType));
    }

    public MultiValueMap<String, String> read(HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        Map<String,String> body = JSONUtil.instant().readValue(inputMessage.getBody(),Map.class);
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
        if(MapUtils.isNotEmpty(body)) {
            result.add(usernameParameter, body.get(usernameParameter));
            result.add(tenantCodeParameter, body.get(tenantCodeParameter));
        }
        return result;
    }

    private static class FormContentRequestWrapper extends HttpServletRequestWrapper {

        private MultiValueMap<String, String> formParameters;

        public FormContentRequestWrapper(HttpServletRequest request, MultiValueMap<String, String> parameters) {
            super(request);
            this.formParameters = parameters;
        }

        @Override
        @Nullable
        public String getParameter(String name) {
            String queryStringValue = super.getParameter(name);
            String formValue = this.formParameters.getFirst(name);
            return (queryStringValue != null ? queryStringValue : formValue);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> result = new LinkedHashMap<>();
            Enumeration<String> names = getParameterNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                result.put(name, getParameterValues(name));
            }
            return result;
        }

        @Override
        public Enumeration<String> getParameterNames() {
            Set<String> names = new LinkedHashSet<>();
            names.addAll(Collections.list(super.getParameterNames()));
            names.addAll(this.formParameters.keySet());
            return Collections.enumeration(names);
        }

        @Override
        @Nullable
        public String[] getParameterValues(String name) {
            String[] parameterValues = super.getParameterValues(name);
            List<String> formParam = this.formParameters.get(name);
            if (formParam == null) {
                return parameterValues;
            }
            if (parameterValues == null || getQueryString() == null) {
                return StringUtils.toStringArray(formParam);
            } else {
                List<String> result = new ArrayList<>(parameterValues.length + formParam.size());
                result.addAll(Arrays.asList(parameterValues));
                result.addAll(formParam);
                return StringUtils.toStringArray(result);
            }
        }
    }
}
