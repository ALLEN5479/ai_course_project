package com.example.ai_lesson.study_resources.controller;

import com.example.ai_lesson.study_resources.entity.ResourceEntity;
import com.example.ai_lesson.study_resources.mapper.ResourceMapper;
import com.example.ai_lesson.aiquestion.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// PDF处理相关导入
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

// Word文档处理相关导入
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

@RestController
@CrossOrigin
public class ResourceController {
    @Autowired
    private ResourceMapper resourceMapper;
    
    @Autowired
    private ChatService chatService;

    @RequestMapping("/getresources")
    public List<ResourceEntity> getResources(@RequestParam int resource_id) {
        return resourceMapper.getResources(resource_id);
    }

    @RequestMapping("/getAllResources")
    public List<ResourceEntity> getAllResources() {
        return resourceMapper.getAllResources();
    }
    
    /**
     * AI分析资源内容
     */
    @RequestMapping("/analyzeResource")
    public Map<String, Object> analyzeResource(@RequestParam int resource_id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取资源信息
            List<ResourceEntity> resources = resourceMapper.getResources(resource_id);
            if (resources.isEmpty()) {
                result.put("success", false);
                result.put("message", "资源不存在");
                return result;
            }
            
            ResourceEntity resource = resources.get(0);
            String resourceContent = extractResourceContent(resource);
            
            if (resourceContent == null || resourceContent.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "无法提取资源内容或资源内容为空");
                return result;
            }
            
            // 构建AI分析提示词
            String prompt = String.format(
                "请分析以下学习资源并提供一个详细的总结：\n\n" +
                "资源名称：%s\n" +
                "资源类型：%s\n" +
                "资源描述：%s\n\n" +
                "资源内容：\n%s\n\n" +
                "请从以下几个方面进行分析：\n" +
                "1. 主要内容概述\n" +
                "2. 关键知识点\n" +
                "3. 学习重点\n" +
                "4. 学习建议\n" +
                "5. 相关扩展知识\n\n" +
                "请用中文回答，格式要清晰易读。",
                resource.getResource_name(),
                resource.getRes_type(),
                resource.getRes_description() != null ? resource.getRes_description() : "无描述",
                resourceContent
            );
            
            // 调用AI服务
            String analysis = chatService.sendMessage(prompt, "resource-analysis-" + resource_id);
            
            result.put("success", true);
            result.put("analysis", analysis);
            result.put("resourceName", resource.getResource_name());
            result.put("resourceType", resource.getRes_type());
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "AI分析失败：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 提取资源内容
     */
    private String extractResourceContent(ResourceEntity resource) {
        try {
            String resourceUrl = resource.getResource_url();
            String resourceType = resource.getRes_type().toLowerCase();
            
            // 构建正确的文件路径
            String filePath = buildCorrectPath(resourceUrl);
            
            System.out.println("正在提取资源内容:");
            System.out.println("资源ID: " + resource.getResource_id());
            System.out.println("资源名称: " + resource.getResource_name());
            System.out.println("原始URL: " + resourceUrl);
            System.out.println("处理后的路径: " + filePath);
            System.out.println("资源类型: " + resourceType);
            
            // 检查文件是否存在
            java.io.File file = new java.io.File(filePath);
            if (!file.exists()) {
                System.err.println("文件不存在: " + filePath);
                return "文件不存在或无法访问: " + resourceUrl;
            }
            
            // 根据资源类型提取内容
            switch (resourceType) {
                case "pdf":
                    return extractPdfContent(filePath);
                case "word":
                case "doc":
                case "docx":
                    return extractWordContent(filePath);
                case "video":
                    // 视频资源无法直接提取文本内容
                    return "这是一个视频资源，无法直接提取文本内容进行AI分析。";
                default:
                    // 其他类型尝试作为文本文件处理
                    return extractTextContent(filePath);
            }
        } catch (Exception e) {
            System.err.println("资源内容提取失败: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 构建正确的文件路径
     */
    private String buildCorrectPath(String resourceUrl) {
        // 获取项目根目录
        String projectRoot = System.getProperty("user.dir");
        System.out.println("项目根目录: " + projectRoot);
        
        String filePath;
        
        // 处理不同的路径格式
        if (resourceUrl.startsWith("/public/")) {
            // 将 /public/xxx.pdf 转换为 ailesson_ui/public/xxx.pdf
            filePath = projectRoot + "/ailesson_ui" + resourceUrl;
        } else if (resourceUrl.startsWith("public/")) {
            // 将 public/xxx.pdf 转换为 ailesson_ui/public/xxx.pdf
            filePath = projectRoot + "/ailesson_ui/" + resourceUrl;
        } else if (resourceUrl.startsWith("http://") || resourceUrl.startsWith("https://")) {
            // 网络URL，直接返回
            return resourceUrl;
        } else {
            // 其他情况，假设是相对路径
            filePath = projectRoot + "/" + resourceUrl;
        }
        
        // 标准化路径
        try {
            filePath = new java.io.File(filePath).getCanonicalPath();
        } catch (Exception e) {
            System.err.println("路径标准化失败: " + e.getMessage());
        }
        
        return filePath;
    }
    
    /**
     * 提取PDF内容
     */
    private String extractPdfContent(String filePath) throws Exception {
        try (PDDocument document = PDDocument.load(new java.io.File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String content = stripper.getText(document);
            // 清理内容，去除多余空行和空格
            content = content.replaceAll("(?m)^[ \t]*\r?\n", "").trim();
            // 限制最大长度
            int maxLen = 3000;
            if (content.length() > maxLen) {
                content = content.substring(0, maxLen) + "\n...(内容过长已截断)";
            }
            return content;
        } catch (Exception e) {
            System.err.println("PDF内容提取失败: " + filePath + ", 错误: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 提取Word文档内容
     */
    private String extractWordContent(String filePath) throws Exception {
        try (java.io.FileInputStream inputStream = new java.io.FileInputStream(filePath)) {
            String content;
            
            if (filePath.toLowerCase().endsWith(".docx")) {
                // 处理.docx文件
                try (XWPFDocument doc = new XWPFDocument(inputStream)) {
                    XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
                    content = extractor.getText();
                }
            } else {
                // 处理.doc文件
                try (HWPFDocument doc = new HWPFDocument(inputStream)) {
                    WordExtractor extractor = new WordExtractor(doc);
                    content = extractor.getText();
                }
            }
            
            // 清理内容
            content = content.replaceAll("(?m)^[ \t]*\r?\n", "").trim();
            // 限制最大长度
            int maxLen = 3000;
            if (content.length() > maxLen) {
                content = content.substring(0, maxLen) + "\n...(内容过长已截断)";
            }
            return content;
        } catch (Exception e) {
            System.err.println("Word文档内容提取失败: " + filePath + ", 错误: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 提取文本内容
     */
    private String extractTextContent(String filePath) throws Exception {
        try (java.io.FileInputStream inputStream = new java.io.FileInputStream(filePath)) {
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            content = content.trim();
            // 限制最大长度
            int maxLen = 3000;
            if (content.length() > maxLen) {
                content = content.substring(0, maxLen) + "\n...(内容过长已截断)";
            }
            return content;
        } catch (Exception e) {
            System.err.println("文本内容提取失败: " + filePath + ", 错误: " + e.getMessage());
            throw e;
        }
    }
}
