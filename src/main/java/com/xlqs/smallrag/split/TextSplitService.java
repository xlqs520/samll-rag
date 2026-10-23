
package com.xlqs.smallrag.split;

import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TextSplitService {

    /**
     * 分片目标最大字符数。
     * 仅用于切分超长段落，不强制截断普通段落的重叠内容。
     */
    private static final int MAX_CHUNK_CHARS = 500;

    /**
     * 相邻分片之间重叠的字符数。
     */
    private static final int OVERLAP_CHARS = 10;

    /**
     * 最小分片字符数。
     */
    private static final int MIN_CHUNK_CHARS = 10;

    /**
     * 按段落切分文本，并保留上一段结尾的部分内容。
     */
    public List<Document> split(String text, Long documentId) {
        List<Document> chunks = new ArrayList<>();

        if (text == null || text.isBlank()) {
            return chunks;
        }

        if (documentId == null) {
            throw new IllegalArgumentException("documentId 不能为空");
        }

        // 1. 按空行切分段落
        String[] paragraphs = text.split("(?:\\R\\s*){2,}");

        int chunkIndex = 0;
        String previousContent = "";

        for (String paragraph : paragraphs) {
            paragraph = paragraph.trim();

            if (paragraph.isEmpty()) {
                continue;
            }

            // 2. 超长段落进行二次切分
            List<String> segments = splitLongParagraph(paragraph);

            for (String segment : segments) {
                if (segment.isBlank()) {
                    continue;
                }

                // 3. 获取上一分片结尾的重叠内容
                String overlap = getTail(previousContent, OVERLAP_CHARS);

                // 4. 拼接重叠内容与当前分片
                String content;

                if (overlap.isEmpty()) {
                    content = segment;
                } else {
                    content = overlap + "\n" + segment;
                }

                // 5. 保存分片
                if (content.length() >= MIN_CHUNK_CHARS) {
                    chunks.add(createDocument(
                            content,
                            documentId,
                            chunkIndex++
                    ));
                }

                // 保存当前正文，供下一个分片使用
                previousContent = segment;
            }
        }

        return chunks;
    }

    /**
     * 创建 Spring AI Document，并保存分片元数据。
     */
    private Document createDocument(
            String content,
            Long documentId,
            int chunkIndex) {

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("document_id", documentId);
        metadata.put("chunk_index", chunkIndex);

        return new Document(content, metadata);
    }

    /**
     * 获取字符串末尾指定数量的字符。
     */
    private String getTail(String text, int length) {
        if (text == null || text.isEmpty() || length <= 0) {
            return "";
        }

        int start = Math.max(0, text.length() - length);
        return text.substring(start);
    }

    /**
     * 拆分超长段落。
     * 优先在句子结束标点处切分，并保留段落内部的重叠内容。
     */
    private List<String> splitLongParagraph(String paragraph) {
        List<String> result = new ArrayList<>();

        if (paragraph.length() <= MAX_CHUNK_CHARS) {
            result.add(paragraph);
            return result;
        }

        int start = 0;

        while (start < paragraph.length()) {
            int end = Math.min(
                    start + MAX_CHUNK_CHARS,
                    paragraph.length()
            );

            // 尽量在后半段的句子结束标点处切分
            if (end < paragraph.length()) {
                int minEnd = start + MAX_CHUNK_CHARS / 2;

                for (int i = end - 1; i >= minEnd; i--) {
                    if (isSentenceEnd(paragraph.charAt(i))) {
                        end = i + 1;
                        break;
                    }
                }
            }

            String part = paragraph.substring(start, end).trim();

            if (!part.isEmpty()) {
                result.add(part);
            }

            if (end >= paragraph.length()) {
                break;
            }

            // 下一片从当前分片末尾往前回退指定字符数
            int nextStart = end - OVERLAP_CHARS;

            // 防止异常配置导致循环无法前进
            start = Math.max(start + 1, nextStart);
        }

        return result;
    }

    /**
     * 判断是否为常见的句子结束标点。
     */
    private boolean isSentenceEnd(char c) {
        return c == '。'
                || c == '！'
                || c == '？'
                || c == '.'
                || c == '!'
                || c == '?'
                || c == '；'
                || c == ';';
    }
}