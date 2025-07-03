<template>
  <div class="knowledge-graph-builder">
    <!-- 文件上传和构建区域 -->
    <div v-if="!showGraph" class="upload-section">
      <div class="upload-header">
        <h3>构建知识图谱</h3>
        <p>请上传课程相关文件，系统将自动构建知识图谱</p>
      </div>
      
      <div class="upload-area">
        <el-upload
          class="upload-demo"
          drag
          multiple
          :auto-upload="false"
          :on-change="handleFileChange"
          :file-list="fileList"
          accept=".pdf,.doc,.docx,.txt,.md"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              支持 PDF、Word、TXT、Markdown 格式文件，单个文件不超过10MB
            </div>
          </template>
        </el-upload>
      </div>
      
      <div class="file-list" v-if="fileList.length > 0">
        <h4>已选择的文件：</h4>
        <el-table :data="fileList" style="width: 100%">
          <el-table-column prop="name" label="文件名" />
          <el-table-column prop="size" label="文件大小">
            <template #default="scope">
              {{ formatFileSize(scope.row.size) }}
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button size="small" type="danger" @click="removeFile(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <div class="build-actions">
        <el-button 
          type="primary" 
          size="large" 
          @click="buildKnowledgeGraph"
          :loading="building"
          :disabled="fileList.length === 0"
        >
          {{ building ? '构建中...' : '构建知识图谱' }}
        </el-button>
      </div>
    </div>
    
    <!-- 知识图谱展示区域 -->
    <div v-else class="graph-section">
      <div class="graph-header">
        <h3>知识图谱</h3>
        <div class="graph-actions">
          <el-button @click="backToUpload">重新构建</el-button>
          <el-button type="primary" @click="exportGraph">导出图谱</el-button>
        </div>
      </div>
      
      <div class="graph-container">
        <!-- 这里可以集成 D3.js、ECharts 或其他图表库 -->
        <div class="graph-placeholder">
          <div class="graph-info">
            <h4>知识图谱预览</h4>
            <p>这里将显示基于上传文件构建的知识图谱</p>
            <div class="graph-stats">
              <div class="stat-item">
                <span class="stat-number">{{ graphStats.nodeCount }}</span>
                <span class="stat-label">知识点</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ graphStats.edgeCount }}</span>
                <span class="stat-label">关联关系</span>
              </div>
              <div class="stat-item">
                <span class="stat-number">{{ graphStats.fileCount }}</span>
                <span class="stat-label">源文件</span>
              </div>
            </div>
          </div>
          <div class="graph-visualization">
            <!-- 模拟知识图谱可视化 -->
            <div class="mock-graph">
              <!-- 中心节点 -->
              <div class="node central-node" @click="selectNode('数据结构')">数据结构</div>
              
              <!-- 第一层节点 -->
              <div class="node level1-node" @click="selectNode('线性结构')">线性结构</div>
              <div class="node level1-node" @click="selectNode('非线性结构')">非线性结构</div>
              <div class="node level1-node" @click="selectNode('查找算法')">查找算法</div>
              <div class="node level1-node" @click="selectNode('排序算法')">排序算法</div>
              
              <!-- 第二层节点 -->
              <div class="node level2-node" @click="selectNode('数组')">数组</div>
              <div class="node level2-node" @click="selectNode('链表')">链表</div>
              <div class="node level2-node" @click="selectNode('栈')">栈</div>
              <div class="node level2-node" @click="selectNode('队列')">队列</div>
              <div class="node level2-node" @click="selectNode('树')">树</div>
              <div class="node level2-node" @click="selectNode('图')">图</div>
              <div class="node level2-node" @click="selectNode('哈希表')">哈希表</div>
              <div class="node level2-node" @click="selectNode('二分查找')">二分查找</div>
              <div class="node level2-node" @click="selectNode('冒泡排序')">冒泡排序</div>
              <div class="node level2-node" @click="selectNode('快速排序')">快速排序</div>
              
              <!-- 第三层节点 -->
              <div class="node level3-node" @click="selectNode('一维数组')">一维数组</div>
              <div class="node level3-node" @click="selectNode('二维数组')">二维数组</div>
              <div class="node level3-node" @click="selectNode('单链表')">单链表</div>
              <div class="node level3-node" @click="selectNode('双链表')">双链表</div>
              <div class="node level3-node" @click="selectNode('二叉树')">二叉树</div>
              <div class="node level3-node" @click="selectNode('AVL树')">AVL树</div>
              <div class="node level3-node" @click="selectNode('红黑树')">红黑树</div>
              <div class="node level3-node" @click="selectNode('邻接矩阵')">邻接矩阵</div>
              <div class="node level3-node" @click="selectNode('邻接表')">邻接表</div>
              
              <!-- 连接线 -->
              <div class="connection conn1"></div>
              <div class="connection conn2"></div>
              <div class="connection conn3"></div>
              <div class="connection conn4"></div>
              <div class="connection conn5"></div>
              <div class="connection conn6"></div>
              <div class="connection conn7"></div>
              <div class="connection conn8"></div>
              <div class="connection conn9"></div>
              <div class="connection conn10"></div>
              <div class="connection conn11"></div>
              <div class="connection conn12"></div>
              <div class="connection conn13"></div>
              <div class="connection conn14"></div>
              <div class="connection conn15"></div>
              <div class="connection conn16"></div>
              <div class="connection conn17"></div>
              <div class="connection conn18"></div>
              <div class="connection conn19"></div>
              <div class="connection conn20"></div>
              
              <!-- 选中节点高亮 -->
              <div v-if="selectedNode" class="node-info">
                <h4>{{ selectedNode }}</h4>
                <p>这是关于 {{ selectedNode }} 的详细描述信息...</p>
                <div class="node-actions">
                  <el-button size="small" @click="viewNodeDetail">查看详情</el-button>
                  <el-button size="small" @click="relatedNodes">相关节点</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 学习资源上传区域 -->
    <div class="resource-upload-section">
      <div class="section-header">
        <h3>学习资源管理</h3>
        <p>选择知识节点并上传相关学习资源</p>
      </div>
      
      <div class="resource-form">
        <el-form :inline="true" :model="resourceForm">
          <el-form-item label="选择知识节点">
            <el-select v-model="resourceForm.selectedNode" placeholder="请选择知识节点" style="width: 200px;" @change="handleNodeChange">
              <el-option
                v-for="node in allNodes"
                :key="node.value"
                :label="node.label"
                :value="node.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="uploadResource" :disabled="!resourceForm.selectedNode">
              上传资源
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 资源上传对话框 -->
      <el-dialog v-model="uploadDialogVisible" title="上传学习资源" width="500px">
        <el-form :model="uploadForm" label-width="100px">
          <el-form-item label="资源描述">
            <el-input
              v-model="uploadForm.description"
              type="textarea"
              placeholder="请输入资源描述"
              :rows="3"
            />
          </el-form-item>
          <el-form-item label="上传文件">
            <el-upload
              class="resource-upload"
              :auto-upload="false"
              :on-change="handleResourceFileChange"
              :on-remove="handleResourceFileRemove"
              :file-list="resourceFileList"
              accept=".pdf,.mp4,.avi,.mov,.wmv,.flv,.mkv"
            >
              <el-button type="primary">选择文件</el-button>
              <template #tip>
                <div class="el-upload__tip">
                  支持PDF文档和视频文件（mp4、avi、mov、wmv、flv、mkv），暂不支持其他格式
                </div>
              </template>
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="uploadDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleUploadResource">确定</el-button>
        </template>
      </el-dialog>
      
      <!-- 资源列表 -->
      <div class="resource-list" v-if="resourceForm.selectedNode">
        <h4>{{ getNodeName(resourceForm.selectedNode) }} - 学习资源</h4>
        <el-table :data="filteredResources" style="width: 100%">
          <el-table-column prop="name" label="资源名称" />
          <el-table-column prop="type" label="资源类型">
            <template #default="scope">
              <el-tag :type="getResourceTypeColor(scope.row.type)">
                {{ getResourceTypeName(scope.row.type) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="描述" show-overflow-tooltip />
          <el-table-column prop="uploadTime" label="上传时间" width="180" />
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button size="small" @click="viewResource(scope.row)">查看</el-button>
              <el-button size="small" type="danger" @click="deleteResource(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue';
import { UploadFilled } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';

const props = defineProps<{ courseId: number | string }>();

// 状态管理
const showGraph = ref(true); // 默认显示图谱界面
const building = ref(false);
const fileList = ref<any[]>([]);

// 图谱统计信息
const graphStats = reactive({
  nodeCount: 42,
  edgeCount: 78,
  fileCount: 5
});

// 选中的节点
const selectedNode = ref('');

// 文件处理
function handleFileChange(file: any, fileList: any[]) {
  console.log('文件变化:', file, fileList);
}

function removeFile(index: number) {
  fileList.value.splice(index, 1);
}

function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

// 构建知识图谱
function buildKnowledgeGraph() {
  if (fileList.value.length === 0) {
    ElMessage.warning('请先选择文件');
    return;
  }
  
  building.value = true;
  
  // 模拟构建过程
  setTimeout(() => {
    building.value = false;
    showGraph.value = true;
    ElMessage.success('知识图谱构建完成！');
  }, 3000);
}

// 返回上传页面
function backToUpload() {
  showGraph.value = false;
  fileList.value = [];
}

// 导出图谱
function exportGraph() {
  ElMessage.success('知识图谱导出成功！');
}

// 节点选择
function selectNode(nodeName: string) {
  selectedNode.value = nodeName;
}

function viewNodeDetail() {
  ElMessage.info(`查看 ${selectedNode.value} 的详细信息`);
}

function relatedNodes() {
  ElMessage.info(`显示与 ${selectedNode.value} 相关的节点`);
}

// 资源管理相关
const resourceForm = reactive({
  selectedNode: ''
});

const uploadDialogVisible = ref(false);
const uploadForm = reactive({
  description: ''
});

const resourceFileList = ref<any[]>([]);

// 所有知识节点
const allNodes = ref<any[]>([]);

// 获取知识点数据
const fetchKnowledgeNodes = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/nodes/all');
    if (response.data.success && Array.isArray(response.data.data)) {
      allNodes.value = response.data.data.map((node: any) => ({
        label: node.name,
        value: node.id
      }));
    }
  } catch (error) {
    console.error('获取知识点失败:', error);
    ElMessage.error('获取知识点失败');
  }
};

