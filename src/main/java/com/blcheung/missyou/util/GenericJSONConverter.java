package com.blcheung.missyou.util;

import com.blcheung.missyou.exception.http.ServerErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenericJSONConverter {

    private static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        GenericJSONConverter.objectMapper = objectMapper;
    }

    /**
     * 把任意类型实体字段序列化到数据库字段
     *
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String convertObjectToJSON(T value) {
        if (value == null) return null;
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

    /**
     * 从数据库字段反序列化到实体字段
     *
     * @param s
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T convertJSONToObject(String s, TypeReference<T> typeReference) {
        if (s == null) return null;
        try {
            return objectMapper.readValue(s, typeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }
    //    public static <T> T convertJSONToObject(String s, Class<T> clazz) {
    //        if (s == null) return null;
    //        try {
    //            return objectMapper.readValue(s, clazz);
    //        } catch (JsonProcessingException e) {
    //            e.printStackTrace();
    //            throw new ServerErrorException(9999);
    //        }
    //    }

    /**
     * 从数据库字段反序列化为数组到实体
     * 同convertJSONToObject，废弃
     *
     * @param s
     * @param typeReference 整个数组类型
     * @param <T>           数组元素类型
     * @return
     * @deprecated 同convertJSONToObject，废弃
     */
    public static <T> T convertJSONToList(String s, TypeReference<T> typeReference) {
        if (s == null) return null;
        try {
            // 这种方法可以被推倒为T类型的元素
            return objectMapper.readValue(s, typeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

    /**
     * 从数据库字段反序列化为数组到实体
     *
     * @param s
     * @param <T> List数组元素的类型
     * @return
     * @deprecated 见convertJSONToList(String s)
     */
    public static <T> List<T> convertJSONToList(String s) {
        if (s == null) return null;
        try {
            // 这种方法的List内部的子元素并不能被推倒为T类型的子元素
            List<T> list = objectMapper.readValue(s, new TypeReference<List<T>>() {});
            return list;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }
}
