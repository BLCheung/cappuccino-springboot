package com.blcheung.missyou.core.payment;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.InputStream;

/**
 * App微信支付配置文件
 */
public class WechatAppPayConfig implements WXPayConfig {
    @Override
    public String getAppID() {
        return null;
    }

    @Override
    public String getMchID() {
        return null;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 0;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 0;
    }
}
