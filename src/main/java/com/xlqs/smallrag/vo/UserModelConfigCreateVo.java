package com.xlqs.smallrag.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/25 20:48
 * @description UserModelConfigCreateVo 类说明：TODO
 * @since 2026/10/25
 */
@Data
public class UserModelConfigCreateVo {

    /**
     * 配置名称
     */
    @NotBlank(message = "模型配置名称不能为空")
    private String name;

    /**
     * 模型提供商
     */
    @NotBlank(message = "模型提供商不能为空")
    private String provider;

    /**
     * 模型名称
     */
    @NotBlank(message = "模型名称不能为空")
    private String modelName;

    /**
     * API 地址
     */
    private String baseUrl;

    /**
     * 原始 API Key
     */
    @NotBlank(message = "API Key 不能为空")
    private String apiKey;

    /**
     * 模型类型：chat、embedding、rerank
     */
    @NotBlank(message = "模型类型不能为空")
    private String modelType;

    /**
     * 是否设为默认模型
     */
    @NotNull(message = "是否默认模型不能为空")
    private Integer isDefault;
}
