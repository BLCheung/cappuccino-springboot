package com.blcheung.missyou.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageResult<T> {
    private Boolean hasNext;
    private Integer pageNum;
    private Integer pageSize;
    private Long    total;
    private Integer totalPage;
    private List<T> list;

    public PageResult(Page<T> page) {
        this.initPageParams(page);
        this.list = page.getContent();
    }


    void initPageParams(Page<T> page) {
        this.hasNext   = page.hasNext();
        this.pageNum   = page.getNumber() + 1;  // JPA分页默认从0开始，+1同步前端
        this.pageSize  = page.getSize();
        this.total     = page.getTotalElements();
        this.totalPage = page.getTotalPages();
    }
}
