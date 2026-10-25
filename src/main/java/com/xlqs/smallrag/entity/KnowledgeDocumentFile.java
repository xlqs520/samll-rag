package com.xlqs.smallrag.entity;


import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
* 知识文档文件信息表
*
* @author 卿本佳人
* @since 2026年10月23日
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("knowledge_document_file")
public class KnowledgeDocumentFile implements Serializable {


    /**
    * 文件记录主键ID
    */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 所属知识文档ID
    */
    private Long documentId;

    /**
    * 原始文件名
    */
    private String fileName;

    /**
    * 文件扩展名，例如pdf、docx、txt
    */
    private String fileType;

    /**
    * 文件MIME类型
    */
    private String contentType;

    /**
    * 文件大小，单位字节
    */
    private Long fileSize;

    /**
    * 文件SHA-256哈希值
    */
    private String fileHash;

    /**
    * MinIO存储桶名称
    */
    private String fileBucket;

    /**
    * MinIO对象Key
    */
    private String fileUri;

    /**
    * 文件状态：PENDING-待上传，UPLOADED-已上传，FAILED-上传失败
    */
    private String fileStatus;

    /**
    * 文件上传失败时的错误信息
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

}