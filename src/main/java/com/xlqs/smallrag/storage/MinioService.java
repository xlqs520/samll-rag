package com.xlqs.smallrag.storage;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author xlqs
 * @version 1.0
 * @date 2026/10/23 17:00
 * @description MinIO 对象存储服务
 * @since 2026/10/23
 */
@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    /**
     * 上传文件，返回对象名称
     *
     * @param objectName  对象名称，例如 document/2026/redis.txt
     * @param inputStream 文件流
     * @param size        文件大小，未知可传 -1
     * @param contentType 内容类型，例如 text/plain
     * @return 对象名称
     */
    public String upload(String objectName, InputStream inputStream, long size, String contentType) {
        try {
            ensureBucket();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(inputStream, size, -1)
                    .contentType(contentType)
                    .build());
            return objectName;
        } catch (Exception e) {
            throw new IllegalStateException("上传 MinIO 失败: " + objectName, e);
        }
    }

    /**
     * 上传字节数组，返回对象名称
     */
    public String upload(String objectName, byte[] content, String contentType) {
        return upload(objectName, new ByteArrayInputStream(content), content.length, contentType);
    }

    /**
     * 删除对象
     */
    public void delete(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new IllegalStateException("删除 MinIO 对象失败: " + objectName, e);
        }
    }

    /**
     * 生成临时访问地址
     *
     * @param objectName 对象名称
     * @param minutes    有效期（分钟）
     */
    public String getPresignedUrl(String objectName, int minutes) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucket)
                    .object(objectName)
                    .expiry(minutes, TimeUnit.MINUTES)
                    .build());
        } catch (Exception e) {
            throw new IllegalStateException("生成 MinIO 访问地址失败: " + objectName, e);
        }
    }

    /**
     * 对象路径，写入数据库 file_uri 使用
     */
    public String objectUri(String objectName) {
        return "minio://" + bucket + "/" + objectName;
    }

    /**
     * 当前使用的存储桶名称
     */
    public String getBucket() {
        return bucket;
    }

    private void ensureBucket() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucket)
                .build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build());
        }
    }
}
