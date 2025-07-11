<template>
  <div class="task-detail-layout">
    <el-row :gutter="24" class="main-row">
      <el-col :span="10" class="left-col">
        <div class="left-panel">
          <el-collapse v-model="activeNames" class="left-collapse">
            <el-collapse-item name="1">
              <template #title>
                <el-tag type="warning">报告任务</el-tag>
                <span class="task-title">{{ task.mission_name }}</span>
              </template>
              <div class="task-desc">{{ task.mission_description }}</div>
              <div class="task-meta">
                <span>开始时间：{{ formatDateTime(task.start_time) }}</span>
                <span>结束时间：{{ formatDateTime(task.end_time) }}</span>
              </div>
            </el-collapse-item>
            <el-collapse-item title="任务内容" name="2">
              <div class="task-content">
                <div v-if="reportResource" class="resource-section">
                  <h4 class="resource-title">相关资源文件：</h4>
                  <div class="resource-list">
                    <div 
                      class="resource-item"
                      @click="downloadResource(reportResource.path, reportResource.resource_name)"
                    >
                      <i class="el-icon-document"></i>
                      <span class="resource-name">{{ reportResource.resource_name }}</span>
                      <i class="el-icon-download download-icon"></i>
                    </div>
                  </div>
                </div>
                <div v-else class="no-resources">
                  <p>暂无相关资源文件</p>
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
          <el-button class="back-btn" @click="goBack" type="primary" size="large" round>返回</el-button>
        </div>
      </el-col>
      <el-col :span="14" class="right-col">
        <el-card class="task-action-card">
          <div class="action-title">报告上传</div>
          <div class="upload-area">
            <el-upload
              class="upload-demo"
              drag
              :auto-upload="false"
              :on-change="handleFileChange"
              :before-upload="beforeUpload"
              :show-file-list="true"
              v-model:file-list="fileList"
              ref="uploadRef"
            >
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将文件拖到此处，或<em>点击选择</em></div>
              <div class="el-upload__tip" slot="tip">支持pdf/doc/docx/zip等格式，最大50MB（点击确定按钮时上传）</div>
            </el-upload>
            <textarea
              v-model="introduction"
              id="description"
              type="textarea"
              :rows="4"
              placeholder="请输入报告介绍"
              class="intro-input"
              style="margin: 40px 0 10px 0;"
            />
            <div v-if="fileList.length > 0" class="file-status">
              <el-tag type="info" size="small">
                <i class="el-icon-document"></i>
                已选择文件: {{ fileList[0].name }}
              </el-tag>
            </div>
            <el-button type="primary" @click="handleSubmit" :loading="submitting" size="large" round class="submit-btn">
              {{ submitting ? '上传中...' : '确定' }}
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const studentId = route.query.user_id || ''

const task = ref({
  mission_id: route.query.mission_id || '',
  mission_name: route.query.mission_name || '',
  mission_description: route.query.mission_description || '',
  start_time: route.query.start_time || '',
  end_time: route.query.end_time || '',
})

const content = ref('')
const activeNames = ref(['1', '2'])
const reportResources = ref<any[]>([])
const reportResource = ref<any>(null)

const fileList = ref([])
const introduction = ref('')
const submitting = ref(false)
const uploadedFileInfo = ref<any>(null)
const uploadRef = ref()

const uploadUrl = 'http://localhost:8080/api/task-manager/report/upload'

function goBack() {
  router.back()
}

function formatDateTime(dt: string) {
  if (!dt) return ''
  return dt.replace('T', ' ').slice(0, 16)
}

async function fetchContent() {
  if (!task.value.mission_id) return
  try {
    const res = await axios.get('http://localhost:8080/api/task-manager/published-missions/showcontent', {
      params: { mission_id: task.value.mission_id }
    })
    content.value = res.data || ''
    // 获取到content后，立即获取报告资源
    if (content.value) {
      await fetchReportResources()
    }
  } catch (e) {
    content.value = '任务内容加载失败'
  }
}

async function fetchReportResources() {
  if (!content.value) return
  try {
    const res = await axios.get('http://localhost:8080/api/task-manager/published-missions/showreportresource', {
      params: { resource_id: content.value }
    })
    reportResource.value = res.data || null
  } catch (e) {
    console.error('获取报告资源失败:', e)
    reportResource.value = null
  }
}

function downloadResource(path: string, fileName: string) {
  if (!path) {
    ElMessage.error('文件路径不存在')
    return
  }
  
  try {
    // 创建一个临时的a标签来下载文件
    const link = document.createElement('a')
    link.href = `http://localhost:5173${path}`
    link.download = fileName
    link.target = '_blank'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    ElMessage.success(`开始下载文件: ${fileName}`)
  } catch (error) {
    console.error('下载文件失败:', error)
    ElMessage.error('下载文件失败')
  }
}

function handleFileChange(file: any, fileList: any) {
  // 文件选择变化时的处理
  console.log('文件已选择:', file.name)
  // 清空之前的上传信息
  uploadedFileInfo.value = null
}

