package com.xlqs.smallrag.utils;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 17:22
 * @description DocumentParser 类说明：TODO
 * @since 2026/10/23
 */
@Service
public class DocumentParser {

    private final Tika tika = new Tika();

    public String parse(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        try (var inputStream = file.getInputStream()) {
            String text = tika.parseToString(inputStream);

            if (text == null || text.isBlank()) {
                throw new IllegalArgumentException("文件未提取到文本内容");
            }

            return text.trim();
        } catch (IOException | TikaException e) {
            throw new IllegalStateException("文件解析失败", e);
        }
    }
}
