package com.blcheung.cappuccino.kit;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.blcheung.cappuccino.util.CommonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class TokenKit {
    // 盐钥匙
    private static       String  tokenKey;
    // 到期时间
    private static       Long    expiredTime;
    // 默认的权限等级
    private static final Integer defaultScope = 8;

    @Value("${zbl.security.jwt_key}")
    public void setTokenKey(String jwt_key) {
        TokenKit.tokenKey = jwt_key;
    }

    @Value("${zbl.security.token_expired_time}")
    public void setExpiredTime(Long token_expired_time) { TokenKit.expiredTime = token_expired_time; }

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
     * 获取加密的token内部附加数据
     *
     * @param token
     * @return
     */
    public static Optional<Map<String, Claim>> getTokenClaim(String token) {
        Algorithm algorithm = Algorithm.HMAC256(tokenKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm)
                                     .build();
        DecodedJWT decodedJWT;
        try {
            // 是否同一token
            decodedJWT = jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            // 验证失败
            return Optional.empty();
        }
        return Optional.of(decodedJWT.getClaims());
    }

    /**
     * 是否有效的token
     *
     * @param token
     * @return
     */
    public static Boolean isVerifyToken(String token) {
        Optional<Map<String, Claim>> tokenClaim = getTokenClaim(token);
        return tokenClaim.isPresent();
    }

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
