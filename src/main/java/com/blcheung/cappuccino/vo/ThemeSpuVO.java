package com.blcheung.cappuccino.vo;

import com.blcheung.cappuccino.model.Theme;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ThemeSpuVO extends ThemeVO {

    private List<SpuPagingVO> spuList;

    public ThemeSpuVO(Theme theme) {
        super(theme);
        this.spuList = theme.getSpuList()
                            .stream()
                            .map(spu -> DozerBeanMapperBuilder.buildDefault()
                                                              .map(spu, SpuPagingVO.class))
                            .collect(Collectors.toList());
    }
}
