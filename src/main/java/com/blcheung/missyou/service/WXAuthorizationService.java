package com.blcheung.missyou.service;

import com.blcheung.missyou.exception.http.ParameterException;
import com.blcheung.missyou.kit.TokenKit;
import com.blcheung.missyou.model.User;
import com.blcheung.missyou.repository.UserRepository;
import com.blcheung.missyou.util.GenericJSONConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;

@Service
public class WXAuthorizationService {
    @Value("${wx.app_id}")
    private String appId;
    @Value("${wx.secret}")
    private String secret;
    @Value("${wx.code2session}")
    private String code2SessionUrl;

    @Autowired
    private UserRepository userRepository;

    public String code2Session(String code) {
        String wxSessionUrl = MessageFormat.format(code2SessionUrl, this.appId, this.secret, code);
        RestTemplate restTemplate = new RestTemplate();
        String sessionJson = restTemplate.getForObject(wxSessionUrl, String.class);
        Map<String, Object> session = GenericJSONConverter.convertJSONToObject(sessionJson,
                                                                               new TypeReference<Map<String, Object>>() {});

        assert session != null;
        return this.registerUser(session);
    }

    /**
     * 注册用户
     *
     * @param session
     * @return
     */
    private String registerUser(Map<String, Object> session) {
        String openId = (String) session.get("openid");
        if (StringUtils.isEmpty(openId)) throw new ParameterException(20004);

        Optional<User> userOptional = Optional.ofNullable(userRepository.findByOpenid(openId));
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            // 新用户，插入数据库
            user = User.builder()
                       .openid(openId)
                       .build();
            userRepository.save(user);
        }
        return TokenKit.getToken(user.getId());
    }
}
