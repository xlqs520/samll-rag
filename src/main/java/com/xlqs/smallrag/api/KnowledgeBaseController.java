package com.xlqs.smallrag.api;

import com.xlqs.smallrag.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/25 15:22
 * @description KnowledgeBaseController 类说明：知识库管理
 * @since 2026/10/25
 */
@RequestMapping("/knowledge-base")
public interface KnowledgeBaseController {

    @PostMapping("/create")
    Result<String> createKnowledgeBase();


}
