<template>
  <div class="task-grading">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      <span class="page-title">任务批改</span>
    </div>

    <!-- 任务信息 -->
    <div class="task-info-section">
      <el-card>
        <div class="task-header">
          <h3>{{ taskInfo.taskName }}</h3>
          <el-tag :type="taskInfo.status === 'pending' ? 'warning' : 'success'">
            {{ taskInfo.status === 'pending' ? '待批改' : '已批改' }}
          </el-tag>
        </div>
        <div class="task-details">
          <div class="detail-item">
            <span class="label">学生姓名：</span>
            <span class="value">{{ taskInfo.studentName }}</span>
          </div>
          <div class="detail-item">
            <span class="label">提交时间：</span>
            <span class="value">{{ taskInfo.submitTime }}</span>
          </div>
          <div class="detail-item">
            <span class="label">任务类型：</span>
            <span class="value">{{ taskInfo.taskType }}</span>
          </div>
          <div class="detail-item">
            <span class="label">截止时间：</span>
            <span class="value">{{ taskInfo.deadline }}</span>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 报告显示区域 -->
    <div class="report-section">
      <el-card>
        <div class="section-header">
          <h4>学生提交的报告</h4>
          <div class="report-actions">
            <el-button type="primary" @click="previewReport">
              <el-icon><View /></el-icon>
              预览报告
            </el-button>
            <el-button type="success" @click="downloadReport">
              <el-icon><Download /></el-icon>
              下载报告
            </el-button>
          </div>
        </div>
        
        <div class="report-content">
          <div class="report-icon">
            <el-icon size="80" color="#409EFF"><Document /></el-icon>
            <p>{{ taskInfo.reportName }}</p>
            <p class="file-info">{{ taskInfo.fileSize }} | {{ taskInfo.fileType }}</p>
          </div>
          
          <!-- 报告预览区域 -->
          <div v-if="showPreview" class="report-preview">
            <div class="preview-header">
              <h5>报告预览</h5>
              <el-button size="small" @click="showPreview = false">关闭预览</el-button>
            </div>
            <div class="preview-content">
              <div class="preview-text">
                <h4>数据结构项目报告</h4>
                <p><strong>学生：</strong>{{ taskInfo.studentName }}</p>
                <p><strong>提交时间：</strong>{{ taskInfo.submitTime }}</p>
                <hr>
                <h5>1. 项目背景</h5>
                <p>本项目旨在通过实现基本的数据结构来加深对算法和数据组织的理解。选择实现数组、链表、栈、队列等基础数据结构，并分析其性能特点。</p>
                
                <h5>2. 技术方案</h5>
                <p>采用面向对象的设计方法，为每种数据结构创建独立的类。使用C++语言实现，确保代码的可读性和可维护性。</p>
                
                <h5>3. 实现过程</h5>
                <p>首先实现了基础的数组结构，包括动态数组的扩容机制。然后实现了单链表和双链表，支持插入、删除、查找等操作。</p>
                
                <h5>4. 结果分析</h5>
                <p>通过性能测试发现，数组在随机访问方面表现优异，而链表在插入删除操作上更有优势。栈和队列的实现验证了后进先出和先进先出的特性。</p>
                
                <h5>5. 总结与反思</h5>
                <p>通过本次项目，深入理解了不同数据结构的特点和适用场景。在实现过程中遇到了一些挑战，如内存管理和边界条件处理，这些经验对今后的编程工作很有帮助。</p>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 批改区域 -->
    <div class="grading-section">
      <el-card>
        <div class="section-header">
          <h4>教师批改</h4>
          <el-button type="warning" @click="aiAssistGrading">
            <el-icon><MagicStick /></el-icon>
            AI辅助批改
          </el-button>
        </div>
        
        <el-form :model="gradingForm" label-width="100px">
          <el-form-item label="评分">
            <el-input-number
              v-model="gradingForm.score"
              :min="0"
              :max="100"
              :precision="1"
              placeholder="请输入分数（0-100）"
            />
            <span class="score-hint">满分100分</span>
          </el-form-item>
          
          <el-form-item label="评价内容">
            <el-input
              v-model="gradingForm.comment"
              type="textarea"
              :rows="6"
              placeholder="请输入评价内容，包括优点、不足和改进建议..."
            />
          </el-form-item>
          
          <el-form-item label="评价标签">
            <el-checkbox-group v-model="gradingForm.tags">
              <el-checkbox label="内容完整">内容完整</el-checkbox>
              <el-checkbox label="逻辑清晰">逻辑清晰</el-checkbox>
              <el-checkbox label="创新性强">创新性强</el-checkbox>
              <el-checkbox label="格式规范">格式规范</el-checkbox>
              <el-checkbox label="技术深度">技术深度</el-checkbox>
              <el-checkbox label="按时提交">按时提交</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="submitGrading" :loading="submitting">
              {{ submitting ? '提交中...' : '提交批改' }}
            </el-button>
            <el-button @click="saveDraft">保存草稿</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- AI辅助批改对话框 -->
    <el-dialog v-model="aiDialogVisible" title="AI辅助批改" width="600px">
      <div class="ai-grading-content">
        <div class="ai-analysis">
          <h5>AI分析结果</h5>
          <div class="ai-score">
            <span class="score-label">AI建议评分：</span>
            <span class="score-value">{{ aiAnalysis.score }}分</span>
          </div>
          <div class="ai-comment">
            <h6>评价建议：</h6>
            <p>{{ aiAnalysis.comment }}</p>
          </div>
          <div class="ai-tags">
            <h6>推荐标签：</h6>
            <el-tag
              v-for="tag in aiAnalysis.tags"
              :key="tag"
              class="ai-tag"
              @click="addAiTag(tag)"
            >
              {{ tag }}
            </el-tag>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="aiDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="applyAiGrading">应用AI建议</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { View, Download, Document, MagicStick } from '@element-plus/icons-vue';

