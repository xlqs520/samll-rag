package com.xlqs.smallrag.mapper;

import com.xlqs.smallrag.entity.KnowledgeDocumentJson;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author HUAWEI
* @description 针对表【knowledge_document_json(知识库文档JSON结构及向量化记录表)】的数据库操作Mapper
* @createDate 2026-10-23 13:36:33
* @Entity com.xlqs.smallrag.entity.KnowledgeDocumentJson
*/
@Mapper
public interface KnowledgeDocumentJsonDao extends BaseMapper<KnowledgeDocumentJson> {

}




