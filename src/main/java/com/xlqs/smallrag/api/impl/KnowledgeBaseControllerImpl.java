package com.xlqs.smallrag.api.impl;

import com.xlqs.smallrag.api.KnowledgeBaseController;
import com.xlqs.smallrag.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

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
    @Override
    public Result<String> createKnowledgeBase() {
        return null;
    }
}
