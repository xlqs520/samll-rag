package com.xlqs.smallrag.api.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.xlqs.smallrag.api.AuthController;
import com.xlqs.smallrag.entity.SysUser;
import com.xlqs.smallrag.user.SysUserService;
import com.xlqs.smallrag.vo.LoginDto;
import com.xlqs.smallrag.vo.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 18:50
 * @description AuthControllerImpl 类说明：TODO
 * @since 2026/10/23
 */

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final SysUserService sysUserService;

    @Override
    public Map<String, Object> register(RegisterDto dto) {
        sysUserService.register(dto);
        return Map.of("message", "注册成功");
    }

    @Override
    public Map<String, Object> login(LoginDto dto) {
        SysUser user = sysUserService.authenticate(
                dto.getUsername(),
                dto.getPassword()
        );

        StpUtil.login(user.getId());

        return Map.of(
                "tokenName", StpUtil.getTokenName(),
                "token", StpUtil.getTokenValue(),
                "userId", user.getId(),
                "username", user.getUsername()
        );
    }

    @Override
    public Map<String, String> logout() {
        StpUtil.logout();
        return Map.of("message", "退出登录成功");
    }

    @Override
    public Map<String, Object> me() {
        StpUtil.checkLogin();

        return Map.of(
                "userId", StpUtil.getLoginId()
        );
    }
}
