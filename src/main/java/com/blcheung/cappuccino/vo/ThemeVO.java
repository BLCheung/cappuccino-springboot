package com.blcheung.cappuccino.vo;

import com.blcheung.cappuccino.model.Theme;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class ThemeVO {
    private Long    id;
    private String  title;
    private String  description;
    private String  name;
    private String  tplName;
    private String  entranceImg;
    private String  extend;
    private String  internalTopImg;
    private String  titleImg;
    private Boolean online;

    public ThemeVO(Theme theme) {
        BeanUtils.copyProperties(theme, this);
    }
}
