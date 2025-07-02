package com.example.ai_lesson.mission.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUploadUtil {
    /**
     * 上传文件到指定目录，返回保存后的文件名（带后缀）
     * @param file MultipartFile对象
     * @param uploadDir 存储目录（如ailesson_ui/public/teacher-report/）
     * @param allowTypes 允许的文件类型（如{"pdf","doc","txt"}），不校验可传null
     * @param maxSize 单文件最大字节数（如10*1024*1024），不校验可传0
     * @return 保存后的文件名
     * @throws IOException 文件保存失败
     * @throws IllegalArgumentException 文件类型或大小不合法
     */
    public static String uploadFile(MultipartFile file, String uploadDir, String[] allowTypes, long maxSize) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件为空");
        }
        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".") ? originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase() : "";
        if (allowTypes != null && allowTypes.length > 0) {
            boolean allowed = false;
            for (String type : allowTypes) {
                if (type.equalsIgnoreCase(ext)) {
                    allowed = true;
                    break;
                }
            }
            if (!allowed) {
                throw new IllegalArgumentException("不支持的文件类型: " + ext);
            }
        }
        if (maxSize > 0 && file.getSize() > maxSize) {
            throw new IllegalArgumentException("文件过大，最大允许: " + maxSize + " 字节");
        }
        // 确保路径分隔符正确，并创建目录
        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);
        
        String saveName = UUID.randomUUID().toString().replaceAll("-","") + (ext.isEmpty() ? "" : "." + ext);
        Path savePath = uploadPath.resolve(saveName);
        file.transferTo(savePath.toFile());
        return saveName;
    }
} 