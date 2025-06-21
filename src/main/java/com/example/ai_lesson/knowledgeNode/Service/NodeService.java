package com.example.ai_lesson.knowledgeNode.Service;

import com.example.ai_lesson.knowledgeNode.Domain.Node;
import com.example.ai_lesson.knowledgeNode.Mapper.NodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeService {
    private final NodeMapper nodeMapper;

    @Autowired
    public NodeService(NodeMapper nodeMapper) {
        this.nodeMapper = nodeMapper;
    }

    public int addNode(Node node) {
        return nodeMapper.insert(node);
    }

    public int updateNode(Node node) {
        return nodeMapper.update(node);
    }

    public int deleteNode(int id) {
        return nodeMapper.deleteById(id);
    }

    public Node getNodeById(int id) {
        return nodeMapper.selectById(id);
    }

    public List<Node> getChildren(int parentId) {
        return nodeMapper.selectByParentId(parentId);
    }

    public List<Node> getAllNodes() {
        return nodeMapper.selectAll();
    }

    public List<Node> getSubtree(int rootId) {
        return nodeMapper.selectSubtree(rootId);
    }

    public List<Node> getKnowledgeGraph() {
        // 这里可以实现更复杂的图谱构建逻辑
        return nodeMapper.selectAll();
    }
}