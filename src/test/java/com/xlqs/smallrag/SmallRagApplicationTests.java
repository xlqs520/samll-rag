package com.xlqs.smallrag;


import com.xlqs.smallrag.entity.KnowledgeBase;
import com.xlqs.smallrag.knowledge.JsonDocumentServe;
import com.xlqs.smallrag.knowledge.KnowledgeBaseService;
import com.xlqs.smallrag.mapper.KnowledgeBaseDao;
import com.xlqs.smallrag.qdrant.CollectionHelper;
import com.xlqs.smallrag.utils.JsonHelper;
import com.xlqs.smallrag.vo.KnowledgeBaseCreateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
class SmallRagApplicationTests {

    @Autowired
    private KnowledgeBaseDao knowledgeBaseDao;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private CollectionHelper collectionHelper;

    @Autowired
    private JsonDocumentServe jsonDocumentServe;

    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    @Autowired
    private JsonHelper jsonHelper;

    @Test
    void contextLoads() {
        KnowledgeBase knowledgeBase = knowledgeBaseDao.selectAllKnowledgeBase().getFirst();
        jsonHelper.objectToMap(knowledgeBase.getFilterFields()).entrySet().stream()
                .filter(entry -> {
                    Object val = entry.getValue();
                    return Boolean.TRUE.equals(val) || "true".equalsIgnoreCase(String.valueOf(val));
                })
                .map(Map.Entry::getKey)
                .toList()
                .forEach(System.out::println);
    }

    @Test
    void testEmbeddingModel() {
        System.out.println(Arrays.toString(embeddingModel.embed("hello world")));
    }

    @Test
    void testCollectionHelper() {
        collectionHelper.createCollection("kb_tenant_1001_products", 1024);
    }

    @Test
    void testDeleteCollection() {
        collectionHelper.deleteCollection("kb_tenant_1001_products");
    }

    @Test
    void testAddJsonRecord() {
        String collectionName = "kb_tenant_1001_products";

        Map<String, Object> jsonMap = Map.of(
                "title", "Spring Boot Redis 配置",
                "content", "Spring Boot 如何连接 Redis",
                "category", "java"
        );

        List<String> vectorFields = List.of("title", "content");

        jsonDocumentServe.addJsonRecord(collectionName, jsonMap, vectorFields);

        System.out.println("写入成功，Collection: " + collectionName);
    }

    @Test
    void testCreateKnowledgeBaseName() {
        System.out.println(knowledgeBaseService.generateCollectionName(0L));
    }


    @Test
    public void testCreateKnowledgeBase() {
        // 1. 模拟前端传入的 DTO 参数（不再需要手动传 collectionName）
        KnowledgeBaseCreateDto dto = new KnowledgeBaseCreateDto();
        dto.setName("商品知识库-" + System.currentTimeMillis()); // 加上时间戳防重
        dto.setDescription("这是一个用于测试创建 JSON 知识库的用例");
        dto.setDataType("JSON");
        dto.setVectorFields(Map.of("product_name", true, "description", true));
        dto.setFilterFields(Map.of("category", true, "brand", true));

        // 2. 模拟租户 ID
        Long tenantId = 1001L;

        // 3. 执行创建方法
        System.out.println("====== 开始测试创建知识库 ======");
        Long kbId = knowledgeBaseService.createKnowledgeBase(dto, tenantId);

        // 4. 验证结果
        System.out.println("🎉 知识库创建成功，生成的主键 ID 为: " + kbId);
        Assertions.assertNotNull(kbId, "知识库 ID 不应为空");

        // 如果你希望在测试完后自动清理，也可以在这里写一行删除逻辑
        // knowledgeBaseService.deleteKnowledgeBase(kbId);
    }

}
