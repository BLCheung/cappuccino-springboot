package com.blcheung.missyou.service;

import com.blcheung.missyou.model.Banner;

public interface BannerService {
    Banner getById(Long id);

    Banner getByName(String name);
}
