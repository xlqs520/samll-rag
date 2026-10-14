package com.xlqs.smallrag.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/14 19:00
 * @description JsonUtils 类说明：TODO
 * @since 2026/10/14
 */
@Service
public class JsonHelper {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 将对象转换为 Map
     *
     * @param obj 要转换的对象
     * @return 转换后的 Map
     */
    public Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return Collections.emptyMap();
        }

        // 1. 如果它本来就是 Map，直接强转
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }

        try {
            // 2. 如果它是一个 JSON 字符串（比如 {"brand": true}）
            if (obj instanceof String) {
                return objectMapper.readValue((String) obj, new TypeReference<>() {
                });
            }

            // 3. 其他 Object 对象通过 convertValue 转换
            return objectMapper.convertValue(obj, new TypeReference<>() {
            });

        } catch (Exception e) {
            System.err.println("解析 JSON 转 Map 失败: " + e.getMessage());
            return Collections.emptyMap();
        }
    }
}
