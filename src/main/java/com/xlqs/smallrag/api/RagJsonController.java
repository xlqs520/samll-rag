package com.xlqs.smallrag.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 17:49
 * @description RagJsonController 类说明：TODO
 * @since 2026/10/23
 */

@RequestMapping("/json")
public interface RagJsonController {

    @PostMapping("/create")
    Long importJsonRecord(
        @RequestParam Long knowledgeBaseId,
        @RequestParam String documentName,
        @RequestBody Map<String, Object> jsonMap);
}
