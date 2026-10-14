-- liquibase formatted sql

-- changeset xlqs:001-create-knowledge-base
-- comment: 初始化创建 knowledge_base 知识库表

CREATE TABLE knowledge_base (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '知识库主键ID',

    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户ID，0表示默认租户',

    name VARCHAR(100) NOT NULL COMMENT '知识库名称，同一租户下唯一',

    description VARCHAR(500) COMMENT '知识库描述',

    embedding_model VARCHAR(100) NOT NULL COMMENT '向量嵌入模型名称',

    vector_dimension INT NOT NULL DEFAULT 1024 COMMENT '向量维度，需与嵌入模型输出维度一致',

    collection_name VARCHAR(100) NOT NULL COMMENT 'Qdrant向量集合名称，全局唯一',

    vector_fields JSON COMMENT '参与向量化的字段列表',

    filter_fields JSON COMMENT '允许用于元数据过滤的字段列表',

    chunk_size INT NOT NULL DEFAULT 500 COMMENT '文档分片大小，通常按字符数或Token数计算',

    chunk_overlap INT NOT NULL DEFAULT 50 COMMENT '相邻文档分片之间的重叠大小',

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '知识库状态：ACTIVE启用，DISABLED禁用',

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',

    deleted_at DATETIME NULL COMMENT '删除时间，NULL表示未删除',

    UNIQUE KEY uk_kb_tenant_name (tenant_id, name),

    UNIQUE KEY uk_kb_collection (collection_name),

    KEY idx_kb_tenant_status (tenant_id, status)

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COMMENT='知识库配置表';

-- changeset xlqs:002-add-data-type-to-knowledge-base
-- comment: 为 knowledge_base 表新增 data_type 数据源类型字段

ALTER TABLE knowledge_base
    ADD COLUMN data_type VARCHAR(30) NOT NULL DEFAULT 'DOCUMENT'
        COMMENT '数据源类型：DOCUMENT(传统文档), JSON(结构化数据)'
        AFTER description;
