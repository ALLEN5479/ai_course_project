package com.example.ai_lesson.study_resources.controller;

import com.example.ai_lesson.study_resources.entity.ResourceEntity;
import com.example.ai_lesson.study_resources.mapper.ResourceMapper;
import com.example.ai_lesson.study_resources.mapper.NodeLinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/knowledge-resource")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class KnowledgeResourceController {
    
    @Autowired
    private ResourceMapper resourceMapper;
    
    @Autowired
    private NodeLinkMapper nodeLinkMapper;
    
    @Value("${file.upload.node-resource-dir:ailesson_ui/public/node-resource/}")
    private String nodeResourceDir;
    
    @Value("${file.upload.node-resource-access-path:/node-resource/}")
    private String nodeResourceAccessPath;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResource(
            @RequestParam("file") MultipartFile file,
            @RequestParam("nodeId") Integer nodeId,
            @RequestParam("description") String description) {
        
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "文件不能为空"
                ));
            }
            
            // 获取文件扩展名
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "文件名不能为空"
                ));
            }
            
            String fileExtension = "";
            if (originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
            }
            
            // 根据文件扩展名确定存储路径和资源类型
            String uploadSubDir;
            String resourceType;
            
            if ("pdf".equals(fileExtension)) {
                uploadSubDir = "pdf/";
                resourceType = "pdf";
            } else if ("mp4".equals(fileExtension) || "avi".equals(fileExtension) || 
                       "mov".equals(fileExtension) || "wmv".equals(fileExtension) ||
                       "flv".equals(fileExtension) || "mkv".equals(fileExtension)) {
                uploadSubDir = "video/";
                resourceType = "video";
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "暂不支持该文件类型，仅支持PDF和视频文件"
                ));
            }
            
            // 构建完整的上传路径
            String uploadDir = System.getProperty("user.dir") + "/" + nodeResourceDir + uploadSubDir;
            Path uploadPath = Paths.get(uploadDir);
            
            // 创建目录（如果不存在）
            Files.createDirectories(uploadPath);
            
            // 生成唯一文件名
            String saveName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExtension;
            Path savePath = uploadPath.resolve(saveName);
            
            // 保存文件
            file.transferTo(savePath.toFile());
            
            // 构建访问URL
            String accessUrl = nodeResourceAccessPath + uploadSubDir + saveName;
            
            // 限制文件名长度，避免数据库字段溢出
            String displayName = originalFileName;
            if (displayName.length() > 50) {
                String ext = "";
                if (displayName.contains(".")) {
                    ext = displayName.substring(displayName.lastIndexOf("."));
                }
                String nameWithoutExt = displayName.substring(0, displayName.lastIndexOf("."));
                displayName = nameWithoutExt.substring(0, 50 - ext.length()) + ext;
            }
            
            // 保存到study_resources表
            ResourceEntity resource = new ResourceEntity();
            resource.setResource_name(displayName);
            resource.setResource_url(accessUrl);
            resource.setRes_description(description);
            resource.setRes_type(resourceType);
            
            int insertResult = resourceMapper.insertResource(resource);
            if (insertResult <= 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "保存资源信息失败"
                ));
            }
            
            // 保存到node_resource表
            int linkResult = nodeLinkMapper.insertNodeResource(nodeId, resource.getResource_id());
            if (linkResult <= 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", false,
                    "message", "关联知识点失败"
                ));
            }
            
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "资源上传成功",
                "data", Map.of(
                    "resourceId", resource.getResource_id(),
                    "fileName", displayName,
                    "fileUrl", accessUrl,
                    "resourceType", resourceType,
                    "nodeId", nodeId
                )
            ));
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "文件保存失败: " + e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "上传失败: " + e.getMessage()
            ));
        }
    }
} 