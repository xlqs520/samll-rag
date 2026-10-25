package com.xlqs.smallrag.mapper;

import com.xlqs.smallrag.entity.KnowledgeChunk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author HUAWEI
* @description 针对表【knowledge_chunk(知识库文档分片表)】的数据库操作Mapper
* @createDate 2026-10-23 15:18:25
* @Entity com.xlqs.smallrag.entity.KnowledgeChunk
*/
@Mapper
public interface KnowledgeChunkDao extends BaseMapper<KnowledgeChunk> {

}




