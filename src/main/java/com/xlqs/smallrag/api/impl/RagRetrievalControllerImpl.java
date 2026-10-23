package com.xlqs.smallrag.api.impl;

import com.xlqs.smallrag.api.RagRetrievalController;
import com.xlqs.smallrag.knowledge.KnowledgeRetrievalService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 18:11
 * @description RagRetrievalControllerImpl 类说明：TODO
 * @since 2026/10/23
 */

@RestController
@RequiredArgsConstructor
public class RagRetrievalControllerImpl implements RagRetrievalController {

    private final KnowledgeRetrievalService retrievalService;

    @Override
    public List<Document> retrieve(Long knowledgeBaseId, String query, int topK) {
        return retrievalService.retrieve(knowledgeBaseId, query, topK);
    }
}