// 页面加载时获取知识点
onMounted(() => {
  fetchKnowledgeNodes();
});

// 模拟资源数据
const allResources = ref([
  {
    id: 1,
    nodeId: 'data-structure',
    name: '数据结构基础教程',
    type: 'document',
    description: '详细介绍数据结构的基本概念和分类',
    uploadTime: '2024-01-10 14:30:00'
  },
  {
    id: 2,
    nodeId: 'array',
    name: '数组操作视频',
    type: 'video',
    description: '演示数组的各种操作方法和技巧',
    uploadTime: '2024-01-11 09:15:00'
  },
  {
    id: 3,
    nodeId: 'linked-list',
    name: '链表实现代码',
    type: 'document',
    description: '包含链表的各种实现方式和代码示例',
    uploadTime: '2024-01-12 16:45:00'
  },
  {
    id: 4,
    nodeId: 'binary-search',
    name: '二分查找算法图解',
    type: 'image',
    description: '通过图解方式展示二分查找算法的工作原理',
    uploadTime: '2024-01-13 11:20:00'
  },
  {
    id: 5,
    nodeId: 'quick-sort',
    name: '快速排序在线演示',
    type: 'link',
    description: '在线演示快速排序算法的执行过程',
    uploadTime: '2024-01-14 10:30:00'
  }
]);

