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
* 知识库文档JSON结构及向量化记录表
*
* @author 卿本佳人
* @since 2026年10月23日
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName(value = "knowledge_document_json", autoResultMap = true)
public class KnowledgeDocumentJson implements Serializable {


    /**
    * 主键ID
    */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 所属知识库ID
    */
    private Long knowledgeBaseId;

    /**
    * Qdrant向量数据库中的向量ID
    */
    private String qdrantVectorId;

    /**
    * 文档名称
    */
    private String documentName;

    /**
    * 文档分类
    */
    private String documentCategory;

    /**
    * 原始JSON数据
    */
    @TableField(value = "raw_json_data", typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    private Object rawJsonData;

    /**
    * 实际向量化文本（用于生成向量的纯文本内容）
    */
    private String vectorText;

    /**
    * 向量化状态：PENDING-待处理，PROCESSING-处理中，SUCCESS-成功，FAILED-失败
    */
    private String vectorStatus;

    /**
    * 创建时间
    */
    private Date createdAt;

    /**
    * 更新时间
    */
    private Date updatedAt;

}