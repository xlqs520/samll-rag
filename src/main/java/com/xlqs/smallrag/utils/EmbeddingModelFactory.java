package com.xlqs.smallrag.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/25 13:19
 * @description EmbeddingModelFactory 类说明：TODO
 * @since 2026/10/25
 */
@Service
public class EmbeddingModelFactory {

    private final Cache<EmbeddingModelKey, EmbeddingModel> modelCache =
            Caffeine.newBuilder()
                    .maximumSize(100)
                    .expireAfterAccess(30, TimeUnit.MINUTES)
                    .build();

    public EmbeddingModel create(String baseUrl, String apiKey, String modelName, int dimensions) {
        return create(baseUrl, apiKey, modelName, dimensions, "/embeddings");
    }

    public EmbeddingModel create(String baseUrl, String apiKey, String modelName, int dimensions, String embeddingsPath) {

        EmbeddingModelKey key = new EmbeddingModelKey(baseUrl, apiKey, modelName, dimensions);

        return modelCache.get(key, k -> {
            OpenAiApi openAiApi = OpenAiApi.builder()
                    .baseUrl(k.baseUrl())
                    .apiKey(k.apiKey())
                    .embeddingsPath(embeddingsPath)
                    .build();

            OpenAiEmbeddingOptions options =
                    OpenAiEmbeddingOptions.builder()
                            .model(k.modelName())
                            .dimensions(k.dimensions())
                            .build();

            return new OpenAiEmbeddingModel(
                    openAiApi,
                    MetadataMode.EMBED,
                    options
            );
        });
    }

    private record EmbeddingModelKey(
            String baseUrl,
            String apiKey,
            String modelName,
            int dimensions
    ) {
    }
}
