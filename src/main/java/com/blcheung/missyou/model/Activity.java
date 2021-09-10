package com.blcheung.missyou.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Where(clause = "delete_time is null")
public class Activity extends BaseEntity {
    @Id
    @GeneratedValue
    private Long    id;
    private String  title;
    private String  description;
    private Date    startTime;
    private Date    endTime;
    private String  remark;
    private Boolean online;
    private String  entranceImg;
    private String  internalTopImg;
    private String  name;
}
