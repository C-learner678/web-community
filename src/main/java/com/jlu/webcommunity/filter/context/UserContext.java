package com.jlu.webcommunity.filter.context;

import lombok.Data;

//用ThreadLocal保存用户上下文
public class UserContext {
    private static ThreadLocal<UserData> contexts = new InheritableThreadLocal<>();

    public static void addUserData(UserData userData) {
        contexts.set(userData);
    }

    public static void clear() {
        contexts.remove();
    }

    public static UserData getUserData() {
        return contexts.get();
    }

    @Data
    public static class UserData{
        private String token;
        private Long id;
        private String name;
        private String role;
    }
}
