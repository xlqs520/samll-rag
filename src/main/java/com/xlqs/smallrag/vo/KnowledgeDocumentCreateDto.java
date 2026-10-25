package com.xlqs.smallrag.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 16:00
 * @description 文档创建入参
 * @since 2026/10/23
 */
@Data
public class KnowledgeDocumentCreateDto {

    /**
     * 所属知识库ID
     */
    private Long knowledgeBaseId;

    /**
     * 租户ID，0表示默认租户
     */
    private Long tenantId;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 文件类型或格式，例如pdf、docx、txt
     */
    private String fileType;

    /**
     * 原始文件存储地址，例如MinIO对象地址
     */
    private String fileUri;

    /**
     * 原始文件大小，单位字节
     */
    private Long fileSize;

    /**
     * 原始文件SHA-256哈希值，用于文件去重
     */
    private String fileHash;

    /**
     * 文档语言，例如zh、en
     */
    private String language;

    /**
     * 文档业务版本
     */
    private String docVersion;

    /**
     * 是否共享：0-私有，1-共享
     */
    private Integer isShared;

    /**
     * 文档扩展元数据，例如来源、作者及自定义属性
     */
    private Map<String, Object> metadata;
}
