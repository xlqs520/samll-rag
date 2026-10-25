package com.xlqs.smallrag.api;

import com.xlqs.smallrag.entity.KnowledgeBase;
import com.xlqs.smallrag.vo.KnowledgeBaseCreateVo;
import com.xlqs.smallrag.vo.KnowledgeBaseStatisticsVo;
import com.xlqs.smallrag.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/25 15:22
 * @description KnowledgeBaseController 类说明：知识库管理
 * @since 2026/10/25
 */
@RequestMapping("/knowledge-base")
public interface KnowledgeBaseController {

    /**
     * 创建知识库
     *
     * @param knowledgeBaseCreateVo 知识库创建参数
     * @return 创建结果
     */
    @PostMapping("/create")
    Result<String> createKnowledgeBase(@RequestBody KnowledgeBaseCreateVo knowledgeBaseCreateVo);

    /**
     * 列出所有知识库
     *
     * @param KbName 知识库名称
     * @param dataType 数据类型
     * @return 知识库列表
     */
    @GetMapping("/list")
    Result<List<KnowledgeBase>> listKnowledgeBases(String KbName, String dataType);

    /**
     * 获取知识库详情
     *
     * @param kbId 知识库ID
     * @return 知识库详情
     */
    @GetMapping("/detail")
    Result<KnowledgeBase> getKnowledgeBaseDetail(@RequestParam("kb_id") String kbId);

    /**
     * 更新知识库
     *
     * @param kbId 知识库ID
     * @param knowledgeBaseCreateVo 知识库更新参数
     * @return 更新结果
     */
    @PostMapping("/update/{kb_id}")
    Result<String> updateKnowledgeBase(@PathVariable("kb_id") Long kbId, @RequestBody KnowledgeBaseCreateVo knowledgeBaseCreateVo);

    /**
     * 删除知识库
     *
     * @param kbId 知识库ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{kb_id}")
    Result<String> deleteKnowledgeBase(@PathVariable("kb_id") Long kbId);

    /**
     * 获取知识库统计信息
     *
     * @param id 知识库ID
     * @return 知识库统计信息
     */
    @GetMapping("/bases/{id}/statistics")
    Result<KnowledgeBaseStatisticsVo> getKnowledgeBaseStatistics(@PathVariable Long id);

}