const router = useRouter();
const route = useRoute();

// 任务信息
const taskInfo = reactive({
  taskName: '数据结构项目报告',
  studentName: '张三',
  submitTime: '2024-01-15 14:30:00',
  taskType: '报告任务',
  deadline: '2024-01-16 23:59:59',
  status: 'pending',
  reportName: '数据结构项目报告.pdf',
  fileSize: '2.5MB',
  fileType: 'PDF文档'
});

// 批改表单
const gradingForm = reactive({
  score: null,
  comment: '',
  tags: []
});

// 状态管理
const showPreview = ref(false);
const submitting = ref(false);
const aiDialogVisible = ref(false);

// AI分析结果
const aiAnalysis = reactive({
  score: 85,
  comment: '该报告内容完整，逻辑清晰，技术实现合理。学生在项目背景描述、技术方案设计、实现过程记录等方面都做得很好。代码结构清晰，注释详细。建议在性能分析部分可以更加深入，可以添加更多实际测试数据。总体来说是一份质量较高的项目报告。',
  tags: ['内容完整', '逻辑清晰', '技术深度', '格式规范']
});

// 方法
function goBack() {
  router.back();
}

function previewReport() {
  showPreview.value = true;
}

function downloadReport() {
  ElMessage.success('报告下载成功！');
}

function aiAssistGrading() {
  aiDialogVisible.value = true;
}

function addAiTag(tag: string) {
  if (!gradingForm.tags.includes(tag)) {
    gradingForm.tags.push(tag);
  }
}

function applyAiGrading() {
  gradingForm.score = aiAnalysis.score;
  gradingForm.comment = aiAnalysis.comment;
  aiDialogVisible.value = false;
  ElMessage.success('已应用AI建议！');
}

function submitGrading() {
  if (!gradingForm.score) {
    ElMessage.warning('请填写评分');
    return;
  }
  if (!gradingForm.comment.trim()) {
    ElMessage.warning('请填写评价内容');
    return;
  }
  
  submitting.value = true;
  
  // 模拟提交
  setTimeout(() => {
    submitting.value = false;
    taskInfo.status = 'graded';
    ElMessage.success('批改提交成功！');
  }, 2000);
}

function saveDraft() {
  ElMessage.success('草稿保存成功！');
}

function resetForm() {
  gradingForm.score = null;
  gradingForm.comment = '';
  gradingForm.tags = [];
  ElMessage.info('表单已重置');
}

// 生命周期
onMounted(() => {
  // 从路由参数获取任务ID
  const taskId = route.query.taskId;
  console.log('批改任务ID:', taskId);
  
  // 这里可以根据taskId加载具体的任务数据
});
</script>

<style scoped>
.task-grading {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 20px;
  font-weight: bold;
  margin-left: 16px;
}

.task-info-section {
  margin-bottom: 24px;
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.task-header h3 {
  margin: 0;
  color: #303133;
}

.task-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.detail-item {
  display: flex;
  align-items: center;
}

.detail-item .label {
  font-weight: bold;
  color: #606266;
  margin-right: 8px;
}

.detail-item .value {
  color: #303133;
}

.report-section {
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h4 {
  margin: 0;
  color: #303133;
}

.report-actions {
  display: flex;
  gap: 12px;
}

.report-content {
  position: relative;
}

.report-icon {
  text-align: center;
  padding: 40px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 2px dashed #dcdfe6;
}

.report-icon p {
  margin: 10px 0 0 0;
  color: #606266;
}

.file-info {
  font-size: 12px;
  color: #909399;
}

.report-preview {
  margin-top: 20px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  overflow: hidden;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #f5f7fa;
  border-bottom: 1px solid #dcdfe6;
}

.preview-header h5 {
  margin: 0;
  color: #303133;
}

.preview-content {
  padding: 20px;
  max-height: 400px;
  overflow-y: auto;
}

.preview-text h4 {
  color: #303133;
  margin-bottom: 16px;
}

.preview-text h5 {
  color: #303133;
  margin: 20px 0 10px 0;
}

.preview-text p {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 10px;
}

.grading-section {
  margin-bottom: 24px;
}

.score-hint {
  margin-left: 12px;
  color: #909399;
  font-size: 14px;
}

.ai-grading-content {
  padding: 20px;
}

.ai-analysis h5 {
  color: #303133;
  margin-bottom: 16px;
}

.ai-score {
  margin-bottom: 16px;
}

.score-label {
  font-weight: bold;
  color: #606266;
}

.score-value {
  font-size: 18px;
  font-weight: bold;
  color: #409EFF;
  margin-left: 8px;
}

.ai-comment {
  margin-bottom: 16px;
}

.ai-comment h6 {
  color: #303133;
  margin-bottom: 8px;
}

.ai-comment p {
  color: #606266;
  line-height: 1.6;
  margin: 0;
}

.ai-tags h6 {
  color: #303133;
  margin-bottom: 8px;
}

.ai-tag {
  margin-right: 8px;
  margin-bottom: 8px;
  cursor: pointer;
}

.ai-tag:hover {
  background-color: #409EFF;
  color: white;
}
</style> 