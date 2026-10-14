package com.xlqs.smallrag.knowledge;

import io.qdrant.client.QdrantClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/14 17:17
 * @description JsonDocumentServer 类说明：TODO
 * @since 2026/10/14
 */
@Service
public class JsonDocumentServe {

    @Autowired
    private QdrantClient qdrantClient;

    @Autowired
    private EmbeddingModel embeddingModel;

    /**
     * 导入一条 JSON 数据到指定的 Qdrant 集合（不分片）
     *
     * @param collectionName 知识库对应的 Qdrant 集合名称
     * @param jsonMap        要导入的 JSON 格式数据
     * @param vectorFields   配置的参与向量化的字段列表，如 ["name", "description"]
     */
    public void addJsonRecord(String collectionName, Map<String, Object> jsonMap, List<String> vectorFields) {
        // 1. 构造向量化文本
        String contentText = vectorFields.stream()
                .filter(jsonMap::containsKey)
                .map(field -> field + ": " + jsonMap.get(field))
                .collect(Collectors.joining("; "));

        if (contentText.isBlank()) {
            throw new IllegalArgumentException("没有可用于向量化的字段");
        }
        // 2. 提取过滤元数据
        Map<String, Object> metadata = new HashMap<>(jsonMap);
        // 3. 构造 Document
        String docId = UUID.randomUUID().toString();
        Document document = new Document(docId, contentText, metadata);
        // 指定集合、向量化模型
        QdrantVectorStore store = QdrantVectorStore.builder(qdrantClient, embeddingModel)
                .collectionName(collectionName)
                .build();
        store.add(List.of(document));
    }
}
