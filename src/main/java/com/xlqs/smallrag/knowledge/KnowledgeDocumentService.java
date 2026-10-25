package com.xlqs.smallrag.knowledge;

import com.xlqs.smallrag.entity.KnowledgeDocument;
import com.xlqs.smallrag.entity.KnowledgeDocumentFile;
import com.xlqs.smallrag.mapper.KnowledgeDocumentDao;
import com.xlqs.smallrag.mapper.KnowledgeDocumentFileDao;
import com.xlqs.smallrag.storage.MinioService;
import com.xlqs.smallrag.vo.KnowledgeDocumentCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 16:00
 * @description 知识库文档服务：负责文档元信息的创建与状态维护
 * @since 2026/10/23
 */
@Service
public class KnowledgeDocumentService {

    @Autowired
    private KnowledgeDocumentDao knowledgeDocumentDao;

    @Autowired
    private KnowledgeDocumentFileDao knowledgeDocumentFileDao;

    @Autowired
    private MinioService minioService;

    /**
     * 创建文档记录，初始状态为 PENDING
     *
     * @param dto 文档信息
     * @return 文档主键ID
     */
    public Long createDocument(KnowledgeDocumentCreateDto dto) {
        if (dto.getKnowledgeBaseId() == null) {
            throw new IllegalArgumentException("knowledgeBaseId 不能为空");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("文档名称不能为空");
        }

        KnowledgeDocument document = KnowledgeDocument.builder()
                .knowledgeBaseId(dto.getKnowledgeBaseId())
                .tenantId(dto.getTenantId() == null ? 0L : dto.getTenantId())
                .llamaDocumentId(UUID.randomUUID().toString())
                .name(dto.getName())
                .fileType(dto.getFileType())
                .fileUri(dto.getFileUri())
                .fileSize(dto.getFileSize() == null ? 0L : dto.getFileSize())
                .fileHash(dto.getFileHash())
                .language(dto.getLanguage())
                .docVersion(dto.getDocVersion())
                .isShared(dto.getIsShared() == null ? 0 : dto.getIsShared())
                .metadata(dto.getMetadata())
                .chunkCount(0)
                .vectorStatus("PENDING")
                .build();
        knowledgeDocumentDao.insert(document);
        return document.getId();
    }

    /**
     * 上传文件到 MinIO 并创建文档记录与文件信息记录
     *
     * @param dto  文档信息
     * @param file 上传的文件
     * @return 文档主键ID
     */
    public Long createDocument(KnowledgeDocumentCreateDto dto, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        String objectName = buildObjectName(dto.getKnowledgeBaseId(), file.getOriginalFilename());
        String fileHash = sha256(file);
        String fileType = dto.getFileType() == null || dto.getFileType().isBlank()
                ? extractExtension(file.getOriginalFilename()) : dto.getFileType();
        try {
            minioService.upload(
                    objectName,
                    file.getInputStream(),
                    file.getSize(),
                    file.getContentType()
            );
        } catch (IOException e) {
            throw new IllegalStateException("读取上传文件失败", e);
        }

        dto.setName(dto.getName() == null || dto.getName().isBlank()
                ? file.getOriginalFilename() : dto.getName());
        dto.setFileUri(minioService.objectUri(objectName));
        dto.setFileSize(file.getSize());
        dto.setFileHash(fileHash);
        dto.setFileType(fileType);

        Long documentId;
        try {
            documentId = createDocument(dto);
        } catch (RuntimeException e) {
            minioService.delete(objectName);
            throw e;
        }

        KnowledgeDocumentFile documentFile = KnowledgeDocumentFile.builder()
                .documentId(documentId)
                .fileName(file.getOriginalFilename())
                .fileType(fileType)
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .fileHash(fileHash)
                .fileBucket(minioService.getBucket())
                .fileUri(objectName)
                .fileStatus("UPLOADED")
                .build();
        try {
            knowledgeDocumentFileDao.insert(documentFile);
        } catch (RuntimeException e) {
            minioService.delete(objectName);
            knowledgeDocumentDao.deleteById(documentId);
            throw e;
        }

        return documentId;
    }

    /**
     * 获取文档的临时访问地址
     */
    public String getDownloadUrl(KnowledgeDocument document, int minutes) {
        String fileUri = document.getFileUri();
        if (fileUri == null || !fileUri.startsWith("minio://")) {
            throw new IllegalStateException("该文档未存储在 MinIO: " + fileUri);
        }
        return minioService.getPresignedUrl(extractObjectKey(fileUri), minutes);
    }

    /**
     * 保存文件信息记录
     *
     * @param file 文件信息
     * @return 文件记录主键ID
     */
    public Long saveDocumentFile(KnowledgeDocumentFile file) {
        if (file.getDocumentId() == null) {
            throw new IllegalArgumentException("documentId 不能为空");
        }
        if (file.getFileName() == null || file.getFileName().isBlank()) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        if (file.getFileSize() == null) {
            file.setFileSize(0L);
        }
        if (file.getFileStatus() == null || file.getFileStatus().isBlank()) {
            file.setFileStatus("UPLOADED");
        }
        knowledgeDocumentFileDao.insert(file);
        return file.getId();
    }

    /**
     * 更新文件信息记录
     */
    public void updateDocumentFile(KnowledgeDocumentFile file) {
        knowledgeDocumentFileDao.updateById(file);
    }

    private String extractObjectKey(String fileUri) {
        String objectName = fileUri.substring("minio://".length());
        int index = objectName.indexOf('/');
        return index > 0 ? objectName.substring(index + 1) : objectName;
    }

    private String buildObjectName(Long knowledgeBaseId, String originalFilename) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("kb/%s/%s/%s-%s",
                knowledgeBaseId,
                date,
                UUID.randomUUID(),
                originalFilename == null ? "file" : originalFilename);
    }

    private String extractExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('.');
        return index >= 0 ? filename.substring(index + 1) : null;
    }

    private String sha256(MultipartFile file) {
        try (var inputStream = file.getInputStream()) {
            var digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
            StringBuilder hex = new StringBuilder();
            for (byte b : digest.digest()) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new IllegalStateException("计算文件哈希失败", e);
        }
    }
}
