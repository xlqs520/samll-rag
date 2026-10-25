package com.xlqs.smallrag.mapper;

import com.xlqs.smallrag.entity.KnowledgeDocumentFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author HUAWEI
* @description 针对表【knowledge_document_file(知识文档文件信息表)】的数据库操作Mapper
* @createDate 2026-10-23 16:27:12
* @Entity com.xlqs.smallrag.entity.KnowledgeDocumentFile
*/
@Mapper
public interface KnowledgeDocumentFileDao extends BaseMapper<KnowledgeDocumentFile> {

}




