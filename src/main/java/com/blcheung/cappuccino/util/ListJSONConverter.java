package com.blcheung.cappuccino.util;

import com.blcheung.cappuccino.exception.http.ServerErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter
public class ListJSONConverter implements AttributeConverter<List<Object>, String> {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public String convertToDatabaseColumn(List<Object> objects) {
        if (objects == null) return null;

        try {
            return mapper.writeValueAsString(objects);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> convertToEntityAttribute(String s) {
        if (s == null) return null;

        try {
            return mapper.readValue(s, List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }
}
