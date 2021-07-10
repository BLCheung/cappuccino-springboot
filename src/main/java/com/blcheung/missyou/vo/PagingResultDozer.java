package com.blcheung.missyou.vo;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PagingResultDozer<T, K> extends PageResult {
    public PagingResultDozer(Page<T> page, Class<K> voClass) {
        this.initPageParams(page);

        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        List<T> list = page.getContent();
        List<K> vos = new ArrayList<>();

        list.forEach(item -> {
            K vo = mapper.map(item, voClass);
            vos.add(vo);
        });
        this.setList(vos);
    }
}
