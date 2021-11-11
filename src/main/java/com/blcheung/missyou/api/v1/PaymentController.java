package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.common.Result;
import com.blcheung.missyou.core.annotations.ScopeLevel;
import com.blcheung.missyou.dto.BasePayDTO;
import com.blcheung.missyou.kit.ResultKit;
import com.blcheung.missyou.kit.WechatPayKit;
import com.blcheung.missyou.service.WXPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PaymentController {
    @Autowired
    private WXPaymentService wxPaymentService;

    /**
     * 微信小程序微信支付
     *
     * @param payDTO
     * @return
     */
    @PostMapping("/wechat_min")
    @ScopeLevel()
    public Result<Map<String, String>> payByWechatMin(@RequestBody @Validated BasePayDTO payDTO) {
        Map<String, String> minPaySignature = this.wxPaymentService.unifiedOrder(payDTO);

        return ResultKit.resolve(minPaySignature);
    }

    /**
     * 微信支付完成通知回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/wechat/pay_notify")
    public String wxPayNotify(HttpServletRequest request, HttpServletResponse response) {
        InputStream stream;
        try {
            stream = request.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return WechatPayKit.notifyFail();
        }

        try {
            String xml = WechatPayKit.processNotify(stream);

            Boolean isProcessed = this.wxPaymentService.processNotifyXml(xml);

            if (!isProcessed) return WechatPayKit.notifyFail();
        } catch (Exception e) {
            e.printStackTrace();
            return WechatPayKit.notifyFail();
        }

        return WechatPayKit.notifySuccess();
    }

    /**
     * TODO: App微信支付
     *
     * @param payDTO
     * @return
     * @deprecated
     */
    @PostMapping("/wechat_app")
    public Result<Map<String, String>> payByWechatApp(@RequestBody @Validated BasePayDTO payDTO) { return null; }
}
