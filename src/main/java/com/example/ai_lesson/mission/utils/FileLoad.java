package com.example.ai_lesson.mission.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
@CrossOrigin
public class FileLoad {

    @Value("${file.upload.mission-dir}")
    private String missionUploadDir;

    @Value("${file.upload.avatar-dir}")
    private String avatarUploadDir;

    @Value("${file.upload.mission-access-path}")
    private String accessPath; // 新增访问路径配置

    public String getMissionUploadDir() {
        return missionUploadDir;
    }
    public String getAvatarUploadDir() {
        return avatarUploadDir;
    }
    // 任务文档上传
    public String uploadMissionFile(MultipartFile file) throws IOException {
        // 创建上传目录（如果不存在）
        return uploadFile(file, missionUploadDir);
    }
    public String uploadUserPhoto(MultipartFile file) throws IOException {
        // 创建上传目录（如果不存在）
        return uploadFile(file, avatarUploadDir);
    }


    // 上传头像文件
    public String uploadAvatar(MultipartFile file) throws IOException {
        return uploadFile(file, avatarUploadDir);
    }

    private String uploadFile(MultipartFile file, String uploadDir) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String meaningfulName = originalFileName.substring(0, originalFileName.lastIndexOf("."))
                .replaceAll("\\(\\d+\\)", "")
                .trim();
        String fileName = UUID.randomUUID() + "_" + meaningfulName + fileExtension;

        // 保存文件
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 返回完整访问路径
        return accessPath + fileName;
    }

    public void deleteFile(String filePath) throws IOException {
        if (filePath != null && !filePath.isEmpty()) {
            Path path = Paths.get(filePath.replace(accessPath, ""));
            if (Files.exists(path)) {
                Files.delete(path);
            }
        }
    }
    /**
     * 获取文件资源
     * @param filePath 文件路径
     * @return 文件资源
     * @throws IOException 文件操作异常
     */
    public Resource loadFileAsResource(String filePath) throws IOException {
        try {
            // 处理路径前缀问题（兼容前端传的不同格式）
            String normalizedPath = filePath.replace("/uploads/", "/assets/missions_reports/")
                    .replace("\\", "/");

            // 获取实际存储路径（去掉URL前缀）
            String relativePath = normalizedPath.replace("/assets/missions_reports/", "");
            Path file = Paths.get(missionUploadDir).resolve(relativePath).normalize();

            // 调试日志
            System.out.println("尝试加载文件: " + file.toString());

            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists()) {
                throw new FileNotFoundException("文件不存在: " + file);
            }
            return resource;
        } catch (Exception e) {
            throw new IOException("文件加载失败: " + e.getMessage(), e);
        }
    }
}