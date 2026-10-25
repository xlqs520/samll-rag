package com.xlqs.smallrag.vo;

import lombok.Data;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/25 19:07
 * @description KnowledgeBaseStatisticsVo 类说明：知识库统计类
 * @since 2026/10/25
 */
@Data
public class KnowledgeBaseStatisticsVo {
    /**
     * 知识库 ID
     */
    private Long knowledgeBaseId;

    /**
     * 文档数量
     */
    private Long documentCount;

    /**
     * 分片数量
     */
    private Long chunkCount;

    /**
     * 原始文件总大小，单位：字节
     */
    private Long totalFileSize;
}
