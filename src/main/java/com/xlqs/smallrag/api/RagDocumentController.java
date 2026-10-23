package com.xlqs.smallrag.api;

import com.xlqs.smallrag.vo.KnowledgeDocumentCreateDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 16:48
 * @description RagDocumentController 类说明：文本类知识库接口
 * @since 2026/10/23
 */

@RequestMapping("/document")
public interface RagDocumentController {



    @PostMapping("/create")
     Long createDocument(
            @RequestPart("data") KnowledgeDocumentCreateDto dto,
            @RequestPart("file") MultipartFile file);
}
