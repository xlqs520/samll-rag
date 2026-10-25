package com.xlqs.smallrag.vo;

import lombok.Data;

import java.util.List;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/14 15:42
 * @description KnowledgeBaseCreateVo 类说明：TODO
 * @since 2026/10/14
 */

@Data
public class KnowledgeBaseCreateVo {

    /**
     * 知识库名称
     */
    private String name;
    /**
     * 知识库描述
     */
    private String description;

    /**
     * 知识库数据类型
     * 例如：DOCUMENT (传统文档), JSON (结构化数据)
     */
    private String dataType;

    /**
     * 向量化模型
     */
    private String embeddingModel;

    /**
     * JSON 知识库核心：指定哪些字段拼成文本去转向量
     */
    private List<String> vectorFields;

    /**
     * 可选：允许用于元数据过滤的字段列表
     */
    private List<String> filterFields;

    /**
     * 可选：分片大小
     */
    private Integer chunkSize;

    /**
     * 可选：分片重叠
     */
    private Integer chunkOverlap;

}
