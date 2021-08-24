package com.blcheung.missyou.model;

import com.blcheung.missyou.util.MapJSONConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

@Entity
@Getter
@Setter
@Where(clause = "delete_time is null")  // 会应用当前查询语句在当前Entity内
public class User {
    @Id
    @GeneratedValue
    private Long                id;
    private String              openid;
    private String              nickname;
    private Long                unifyUid;
    private String              email;
    private String              password;
    private String              mobile;
    @SuppressWarnings("JpaAttributeTypeInspection")
    @Convert(converter = MapJSONConverter.class)
    private Map<String, Object> wxProfile;
}
