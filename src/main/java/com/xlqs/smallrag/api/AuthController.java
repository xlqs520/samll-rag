package com.xlqs.smallrag.api;

import com.xlqs.smallrag.vo.LoginDto;
import com.xlqs.smallrag.vo.RegisterDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 18:48
 * @description AuthController 类说明：TODO
 * @since 2026/10/23
 */
@RequestMapping("/auth")
public interface AuthController {

    @PostMapping("/register")
    Map<String, Object> register(
            @Valid @RequestBody RegisterDto dto);

    @PostMapping("/login")
    Map<String, Object> login(
            @Valid @RequestBody LoginDto dto);

    @PostMapping("/logout")
    Map<String, String> logout();

    @GetMapping("/me")
    Map<String, Object> me();
}
