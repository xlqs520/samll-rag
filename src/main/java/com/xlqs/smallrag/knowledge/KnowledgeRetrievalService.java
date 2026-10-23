package com.xlqs.smallrag.knowledge;

import com.xlqs.smallrag.entity.KnowledgeBase;
import com.xlqs.smallrag.mapper.KnowledgeBaseDao;
import io.qdrant.client.QdrantClient;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 18:06
 * @description KnowledgeRetrievalService 类说明：TODO
 * @since 2026/10/23
 */

@Service
@RequiredArgsConstructor
public class KnowledgeRetrievalService {

    private final KnowledgeBaseDao knowledgeBaseDao;

    private final QdrantClient qdrantClient;

    private final EmbeddingModel embeddingModel;

    /**
     * 知识库向量召回
     */
    public List<Document> retrieve(Long knowledgeBaseId, String query, int topK) {
        KnowledgeBase knowledgeBase = knowledgeBaseDao.selectById(knowledgeBaseId);
        if (knowledgeBase == null) {
            throw new IllegalArgumentException("知识库不存在");
        }

        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("查询内容不能为空");
        }

        QdrantVectorStore store = QdrantVectorStore.builder(
                        qdrantClient, embeddingModel)
                .collectionName(knowledgeBase.getCollectionName())
                .build();


        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(Math.max(1, Math.min(topK, 20)))
                .similarityThreshold(0.5)
                .build();

        return store.similaritySearch(request);
    }

}
