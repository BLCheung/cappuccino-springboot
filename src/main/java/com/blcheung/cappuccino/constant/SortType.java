package com.blcheung.cappuccino.constant;

import org.springframework.data.domain.Sort;

public class SortType {
    /**
     * 创建时间倒序
     */
    public static final Sort BY_CREATE_TIME_DESC = Sort.by("createTime")
                                                        .descending();
    /**
     * 创建时间正序
     */
    public static final Sort BY_CREATE_TIME_ASC  = Sort.by("createTime")
                                                        .ascending();
}
