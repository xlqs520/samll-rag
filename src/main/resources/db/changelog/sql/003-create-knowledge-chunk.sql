-- liquibase formatted sql

-- changeset developer:003-create-knowledge-chunk
-- comment: 创建知识库文档分片表，存储文档切分后的文本及向量关联信息

CREATE TABLE knowledge_chunk (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分片主键ID',

    knowledge_base_id BIGINT NOT NULL COMMENT '所属知识库ID',
    document_id BIGINT NOT NULL COMMENT '所属文档ID',
    chunk_index INT NOT NULL COMMENT '分片在原文档中的序号，从0开始',

    llama_node_id VARCHAR(36) COMMENT 'LlamaIndex节点UUID，用于关联原始节点',
    content MEDIUMTEXT NOT NULL COMMENT '分片文本内容，作为向量化和检索的主要文本',

    metadata JSON COMMENT '分片扩展元数据，例如页码、标题、来源及业务属性',

    qdrant_point_id VARCHAR(64) COMMENT 'Qdrant向量点ID，用于关联向量数据库中的记录',
    vector_status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        COMMENT '向量处理状态：PENDING-待处理，PROCESSING-处理中，SUCCESS-成功，FAILED-失败',

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '分片记录创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
        COMMENT '分片记录最后更新时间',

    UNIQUE KEY uk_chunk_document_index (document_id, chunk_index)
        COMMENT '保证同一文档内的分片序号唯一',

    UNIQUE KEY uk_chunk_qdrant_point (qdrant_point_id)
        COMMENT '保证Qdrant向量点ID唯一',

    KEY idx_chunk_kb_document (knowledge_base_id, document_id)
        COMMENT '按知识库和文档查询分片',

    KEY idx_chunk_document_status (document_id, vector_status)
        COMMENT '按文档和向量处理状态查询分片'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COMMENT='知识库文档分片表';
