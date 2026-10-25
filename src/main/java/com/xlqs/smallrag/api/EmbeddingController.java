package com.xlqs.smallrag.api;

import com.xlqs.smallrag.vo.EmbeddingResultVo;
import com.xlqs.smallrag.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/25 15:30
 * @description EmbeddingController 类说明：向量服务
 * @since 2026/10/25
 */
@RequestMapping("/embedding")
public interface EmbeddingController {

    /**
     * 获取向量
     *
     * @param text 文本
     * @return 向量
     */
    @PostMapping("/getEmbedding")
    Result<EmbeddingResultVo> getEmbedding(@RequestParam String text);

    /**
     * 批量获取向量
     *
     * @param texts 文本列表
     * @return 向量列表
     */
    @PostMapping("/getEmbeddings")
    Result<List<EmbeddingResultVo>> getEmbeddings(
            @RequestBody List<String> texts
    );

}