// 计算属性
const filteredResources = computed(() => {
  if (!resourceForm.selectedNode) return [];
  return allResources.value.filter(resource => resource.nodeId === resourceForm.selectedNode);
});

// 监听节点选择变化
const handleNodeChange = (nodeId: string) => {
  if (nodeId) {
    fetchNodeResources(nodeId);
  } else {
    allResources.value = [];
  }
};

// 方法
function getNodeName(nodeId: string): string {
  const node = allNodes.value.find((n: any) => n.value === nodeId);
  return node ? node.label : '';
}

function getResourceTypeName(type: string): string {
  const typeMap: { [key: string]: string } = {
    'document': '文档',
    'video': '视频',
    'image': '图片',
    'link': '链接',
    'other': '其他'
  };
  return typeMap[type] || type;
}

function getResourceTypeColor(type: string): string {
  const colorMap: { [key: string]: string } = {
    'document': 'primary',
    'video': 'success',
    'image': 'warning',
    'link': 'info',
    'other': 'default'
  };
  return colorMap[type] || 'default';
}

function uploadResource() {
  uploadDialogVisible.value = true;
  uploadForm.description = '';
  resourceFileList.value = [];
}

function handleResourceFileChange(file: any, fileList: any[]) {
  console.log('资源文件变化:', file, fileList);
  // 更新文件列表
  resourceFileList.value = fileList;
}

function handleResourceFileRemove(file: any, fileList: any[]) {
  console.log('资源文件移除:', file, fileList);
  // 更新文件列表
  resourceFileList.value = fileList;
}

