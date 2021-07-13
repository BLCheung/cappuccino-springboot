package com.blcheung.missyou.core.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "zbl")    // 配置与之关联的配置文件字段属性前缀
@PropertySource("classpath:config/exception-code.properties")   // 将一个类与一个properties配置文件关联
@Getter
@Setter
public class ExceptionCodeConfiguration {

    private Map<Integer, String> codes = new HashMap<>();

    public ExceptionCodeConfiguration() {}

    /**
     * 获取对应code码的提示信息
     *
     * @param code
     * @return 对应的code码错误提示
     */
    public String getMessage(int code) { return codes.get(code); }
}
