package com.xlqs.smallrag.knowledge;

import com.xlqs.smallrag.entity.KnowledgeBase;
import com.xlqs.smallrag.mapper.KnowledgeBaseDao;
import com.xlqs.smallrag.qdrant.CollectionHelper;
import com.xlqs.smallrag.vo.KnowledgeBaseCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/14 17:53
 * @description KnowledgeBaseService 类说明：TODO
 * @since 2026/10/14
 */
@Service
public class KnowledgeBaseService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

    @Autowired
    private CollectionHelper collectionHelper;

    @Autowired
    private KnowledgeBaseDao knowledgeBaseDao;

    @Value("${spring.ai.openai.embedding.options.model}")
    private String embeddingModelName;

    public Long createKnowledgeBase(KnowledgeBaseCreateDto dto, Long tenantId) {

        long count = knowledgeBaseDao.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<KnowledgeBase>()
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
                .embeddingModel(embeddingModelName)
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




    public String generateCollectionName(Long tenantId) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        // 最终生成的 Collection 名称如: kb_t1001_2026-10-14-03-09-45
        return String.format("kb_no_%d_%s", tenantId, timestamp);
    }
}
