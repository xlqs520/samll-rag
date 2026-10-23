package com.xlqs.smallrag.api;

import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 18:10
 * @description RagRetrievalController 类说明：TODO
 * @since 2026/10/23
 */
@RequestMapping("/retrieval")
public interface RagRetrievalController {

    @GetMapping("/byText")
    List<Document> retrieve(
            @RequestParam Long knowledgeBaseId,
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK);

}
