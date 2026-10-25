package com.xlqs.smallrag.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 18:44
 * @description RegisterDto 类说明：TODO
 * @since 2026/10/23
 */
@Data
public class RegisterDto {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 64, message = "用户名长度必须为3到64位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 128, message = "密码长度必须为8到128位")
    private String password;

    @Size(max = 100, message = "昵称不能超过100位")
    private String nickname;
}
