package com.xlqs.smallrag.entity;


import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.util.List;

/**
* 知识库配置表
*
* @author 卿本佳人
* @since 2026年10月14日
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@TableName(value = "knowledge_base", autoResultMap = true)
public class KnowledgeBase implements Serializable {


    /**
    * 知识库主键ID
    */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
    * 租户ID，0表示默认租户
    */
    private Long tenantId;

    /**
    * 知识库名称，同一租户下唯一
    */
    private String name;

    /**
    * 知识库描述
    */
    private String description;

    /**
    * 嵌入模型
    */
    private String embeddingModel;

    /**
    * 向量维度
    */
    private Integer vectorDimension;

    /**
    * 向量数据库集合名称
    */
    private String collectionName;

    /**
    * 文档类型 DOCUMENT(传统文档), JSON(结构化数据)
    */
    private String dataType;

    /**
    * 参与向量化的字段
    */
    @TableField(value = "vector_fields", typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    private List<String> vectorFields;

    /**
    * 允许使用的过滤字段
    */
    @TableField(value = "filter_fields", typeHandler = JacksonTypeHandler.class, jdbcType = JdbcType.VARCHAR)
    private List<String> filterFields;

    /**
    * 分片大小
    */
    private Integer chunkSize;

    /**
    * 分片重叠
    */
    private Integer chunkOverlap;

    /**
    * 状态
    */
    private String status;

    /**
    * 创建时间
    */
    private Date createdAt;

    /**
    * 更新时间
    */
    private Date updatedAt;

    /**
    * 删除时间
    */
    private Date deletedAt;

}