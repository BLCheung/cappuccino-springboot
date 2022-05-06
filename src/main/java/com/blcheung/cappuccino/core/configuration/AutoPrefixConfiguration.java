package com.blcheung.cappuccino.core.configuration;

import com.blcheung.cappuccino.core.hack.AutoPrefixUrlMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 自定义springMVC配置的路由信息
 */
@Component  // Spring会为通过特定注解并且实现了特定的类，才能被Spring做特殊处理
public class AutoPrefixConfiguration implements WebMvcRegistrations {

    /**
     * 自定义路由映射器的信息
     * @return
     */
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new AutoPrefixUrlMapping();
    }
}
