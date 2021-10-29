package com.blcheung.missyou.core.payment;

import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 微信小程序支付配置文件
 */
@Component
public class WechatMinPayConfig implements WXPayConfig {
    @Value("${wx.app_id}")
    private String appId;
    @Value("${wx.pay.mch_id}")
    private String mchId;
    @Value("${wx.pay.mch_key}")
    private String mchKey;

    @Override
    public String getAppID() {
        return this.appId;
    }

    @Override
    public String getMchID() {
        return this.mchId;
    }

    @Override
    public String getKey() {
        return this.mchKey;
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 6 * 1000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 8 * 1000;
    }
}
