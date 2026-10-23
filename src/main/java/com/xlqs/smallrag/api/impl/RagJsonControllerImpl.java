package com.xlqs.smallrag.api.impl;

import com.xlqs.smallrag.api.RagJsonController;
import com.xlqs.smallrag.knowledge.JsonDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 17:50
 * @description RagJsonControllerImpl 类说明：TODO
 * @since 2026/10/23
 */

@RestController
@RequiredArgsConstructor
public class RagJsonControllerImpl implements RagJsonController {

    private final JsonDocumentService jsonDocumentService;

    @Override
    public Long importJsonRecord(Long knowledgeBaseId, String documentName, Map<String, Object> jsonMap) {
        return jsonDocumentService.importJsonRecord(
                knowledgeBaseId,
                documentName,
                jsonMap
        );
    }
}
