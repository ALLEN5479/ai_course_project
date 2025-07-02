package com.example.ai_lesson.aiquestion.controller;

import com.example.ai_lesson.aiquestion.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;

import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {
    
    private final ChatService chatService;
    
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    // 发送聊天消息
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody ChatRequest request) {
        try {
            String prompt = request.getMessage();
            if (request.getFileContent() != null && !request.getFileContent().isEmpty()) {
                prompt = "请结合以下文件内容回答问题：\n" +
                         "文件内容：\n" + request.getFileContent() + "\n" +
                         "问题：" + request.getMessage();
            }
            String response = chatService.sendMessage(prompt, request.getConversationId());
            return ResponseEntity.ok(Map.of("response", response));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "AI服务暂时不可用，请稍后重试"));
        } finally {
            request.setFileContent(null);
        }
    }
    
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String content;
            if (fileName != null && (fileName.endsWith(".docx") || fileName.endsWith(".doc"))) {
                if (fileName.endsWith(".docx")) {
                    // XWPFDocument 解析
                    try (XWPFDocument doc = new XWPFDocument(file.getInputStream())) {
                        XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
                        content = extractor.getText();
                        // 去除多余空行和空格
                        content = content.replaceAll("(?m)^[ \t]*\r?\n", "").trim();
                        System.out.println("DOCX内容预览：" + content);
                    }
                } else {
                    // HWPFDocument 解析
                    try (HWPFDocument doc = new HWPFDocument(file.getInputStream())) {
                        WordExtractor extractor = new WordExtractor(doc);
                        content = extractor.getText();
                        content = content.replaceAll("(?m)^[ \t]*\r?\n", "").trim();
                        System.out.println("DOC内容预览：" + content);
                    }
                }
                // 统一的内容处理
            } else if (fileName != null && (fileName.endsWith(".xlsx") || fileName.endsWith(".xls"))) {
                // 处理Excel文件（.xlsx和.xls）
                try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
                    StringBuilder sb = new StringBuilder();
                    int maxRows = 20; // 只读取前20行
                    int rowCount = 0;
                    for (Sheet sheet : workbook) {
                        sb.append("[Sheet: ").append(sheet.getSheetName()).append("]\n");
                        for (Row row : sheet) {
                            if (rowCount++ >= maxRows) break;
                            for (Cell cell : row) {
                                sb.append(cell.toString()).append("\t");
                            }
                            sb.append("\n");
                        }
                        if (rowCount >= maxRows) break;
                    }
                    content = sb.toString().trim();
                    System.out.println("Excel内容预览：" + content);
                }
            } else if (fileName != null && fileName.endsWith(".pdf")) {
                // 处理PDF文件
                try (PDDocument document = PDDocument.load(file.getInputStream())) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    content = stripper.getText(document);
                    content = content.replaceAll("(?m)^[ \t]*\r?\n", "").trim();
                    // 限制最大长度
                    int maxLen = 2000;
                    if (content.length() > maxLen) {
                        content = content.substring(0, maxLen) + "\n...(内容过长已截断)";
                    }
                    System.out.println("PDF内容预览：" + content);
                }
            } else {
                // 默认按文本文件处理
                content = new String(file.getBytes(), StandardCharsets.UTF_8);
                content = content.trim();
            }
            // 限制最大长度
            int maxLen = 2000;
            if (content.length() > maxLen) {
                content = content.substring(0, maxLen) + "\n...(内容过长已截断)";
            }
            return ResponseEntity.ok(Map.of(
                "fileName", fileName,
                "fileContent", content
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "文件上传失败"));
        }
    }
    
    // 聊天请求类
    public static class ChatRequest {
        private String message;
        private String conversationId;
        private String fileContent;
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public String getConversationId() {
            return conversationId;
        }
        
        public void setConversationId(String conversationId) {
            this.conversationId = conversationId;
        }
        
        public String getFileContent() {
            return fileContent;
        }
        
        public void setFileContent(String fileContent) {
            this.fileContent = fileContent;
        }
    }
} 