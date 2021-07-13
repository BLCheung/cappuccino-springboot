package com.blcheung.missyou.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity // 通常打上该注解的类都是对应数据库内的一张表
@Getter
@Setter
@Where(clause = "delete_time is null")  // 会应用当前查询语句在当前Entity内
public class Banner extends BaseEntity {
    @Id
    @GeneratedValue
    private Long   id;
    private String name;
    private String description;
    private String title;
    private String img;

    @OneToMany(fetch = FetchType.LAZY)  // 虽然配为懒加载，但实体被进行序列化的时候(getter方法)会被触发，进而会返回结果
    @JoinColumn(name = "bannerId")
    private List<BannerItem> items;
}
