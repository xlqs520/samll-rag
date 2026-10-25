package com.xlqs.smallrag.knowledge;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xlqs.smallrag.entity.KnowledgeBase;
import com.xlqs.smallrag.entity.KnowledgeChunk;
import com.xlqs.smallrag.entity.KnowledgeDocument;
import com.xlqs.smallrag.mapper.KnowledgeBaseDao;
import com.xlqs.smallrag.mapper.KnowledgeChunkDao;
import com.xlqs.smallrag.mapper.KnowledgeDocumentDao;
import com.xlqs.smallrag.qdrant.CollectionHelper;
import com.xlqs.smallrag.vo.KnowledgeBaseCreateVo;
import com.xlqs.smallrag.vo.KnowledgeBaseStatisticsVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/14 17:53
 * @description KnowledgeBaseService 类说明：TODO
 * @since 2026/10/14
 */
@Service
@RequiredArgsConstructor
public class KnowledgeBaseService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

    private final CollectionHelper collectionHelper;

    private final KnowledgeBaseDao knowledgeBaseDao;

    @Value("${spring.ai.openai.embedding.options.model}")
    private String embeddingModelName;

    private final KnowledgeDocumentDao knowledgeDocumentDao;

    private final KnowledgeChunkDao knowledgeChunkDao;

    public Long createKnowledgeBase(KnowledgeBaseCreateVo dto, Long tenantId) {

        long count = knowledgeBaseDao.selectCount(
                new LambdaQueryWrapper<KnowledgeBase>()
                        .eq(KnowledgeBase::getTenantId, tenantId)
                        .eq(KnowledgeBase::getName, dto.getName())
        );
        if (count > 0) {
            throw new RuntimeException("该租户下已存在同名知识库");
        }
        KnowledgeBase knowledgeBase = KnowledgeBase.builder()
                .tenantId(tenantId)
                .name(dto.getName())
                .description(dto.getDescription())
                .embeddingModel(dto.getEmbeddingModel() != null ? dto.getEmbeddingModel() : embeddingModelName)
                .vectorDimension(1024)
                .collectionName(generateCollectionName(tenantId))
                .dataType(dto.getDataType())
                .vectorFields(dto.getVectorFields())
                .filterFields(dto.getFilterFields())
                .status("active")
                .build();
        collectionHelper.createCollection(knowledgeBase.getCollectionName(), 1024);
        knowledgeBaseDao.insert(knowledgeBase);
        return knowledgeBase.getId();

    }

    public List<KnowledgeBase> listKnowledgeBases(Long tenantId, String kbName, String dataType) {
        return knowledgeBaseDao.selectList(
                new LambdaQueryWrapper<KnowledgeBase>()
                        .eq(KnowledgeBase::getTenantId, tenantId)
                        .like(kbName != null && !kbName.isBlank(),
                                KnowledgeBase::getName, kbName)
                        .eq(dataType != null && !dataType.isBlank(),
                                KnowledgeBase::getDataType, dataType)
        );
    }

    public KnowledgeBase getKnowledgeBaseDetail(String kbId, Long tenantId) {
        return knowledgeBaseDao.selectOne(
                new LambdaQueryWrapper<KnowledgeBase>()
                        .eq(KnowledgeBase::getId, kbId)
                        .eq(KnowledgeBase::getTenantId, tenantId)
        );
    }

    public void updateKnowledgeBase(Long kbId, Long tenantId, KnowledgeBaseCreateVo dto){
        KnowledgeBase knowledgeBase = knowledgeBaseDao.selectOne(
                new LambdaQueryWrapper<KnowledgeBase>()
                        .eq(KnowledgeBase::getId, kbId)
                        .eq(KnowledgeBase::getTenantId, tenantId)
        );
        if (knowledgeBase == null) {
            throw new RuntimeException("知识库不存在");
        }
        knowledgeBaseDao.updateKnowledgeBase(kbId, tenantId, dto);
    }

    public String deleteKnowledgeBase(Long kbId, Long tenantId) {
        KnowledgeBase knowledgeBase = knowledgeBaseDao.selectOne(
                new LambdaQueryWrapper<KnowledgeBase>()
                        .eq(KnowledgeBase::getId, kbId)
                .eq(KnowledgeBase::getTenantId, tenantId)
        );
        if (knowledgeBase == null) {
            throw new RuntimeException("知识库不存在");
        }
        knowledgeBaseDao.deleteById(knowledgeBase.getId());
        collectionHelper.deleteCollection(knowledgeBase.getCollectionName());
        return knowledgeBase.getCollectionName();
    }

    public KnowledgeBaseStatisticsVo getKnowledgeBaseStatistics(Long kbId, Long tenantId) {

        KnowledgeBaseStatisticsVo statisticsVo = new KnowledgeBaseStatisticsVo();
        if (knowledgeBaseDao.selectById(kbId) == null) {
            throw new RuntimeException("知识库不存在");
        }

        List<KnowledgeDocument> knowledgeDocuments =
                knowledgeDocumentDao.selectList(
                        new LambdaQueryWrapper<KnowledgeDocument>()
                                .eq(KnowledgeDocument::getKnowledgeBaseId, kbId)
                                .eq(KnowledgeDocument::getTenantId, tenantId)
                );

        KnowledgeBase knowledgeBase = knowledgeBaseDao.selectById(kbId);

        statisticsVo.setKnowledgeBaseId(kbId);
        if ("Json".equals(knowledgeBase.getDataType())){
            statisticsVo.setDocumentCount((long) knowledgeDocuments.size());
            statisticsVo.setChunkCount((long) knowledgeDocuments.size());
            statisticsVo.setTotalFileSize(0L);
            return statisticsVo;
        }
        statisticsVo.setDocumentCount((long) knowledgeDocuments.size());

        Long chunkCount = knowledgeChunkDao.selectCount(
                new LambdaQueryWrapper<KnowledgeChunk>()
                        .eq(KnowledgeChunk::getKnowledgeBaseId, kbId)
        );
        statisticsVo.setChunkCount(chunkCount);

        List<Map<String, Object>> maps = knowledgeDocumentDao.selectMaps(
                new QueryWrapper<KnowledgeDocument>()
                        .select("COALESCE(SUM(file_size), 0) AS total_size")
                        .eq("knowledge_base_id", kbId)
        );
        Long totalSize = maps.stream()
                .findFirst()
                .map(m -> m.get("total_size"))
                .map(v -> ((Number) v).longValue())
                .orElse(0L);
        statisticsVo.setTotalFileSize(totalSize);

        return statisticsVo;
    }


    public String generateCollectionName(Long tenantId) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        // 最终生成的 Collection 名称如: kb_t1001_2026-10-14-03-09-45
        return String.format("kb_no_%d_%s", tenantId, timestamp);
    }
}
