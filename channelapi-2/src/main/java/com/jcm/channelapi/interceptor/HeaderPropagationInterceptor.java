package com.jcm.channelapi.interceptor;

import com.jcm.common.context.UserContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class HeaderPropagationInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(HeaderPropagationInterceptor.class);

    @Override
    @NonNull
    public ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body, @NonNull ClientHttpRequestExecution execution) throws IOException {
        String userId = UserContext.getUserId();
        String sessionId = UserContext.getSessionId();

        logger.info("HeaderPropagationInterceptor - Propagating headers: X-User-Id={} | X-Session-Id={}", userId, sessionId);

        if (userId != null) {
            request.getHeaders().add("X-User-Id", userId);
        }
        if (sessionId != null) {
            request.getHeaders().add("X-Session-Id", sessionId);
        }
        return execution.execute(request, body);
    }
}
