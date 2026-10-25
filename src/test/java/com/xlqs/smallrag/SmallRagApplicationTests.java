package com.xlqs.smallrag;


import com.xlqs.smallrag.entity.KnowledgeBase;
import com.xlqs.smallrag.entity.KnowledgeDocument;
import com.xlqs.smallrag.entity.KnowledgeDocumentJson;
import com.xlqs.smallrag.knowledge.ChunkDocumentService;
import com.xlqs.smallrag.knowledge.JsonDocumentService;
import com.xlqs.smallrag.knowledge.KnowledgeBaseService;
import com.xlqs.smallrag.mapper.KnowledgeBaseDao;
import com.xlqs.smallrag.mapper.KnowledgeDocumentDao;
import com.xlqs.smallrag.mapper.KnowledgeDocumentJsonDao;
import com.xlqs.smallrag.qdrant.CollectionHelper;
import com.xlqs.smallrag.split.TextLoadService;
import com.xlqs.smallrag.split.TextSplitService;
import com.xlqs.smallrag.storage.MinioService;
import com.xlqs.smallrag.utils.EmbeddingModelFactory;
import com.xlqs.smallrag.vo.KnowledgeBaseCreateVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
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
    private JsonDocumentService jsonDocumentService;

    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    @Autowired
    private KnowledgeDocumentJsonDao knowledgeDocumentJsonDao;

    @Test
    void contextLoads() {
        KnowledgeBase knowledgeBase = knowledgeBaseDao.selectAllKnowledgeBase().getFirst();
        knowledgeBase.getFilterFields().forEach(System.out::println);
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
        String collectionName = "default_collection";

        Map<String, Object> jsonMap = Map.of(
                "title", "Spring Boot Redis 配置",
                "content", "Spring Boot 如何连接 Redis",
                "category", "java"
        );

        List<String> vectorFields = List.of("title", "content");

        jsonDocumentService.addJsonRecord(collectionName, jsonMap, vectorFields);

        System.out.println("写入成功，Collection: " + collectionName);
    }

    @Test
    void testImportJsonRecord() {

        Map<String, Object> jsonMap = Map.of(
                "product_name", "iPhone 15 Pro",
                "description", "苹果旗舰手机，钛金属边框",
                "category", "手机",
                "brand", "Apple"
        );

        Long recordId = jsonDocumentService.importJsonRecord(1L, "iPhone 15 Pro 商品数据", jsonMap);
        KnowledgeDocumentJson record = knowledgeDocumentJsonDao.selectById(recordId);
        System.out.println("🎉 JSON 记录导入成功，记录 ID: " + recordId
                + "，Qdrant 向量 ID: " + record.getQdrantVectorId());
    }

    @Test
    void testCreateKnowledgeBaseName() {
        System.out.println(knowledgeBaseService.generateCollectionName(0L));
    }


    @Test
    public void testCreateKnowledgeBase() {
        // 1. 模拟前端传入的 DTO 参数（不再需要手动传 collectionName）
        KnowledgeBaseCreateVo dto = new KnowledgeBaseCreateVo();
        dto.setName("商品知识库-" + System.currentTimeMillis()); // 加上时间戳防重
        dto.setDescription("这是一个用于测试创建 text 知识库的用例");
        dto.setDataType("JSON");
        dto.setVectorFields(List.of("product_name", "description"));
        dto.setFilterFields(List.of("category", "brand"));

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

    @Autowired
    private TextSplitService textSplitService;

    @Autowired
    private TextLoadService textLoadService;

    @Autowired
    private ChunkDocumentService chunkDocumentService;

    @Autowired
    private MinioService minioService;

    @Test
    public void testSplitText() {

        List<Document> chunks = textSplitService.split(
                textLoadService.readText("classpath:guide/redis.txt"),
                2L
        );

        for (Document chunk : chunks) {
            System.out.println("分片内容：" + chunk.getText());
            System.out.println("分片元数据：" + chunk.getMetadata());
        }
    }

    @Test
    public void testCreateKnowledgeBaseText() {
        // 1. 模拟前端传入的 DTO 参数（不再需要手动传 collectionName）
        KnowledgeBaseCreateVo dto = new KnowledgeBaseCreateVo();
        dto.setName("Redis知识库-" + System.currentTimeMillis()); // 加上时间戳防重
        dto.setDescription("这是一个用于测试创建 text 知识库的用例");
        dto.setDataType("Document");
        dto.setVectorFields(List.of("doc_content"));
        dto.setFilterFields(List.of("userId"));

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

    @Autowired
    private KnowledgeDocumentDao knowledgeDocumentDao;

    @Test
    public void testSaveChunks() {
        Long knowledgeBaseId = 2L;
        // 1. 创建知识文档并保存到数据库
        KnowledgeDocument document = new KnowledgeDocument();
        document.setKnowledgeBaseId(knowledgeBaseId);
        document.setTenantId(0L);
        document.setName("Redis 配置指南");

        knowledgeDocumentDao.insert(document);

        // 2. 获取真实的数据库主键
//        Long documentId = document.getId();
//
//        Assertions.assertNotNull(documentId, "文档 ID 不应为空");
//
//        List<Document> chunks = textSplitService.split(
//                textLoadService.readText("classpath:guide/redis.txt"),
//                documentId
//        );
//
//        int saved = chunkDocumentService.saveChunks(knowledgeBaseId, documentId, chunks, null);
//
//        Assertions.assertTrue(saved > 0, "应至少保存一个分片");
//        System.out.println("🎉 分片保存成功，数量: " + saved);
    }

    @Test
    public void testMinio() {
        String content = "MinIO 连通性测试 " + System.currentTimeMillis();
        String objectName = "test/minio-check-" + System.currentTimeMillis() + ".txt";

        String uploaded = minioService.upload(objectName, content.getBytes(StandardCharsets.UTF_8), "text/plain");
        Assertions.assertEquals(objectName, uploaded, "上传应返回对象名称");

        String url = minioService.getPresignedUrl(objectName, 10);
        Assertions.assertNotNull(url, "临时访问地址不应为空");
        System.out.println("🎉 MinIO 上传成功，对象: " + uploaded);
        System.out.println("访问地址: " + url);

//        minioService.delete(objectName);
//        System.out.println("🗑️ 测试对象已删除");
    }

    @Autowired
    private EmbeddingModelFactory embeddingModelFactory;

    @Test
    public void testEmbedding() {
        EmbeddingModel model = embeddingModelFactory.create(
                "http://localhost:11434/v1",
                "1111",
                "quentinz/bge-large-zh-v1.5:q8_0",
                1024
        );
        float[] vector = model.embed("Spring Boot 如何连接 Redis");
        System.out.println(vector.length); // 1024
    }

    @Test
    public void testEmbedding01() {
        EmbeddingModel model = embeddingModelFactory.create(
                "https://ark.cn-beijing.volces.com/api/coding/v3",
                "******",
                "doubao-embedding-vision",
                1024
        );
        float[] vector = model.embed("Spring Boot 如何连接 Redis");
        System.out.println(vector.length); // 1024
    }

    @Test
    public void testEmbedding2() {
        float[] vector = embeddingModel.embed("Spring Boot 如何连接 Redis");
        System.out.println(vector.length); // 1024
    }

}