function beforeUpload(file: File) {
  const isAllowed = [
    'application/pdf',
    'application/msword',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    'application/zip',
    'application/x-zip-compressed'
  ].includes(file.type)
  const isLt50M = file.size / 1024 / 1024 < 50
  if (!isAllowed) {
    ElMessage.error('仅支持pdf/doc/docx/zip格式')
  }
  if (!isLt50M) {
    ElMessage.error('文件大小不能超过50MB')
  }
  return isAllowed && isLt50M
}

async function handleSubmit() {
  if (!fileList.value.length) {
    ElMessage.warning('请先选择文件')
    return
  }
  if (!introduction.value.trim()) {
    ElMessage.warning('请输入报告介绍')
    return
  }

  submitting.value = true
  try {
    // 1. 上传文件
    const file = fileList.value[0].raw
    const formData = new FormData()
    formData.append('file', file)
    const uploadResponse = await axios.post(uploadUrl, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    if (!uploadResponse.data.success) {
      ElMessage.error(uploadResponse.data.message || '文件上传失败')
      submitting.value = false
      return
    }
    const { fileName, filePath } = uploadResponse.data.data

    // 2. 提交业务数据
    const submitData = {
      mission_id: task.value.mission_id,
      student_id: studentId,
      report_name: fileName,
      report_des: introduction.value,
      report_url: filePath
    }
    console.log('提交数据:', submitData)
    const submitResponse = await axios.post('http://localhost:8080/api/task-manager/report/submit', submitData)
    if (submitResponse.data.success) {
      ElMessage.success('报告提交成功！')
    } else {
      ElMessage.error(submitResponse.data.message || '报告提交失败')
    }
  } catch (e: any) {
    ElMessage.error('提交失败: ' + (e.response?.data?.message || e.message))
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchContent()
})
</script>

<style scoped>
.task-detail-layout {
  min-height: 100vh;
  background: linear-gradient(120deg, #f5f7fa 60%, #e0e7ff 100%);
  padding: 30px 40px;
}
.main-row {
  min-height: 80vh;
}
.left-col, .right-col {
  display: flex;
  align-items: stretch;
}
.left-panel {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
}
.left-collapse {
  flex: 1 1 0;
  background: linear-gradient(135deg, #fff 80%, #e0e7ff 100%);
  border-radius: 18px;
  margin-bottom: 18px;
  box-shadow: 0 4px 24px 0 rgba(80,120,255,0.08);
  transition: box-shadow 0.2s;
  min-height: 400px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.left-collapse:hover {
  box-shadow: 0 8px 32px 0 rgba(80,120,255,0.16);
}
.task-title {
  font-size: 20px;
  font-weight: 700;
  margin-left: 12px;
  color: #2d3a4b;
}
.task-desc {
  color: #666;
  margin-bottom: 14px;
  font-size: 16px;
  line-height: 1.7;
}
.task-meta {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 14px;
  color: #999;
}
.task-content {
  padding: 10px 0;
}
.content-text {
  margin-bottom: 20px;
  line-height: 1.6;
  color: #333;
}
.resource-section {
  margin-top: 20px;
}
.resource-title {
  font-size: 16px;
  font-weight: 600;
  color: #2d3a4b;
  margin-bottom: 12px;
}
.resource-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.resource-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: #f8faff;
  border: 1px solid #e0e7ff;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
}
.resource-item:hover {
  background: #e0e7ff;
  border-color: #409eff;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.15);
}
.resource-item i:first-child {
  color: #409eff;
  margin-right: 8px;
  font-size: 16px;
}
.resource-name {
  flex: 1;
  color: #2d3a4b;
  font-weight: 500;
}
.download-icon {
  color: #409eff;
  font-size: 16px;
  margin-left: 8px;
}
.no-resources {
  text-align: center;
  color: #999;
  font-style: italic;
  margin-top: 20px;
}
.back-btn {
  margin-top: 22px;
  width: 100%;
  font-size: 16px;
  letter-spacing: 2px;
}
.right-col {
  align-items: stretch;
  justify-content: center;
}
.task-action-card {
  min-height: 400px;
  width: 100%;
  max-width: 600px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  background: linear-gradient(135deg, #fafdff 60%, #e0e7ff 100%);
  border-radius: 18px;
  box-shadow: 0 4px 24px 0 rgba(80,120,255,0.10);
  padding: 40px 32px 32px 32px;
}
.action-title {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 18px;
  color: #2d3a4b;
}
.upload-area {
  width: 88%;
  max-width: 420px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.upload-demo {
  width: 100%;
  margin-bottom: 16px;
}
.intro-input {
  width: 450px;
  height: 120px;
}
.submit-btn {
  width: 60%;
  min-width: 120px;
  margin: 0 auto 0 auto;
  display: block;
}
.file-status {
  margin: 10px 0;
  text-align: center;
}
.file-status .el-tag {
  font-size: 12px;
  padding: 4px 8px;
}
.file-status .el-tag i {
  margin-right: 4px;
}
@media (max-width: 900px) {
  .main-row { flex-direction: column; }
  .left-col, .right-col { width: 100% !important; max-width: 100% !important; }
  .task-action-card { max-width: 100%; }
  .left-panel { min-height: 400px; }
  .upload-area { width: 98%; max-width: 100%; }
}
</style> 