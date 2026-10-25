-- liquibase formatted sql

-- changeset developer:010-create-user-model-config
-- comment 创建用户模型配置表
CREATE TABLE user_model_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    tenant_id BIGINT NOT NULL DEFAULT 0 COMMENT '租户ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    name VARCHAR(100) NOT NULL COMMENT '模型配置名称',
    provider VARCHAR(50) NOT NULL COMMENT '模型提供商，如 deepseek、openai',
    model_name VARCHAR(100) NOT NULL COMMENT '模型名称',
    base_url VARCHAR(500) DEFAULT NULL COMMENT '模型 API 地址',
    api_key TEXT NOT NULL COMMENT '加密后的模型 API Key',
    model_type VARCHAR(30) NOT NULL COMMENT '模型类型：chat、embedding、rerank',
    is_default TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认模型：0否，1是',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用，1启用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_tenant_user (tenant_id, user_id),
    INDEX idx_tenant_type (tenant_id, model_type)
) COMMENT='用户模型配置表';