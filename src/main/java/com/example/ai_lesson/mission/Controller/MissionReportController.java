package com.example.ai_lesson.mission.Controller;

import com.example.ai_lesson.mission.Domain.MissionReport;
import com.example.ai_lesson.mission.Mapper.MissionReportMapper;
import com.example.ai_lesson.mission.utils.FileLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/api/mission/reports")
public class MissionReportController {

    @Autowired
    private MissionReportMapper missionReportMapper;

    @Autowired
    private FileLoad fileLoad;

    // 上传任务文档
    /*@PostMapping("/upload")
    public String uploadReport(@RequestParam("file") MultipartFile file,
                               @RequestParam("missionId") Integer missionId) throws IOException {
        String fileName = fileLoad.uploadMissionFile(file);

        MissionReport report = new MissionReport();

        report.setMissionId(missionId);
        report.setFileName(fileName);
        //System.out.println("missionId"+missionId);
        //System.out.println("fileName"+fileName);
        report.setPath(fileName); // 实际项目中可能需要完整路径

        missionReportMapper.insertMissionReport(report);
        return "文件上传成功";
    }*/
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadReport(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "missionId", required = false) Integer missionId) {
        try {
            if(file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", 400,
                        "message", "文件不能为空"
                ));
            }

            String contentType = file.getContentType();
            if(!Arrays.asList("application/pdf",
                            "application/msword",
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    .contains(contentType)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", 400,
                        "message", "仅支持PDF/Word文件"
                ));
            }
            // 保存文件并获取访问路径
            String filePath = fileLoad.uploadMissionFile(file);

            // 获取文件名部分（去掉路径）
            String fullFileName = filePath.substring(filePath.lastIndexOf("/") + 1);

            // 提取有意义的部分（去掉UUID和前面的下划线）
            String fileName = fullFileName.contains("_")
                    ? fullFileName.substring(fullFileName.indexOf("_") + 1)
                    : fullFileName; // 如果没有下划线，返回完整文件名
            // 保存记录到数据库
            MissionReport report = new MissionReport();
            report.setMissionId(missionId);
            report.setFileName(fileName);
            report.setPath(filePath); // 存储访问路径

            // 根据missionId决定操作
            /*if (missionId != null) {
                // 检查是否已存在记录
                List<MissionReport> existingReports = missionReportMapper.selectMissionReportList(missionId);
                if (existingReports != null && !existingReports.isEmpty()) {
                    // 更新现有记录
                    report.setId(existingReports.get(0).getId());
                    missionReportMapper.updateMissionReport(report);
                } else {
                    // 插入新记录
                    missionReportMapper.insertMissionReport(report);
                }
            } else {*/
                // 插入新记录(无missionId)
                missionReportMapper.insertMissionReport(report);
            //}
            report = missionReportMapper.selectByPath(filePath);
            //System.out.println("report:"+report.toString());
            Integer fileId = report.getId();
            /*if(fileId == null) {
                throw new RuntimeException("查询上传记录失败");
            }*/
            // 返回响应（包含可直接访问的路径）
            if(missionId == null) {
                return ResponseEntity.ok(Map.of(
                        "status", 200,
                        "message", "文件上传成功",
                        "data", Map.of(
                                "fileId", fileId,
                                "fileName", fileName,
                                "filePath", filePath  // 前端可直接使用的路径
                        )
                ));
            }
            else {
                return ResponseEntity.ok(Map.of(
                        "status", 200,
                        "message", "文件上传成功",
                        "data", Map.of(
                                "fileId", fileId,
                                "fileName", fileName,
                                "filePath", filePath,  // 前端可直接使用路径
                                "missionId",missionId
                        )
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", 500,
                    "message", "文件上传失败: " + e.getMessage()
            ));
        }
    }
   /* @GetMapping("/update")
    public ResponseEntity<Map<String, Object>> updateReport(
            @RequestParam("file") MultipartFile file,
            @RequestParam("missionId") Integer missionId) {

        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", 400,
                        "message", "文件不能为空"
                ));
            }

            String contentType = file.getContentType();
            if(!Arrays.asList("application/pdf",
                            "application/msword",
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    .contains(contentType)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", 400,
                        "message", "仅支持PDF/Word文件"
                ));
            }
            // 保存文件并获取访问路径
            String filePath = fileLoad.uploadMissionFile(file);

            // 获取文件名部分（去掉路径）
            String fullFileName = filePath.substring(filePath.lastIndexOf("/") + 1);

            // 提取有意义的部分（去掉UUID和前面的下划线）
            String fileName = fullFileName.contains("_")
                    ? fullFileName.substring(fullFileName.indexOf("_") + 1)
                    : fullFileName; // 如果没有下划线，返回完整文件名
            // 保存记录到数据库
            List<MissionReport> existingReports = missionReportMapper.selectMissionReportList(missionId);
            MissionReport report = new MissionReport();
            report.setMissionId(missionId);
            report.setFileName(fileName);
            report.setPath(filePath); // 存储访问路径
            if (existingReports != null && !existingReports.isEmpty()) {
                // 更新现有记录
                report.setId(existingReports.get(0).getId()); // 取第一条记录的ID
                missionReportMapper.updateMissionReport(report);
            } else {
                // 插入新记录
                missionReportMapper.insertMissionReport(report);
            }

            // 返回响应（包含可直接访问的路径）
            return ResponseEntity.ok(Map.of(
                    "status", 200,
                    "message", "文件上传成功",
                    "data", Map.of(
                            "fileId", report.getId(),
                            "fileName", fileName,
                            "filePath", filePath, // 前端可直接使用的路径
                            "missionId", missionId
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", 500,
                    "message", "文件上传失败: " + e.getMessage()
            ));
        }
    }*/


    // 获取任务文档列表
    @GetMapping("/list")
    public List<MissionReport> getReportsByMissionId(@RequestParam Integer missionId) {
        return missionReportMapper.selectMissionReportList(missionId);
    }
    @GetMapping("{id}")
    public ResponseEntity<MissionReport> getMissionReportById(@PathVariable Integer id) {
        MissionReport report = missionReportMapper.selectMissionReportById(id);
        return ResponseEntity.ok(report);
    }
    @GetMapping("/file/preview")
    public ResponseEntity<Resource> previewFile(
            @RequestParam String filePath,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {
        try {
            // 解码并规范化路径
            String decodedPath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
            Resource resource = fileLoad.loadFileAsResource(decodedPath);

            // 自动检测MIME类型
            String contentType = Files.probeContentType(Paths.get(resource.getURI()));
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // 支持PDF分片加载（提升大文件预览体验）
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.set("Accept-Ranges", "bytes");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    /**
     * 文件下载接口
     * @param filePath 文件路径
     * @param fileName 下载时显示的文件名
     * @return 返回文件资源，浏览器会触发下载
     */
    @GetMapping("/file/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam String filePath,
            @RequestParam String fileName) {
        try {
            // 1. 获取文件资源
            Resource resource = fileLoad.loadFileAsResource(filePath);

            // 2. 设置响应头，强制下载
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            // 3. 返回文件资源
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleFileException(IOException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "message", ex.getMessage()
        ));
    }
}