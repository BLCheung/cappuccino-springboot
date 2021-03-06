package com.blcheung.missyou.service;

import org.springframework.stereotype.Service;

@Service
public interface BannerService {

    void getByName(String name);
}
