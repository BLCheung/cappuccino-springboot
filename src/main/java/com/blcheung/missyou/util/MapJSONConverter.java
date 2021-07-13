package com.blcheung.missyou.util;

import com.blcheung.missyou.exception.http.ServerErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库JSON对象与Java实体Map对象转换器
 * X: 当前实体类字段的类型，Y: 对应数据库里的类型
 */
@Converter  // 让JPA知道这是一个序列化转换器，调用该转换器实现数据库与Java的序列化与反序列化
public class MapJSONConverter implements AttributeConverter<Map<String, Object>, String> {

    @Autowired
    private ObjectMapper mapper;

    /**
     * Java实体内的Map -> 数据库内的JSON对象（字符串表示）
     *
     * @param stringObjectMap
     * @return
     */
    @Override
    public String convertToDatabaseColumn(Map<String, Object> stringObjectMap) {
        if (stringObjectMap == null) return null;
        try {
            return mapper.writeValueAsString(stringObjectMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

    /**
     * 数据库内的JSON对象（字符串表示） -> Java实体内的Map
     *
     * @param s
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> convertToEntityAttribute(String s) {
        if (s == null) return null;
        // 需要读取的数据库字段内容，需要被反序列化为对应Java字段的类型元类
        try {
            return mapper.readValue(s, HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }
}
