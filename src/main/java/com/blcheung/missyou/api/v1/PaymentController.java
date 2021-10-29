package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.common.Result;
import com.blcheung.missyou.dto.BasePayDTO;
import com.blcheung.missyou.kit.ResultKit;
import com.blcheung.missyou.service.WXPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<Map<String, String>> payByWechatMin(@RequestBody @Validated BasePayDTO payDTO) {
        Map<String, String> minPaySignature = this.wxPaymentService.unifiedOrder(payDTO);

        return ResultKit.resolve(minPaySignature);
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
