package com.xlqs.smallrag.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xlqs.smallrag.api.RagDocumentController;
import com.xlqs.smallrag.entity.KnowledgeDocumentFile;
import com.xlqs.smallrag.knowledge.ChunkDocumentService;
import com.xlqs.smallrag.knowledge.KnowledgeDocumentService;
import com.xlqs.smallrag.mapper.KnowledgeDocumentFileDao;
import com.xlqs.smallrag.split.TextSplitService;
import com.xlqs.smallrag.utils.DocumentParser;
import com.xlqs.smallrag.vo.KnowledgeDocumentCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 16:51
 * @description RagDocumentControllerImpl 类说明：TODO
 * @since 2026/10/23
 */
@RestController
@RequiredArgsConstructor
public class RagDocumentControllerImpl implements RagDocumentController {

    private final KnowledgeDocumentService knowledgeDocumentService;

    private final DocumentParser documentParser;

    private final ChunkDocumentService chunkDocumentService;

    private final TextSplitService textSplitService;

    private final KnowledgeDocumentFileDao knowledgeDocumentFileDao;

    @Override
    public Long createDocument(KnowledgeDocumentCreateDto dto, MultipartFile file) {
        Long documentId = knowledgeDocumentService.createDocument(dto, file);
        String text = documentParser.parse(file);
        KnowledgeDocumentFile knowledgeDocumentFile = knowledgeDocumentFileDao.selectOne(
                new LambdaQueryWrapper<KnowledgeDocumentFile>()
                        .eq(KnowledgeDocumentFile::getDocumentId, documentId)
        );
        int saveChunks = chunkDocumentService.saveChunks(
                dto.getKnowledgeBaseId(),
                documentId,
                textSplitService.split(text, documentId),
                null,
                knowledgeDocumentFile
        );
        System.out.println(knowledgeDocumentFile);
        return (long) saveChunks;
    }
}
