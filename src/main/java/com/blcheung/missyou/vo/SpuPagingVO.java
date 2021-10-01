package com.blcheung.missyou.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 最新的Spu视图层数据
 */
@Getter
@Setter
public class SpuPagingVO {
    //    VO: 根据前端页面有选择的返回的视图层数据对象

    private Long    id;
    private String  title;
    private String  subtitle;
    private Boolean online;
    private String  price;
    private String  img;
    private String  discountPrice;
    private String  description;
    private String  tags;
    private String  forThemeImg;
    private Long    sketchSpecId;
}
