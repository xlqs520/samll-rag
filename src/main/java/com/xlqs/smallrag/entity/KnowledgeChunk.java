package com.xlqs.smallrag.entity;


import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
* 知识库文档分片表
*
* @author 卿本佳人
* @since 2026年10月23日
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName(value = "knowledge_chunk", autoResultMap = true)
public class KnowledgeChunk implements Serializable {


    /**
    * 分片主键ID
    */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 所属知识库ID
    */
    private Long knowledgeBaseId;

    /**
    * 所属文档ID
    */
    private Long documentId;

    /**
    * 分片在原文档中的序号，从0开始
    */
    private Integer chunkIndex;

    /**
    * LlamaIndex节点UUID，用于关联原始节点
    */
    private String llamaNodeId;

    /**
    * 分片文本内容，作为向量化和检索的主要文本
    */
    private String content;

    /**
    * 分片扩展元数据，例如页码、标题、来源及业务属性
    */
    @TableField(value = "metadata", typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    private Object metadata;

    /**
    * Qdrant向量点ID，用于关联向量数据库中的记录
    */
    private String qdrantPointId;

    /**
    * 向量处理状态：PENDING-待处理，PROCESSING-处理中，SUCCESS-成功，FAILED-失败
    */
    private String vectorStatus;

    /**
    * 分片记录创建时间
    */
    private Date createdAt;

    /**
    * 分片记录最后更新时间
    */
    private Date updatedAt;

}