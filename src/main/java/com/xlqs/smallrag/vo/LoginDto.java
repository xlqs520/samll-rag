package com.xlqs.smallrag.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 18:45
 * @description LoginDto 类说明：TODO
 * @since 2026/10/23
 */
@Data
public class LoginDto {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
