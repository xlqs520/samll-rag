package com.xlqs.smallrag.api;

import com.xlqs.smallrag.vo.Result;
import com.xlqs.smallrag.vo.UserModelConfigCreateVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/25 20:33
 * @description UserModelController 类说明：用户模型配置
 * @since 2026/10/25
 */
@RequestMapping("/user-model-config")
public interface UserModelController {

    /**
     * 保存用户模型配置
     *
     * @param userModelConfigCreateVo 用户模型配置创建参数
     * @return 保存结果
     */
    @PostMapping("/save")
    Result<String> saveUserModelConfig(@RequestBody UserModelConfigCreateVo userModelConfigCreateVo);

}
