<template>
  <div class="course-submission-list">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      <span class="page-title">课程提交列表</span>
    </div>

    <!-- 课程信息 -->
    <div class="course-info-section">
      <el-card>
        <div class="course-header">
          <h3>{{ courseInfo.courseName }}</h3>
          <el-tag type="primary">{{ courseInfo.taskName }}</el-tag>
        </div>
        <div class="course-details">
          <div class="detail-item">
            <span class="label">任务类型：</span>
            <span class="value">{{ courseInfo.taskType }}</span>
          </div>
          <div class="detail-item">
            <span class="label">截止时间：</span>
            <span class="value">{{ courseInfo.deadline }}</span>
          </div>
          <div class="detail-item">
            <span class="label">总学生数：</span>
            <span class="value">{{ courseInfo.totalStudents }}人</span>
          </div>
          <div class="detail-item">
            <span class="label">已提交：</span>
            <span class="value">{{ courseInfo.submittedCount }}人</span>
          </div>
          <div class="detail-item">
            <span class="label">已批改：</span>
            <span class="value">{{ courseInfo.gradedCount }}人</span>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-section">
      <el-card>
        <el-form :model="searchForm" inline>
          <el-form-item label="学生ID">
            <el-input
              v-model="searchForm.studentId"
              placeholder="请输入学生ID"
              clearable
              style="width: 150px"
            />
          </el-form-item>
          <el-form-item label="学生姓名">
            <el-input
              v-model="searchForm.studentName"
              placeholder="请输入学生姓名"
              clearable
              style="width: 150px"
            />
          </el-form-item>
          <el-form-item label="提交状态">
            <el-select v-model="searchForm.submitStatus" placeholder="请选择状态" clearable style="width: 120px">
              <el-option label="已提交" value="submitted" />
              <el-option label="未提交" value="not_submitted" />
            </el-select>
          </el-form-item>
          <el-form-item label="批改状态">
            <el-select v-model="searchForm.gradeStatus" placeholder="请选择状态" clearable style="width: 120px">
              <el-option label="已批改" value="graded" />
              <el-option label="待批改" value="pending" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 学生提交列表 -->
    <div class="submission-list-section">
      <el-card>
        <div class="list-header">
          <h4>学生提交列表</h4>
          <div class="list-actions">
            <el-button type="success" @click="batchGrade" :disabled="!hasPendingSubmissions">
              批量批改
            </el-button>
            <el-button type="primary" @click="exportList">
              导出列表
            </el-button>
          </div>
        </div>
        
        <el-table :data="filteredSubmissions" style="width: 100%" v-loading="loading">
          <el-table-column prop="studentId" label="学生ID" width="120" />
          <el-table-column prop="studentName" label="学生姓名" width="120" />
          <el-table-column prop="submitTime" label="提交时间" width="180" />
          <el-table-column prop="submitStatus" label="提交状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.submitStatus === 'submitted' ? 'success' : 'danger'">
                {{ scope.row.submitStatus === 'submitted' ? '已提交' : '未提交' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="gradeStatus" label="批改状态" width="100">
            <template #default="scope">
              <el-tag :type="getGradeStatusType(scope.row.gradeStatus)">
                {{ getGradeStatusText(scope.row.gradeStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="score" label="得分" width="80">
            <template #default="scope">
              <span v-if="scope.row.score !== null">{{ scope.row.score }}分</span>
              <span v-else class="no-score">-</span>
            </template>
          </el-table-column>
          <el-table-column prop="fileInfo" label="文件信息" min-width="200">
            <template #default="scope">
              <div v-if="scope.row.submitStatus === 'submitted'" class="file-info">
                <el-icon><Document /></el-icon>
                <span>{{ scope.row.fileName }}</span>
                <span class="file-size">{{ scope.row.fileSize }}</span>
              </div>
              <span v-else class="no-file">未提交文件</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button 
                v-if="scope.row.submitStatus === 'submitted' && scope.row.gradeStatus === 'pending'"
                type="primary" 
                size="small" 
                @click="gradeSubmission(scope.row)"
              >
                批改
              </el-button>
              <el-button 
                v-else-if="scope.row.submitStatus === 'submitted' && scope.row.gradeStatus === 'graded'"
                type="info" 
                size="small" 
                @click="viewGrade(scope.row)"
              >
                查看批改
              </el-button>
              <el-button 
                v-if="scope.row.submitStatus === 'submitted'"
                type="success" 
                size="small" 
                @click="downloadSubmission(scope.row)"
              >
                下载
              </el-button>
              <el-button 
                v-if="scope.row.submitStatus === 'submitted'"
                type="warning" 
                size="small" 
                @click="previewSubmission(scope.row)"
              >
                预览
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-wrapper">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="totalCount"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>
    </div>

    <!-- 批量批改对话框 -->
    <el-dialog v-model="batchGradeDialogVisible" title="批量批改" width="800px">
      <div class="batch-grade-content">
        <el-alert
          title="批量批改说明"
          description="将对待批改的提交进行批量处理，可以设置统一的评分标准和评价模板。"
          type="info"
          show-icon
          style="margin-bottom: 20px"
        />
        
        <el-form :model="batchGradeForm" label-width="120px">
          <el-form-item label="评分标准">
            <el-radio-group v-model="batchGradeForm.gradeStandard">
              <el-radio label="excellent">优秀 (90-100分)</el-radio>
              <el-radio label="good">良好 (80-89分)</el-radio>
              <el-radio label="average">中等 (70-79分)</el-radio>
              <el-radio label="pass">及格 (60-69分)</el-radio>
              <el-radio label="fail">不及格 (0-59分)</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="评价模板">
            <el-select v-model="batchGradeForm.commentTemplate" placeholder="请选择评价模板" style="width: 100%">
              <el-option label="优秀作业模板" value="excellent" />
              <el-option label="良好作业模板" value="good" />
              <el-option label="中等作业模板" value="average" />
              <el-option label="及格作业模板" value="pass" />
              <el-option label="不及格作业模板" value="fail" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="自定义评价">
            <el-input
              v-model="batchGradeForm.customComment"
              type="textarea"
              :rows="4"
              placeholder="可以添加自定义评价内容..."
            />
          </el-form-item>
        </el-form>
        
        <div class="batch-preview">
          <h5>待批改列表 ({{ pendingSubmissions.length }}个)</h5>
          <el-table :data="pendingSubmissions" style="width: 100%" max-height="200">
            <el-table-column prop="studentId" label="学生ID" width="100" />
            <el-table-column prop="studentName" label="学生姓名" width="120" />
            <el-table-column prop="submitTime" label="提交时间" width="180" />
          </el-table>
        </div>
      </div>
      <template #footer>
        <el-button @click="batchGradeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmBatchGrade" :loading="batchGrading">
          {{ batchGrading ? '批改中...' : '确认批量批改' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Document } from '@element-plus/icons-vue';

const router = useRouter();
const route = useRoute();

// 课程信息
const courseInfo = reactive({
  courseName: '数据结构与算法',
  taskName: '数据结构项目报告',
  taskType: '报告任务',
  deadline: '2024-01-16 23:59:59',
  totalStudents: 45,
  submittedCount: 38,
  gradedCount: 25
});

// 搜索表单
const searchForm = reactive({
  studentId: '',
  studentName: '',
  submitStatus: '',
  gradeStatus: ''
});

// 分页
const currentPage = ref(1);
const pageSize = ref(20);
const totalCount = ref(0);
const loading = ref(false);

// 批量批改相关
const batchGradeDialogVisible = ref(false);
const batchGrading = ref(false);
const batchGradeForm = reactive({
  gradeStandard: 'good',
  commentTemplate: 'good',
  customComment: ''
});

// 模拟学生提交数据
const submissions = ref([
  {
    id: 1,
    studentId: '2021001',
    studentName: '张三',
    submitTime: '2024-01-15 14:30:00',
    submitStatus: 'submitted',
    gradeStatus: 'graded',
    score: 85,
    fileName: '数据结构项目报告.pdf',
    fileSize: '2.5MB'
  },
  {
    id: 2,
    studentId: '2021002',
    studentName: '李四',
    submitTime: '2024-01-15 16:20:00',
    submitStatus: 'submitted',
    gradeStatus: 'pending',
    score: null,
    fileName: '数据结构项目报告.pdf',
    fileSize: '3.1MB'
  },
  {
    id: 3,
    studentId: '2021003',
    studentName: '王五',
    submitTime: null,
    submitStatus: 'not_submitted',
    gradeStatus: 'pending',
    score: null,
    fileName: null,
    fileSize: null
  },
  {
    id: 4,
    studentId: '2021004',
    studentName: '赵六',
    submitTime: '2024-01-16 09:15:00',
    submitStatus: 'submitted',
    gradeStatus: 'graded',
    score: 92,
    fileName: '数据结构项目报告.pdf',
    fileSize: '2.8MB'
  },
  {
    id: 5,
    studentId: '2021005',
    studentName: '钱七',
    submitTime: '2024-01-16 11:45:00',
    submitStatus: 'submitted',
    gradeStatus: 'pending',
    score: null,
    fileName: '数据结构项目报告.pdf',
    fileSize: '2.2MB'
  }
]);

// 计算属性
const filteredSubmissions = computed(() => {
  let result = submissions.value;
  
  if (searchForm.studentId) {
    result = result.filter(item => item.studentId.includes(searchForm.studentId));
  }
  
  if (searchForm.studentName) {
    result = result.filter(item => item.studentName.includes(searchForm.studentName));
  }
  
  if (searchForm.submitStatus) {
    result = result.filter(item => item.submitStatus === searchForm.submitStatus);
  }
  
  if (searchForm.gradeStatus) {
    result = result.filter(item => item.gradeStatus === searchForm.gradeStatus);
  }
  
  totalCount.value = result.length;
  return result;
});

const pendingSubmissions = computed(() => {
  return submissions.value.filter(item => 
    item.submitStatus === 'submitted' && item.gradeStatus === 'pending'
  );
});

const hasPendingSubmissions = computed(() => {
  return pendingSubmissions.value.length > 0;
});

// 方法
function goBack() {
  router.back();
}

function handleSearch() {
  currentPage.value = 1;
  ElMessage.success('搜索完成');
}

function resetSearch() {
  searchForm.studentId = '';
  searchForm.studentName = '';
  searchForm.submitStatus = '';
  searchForm.gradeStatus = '';
  currentPage.value = 1;
}

function getGradeStatusType(status: string) {
  switch (status) {
    case 'graded': return 'success';
    case 'pending': return 'warning';
    default: return 'info';
  }
}

function getGradeStatusText(status: string) {
  switch (status) {
    case 'graded': return '已批改';
    case 'pending': return '待批改';
    default: return '未知';
  }
}

function gradeSubmission(submission: any) {
  router.push({
    path: '/teacher/task-grading',
    query: { 
      taskId: route.query.taskId,
      submissionId: submission.id,
      studentId: submission.studentId
    }
  });
}

function viewGrade(submission: any) {
  ElMessage.info(`查看学生 ${submission.studentName} 的批改结果`);
  // 这里可以跳转到查看批改结果页面
}

function downloadSubmission(submission: any) {
  ElMessage.success(`下载学生 ${submission.studentName} 的提交文件`);
}

function previewSubmission(submission: any) {
  ElMessage.info(`预览学生 ${submission.studentName} 的提交文件`);
}

function batchGrade() {
  if (pendingSubmissions.value.length === 0) {
    ElMessage.warning('没有待批改的提交');
    return;
  }
  batchGradeDialogVisible.value = true;
}

function confirmBatchGrade() {
  batchGrading.value = true;
  
  setTimeout(() => {
    batchGrading.value = false;
    batchGradeDialogVisible.value = false;
    
    // 更新批改状态
    pendingSubmissions.value.forEach(submission => {
      submission.gradeStatus = 'graded';
      submission.score = getScoreByStandard(batchGradeForm.gradeStandard);
    });
    
    courseInfo.gradedCount = submissions.value.filter(s => s.gradeStatus === 'graded').length;
    
    ElMessage.success(`批量批改完成，共处理 ${pendingSubmissions.value.length} 个提交`);
  }, 2000);
}

function getScoreByStandard(standard: string) {
  const scoreRanges = {
    excellent: [90, 100],
    good: [80, 89],
    average: [70, 79],
    pass: [60, 69],
    fail: [0, 59]
  };
  
  const range = scoreRanges[standard as keyof typeof scoreRanges];
  return Math.floor(Math.random() * (range[1] - range[0] + 1)) + range[0];
}

function exportList() {
  ElMessage.success('导出列表成功');
}

function handleSizeChange(size: number) {
  pageSize.value = size;
  currentPage.value = 1;
}

function handleCurrentChange(page: number) {
  currentPage.value = page;
}

// 生命周期
onMounted(() => {
  const taskId = route.query.taskId;
  console.log('任务ID:', taskId);
  
  // 这里可以根据taskId加载具体的课程和提交数据
  loading.value = true;
  
  setTimeout(() => {
    loading.value = false;
  }, 1000);
});
</script>

<style scoped>
.course-submission-list {
  padding: 24px;
  max-width: 1400px;
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

.course-info-section {
  margin-bottom: 24px;
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.course-header h3 {
  margin: 0;
  color: #303133;
}

.course-details {
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

.search-section {
  margin-bottom: 24px;
}

.submission-list-section {
  margin-bottom: 24px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.list-header h4 {
  margin: 0;
  color: #303133;
}

.list-actions {
  display: flex;
  gap: 12px;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.file-size {
  color: #909399;
  font-size: 12px;
}

.no-score, .no-file {
  color: #c0c4cc;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.batch-grade-content {
  max-height: 600px;
  overflow-y: auto;
}

.batch-preview {
  margin-top: 20px;
}

.batch-preview h5 {
  margin-bottom: 12px;
  color: #303133;
}
</style> 