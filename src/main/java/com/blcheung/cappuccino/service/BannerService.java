package com.blcheung.cappuccino.service;

import com.blcheung.cappuccino.model.Banner;

public interface BannerService {
    Banner getById(Long id);

    Banner getByName(String name);
}
