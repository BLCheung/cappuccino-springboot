package com.blcheung.missyou.api.v1;

import com.blcheung.missyou.core.enumeration.LoginType;
import com.blcheung.missyou.dto.TokenGetDTO;
import com.blcheung.missyou.exception.http.NotFoundException;
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

    @PostMapping("")
    public Map<String, String> getToken(@RequestBody @Validated TokenGetDTO userData) {
        Map<String, String> map = new HashMap<>();

        String type = userData.getType();
        // switch一个null会导致NPE
        if (type == null) throw new NotFoundException(10003);

        switch (LoginType.valueOf(type)) {
            case wx:
                wxAuthorizationService.code2Session(userData.getAccount());
                break;
            case email:
                break;

            default:
                throw new NotFoundException(10003);
        }

        return map;
    }
}
