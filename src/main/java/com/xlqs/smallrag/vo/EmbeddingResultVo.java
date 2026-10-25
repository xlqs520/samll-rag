package com.xlqs.smallrag.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/25 21:29
 * @description EmbeddingResultVo 类说明：TODO
 * @since 2026/10/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmbeddingResultVo {
    /**
     * 文本索引
     */
    private Integer index;

    /**
     * 原始文本
     */
    private String text;

    /**
     * 文本向量
     */
    private float[] embedding;
}
