package com.xlqs.smallrag.mapper;

import com.xlqs.smallrag.entity.KnowledgeDocument;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author HUAWEI
* @description 针对表【knowledge_document(知识库文档表)】的数据库操作Mapper
* @createDate 2026-10-14 14:49:32
* @Entity com.xlqs.smallrag.entity.KnowledgeDocument
*/
@Mapper
public interface KnowledgeDocumentDao extends BaseMapper<KnowledgeDocument> {

}




