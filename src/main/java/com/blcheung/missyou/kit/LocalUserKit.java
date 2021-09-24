package com.blcheung.missyou.kit;

import com.blcheung.missyou.model.User;

import java.util.HashMap;
import java.util.Map;

public class LocalUserKit {
    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();


    public static void init(User user, Integer scope) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("scope", scope);
        threadLocal.set(map);
    }

    public static User getUser() {
        Map<String, Object> map = threadLocal.get();
        return (User) map.get("user");
    }

    public static Integer getScope() {
        Map<String, Object> map = threadLocal.get();
        return (Integer) map.get("scope");
    }

    public static void clear() { threadLocal.remove(); }
}
