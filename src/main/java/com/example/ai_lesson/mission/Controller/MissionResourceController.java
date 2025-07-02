package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.MissionResource;
import com.example.ai_lesson.mission.Mapper.MissionResourceMapper;
import com.example.ai_lesson.mission.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
@CrossOrigin( origins = "http://localhost:5173",
        allowCredentials = "true")
@RestController
@RequestMapping("/api/mission-resource")
public class MissionResourceController {
    @Autowired
    private MissionResourceMapper missionResourceMapper;
    
    @Value("${file.upload.teacher-report-dir}")
    private String teacherReportDir;
    
    @Value("${file.upload.teacher-report-access-path}")
    private String teacherReportAccessPath;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadReport(@RequestParam("file") MultipartFile file) {
        String[] allowTypes = {"pdf", "doc", "txt"};
        long maxSize = 10 * 1024 * 1024; // 10MB
        // 使用配置文件中的路径，并确保是绝对路径
        String uploadDir = System.getProperty("user.dir") + "/" + teacherReportDir;
        try {
            String saveName = FileUploadUtil.uploadFile(file, uploadDir, allowTypes, maxSize);
            // 获取原始文件名（用于显示），限制长度避免数据库字段溢出
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                originalFileName = "未命名文件";
            } else {
                // 限制文件名长度，避免数据库字段溢出（假设字段长度为50）
                if (originalFileName.length() > 50) {
                    String ext = "";
                    if (originalFileName.contains(".")) {
                        ext = originalFileName.substring(originalFileName.lastIndexOf("."));
                    }
                    String nameWithoutExt = originalFileName.substring(0, originalFileName.lastIndexOf("."));
                    originalFileName = nameWithoutExt.substring(0, 50 - ext.length()) + ext;
                }
            }
            
            // 存入数据库
            MissionResource resource = new MissionResource();
            resource.setPath(teacherReportAccessPath + saveName); // 前端可访问路径
            resource.setResource_name(originalFileName); // 存储原始文件名，便于用户识别
            resource.setResource_description(""); // 先不写描述
            missionResourceMapper.insertMissionResource(resource);
            
            // 返回包含resource_id的响应
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "上传成功",
                "data", Map.of(
                    "resourceId", resource.getResource_id(),
                    "fileName", originalFileName,
                    "filePath", teacherReportAccessPath + saveName
                )
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件保存失败: " + e.getMessage());
        }
    }
} 