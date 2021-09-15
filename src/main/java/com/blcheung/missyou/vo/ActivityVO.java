package com.blcheung.missyou.vo;

import com.blcheung.missyou.model.Activity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
public class ActivityVO {
    private Long    id;
    private String  title;
    private String  name;
    private String  description;
    private Date    startTime;
    private Date    endTime;
    private String  remark;
    private Boolean online;
    private String  entranceImg;
    private String  internalTopImg;

    public ActivityVO(Activity activity) { BeanUtils.copyProperties(activity, this); }
}
