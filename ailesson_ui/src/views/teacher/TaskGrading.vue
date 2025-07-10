<template>
  <div class="task-grading">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      <span class="page-title">任务批改</span>
    </div>

    <!-- 学生列表选择 -->
    <div class="student-list-section" style="margin-bottom: 20px;">
      <el-radio-group v-model="selectedStudentId" @change="onStudentChange">
        <el-radio-button
          v-for="student in studentList"
          :key="student.studentId"
          :label="student.studentId"
        >
          {{ student.studentName }}（{{ student.studentId }}）
        </el-radio-button>
      </el-radio-group>
    </div>

    <!-- 任务信息 -->
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
            <p class="file-info">{{ taskInfo.fileType }}</p>
          </div>
          
          <!-- 报告预览区域 -->
          <div v-if="showPreview && taskInfo.reportUrl" class="report-preview">
            <div class="preview-header">
              <h5>报告预览</h5>
              <el-button size="small" @click="showPreview = false">关闭预览</el-button>
            </div>
            <div class="preview-content">
              <template v-if="taskInfo.fileType === 'PDF'">
                <iframe :src="taskInfo.reportUrl" width="100%" height="500px" style="border:none;"></iframe>
              </template>
              <template v-else-if="taskInfo.fileType === 'TXT'">
                <iframe :src="taskInfo.reportUrl" width="100%" height="500px" style="border:none;"></iframe>
              </template>
              <template v-else-if="taskInfo.fileType === 'Word'">
                <input type="file" accept=".docx" @change="onLocalDocxChange" />
                <div v-html="docxHtml" style="margin-top:16px; border:1px solid #eee; padding:12px; background:#fafbfc;"></div>
              </template>
              <template v-else>
                <p>暂不支持该类型文件的在线预览，请下载后查看。</p>
              </template>
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
          <el-form-item v-if="hasGraded && !editingScore">
            <el-tag type="success">已批改</el-tag>
            <span style="margin-left: 12px;">评分：{{ gradedScore }}</span>
            <el-button type="primary" size="small" @click="editingScore = true" style="margin-left: 16px;">更新成绩</el-button>
          </el-form-item>
          <template v-else>
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
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </template>
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
import { taskGradingApi } from '@/api/taskApi';
import axios from 'axios';
import * as mammoth from 'mammoth';

const router = useRouter();
const route = useRoute();

interface TaskInfo {
  taskName: string;
  studentName: string;
  submitTime: string;
  taskType: string;
  deadline: string;
  status: string;
  reportName: string;
  fileSize: string;
  fileType: string;
  reportUrl: string;
}

interface GradingForm {
  score: number | null;
  comment: string;
  tags: string[];
}

// 任务信息
const taskInfo = reactive<TaskInfo>({
  taskName: '',
  studentName: '',
  submitTime: '',
  taskType: '',
  deadline: '',
  status: 'pending',
  reportName: '',
  fileSize: '',
  fileType: '',
  reportUrl: ''
});

// 批改表单
const gradingForm = reactive<GradingForm>({
  score: null,
  comment: '',
  tags: []
});

// 状态管理
const showPreview = ref(false);
const submitting = ref(false);
const aiDialogVisible = ref(false);
const editingScore = ref(false);

// AI分析结果
const aiAnalysis = reactive({
  score: 85,
  comment: '该报告内容完整，逻辑清晰，技术实现合理。学生在项目背景描述、技术方案设计、实现过程记录等方面都做得很好。代码结构清晰，注释详细。建议在性能分析部分可以更加深入，可以添加更多实际测试数据。总体来说是一份质量较高的项目报告。',
  tags: ['内容完整', '逻辑清晰', '技术深度', '格式规范']
});

// 学生列表
const studentList = ref<{ studentId: string; studentName: string }[]>([]);
const selectedStudentId = ref<string>('');

// 获取学生列表
const fetchStudentList = async (publishedMissionId: string) => {
  try {
    // 新接口，直接用publishedMissionId
    const { data } = await axios.get(`http://localhost:8080/api/task-grading/students`, { params: { publishedMissionId } });
    if (Array.isArray(data)) {
      studentList.value = data;
      if (data.length > 0) {
        selectedStudentId.value = data[0].studentId;
        await fetchStudentReport(publishedMissionId, selectedStudentId.value);
      }
    }
  } catch (e) {
    ElMessage.error('获取学生列表失败');
  }
};

