-- liquibase formatted sql

-- changeset developer:002-create-knowledge-document
-- comment: 初始化创建 knowledge_document 知识库文档表

CREATE TABLE knowledge_document (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文档主键ID',

    knowledge_base_id BIGINT NOT NULL COMMENT '所属知识库ID',

    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户ID，0表示默认租户',

    llama_document_id VARCHAR(36) COMMENT 'LlamaIndex原始文档UUID',

    name VARCHAR(255) NOT NULL COMMENT '文档名称',

    file_type VARCHAR(50) COMMENT '文件类型或格式，例如pdf、docx、txt',

    file_uri VARCHAR(1000) COMMENT '原始文件存储地址，例如MinIO对象地址',

    file_size BIGINT NOT NULL DEFAULT 0 COMMENT '原始文件大小，单位字节',

    file_hash CHAR(64) COMMENT '原始文件SHA-256哈希值，用于文件去重',

    language VARCHAR(20) COMMENT '文档语言，例如zh、en',

    doc_version VARCHAR(50) COMMENT '文档业务版本',

    is_shared TINYINT(1) NOT NULL DEFAULT 0
        COMMENT '是否共享：0-私有，1-共享',

    metadata JSON COMMENT '文档扩展元数据，例如来源、作者及自定义属性',

    chunk_count INT NOT NULL DEFAULT 0 COMMENT '文档切片数量',

    vector_status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        COMMENT '向量处理状态：PENDING-待处理，PROCESSING-处理中，COMPLETED-已完成，FAILED-失败',

    error_message TEXT COMMENT '向量处理失败时的错误信息',

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '创建时间',

    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',

    deleted_at DATETIME NULL COMMENT '删除时间，NULL表示未删除',

    KEY idx_document_kb_status (knowledge_base_id, vector_status),

    KEY idx_document_tenant_kb (tenant_id, knowledge_base_id),

    KEY idx_document_file_hash (knowledge_base_id, file_hash),

    KEY idx_document_llama_id (llama_document_id)

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COMMENT='知识库文档表';
