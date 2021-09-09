package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.common.Result;
import com.blcheung.missyou.core.enumeration.LoginType;
import com.blcheung.missyou.dto.TokenDTO;
import com.blcheung.missyou.dto.TokenGetDTO;
import com.blcheung.missyou.exception.http.NotFoundException;
import com.blcheung.missyou.kit.ResultKit;
import com.blcheung.missyou.kit.TokenKit;
import com.blcheung.missyou.service.WXAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/token")
public class TokenController {
    @Autowired
    private WXAuthorizationService wxAuthorizationService;

    /**
     * 获取token
     * @param userData
     * @return
     */
    @PostMapping("")
    public Result<Map<String, Object>> getToken(@RequestBody @Validated TokenGetDTO userData) {
        Map<String, Object> map = new HashMap<>();

        String type = userData.getType();
        // switch一个null会导致NPE
        if (type == null) throw new NotFoundException(10003);
        String token = null;
        switch (LoginType.valueOf(type)) {
            case wx:
                token = wxAuthorizationService.code2Session(userData.getAccount());
                break;
            case email:
                break;

            default:
                throw new NotFoundException(10003);
        }
        map.put("token", token);
        return ResultKit.resolve(map);
    }

    /**
     * token是否合法
     *
     * @param tokenDTO
     * @return
     */
    @PostMapping("/verify")
    public Result<Map<String, Object>> verify(@RequestBody @Validated TokenDTO tokenDTO) {
        Map<String, Object> map = new HashMap<>();
        String token = tokenDTO.getToken();
        Boolean isValid = TokenKit.isVerifyToken(token);
        map.put("isValid", isValid);
        return ResultKit.resolve(map);
    }
}
