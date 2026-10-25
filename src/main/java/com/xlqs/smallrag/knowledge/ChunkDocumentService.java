package com.xlqs.smallrag.knowledge;

import com.xlqs.smallrag.entity.KnowledgeBase;
import com.xlqs.smallrag.entity.KnowledgeChunk;
import com.xlqs.smallrag.entity.KnowledgeDocument;
import com.xlqs.smallrag.entity.KnowledgeDocumentFile;
import com.xlqs.smallrag.mapper.KnowledgeBaseDao;
import com.xlqs.smallrag.mapper.KnowledgeChunkDao;
import com.xlqs.smallrag.mapper.KnowledgeDocumentDao;
import io.qdrant.client.QdrantClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 15:00
 * @description 文档分片入库服务：将分片写入 knowledge_chunk 并向量化写入 Qdrant
 * @since 2026/10/23
 */
@Service
public class ChunkDocumentService {

    @Autowired
    private QdrantClient qdrantClient;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private KnowledgeBaseDao knowledgeBaseDao;

    @Autowired
    private KnowledgeDocumentDao knowledgeDocumentDao;

    @Autowired
    private KnowledgeChunkDao knowledgeChunkDao;

    @Autowired
    private KnowledgeDocumentService knowledgeDocumentService;

    /**
     * 保存文档分片：先写入 knowledge_chunk（状态 PROCESSING），
     * 再向量化写入 Qdrant，最后更新分片与文档的向量化状态
     *
     * @param knowledgeBaseId 知识库ID
     * @param documentId      文档ID
     * @param chunks          文本分片
     * @param file            所属文件信息，可为空
     * @return 保存的分片数量
     */
    public int saveChunks(Long knowledgeBaseId, Long documentId, List<Document> chunks, Map<String, Object> extra,
                          KnowledgeDocumentFile file) {
        if (chunks == null || chunks.isEmpty()) {
            return 0;
        }

        KnowledgeBase knowledgeBase = knowledgeBaseDao.selectById(knowledgeBaseId);
        if (knowledgeBase == null) {
            throw new IllegalArgumentException("知识库不存在: " + knowledgeBaseId);
        }

        List<KnowledgeChunk> records = new ArrayList<>(chunks.size());
        List<Document> vectorDocuments = new ArrayList<>(chunks.size());

        for (int i = 0; i < chunks.size(); i++) {
            Document chunk = chunks.get(i);
            String vectorId = UUID.randomUUID().toString();

            Map<String, Object> metadata = new HashMap<>(chunk.getMetadata());
            metadata.put("knowledge_base_id", knowledgeBaseId);
            metadata.put("document_id", documentId);
            if (extra != null) {
                metadata.putAll(extra);
            }

            KnowledgeChunk record = KnowledgeChunk.builder()
                    .knowledgeBaseId(knowledgeBaseId)
                    .documentId(documentId)
                    .chunkIndex(resolveChunkIndex(metadata, i))
                    .content(chunk.getText())
                    .metadata(metadata)
                    .qdrantPointId(vectorId)
                    .vectorStatus("PROCESSING")
                    .build();
            knowledgeChunkDao.insert(record);
            records.add(record);

            vectorDocuments.add(new Document(vectorId, chunk.getText(), metadata));
        }

        try {
            QdrantVectorStore store = QdrantVectorStore.builder(qdrantClient, embeddingModel)
                    .collectionName(knowledgeBase.getCollectionName())
                    .build();
            store.add(vectorDocuments);

            updateStatus(records, "SUCCESS");

            if (file != null) {
                KnowledgeDocumentFile uploadedFile = new KnowledgeDocumentFile();
                uploadedFile.setId(file.getId());
                uploadedFile.setFileStatus("UPLOADED");
                knowledgeDocumentService.updateDocumentFile(uploadedFile);
            }
        } catch (RuntimeException e) {
            updateStatus(records, "FAILED");

            if (file != null) {
                KnowledgeDocumentFile failedFile = new KnowledgeDocumentFile();
                failedFile.setId(file.getId());
                failedFile.setFileStatus("FAILED");
                failedFile.setErrorMessage(e.getMessage());
                knowledgeDocumentService.updateDocumentFile(failedFile);
            }

            KnowledgeDocument failed = new KnowledgeDocument();
            failed.setId(documentId);
            failed.setVectorStatus("FAILED");
            failed.setErrorMessage(e.getMessage());
            knowledgeDocumentDao.updateById(failed);
            throw e;
        }

        KnowledgeDocument update = new KnowledgeDocument();
        update.setId(documentId);
        update.setChunkCount(records.size());
        update.setVectorStatus("COMPLETED");
        knowledgeDocumentDao.updateById(update);

        return records.size();
    }

    private void updateStatus(List<KnowledgeChunk> records, String status) {
        for (KnowledgeChunk record : records) {
            KnowledgeChunk update = new KnowledgeChunk();
            update.setId(record.getId());
            update.setVectorStatus(status);
            knowledgeChunkDao.updateById(update);
        }
    }

    private int resolveChunkIndex(Map<String, Object> metadata, int fallback) {
        Object value = metadata.get("chunk_index");
        return value instanceof Number number ? number.intValue() : fallback;
    }
}
