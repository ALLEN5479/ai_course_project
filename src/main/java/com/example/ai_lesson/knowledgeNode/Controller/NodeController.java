package com.example.ai_lesson.knowledgeNode.Controller;


import com.example.ai_lesson.knowledgeNode.Domain.Node;
import com.example.ai_lesson.knowledgeNode.Service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nodes")
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
    public List<Node> getAllNodes() {
        return nodeService.getAllNodes();
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