package com.xlqs.smallrag.api.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.xlqs.smallrag.api.KnowledgeBaseController;
import com.xlqs.smallrag.entity.KnowledgeBase;
import com.xlqs.smallrag.knowledge.KnowledgeBaseService;
import com.xlqs.smallrag.vo.KnowledgeBaseCreateVo;
import com.xlqs.smallrag.vo.KnowledgeBaseStatisticsVo;
import com.xlqs.smallrag.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/25 15:50
 * @description KnowledgeBaseControllerImpl 类说明：TODO
 * @since 2026/10/25
 */
@RestController
@RequiredArgsConstructor
public class KnowledgeBaseControllerImpl implements KnowledgeBaseController {

    private final KnowledgeBaseService knowledgeBaseService;

    @Override
    public Result<String> createKnowledgeBase(KnowledgeBaseCreateVo knowledgeBaseCreateVo) {
        Long tenantId = StpUtil.getLoginIdAsLong();
        knowledgeBaseService.createKnowledgeBase(knowledgeBaseCreateVo, tenantId);
        return Result.success("知识库创建成功");
    }

    @Override
    public Result<List<KnowledgeBase>> listKnowledgeBases(String KbName, String dataType) {
        Long tenantId = StpUtil.getLoginIdAsLong();
        List<KnowledgeBase> knowledgeBases =
                knowledgeBaseService.listKnowledgeBases(tenantId, KbName, dataType);
        return Result.success(knowledgeBases);
    }

    @Override
    public Result<KnowledgeBase> getKnowledgeBaseDetail(String kbId) {
        Long tenantId = StpUtil.getLoginIdAsLong();
        KnowledgeBase knowledgeBase = knowledgeBaseService.getKnowledgeBaseDetail(kbId, tenantId);
        return Result.success(knowledgeBase);
    }

    @Override
    public Result<String> updateKnowledgeBase(Long kbId, KnowledgeBaseCreateVo knowledgeBaseCreateVo) {
        Long tenantId = StpUtil.getLoginIdAsLong();
        knowledgeBaseService.updateKnowledgeBase(kbId, tenantId, knowledgeBaseCreateVo);
        return Result.success("知识库更新成功");
    }

    @Override
    public Result<String> deleteKnowledgeBase(Long kbId) {
        Long tenantId = StpUtil.getLoginIdAsLong();
        String collectionName = knowledgeBaseService.deleteKnowledgeBase(kbId, tenantId);
        return Result.success("知识库 : " + collectionName + " 删除成功");
    }

    @Override
    public Result<KnowledgeBaseStatisticsVo> getKnowledgeBaseStatistics(Long id) {
        Long tenantId = StpUtil.getLoginIdAsLong();
        KnowledgeBaseStatisticsVo statistics = knowledgeBaseService.getKnowledgeBaseStatistics(id, tenantId);
        return Result.success(statistics);
    }
}
