package com.xlqs.smallrag.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoForRedisTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/24 23:44
 * @description SaTokenDaoConfig 类说明：TODO
 * @since 2026/10/24
 */
@Configuration
public class SaTokenDaoConfig {
    @Bean
    @Primary
    public SaTokenDao saTokenDao() {
        return new SaTokenDaoForRedisTemplate() {
            @Override
            public String wrapKey(String key) {
                return "small-rag:sa-token:" + key; // 自定义统一前缀
            }
        };
    }
}
