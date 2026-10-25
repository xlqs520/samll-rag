package com.xlqs.smallrag.config;

import com.xlqs.smallrag.utils.ApiKeyCryptoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/25 20:11
 * @description ApiKeyConfig 类说明：TODO
 * @since 2026/10/25
 */
@Configuration
public class ApiKeyConfig {

    @Bean
    public ApiKeyCryptoService apiKeyEncryptor(
            @Value("${api-key.encryption-key}") String key) {
        return new ApiKeyCryptoService(key);
    }

}
