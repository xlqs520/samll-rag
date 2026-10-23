package com.xlqs.smallrag.knowledge;


import com.xlqs.smallrag.entity.KnowledgeBase;
import com.xlqs.smallrag.entity.KnowledgeDocumentJson;
import com.xlqs.smallrag.mapper.KnowledgeBaseDao;
import com.xlqs.smallrag.mapper.KnowledgeDocumentJsonDao;
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
public class JsonDocumentService {

    @Autowired
    private QdrantClient qdrantClient;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private KnowledgeBaseDao knowledgeBaseDao;

    @Autowired
    private KnowledgeDocumentJsonDao knowledgeDocumentJsonDao;

    /**
     * 导入一条 JSON 数据到指定的 Qdrant 集合（不分片）
     *
     * @param collectionName 知识库对应的 Qdrant 集合名称
     * @param jsonMap        要导入的 JSON 格式数据
     * @param vectorFields   配置的参与向量化的字段列表，如 ["name", "description"]
     */
    public void addJsonRecord(String collectionName, Map<String, Object> jsonMap, List<String> vectorFields) {
        // 1. 构造向量化文本
        String contentText = buildVectorText(jsonMap, vectorFields);

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

    /**
     * 导入一条 JSON 数据：按知识库配置的 vector_fields 向量化后写入 Qdrant，
     * 同时保存文档信息与 JSON 向量化记录到数据库
     *
     * @param knowledgeBaseId 知识库ID
     * @param documentName    文档名称
     * @param jsonMap         要导入的 JSON 格式数据
     * @return 生成的 JSON 记录主键ID
     */
    public Long importJsonRecord(Long knowledgeBaseId, String documentName, Map<String, Object> jsonMap) {
        KnowledgeBase knowledgeBase = knowledgeBaseDao.selectById(knowledgeBaseId);
        if (knowledgeBase == null) {
            throw new IllegalArgumentException("知识库不存在: " + knowledgeBaseId);
        }
        List<String> vectorFields = knowledgeBase.getVectorFields();
        if (vectorFields == null || vectorFields.isEmpty()) {
            throw new IllegalArgumentException("知识库未配置向量化字段: " + knowledgeBaseId);
        }

        String contentText = buildVectorText(jsonMap, vectorFields);
        if (contentText.isBlank()) {
            throw new IllegalArgumentException("没有可用于向量化的字段");
        }

        String vectorId = UUID.randomUUID().toString();

        KnowledgeDocumentJson documentJson = KnowledgeDocumentJson.builder()
                .knowledgeBaseId(knowledgeBaseId)
                .documentCategory(knowledgeBase.getDataType())
                .qdrantVectorId(vectorId)
                .documentName(documentName)
                .rawJsonData(jsonMap)
                .vectorText(contentText)
                .vectorStatus("PROCESSING")
                .build();
        knowledgeDocumentJsonDao.insert(documentJson);

        try {
            Map<String, Object> metadata = new HashMap<>(jsonMap);
            Document document = new Document(vectorId, contentText, metadata);
            QdrantVectorStore store = QdrantVectorStore.builder(qdrantClient, embeddingModel)
                    .collectionName(knowledgeBase.getCollectionName())
                    .build();
            store.add(List.of(document));

            KnowledgeDocumentJson update = new KnowledgeDocumentJson();
            update.setId(documentJson.getId());
            update.setVectorStatus("SUCCESS");
            knowledgeDocumentJsonDao.updateById(update);

            return documentJson.getId();
        } catch (RuntimeException e) {
            KnowledgeDocumentJson failed = new KnowledgeDocumentJson();
            failed.setId(documentJson.getId());
            failed.setVectorStatus("FAILED");
            knowledgeDocumentJsonDao.updateById(failed);
            throw e;
        }
    }

    private String buildVectorText(Map<String, Object> jsonMap, List<String> vectorFields) {
        return vectorFields.stream()
                .filter(jsonMap::containsKey)
                .map(field -> field + ": " + jsonMap.get(field))
                .collect(Collectors.joining("; "));
    }
}
