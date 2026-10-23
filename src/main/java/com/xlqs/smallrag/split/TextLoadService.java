package com.xlqs.smallrag.split;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TextLoadService {

    private final ResourceLoader resourceLoader;

    public TextLoadService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 根据路径读取 TXT 文件，使用 UTF-8 编码。
     * 支持：
     * 1. classpath:redis.txt
     * 2. file:D:/data/redis.txt
     * 3. 普通文件路径：D:/data/redis.txt
     * 4. 相对路径：data/redis.txt
     */
    public String readText(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        try {
            // classpath 路径交给 Spring 读取
            if (filePath.startsWith("classpath:")) {
                Resource resource =
                        resourceLoader.getResource(filePath);

                try (var inputStream = resource.getInputStream()) {
                    return new String(
                            inputStream.readAllBytes(),
                            StandardCharsets.UTF_8
                    );
                }
            }

            // file: 路径交给 Spring 读取
            if (filePath.startsWith("file:")) {
                Resource resource =
                        resourceLoader.getResource(filePath);

                try (var inputStream = resource.getInputStream()) {
                    return new String(
                            inputStream.readAllBytes(),
                            StandardCharsets.UTF_8
                    );
                }
            }

            // 普通路径：按本地文件系统路径处理
            return Files.readString(
                    Path.of(filePath),
                    StandardCharsets.UTF_8
            );

        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException(
                    "读取文件失败，路径：" + filePath,
                    e
            );
        }
    }
}