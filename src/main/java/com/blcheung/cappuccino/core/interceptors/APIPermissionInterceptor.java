package com.blcheung.cappuccino.core.interceptors;

import com.auth0.jwt.interfaces.Claim;
import com.blcheung.cappuccino.core.annotations.ScopeLevel;
import com.blcheung.cappuccino.exception.http.ForbiddenException;
import com.blcheung.cappuccino.exception.http.UnAuthorizedException;
import com.blcheung.cappuccino.kit.LocalUserKit;
import com.blcheung.cappuccino.kit.TokenKit;
import com.blcheung.cappuccino.model.User;
import com.blcheung.cappuccino.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

/**
 * API拦截器
 */
public class APIPermissionInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        // handler为controller层的api的方法
        Optional<ScopeLevel> scopeLevel = this.getScope(handler);
        // 当前api是否有权限注解限定，没有则直接可以访问（开放的api接口）
        if (!scopeLevel.isPresent()) return true;

        // 需要校验权限api
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) throw new UnAuthorizedException(10004);

        Map<String, Claim> tokenClaim = TokenKit.getTokenClaim(token)
                                                .orElseThrow(() -> new UnAuthorizedException(10004));

        Boolean hasPermission = this.hasPermission(scopeLevel.get(), tokenClaim);
        if (hasPermission) this.initLocalUser(tokenClaim);

        return hasPermission;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        LocalUserKit.clear();
        super.afterCompletion(request, response, handler, ex);
    }


    private Optional<ScopeLevel> getScope(Object handler) {
        if (handler instanceof HandlerMethod) {
            ScopeLevel scopeLevel = ( (HandlerMethod) handler ).getMethod()
                                                               .getAnnotation(ScopeLevel.class);
            if (scopeLevel == null) return Optional.empty();

            return Optional.of(scopeLevel);
        }
        return Optional.empty();
    }

    private Boolean hasPermission(ScopeLevel level, Map<String, Claim> tokenClaim) {
        Integer scopeLevel = level.value();
        Integer currentScopeLevel = tokenClaim.get("scope")
                                              .asInt();
        // 权限不足访问
        if (currentScopeLevel < scopeLevel) throw new ForbiddenException(10005);
        return true;
    }

    private void initLocalUser(Map<String, Claim> tokenClaim) {
        Long uid = tokenClaim.get("uid")
                             .asLong();
        Integer currentScopeLevel = tokenClaim.get("scope")
                                              .asInt();
        User user = this.userService.getUserById(uid);
        LocalUserKit.init(user, currentScopeLevel);
    }
}
