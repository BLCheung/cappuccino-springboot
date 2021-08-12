package com.blcheung.missyou.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class WXAuthorizationService {
    @Value("${wx.app_id}")
    private String appId;
    @Value("${wx.secret}")
    private String secret;
    @Value("${wx.code2session}")
    private String code2SessionUrl;

    public void code2Session(String code) {
        String wxSessionUrl = MessageFormat.format(code2SessionUrl, this.appId, this.secret, code);

    }
}
