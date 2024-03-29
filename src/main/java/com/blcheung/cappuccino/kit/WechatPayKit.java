package com.blcheung.cappuccino.kit;

import com.blcheung.cappuccino.core.payment.WechatMinPayConfig;
import com.blcheung.cappuccino.exception.http.ServerErrorException;
import com.blcheung.cappuccino.model.Order;
import com.blcheung.cappuccino.util.CommonUtils;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
     * 初始化一个微信支付
     *
     * @param config
     * @return
     */
    public static WXPay initWxPay(WXPayConfig config) { return new WXPay(config); }

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

        verifyResponseMap(respData);
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
     * 校验微信统一下单返回的结果参数
     *
     * @param respData
     */
    public static void verifyResponseMap(Map<String, String> respData) {
        if (ObjectUtils.isEmpty(respData)) throw new ServerErrorException(10007);

        String returnCode = respData.get("return_code");
        if (WXPayConstants.FAIL.equals(returnCode)) ResultKit.reject(10007, respData.get("return_msg"));

        String resultCode = respData.get("result_code");
        if (WXPayConstants.FAIL.equals(resultCode)) ResultKit.reject(10007, respData.get("err_code_des"));
    }

    /**
     * 校验微信支付成功回调的参数
     *
     * @param respData
     * @return
     */
    public static boolean isPayResultNotifyValid(Map<String, String> respData) {
        if (ObjectUtils.isEmpty(respData)) return false;

        String returnCode = respData.get("return_code");
        if (WXPayConstants.FAIL.equals(returnCode)) return false;

        String resultCode = respData.get("result_code");
        if (WXPayConstants.FAIL.equals(resultCode)) return false;

        return true;
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
     * @deprecated
     */
    public static Map<String, String> generateAppPayResult(Map<String, String> respData) { return null; }

    /**
     * 解析微信回调返回的xml流数据
     *
     * @param stream
     * @return
     */
    public static String processNotify(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();

        String line;
        while (( line = reader.readLine() ) != null) {
            builder.append(line)
                   .append("\n");
        }
        stream.close();
        return builder.toString();
    }

    /**
     * 微信回调成功
     *
     * @return
     */
    public static String notifySuccess() {
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg>OK</return_msg></xml>";
    }

    /**
     * 微信回调失败
     *
     * @return
     */
    public static String notifyFail() {
        return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
    }

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
