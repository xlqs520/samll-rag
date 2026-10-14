package com.xlqs.smallrag.qdrant;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/14 17:03
 * @description CollectionHelper 类说明：TODO
 * @since 2026/10/14
 */

@Service
public class CollectionHelper {

    @Autowired
    private QdrantClient qdrantClient;

    /**
     * 在 Qdrant 中创建集合
     *
     * @param collectionName 集合名称（全局唯一，例如：kb_tenant_1001_products）
     * @param vectorDimension 向量维度（例如：bge-large-zh 是 1024）
     */
    public void createCollection(String collectionName, int vectorDimension) {
        try {
            // 构建集合参数：指定维度和距离度量方式（通常用 Cosine 余弦相似度）
            Collections.VectorParams vectorParams = Collections.VectorParams.newBuilder()
                    .setSize(vectorDimension)
                    .setDistance(Collections.Distance.Cosine)
                    .build();

            // 发起异步创建请求并同步等待完成
            qdrantClient.createCollectionAsync(collectionName, vectorParams).get();

            System.out.println("🎉 Qdrant 集合 [" + collectionName + "] 创建成功！维度: " + vectorDimension);

        } catch (Exception e) {
            // 如果集合已存在或其他异常，按需处理
            throw new RuntimeException("Qdrant 集合创建失败: " + e.getMessage(), e);
        }
    }

    /**
     * 在 Qdrant 中删除集合
     *
     * @param collectionName 要删除的集合名称
     */
    public void deleteCollection(String collectionName) {
        try {
            // 异步发起删除请求并同步等待完成
            qdrantClient.deleteCollectionAsync(collectionName).get();

            System.out.println("🗑️ Qdrant 集合 [" + collectionName + "] 已成功删除！");

        } catch (Exception e) {
            // 抛出运行时异常，可用于触发数据库事务回滚
            throw new RuntimeException("Qdrant 集合删除失败: " + e.getMessage(), e);
        }
    }
}
