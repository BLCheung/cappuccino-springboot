package com.blcheung.missyou.vo;

import com.blcheung.missyou.model.Theme;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ThemeSpuItemVO extends ThemeItemVO {

    private List<SpuPagingVO> spuList;

    public ThemeSpuItemVO(Theme theme) {
        super(theme);
        this.spuList = theme.getSpuList()
                            .stream()
                            .map(spu -> DozerBeanMapperBuilder.buildDefault()
                                                              .map(spu, SpuPagingVO.class))
                            .collect(Collectors.toList());
    }
}
