package com.blcheung.missyou.kit;

import com.blcheung.missyou.exception.http.ForbiddenException;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LocalUserKit {
    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void init(User user, Integer scope) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("scope", scope);
        threadLocal.set(map);
    }

    /**
     * 获取当前请求的用户
     *
     * @return User
     */
    public static User getUser() {
        Map<String, Object> map = threadLocal.get();
        Optional<User> userOptional = Optional.ofNullable((User) map.get("user"));
        return userOptional.orElseThrow(() -> new NotFoundException(20002));
    }

    /**
     * 获取用户的权限级别
     *
     * @return User的scope权限
     */
    public static Integer getScope() {
        Map<String, Object> map = threadLocal.get();
        Optional<Integer> scopeOptional = Optional.ofNullable((Integer) map.get("scope"));
        return scopeOptional.orElseThrow(() -> new ForbiddenException(10005));
    }

    public static void clear() { threadLocal.remove(); }
}
