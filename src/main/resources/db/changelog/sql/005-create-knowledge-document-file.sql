-- liquibase formatted sql

-- changeset developer:005-create-knowledge-document-file
-- comment: 创建知识文档文件信息表

CREATE TABLE knowledge_document_file (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文件记录主键ID',
    document_id BIGINT NOT NULL COMMENT '所属知识文档ID',
    file_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    file_type VARCHAR(50) COMMENT '文件扩展名，例如pdf、docx、txt',
    content_type VARCHAR(100) COMMENT '文件MIME类型',
    file_size BIGINT NOT NULL DEFAULT 0 COMMENT '文件大小，单位字节',
    file_hash CHAR(64) COMMENT '文件SHA-256哈希值',
    file_bucket VARCHAR(100) NOT NULL COMMENT 'MinIO存储桶名称',
    file_uri VARCHAR(1000) NOT NULL COMMENT 'MinIO对象Key',
    file_status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '文件状态：PENDING-待上传，UPLOADED-已上传，FAILED-上传失败',
    error_message TEXT COMMENT '文件上传失败时的错误信息',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    KEY idx_file_document (document_id),
    KEY idx_file_hash (file_hash)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
COMMENT='知识文档文件信息表';