async function handleUploadResource() {
  if (resourceFileList.value.length === 0) {
    ElMessage.warning('请选择要上传的文件');
    return;
  }
  
  try {
    const file = resourceFileList.value[0].raw;
    const formData = new FormData();
    formData.append('file', file);
    formData.append('nodeId', resourceForm.selectedNode);
    formData.append('description', uploadForm.description || '');
    
    const response = await axios.post('http://localhost:8080/api/knowledge-resource/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      withCredentials: true
    });
    
    if (response.data.success) {
      ElMessage.success('资源上传成功！');
      uploadDialogVisible.value = false;
      // 刷新资源列表
      fetchNodeResources(resourceForm.selectedNode);
    } else {
      ElMessage.error(response.data.message || '上传失败');
    }
  } catch (error: any) {
    console.error('上传失败:', error);
    ElMessage.error('上传失败: ' + (error.response?.data?.message || error.message));
  }
}

// 获取指定节点的资源列表
const fetchNodeResources = async (nodeId: string) => {
  try {
    const response = await axios.get(`http://localhost:8080/getResourceId?nodeId=${nodeId}`);
    if (Array.isArray(response.data)) {
      // 获取资源详情
      const resourceDetails = [];
      for (const resourceId of response.data) {
        const resourceResponse = await axios.get(`http://localhost:8080/getresources?resource_id=${resourceId}`);
        if (resourceResponse.data && resourceResponse.data.length > 0) {
          const resource = resourceResponse.data[0];
          resourceDetails.push({
            id: resource.resource_id,
            nodeId: nodeId,
            name: resource.resource_name,
            type: resource.res_type,
            description: resource.res_description,
            uploadTime: new Date().toLocaleString() // 这里可以添加创建时间字段
          });
        }
      }
      allResources.value = resourceDetails;
    }
  } catch (error) {
    console.error('获取节点资源失败:', error);
  }
};

function viewResource(resource: any) {
  ElMessage.info(`查看资源: ${resource.name}`);
}

function deleteResource(resource: any) {
  const index = allResources.value.findIndex(r => r.id === resource.id);
  if (index > -1) {
    allResources.value.splice(index, 1);
    ElMessage.success('资源删除成功！');
  }
}
</script>

<style scoped>
.knowledge-graph-builder {
  padding: 20px;
}

.upload-section {
  max-width: 800px;
  margin: 0 auto;
}

.upload-header {
  text-align: center;
  margin-bottom: 30px;
}

.upload-header h3 {
  color: #303133;
  margin-bottom: 10px;
}

.upload-header p {
  color: #606266;
  margin: 0;
}

.upload-area {
  margin-bottom: 30px;
}

.file-list {
  margin-bottom: 30px;
}

.file-list h4 {
  margin-bottom: 15px;
  color: #303133;
}

.build-actions {
  text-align: center;
}

.graph-section {
  max-width: 1200px;
  margin: 0 auto;
}

.graph-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.graph-header h3 {
  margin: 0;
  color: #303133;
}

.graph-actions {
  display: flex;
  gap: 12px;
}

.graph-container {
  background: #fff;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.graph-placeholder {
  display: flex;
  gap: 40px;
  align-items: center;
}

.graph-info {
  flex: 1;
}

.graph-info h4 {
  color: #303133;
  margin-bottom: 10px;
}

.graph-info p {
  color: #606266;
  margin-bottom: 20px;
}

