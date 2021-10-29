package com.blcheung.missyou.kit;

import com.blcheung.missyou.core.payment.WechatMinPayConfig;
import com.blcheung.missyou.exception.http.ServerErrorException;
import com.blcheung.missyou.model.Order;
import com.blcheung.missyou.util.CommonUtils;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class WechatPayKit {
    private static String             notifyHost;
    private static String             notifyPath;
    // 小程序支付配置文件
    private static WechatMinPayConfig wechatMinPayConfig;

    @Value("${zbl.order.wx_notify_host}")
    public void setNotifyHost(String notifyHost) {
        WechatPayKit.notifyHost = notifyHost;
    }

    @Value("${zbl.order.wx_notify_path}")
    public void setNotifyPath(String notifyPath) {
        WechatPayKit.notifyPath = notifyPath;
    }

    @Autowired
    public void setWechatMinPayConfig(WechatMinPayConfig wechatMinPayConfig) {
        WechatPayKit.wechatMinPayConfig = wechatMinPayConfig;
    }

    /**
     * 微信小程序的统一下单
     *
     * @param order
     * @return
     */
    public static Map<String, String> unifiedOrderByMin(Order order) {
        WXPay wxPay = new WXPay(wechatMinPayConfig);

        Map<String, String> respData = null;
        try {
            respData = wxPay.unifiedOrder(generateMinOrder(order));
        } catch (Exception e) {
            e.printStackTrace();
            ResultKit.reject(10007, e.getMessage());
        }

        verifyUnifyOrderResponse(respData);
        // return generateMinPaySignature(respData);

        return respData;
    }

    /**
     * 生成微信小程序的统一下单参数
     *
     * @param order
     * @return
     */
    public static Map<String, String> generateMinOrder(Order order) {
        Map<String, String> orderParams = generateUnifyOrderParams(order);
        orderParams.put("trade_type", "JSAPI");
        orderParams.put("openid", LocalUserKit.getUser()
                                              .getOpenid());
        return orderParams;
    }

    /**
     * 校验微信返回的下单结果参数
     *
     * @param respData
     */
    public static void verifyUnifyOrderResponse(Map<String, String> respData) {
        if (ObjectUtils.isEmpty(respData)) throw new ServerErrorException(10007);

        String returnCode = respData.get("return_code");
        if (WXPayConstants.FAIL.equals(returnCode)) ResultKit.reject(10007, respData.get("return_msg"));

        String resultCode = respData.get("result_code");
        if (WXPayConstants.FAIL.equals(resultCode)) ResultKit.reject(10007, respData.get("err_code_des"));
    }

    /**
     * 生成前端微信小程序拉起微信支付时的签名参数
     *
     * @param respData
     * @return
     */
    public static Map<String, String> generateMinPaySignature(Map<String, String> respData) {
        Map<String, String> signParams = new HashMap<>();
        signParams.put("appId", wechatMinPayConfig.getAppID());
        signParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        signParams.put("nonceStr", respData.get("nonce_str"));
        signParams.put("package", "prepay_id=" + respData.get("prepay_id"));
        signParams.put("signType", "MD5");

        String paySign = null;
        try {
            // 进行签名，默认MD5
            paySign = WXPayUtil.generateSignature(signParams, wechatMinPayConfig.getKey());
        } catch (Exception e) {
            e.printStackTrace();
            ResultKit.reject(10007, e.getMessage());
        }

        Map<String, String> result = new HashMap<>();
        result.put("paySign", paySign);
        result.putAll(signParams);

        // 微信小程序拉起支付不需要appId
        result.remove("appId");

        return result;
    }

    /**
     * 生成前端App拉起微信支付的必要参数
     *
     * @param respData
     * @return
     */
    public static Map<String, String> generateAppPayResult(Map<String, String> respData) { return null; }


    private static Map<String, String> generateUnifyOrderParams(Order order) {
        Map<String, String> wxOrder = new HashMap<>();
        wxOrder.put("body", "zbl");
        wxOrder.put("out_trade_no", order.getOrderNo());
        wxOrder.put("total_fee", CommonUtils.toPlain(MoneyKit.transferToFen(order.getFinalTotalPrice())));
        wxOrder.put("spbill_create_ip", HttpRequestKit.getRemoteRealAddr());
        wxOrder.put("notify_url", notifyHost + notifyPath);

        return wxOrder;
    }


}
