package com.xlqs.smallrag.api.impl;

import com.xlqs.smallrag.api.EmbeddingController;
import com.xlqs.smallrag.vo.EmbeddingResultVo;
import com.xlqs.smallrag.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/25 19:45
 * @description EmbeddingControllerImpl 类说明：向量服务
 * @since 2026/10/25
 */
@RestController
@RequiredArgsConstructor
public class EmbeddingControllerImpl implements EmbeddingController {

    private final EmbeddingModel embeddingModel;

    @Override
    public Result<EmbeddingResultVo> getEmbedding(String text) {
        EmbeddingResultVo result = EmbeddingResultVo.builder()
                .index(0)
                .text(text)
                .embedding(embeddingModel.embed(text))
                .build();
        return Result.success(result);
    }

    @Override
    public Result<List<EmbeddingResultVo>> getEmbeddings(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            throw new IllegalArgumentException("文本列表不能为空");
        }

        List<float[]> vectors = embeddingModel.embed(texts);

        List<EmbeddingResultVo> results = new ArrayList<>(texts.size());

        for (int i = 0; i < texts.size(); i++) {
            EmbeddingResultVo result = new EmbeddingResultVo();
            result.setIndex(i);
            result.setText(texts.get(i));
            result.setEmbedding(vectors.get(i));
            results.add(result);
        }

        return Result.success(results);
    }
}
