package com.jcm.common.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserContextInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UserContextInterceptor.class);

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String userId = request.getHeader("X-User-Id");
        String sessionId = request.getHeader("X-Session-Id");

        logger.info("UserContextInterceptor - Incoming Request: URI={} | X-User-Id={} | X-Session-Id={}", request.getRequestURI(), userId, sessionId);

        if (userId != null) {
            UserContext.setUserId(userId);
            logger.info("UserContextInterceptor - Setting UserContext userId: {}", userId);
        }
        if (sessionId != null) {
            UserContext.setSessionId(sessionId);
            logger.info("UserContextInterceptor - Setting UserContext sessionId: {}", sessionId);
        }
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        // Not used in this scenario
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) throws Exception {
        logger.info("UserContextInterceptor - Clearing UserContext after completion.");
        UserContext.clear(); // Clear context after request completion
    }
}
