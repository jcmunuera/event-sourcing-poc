package com.jcm.common.context;

public class UserContext {

    private static final ThreadLocal<String> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> sessionId = new ThreadLocal<>();

    public static void setUserId(String userId) {
        UserContext.userId.set(userId);
    }

    public static String getUserId() {
        return userId.get();
    }

    public static void setSessionId(String sessionId) {
        UserContext.sessionId.set(sessionId);
    }

    public static String getSessionId() {
        return sessionId.get();
    }

    public static void clear() {
        userId.remove();
        sessionId.remove();
    }
}