.graph-stats {
  display: flex;
  gap: 30px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.graph-visualization {
  flex: 2;
  height: 400px;
  position: relative;
  background: #f8f9fa;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.mock-graph {
  position: relative;
  width: 100%;
  height: 100%;
}

.node {
  position: absolute;
  padding: 10px 15px;
  background: #409EFF;
  color: white;
  border-radius: 20px;
  font-size: 14px;
  font-weight: bold;
  text-align: center;
  min-width: 80px;
}

.central-node {
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: #67C23A;
}

.level1-node {
  background: #E6A23C;
}

.level1-node:nth-child(2) {
  top: 15%;
  left: 15%;
}

.level1-node:nth-child(3) {
  top: 15%;
  right: 15%;
}

.level1-node:nth-child(4) {
  bottom: 15%;
  left: 15%;
}

.level1-node:nth-child(5) {
  bottom: 15%;
  right: 15%;
}

.level2-node {
  background: #F56C6C;
}

.level2-node:nth-child(6) {
  top: 25%;
  left: 25%;
}

.level2-node:nth-child(7) {
  top: 25%;
  right: 25%;
}

.level2-node:nth-child(8) {
  top: 35%;
  left: 10%;
}

.level2-node:nth-child(9) {
  top: 35%;
  right: 10%;
}

.level2-node:nth-child(10) {
  bottom: 25%;
  left: 25%;
}

.level2-node:nth-child(11) {
  bottom: 25%;
  right: 25%;
}

.level2-node:nth-child(12) {
  bottom: 35%;
  left: 10%;
}

.level2-node:nth-child(13) {
  bottom: 35%;
  right: 10%;
}

.level2-node:nth-child(14) {
  top: 45%;
  left: 5%;
}

.level2-node:nth-child(15) {
  top: 45%;
  right: 5%;
}

.level3-node {
  background: #909399;
  font-size: 12px;
  padding: 8px 12px;
  min-width: 60px;
}

.level3-node:nth-child(16) {
  top: 30%;
  left: 35%;
}

.level3-node:nth-child(17) {
  top: 30%;
  right: 35%;
}

.level3-node:nth-child(18) {
  top: 40%;
  left: 20%;
}

.level3-node:nth-child(19) {
  top: 40%;
  right: 20%;
}

.level3-node:nth-child(20) {
  bottom: 30%;
  left: 35%;
}

.level3-node:nth-child(21) {
  bottom: 30%;
  right: 35%;
}

.level3-node:nth-child(22) {
  bottom: 40%;
  left: 20%;
}

.level3-node:nth-child(23) {
  bottom: 40%;
  right: 20%;
}

.level3-node:nth-child(24) {
  top: 50%;
  left: 15%;
}

.level3-node:nth-child(25) {
  top: 50%;
  right: 15%;
}

.connection {
  position: absolute;
  height: 2px;
  background: #DCDFE6;
  transform-origin: left center;
  z-index: 1;
}

.conn1 {
  top: 50%;
  left: 50%;
  width: 60px;
  transform: translate(-50%, -50%) rotate(-45deg);
}

.conn2 {
  top: 50%;
  left: 50%;
  width: 60px;
  transform: translate(-50%, -50%) rotate(45deg);
}

.conn3 {
  top: 50%;
  left: 50%;
  width: 60px;
  transform: translate(-50%, -50%) rotate(-135deg);
}

.conn4 {
  top: 50%;
  left: 50%;
  width: 60px;
  transform: translate(-50%, -50%) rotate(135deg);
}

.conn5 {
  top: 15%;
  left: 15%;
  width: 40px;
  transform: rotate(45deg);
}

.conn6 {
  top: 15%;
  right: 15%;
  width: 40px;
  transform: rotate(-45deg);
}

.conn7 {
  bottom: 15%;
  left: 15%;
  width: 40px;
  transform: rotate(-45deg);
}

.conn8 {
  bottom: 15%;
  right: 15%;
  width: 40px;
  transform: rotate(45deg);
}

.conn9 {
  top: 25%;
  left: 25%;
  width: 30px;
  transform: rotate(30deg);
}

.conn10 {
  top: 25%;
  right: 25%;
  width: 30px;
  transform: rotate(-30deg);
}

.conn11 {
  bottom: 25%;
  left: 25%;
  width: 30px;
  transform: rotate(-30deg);
}

.conn12 {
  bottom: 25%;
  right: 25%;
  width: 30px;
  transform: rotate(30deg);
}

.conn13 {
  top: 35%;
  left: 10%;
  width: 25px;
  transform: rotate(60deg);
}

.conn14 {
  top: 35%;
  right: 10%;
  width: 25px;
  transform: rotate(-60deg);
}

.conn15 {
  bottom: 35%;
  left: 10%;
  width: 25px;
  transform: rotate(-60deg);
}

.conn16 {
  bottom: 35%;
  right: 10%;
  width: 25px;
  transform: rotate(60deg);
}

.conn17 {
  top: 45%;
  left: 5%;
  width: 20px;
  transform: rotate(75deg);
}

.conn18 {
  top: 45%;
  right: 5%;
  width: 20px;
  transform: rotate(-75deg);
}

.conn19 {
  top: 50%;
  left: 15%;
  width: 15px;
  transform: rotate(90deg);
}

.conn20 {
  top: 50%;
  right: 15%;
  width: 15px;
  transform: rotate(-90deg);
}

.node-info {
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  background: #fff;
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.node-info h4 {
  color: #303133;
  margin-bottom: 10px;
}

.node-info p {
  color: #606266;
  margin-bottom: 20px;
}

.node-actions {
  text-align: right;
}

.resource-upload-section {
  margin-top: 30px;
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section-header {
  text-align: center;
  margin-bottom: 24px;
}

.section-header h3 {
  color: #303133;
  margin-bottom: 8px;
}

.section-header p {
  color: #606266;
  margin: 0;
}

.resource-form {
  margin-bottom: 24px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 6px;
}

.resource-list {
  margin-top: 20px;
}

.resource-list h4 {
  color: #303133;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 2px solid #409EFF;
}

.resource-upload {
  width: 100%;
}

.resource-upload .el-upload {
  width: 100%;
}

.resource-upload .el-upload-dragger {
  width: 100%;
}
</style> 