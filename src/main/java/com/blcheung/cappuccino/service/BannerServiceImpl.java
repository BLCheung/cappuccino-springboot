package com.blcheung.cappuccino.service;

import com.blcheung.cappuccino.model.Banner;
import com.blcheung.cappuccino.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceImpl implements BannerService {
    @Autowired
    private BannerRepository bannerRepository;

    @Override
    public Banner getById(Long id) {
        return bannerRepository.findOneById(id);
    }

    @Override
    public Banner getByName(String name) {
        return bannerRepository.findOneByName(name);
    }
}
