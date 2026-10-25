package com.xlqs.smallrag.mapper;

import com.xlqs.smallrag.entity.KnowledgeBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlqs.smallrag.vo.KnowledgeBaseCreateVo;
import com.xlqs.smallrag.vo.KnowledgeBaseStatisticsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
* @author HUAWEI
* @description 针对表【knowledge_base(知识库配置表)】的数据库操作Mapper
* @createDate 2026-10-14 13:40:04
* @Entity com.xlqs.smallrag.entity.KnowledgeBase
*/
@Mapper
public interface KnowledgeBaseDao extends BaseMapper<KnowledgeBase> {

    /**
     * 查询所有知识库
     */
    List<KnowledgeBase> selectAllKnowledgeBase();

    /**
     * 更新知识库
     * @param kbId 知识库ID
     * @param tenantId 租户ID
     * @param dto 知识库更新DTO
     * @return 影响行数
     */
    int updateKnowledgeBase(
            @Param("kbId") Long kbId,
            @Param("tenantId") Long tenantId,
            @Param("dto") KnowledgeBaseCreateVo dto
    );


}




