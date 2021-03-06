package com.blcheung.missyou.core.hack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * 专门用于处理带有@RequestMapping注解的类
 * 只是一个普通的类
 */
public class AutoPrefixUrlMapping extends RequestMappingHandlerMapping {

    @Value("${missyou.api-path}") private String apiPath;

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        // 拿到路由
        RequestMappingInfo mappingInfo = super.getMappingForMethod(method, handlerType);
        if (mappingInfo != null) {
            String prefix = this.getPrefix(handlerType);
            // 生成带有新的路由前缀的路由映射器，并与现有的路由映射器合并
            return RequestMappingInfo.paths(prefix).build().combine(mappingInfo);
        }
        // 进行修改
        return mappingInfo;
    }

    private String getPrefix(Class<?> handlerType) {
        String packageName = handlerType.getPackage().getName();
        String versionDotPath = packageName.replaceAll(this.apiPath, "");
        return versionDotPath.replace(".", "/");
    }
}
