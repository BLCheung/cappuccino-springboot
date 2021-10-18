package com.blcheung.missyou.kit;

import com.blcheung.missyou.constant.SortType;
import com.blcheung.missyou.dto.PagingDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PagingKit {
    /**
     * 构造最新的排序分页器
     * 默认按创建时间倒序
     *
     * @param pagingDTO
     * @return
     */
    public static Pageable buildLatest(PagingDTO pagingDTO) { return paging(pagingDTO, SortType.BY_CREATE_TIME_DESC); }

    /**
     * 构造无排序的分页器
     *
     * @param pagingDTO
     * @return
     */
    public static Pageable buildNoSort(PagingDTO pagingDTO) { return paging(pagingDTO, Sort.unsorted()); }

    /**
     * 构造一个分页器排序
     *
     * @param pagingDTO
     * @param sort
     * @return
     */
    public static Pageable build(PagingDTO pagingDTO, Sort sort) { return paging(pagingDTO, sort); }

    private static Pageable paging(PagingDTO pagingDTO, Sort sort) {
        return PageRequest.of(pagingDTO.getPageNum(), pagingDTO.getPageSize(), sort);
    }
}
