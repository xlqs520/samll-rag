package com.xlqs.smallrag.api.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.xlqs.smallrag.api.UserModelController;
import com.xlqs.smallrag.entity.UserModelConfig;
import com.xlqs.smallrag.mapper.UserModelConfigDao;
import com.xlqs.smallrag.utils.ApiKeyCryptoService;
import com.xlqs.smallrag.vo.Result;
import com.xlqs.smallrag.vo.UserModelConfigCreateVo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/25 20:36
 * @description UserModelControllerImpl 类说明：TODO
 * @since 2026/10/25
 */
@RestController
@RequiredArgsConstructor
public class UserModelControllerImpl implements UserModelController {

    private final UserModelConfigDao userModelConfigDao;

    private final ApiKeyCryptoService apiKeyCryptoService;

    @Override
    public Result<String> saveUserModelConfig(UserModelConfigCreateVo userModelConfigCreateVo) {
        Long userId = StpUtil.getLoginIdAsLong();
        UserModelConfig userModelConfig = new UserModelConfig();
        userModelConfig.setTenantId(userId);
        userModelConfig.setUserId(userId);
        userModelConfig.setName(userModelConfigCreateVo.getName());
        userModelConfig.setProvider(userModelConfigCreateVo.getProvider());
        userModelConfig.setModelName(userModelConfigCreateVo.getModelName());
        userModelConfig.setBaseUrl(userModelConfigCreateVo.getBaseUrl());
        userModelConfig.setApiKey(apiKeyCryptoService.encrypt(userModelConfigCreateVo.getApiKey()));
        userModelConfig.setModelType(userModelConfigCreateVo.getModelType());
        userModelConfig.setIsDefault(userModelConfigCreateVo.getIsDefault());
        userModelConfigDao.insert(userModelConfig);
        return Result.success("添加模型配置成功");
    }
}
