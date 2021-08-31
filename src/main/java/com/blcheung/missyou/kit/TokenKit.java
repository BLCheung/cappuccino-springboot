package com.blcheung.missyou.kit;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.blcheung.missyou.util.CommonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenKit {
    // 盐钥匙
    private static       String  tokenKey;
    // 到期时间
    private static       Integer expiredTime;
    // 默认的权限等级
    private static final Integer defaultScope = 8;

    @Value("${zbl.security.jwt_key}")
    public void setTokenKey(String jwt_key) {
        TokenKit.tokenKey = jwt_key;
    }

    @Value("${zbl.security.token_expired_time}")
    public void setExpiredTime(Integer token_expired_time) { TokenKit.expiredTime = token_expired_time; }

    /**
     * 获取token
     *
     * @param uid
     * @return
     */
    public static String getToken(Long uid) { return makeToken(uid, defaultScope); }

    /**
     * 获取token
     *
     * @param uid   用户id
     * @param scope 级别
     * @return
     */
    public static String getToken(Long uid, Integer scope) { return makeToken(uid, scope); }

    /**
     * 生成token令牌
     *
     * @param uid
     * @param scope
     * @return
     */
    private static String makeToken(Long uid, Integer scope) {
        Algorithm algorithm = Algorithm.HMAC256(tokenKey);
        return JWT.create()
                  .withClaim("uid", uid) // 用户id
                  .withClaim("scope", scope) // 用户级别
                  .withIssuedAt(CommonUtils.getNowDate()) // 签发时间
                  .withExpiresAt(CommonUtils.getFutureDateWithSecond(expiredTime)) // 过期时间
                  .sign(algorithm);
    }
}
