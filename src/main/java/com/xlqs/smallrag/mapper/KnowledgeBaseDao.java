package com.xlqs.smallrag.mapper;

import com.xlqs.smallrag.entity.KnowledgeBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

/**
* @author HUAWEI
* @description 针对表【knowledge_base(知识库配置表)】的数据库操作Mapper
* @createDate 2026-10-14 13:40:04
* @Entity com.xlqs.smallrag.entity.KnowledgeBase
*/
@Mapper
public interface KnowledgeBaseDao extends BaseMapper<KnowledgeBase> {

    List<KnowledgeBase> selectAllKnowledgeBase();

}




