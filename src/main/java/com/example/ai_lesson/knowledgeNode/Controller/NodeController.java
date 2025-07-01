package com.example.ai_lesson.knowledgeNode.Controller;


import com.example.ai_lesson.knowledgeNode.Domain.Node;
import com.example.ai_lesson.knowledgeNode.Service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/nodes")
@CrossOrigin(origins = "*")
public class NodeController {
    private final NodeService nodeService;

    @Autowired
    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @PostMapping
    public int addNode(@RequestBody Node node) {
        return nodeService.addNode(node);
    }

    @PutMapping
    public int updateNode(@RequestBody Node node) {
        return nodeService.updateNode(node);
    }

    @DeleteMapping("/{id}")
    public int deleteNode(@PathVariable int id) {
        return nodeService.deleteNode(id);
    }

    @GetMapping("/{id}")
    public Node getNode(@PathVariable int id) {
        return nodeService.getNodeById(id);
    }

    @GetMapping("/children/{parentId}")
    public List<Node> getChildren(@PathVariable int parentId) {
        return nodeService.getChildren(parentId);
    }

    @GetMapping("/all")
    public Map<String, Object> getAllNodes() {
        System.out.println("=== 知识点接口被调用 ===");
        System.out.println("请求路径: /api/nodes/all");
        
        Map<String, Object> response = new HashMap<>();
        try {
            List<Node> nodes = nodeService.getAllNodes();
            System.out.println("查询到知识点数量: " + (nodes != null ? nodes.size() : 0));
            if (nodes != null) {
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    System.out.println("知识点" + (i + 1) + ": ID=" + node.getId() + ", 名称=" + node.getName());
                }
            }
            
            response.put("success", true);
            response.put("data", nodes);
            response.put("message", "获取知识点列表成功");
            System.out.println("=== 知识点接口调用完成 ===");
        } catch (Exception e) {
            System.err.println("获取知识点列表失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "获取知识点列表失败: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/subtree/{rootId}")
    public List<Node> getSubtree(@PathVariable int rootId) {
        return nodeService.getSubtree(rootId);
    }

    @GetMapping("/graph")
    public List<Node> getKnowledgeGraph() {
        return nodeService.getKnowledgeGraph();
    }
}