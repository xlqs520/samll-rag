-- liquibase formatted sql

-- changeset developer:006-create-knowledge-document-json
-- comment: 初始化创建知识库文档及JSON结构化数据表

CREATE TABLE knowledge_document_json (

   id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',

   knowledge_base_id BIGINT NOT NULL COMMENT '所属知识库ID',

   qdrant_vector_id VARCHAR(100) COMMENT 'Qdrant向量数据库中的向量ID',

   document_name VARCHAR(255) NOT NULL COMMENT '文档名称',

   document_category VARCHAR(100) COMMENT '文档分类',

   raw_json_data JSON NOT NULL COMMENT '原始JSON数据',

   vector_text TEXT NOT NULL COMMENT '实际向量化文本（用于生成向量的纯文本内容）',

   vector_status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '向量化状态：PENDING-待处理，PROCESSING-处理中，SUCCESS-成功，FAILED-失败',

   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

   KEY idx_doc_json_kb_id (knowledge_base_id),

   KEY idx_doc_json_status (vector_status),

  KEY idx_doc_json_qdrant_id (qdrant_vector_id)

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COMMENT='知识库文档JSON结构及向量化记录表';