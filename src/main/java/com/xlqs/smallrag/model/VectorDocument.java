package com.xlqs.smallrag.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 向量文档对象。
 *
 * content 和元数据处于同一层级，
 * 可以直接通过 put() 添加自定义字段。
 *
 * @author xlqs
 * @version 1.0
 */
public class VectorDocument extends LinkedHashMap<String, Object> {

    /**
     * 创建空的向量文档。
     */
    public VectorDocument() {
    }

    /**
     * 创建向量文档。
     *
     * @param pointId  Qdrant Point ID
     * @param content  分片正文
     * @param metadata 元数据
     */
    public VectorDocument(
            String pointId,
            String content,
            Map<String, Object> metadata) {

        setPointId(pointId);
        setContent(content);
        setMetadata(metadata);
    }

    /**
     * 获取 Qdrant Point ID。
     */
    public String getPointId() {
        Object value = get("pointId");
        return value == null ? null : value.toString();
    }

    /**
     * 设置 Qdrant Point ID。
     */
    public void setPointId(String pointId) {
        put("pointId", pointId);
    }

    /**
     * 获取分片正文。
     */
    public String getContent() {
        Object value = get("content");
        return value == null ? null : value.toString();
    }

    /**
     * 设置分片正文。
     */
    public void setContent(String content) {
        put("content", content);
    }

    /**
     * 添加或覆盖元数据。
     *
     * 元数据与 content 处于同一层级，
     * 不会创建嵌套的 metadata 对象。
     */
    public void setMetadata(Map<String, Object> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return;
        }

        metadata.forEach((key, value) -> {
            if (!"pointId".equals(key) && !"content".equals(key)) {
                put(key, value);
            }
        });
    }

    /**
     * 获取元数据副本。
     *
     * 自动排除 pointId 和 content。
     */
    public Map<String, Object> getMetadata() {
        Map<String, Object> metadata = new LinkedHashMap<>(this);
        metadata.remove("pointId");
        metadata.remove("content");
        return metadata;
    }

    /**
     * 添加单个元数据字段。
     */
    public void addMetadata(String key, Object value) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("元数据 key 不能为空");
        }

        if ("pointId".equals(key) || "content".equals(key)) {
            throw new IllegalArgumentException(
                    "pointId 和 content 不能作为元数据重复添加"
            );
        }

        put(key, value);
    }

    /**
     * 获取指定元数据。
     */
    public Object getMetadata(String key) {
        if ("pointId".equals(key) || "content".equals(key)) {
            return null;
        }

        return get(key);
    }
}
