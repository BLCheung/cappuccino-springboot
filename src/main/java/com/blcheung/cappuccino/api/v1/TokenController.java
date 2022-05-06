package com.blcheung.cappuccino.api.v1;

import com.blcheung.cappuccino.common.Result;
import com.blcheung.cappuccino.core.enumeration.LoginType;
import com.blcheung.cappuccino.dto.TokenDTO;
import com.blcheung.cappuccino.dto.TokenGetDTO;
import com.blcheung.cappuccino.exception.http.NotFoundException;
import com.blcheung.cappuccino.kit.ResultKit;
import com.blcheung.cappuccino.kit.TokenKit;
import com.blcheung.cappuccino.service.WXAuthorizationService;
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
     *
     * @param userData
     * @return
     */
    @PostMapping("")
    public Result<Map<String, Object>> getToken(@RequestBody @Validated TokenGetDTO userData) {
        Map<String, Object> map = new HashMap<>();

        // switch一个null会导致NPE
        LoginType loginType = LoginType.toType(userData.getType())
                                       .orElseThrow(() -> new NotFoundException(10003));
        String token = null;
        switch (loginType) {
            case WX:
                token = wxAuthorizationService.code2Session(userData.getAccount());
                break;
            case PHONE:
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