// 获取学生报告
const fetchStudentReport = async (publishedMissionId: string, studentId: string) => {
  try {
    const missionIdResp = await axios.get(`http://localhost:8080/api/task-grading/published-mission/mission-id`, { params: { publishedMissionId } });
    const missionId = missionIdResp.data.missionId;
    const url = `http://localhost:8080/api/task-grading/student-report?missionId=${missionId}&studentId=${studentId}`;
    const { data } = await axios.get(url);
    taskInfo.reportName = data.report_name;
    taskInfo.reportUrl = data.report_url;
    // 可根据report_url后缀判断类型
    const ext = data.report_url?.split('.').pop()?.toLowerCase();
    if (ext === 'pdf') {
      taskInfo.fileType = 'PDF';
    } else if (ext === 'doc' || ext === 'docx') {
      taskInfo.fileType = 'Word';
    } else if (ext === 'txt') {
      taskInfo.fileType = 'TXT';
    } else {
      taskInfo.fileType = '未知';
    }
  } catch (e) {
    taskInfo.reportName = '';
    taskInfo.reportUrl = '';
    taskInfo.fileType = '';
    ElMessage.error('获取学生报告失败');
  }
};

const hasGraded = ref(false);
const gradedScore = ref<number|null>(null);

const fetchStudentScore = async (publishedMissionId: string, studentId: string) => {
  try {
    const { data } = await axios.get('http://localhost:8080/api/task-grading/score', {
      params: { publishedMissionId, studentId }
    });
    if (typeof data === 'number' && !isNaN(data)) {
      hasGraded.value = true;
      gradedScore.value = data;
      editingScore.value = false; // 查询到分数时默认关闭编辑
    } else {
      hasGraded.value = false;
      gradedScore.value = null;
      editingScore.value = false;
    }
  } catch (e) {
    hasGraded.value = false;
    gradedScore.value = null;
    editingScore.value = false;
  }
};

// 在切换学生和页面加载时调用
onMounted(async () => {
  const publishedMissionId = route.query.publishedMissionId as string | undefined;
  if (typeof publishedMissionId === 'string' && publishedMissionId) {
    await fetchStudentList(publishedMissionId);
    if (selectedStudentId.value) {
      await fetchStudentReport(publishedMissionId, selectedStudentId.value);
      await fetchStudentScore(publishedMissionId, selectedStudentId.value);
    }
  }
});

const onStudentChange = async () => {
  editingScore.value = false;
  if (selectedStudentId.value) {
    const publishedMissionId = route.query.publishedMissionId as string | undefined;
    if (typeof publishedMissionId === 'string' && publishedMissionId) {
      await fetchStudentReport(publishedMissionId, selectedStudentId.value);
      await fetchStudentScore(publishedMissionId, selectedStudentId.value);
    }
  }
};

// 方法
function goBack() {
  router.back();
}

function previewReport() {
  showPreview.value = true;
}

function downloadReport() {
  if (taskInfo.reportUrl) {
    window.open(taskInfo.reportUrl, '_blank');
  } else {
    ElMessage.error('暂无可下载的报告');
  }
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

async function submitGrading() {
  const publishedMissionId = route.query.publishedMissionId as string | undefined;
  if (!publishedMissionId) {
    ElMessage.error('缺少任务ID参数');
    return;
  }
  const studentId = selectedStudentId.value;
  const score = gradingForm.score;
  try {
    submitting.value = true;
    await taskGradingApi.submitGrading(
      Number(publishedMissionId),
      String(studentId),
      Number(score)
    );
    ElMessage.success('批改分数保存成功');
    editingScore.value = false;
    // 其他刷新逻辑
  } catch (e) {
    ElMessage.error('保存失败');
  } finally {
    submitting.value = false;
  }
}

async function saveDraft() {
  try {
    const submissionId = parseInt(route.query.submissionId as string);
    await taskGradingApi.saveGradingDraft(submissionId, {
      score: gradingForm.score,
      comment: gradingForm.comment,
      tags: gradingForm.tags
    });
    
    ElMessage.success('草稿保存成功！');
  } catch (error) {
    console.error('保存草稿失败:', error);
    ElMessage.error('保存草稿失败');
  }
}

function resetForm() {
  gradingForm.score = null;
  gradingForm.comment = '';
  gradingForm.tags = [];
  ElMessage.info('表单已重置');
}

// 获取完整URL用于Microsoft Office Online预览
function getFullUrl(relativeUrl: string): string {
  return `${window.location.origin}/${relativeUrl}`;
}

const docxHtml = ref('');
const isLocalDocx = ref(false);

function onLocalDocxChange(e: Event) {
  const input = e.target as HTMLInputElement;
  const file = input.files && input.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = async (evt) => {
      const arrayBuffer = evt.target?.result;
      if (arrayBuffer && arrayBuffer instanceof ArrayBuffer) {
        const result = await mammoth.convertToHtml({ arrayBuffer });
        docxHtml.value = result.value;
        isLocalDocx.value = true;
      }
    };
    reader.readAsArrayBuffer(file);
  }
}

// 生命周期
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