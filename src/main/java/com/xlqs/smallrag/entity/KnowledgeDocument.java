package com.xlqs.smallrag.entity;


import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
* 知识库文档表
*
* @author 卿本佳人
* @since 2026年10月14日
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("knowledge_document")
public class KnowledgeDocument implements Serializable {


    /**
    * 文档主键ID
    */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 所属知识库ID
    */
    private Long knowledgeBaseId;

    /**
    * 租户ID，0表示默认租户
    */
    private Long tenantId;

    /**
    * LlamaIndex原始文档UUID
    */
    private String llamaDocumentId;

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
    private Object metadata;

    /**
    * 文档切片数量
    */
    private Integer chunkCount;

    /**
    * 向量处理状态：PENDING-待处理，PROCESSING-处理中，COMPLETED-已完成，FAILED-失败
    */
    private String vectorStatus;

    /**
    * 向量处理失败时的错误信息
    */
    private String errorMessage;

    /**
    * 创建时间
    */
    private Date createdAt;

    /**
    * 最后更新时间
    */
    private Date updatedAt;

    /**
    * 删除时间，NULL表示未删除
    */
    private Date deletedAt;

}