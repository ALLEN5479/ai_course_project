<template>
  <div class="career-ability-upload">
    <el-upload
      ref="uploadRef"
      :action="uploadUrl"
      :headers="headers"
      :data="uploadData"
      :before-upload="beforeUpload"
      :on-success="handleSuccess"
      :on-error="handleError"
      :on-progress="handleProgress"
      :show-file-list="false"
      accept=".xlsx,.xls"
      drag
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">
        将文件拖到此处，或<em>点击上传</em>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          只能上传 xlsx/xls 文件，且不超过 10MB
        </div>
      </template>
    </el-upload>

    <!-- 上传进度 -->
    <div v-if="uploadProgress > 0 && uploadProgress < 100" class="upload-progress">
      <el-progress :percentage="uploadProgress" />
      <p>正在上传文件...</p>
    </div>

    <!-- 模板下载 -->
    <div class="template-download">
      <h4>Excel模板格式说明：</h4>
      <ul>
        <li>第一行：学号、姓名、班级、经历1、经历2、经历3 ...</li>
        <li>数据从第二行开始填写</li>
        <li>每个"经历"请填写学生的生涯/学习/社会实践等文本描述（如"参加ACM竞赛获奖"、"实习于华为"等）</li>
        <li>系统将自动分析文本生成能力分数和AI能力报告</li>
        <li>学号必须与系统中已有学生学号一致</li>
      </ul>
      <el-button type="primary" @click="downloadTemplate">
        <el-icon><Download /></el-icon>
        下载Excel模板
      </el-button>
    </div>

    <!-- 预览数据 -->
    <div v-if="previewData.length > 0" class="preview-section">
      <h4>数据预览（前5条）：</h4>
      <el-table :data="previewData" style="width: 100%" size="small">
        <el-table-column prop="studentId" label="学号" width="100" />
        <el-table-column prop="name" label="姓名" width="80" />
        <el-table-column prop="className" label="班级" width="100" />
        <el-table-column prop="courseScore" label="课程能力" width="80" />
        <el-table-column prop="practiceScore" label="实践能力" width="80" />
        <el-table-column prop="qualityScore" label="综合素养" width="80" />
        <el-table-column prop="innovationScore" label="创新能力" width="80" />
        <el-table-column prop="teamworkScore" label="团队协作" width="80" />
        <el-table-column prop="communicationScore" label="沟通表达" width="80" />
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled, Download } from '@element-plus/icons-vue'
import { uploadCareerAbilityData } from '../../api/careerAbilityApi'

// 定义事件
const emit = defineEmits(['upload-success'])

// 响应式数据
const uploadRef = ref()
const uploadProgress = ref(0)
const previewData = ref([])

// 上传配置
const uploadUrl = '/api/career/ability/upload'
const headers = {
  'Authorization': `Bearer ${localStorage.getItem('token')}`
}
const uploadData = {
  type: 'career_ability'
}

// 方法
const beforeUpload = (file: File) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || 
                  file.type === 'application/vnd.ms-excel'
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isExcel) {
    ElMessage.error('只能上传Excel文件!')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过 10MB!')
    return false
  }
  
  uploadProgress.value = 0
  return true
}

const handleProgress = (event: any) => {
  uploadProgress.value = Math.round(event.percent)
}

const handleSuccess = (response: any) => {
  uploadProgress.value = 100
  if (response.code === 200) {
    ElMessage.success('文件上传成功')
    previewData.value = (response.data && response.data.preview) ? response.data.preview : []
    emit('upload-success', response.data)
  } else {
    ElMessage.error(response.msg || '上传失败')
    previewData.value = []
  }
}

const handleError = (error: any) => {
  uploadProgress.value = 0
  ElMessage.error('文件上传失败')
  console.error('Upload error:', error)
}

const downloadTemplate = () => {
  const templateData = [
    ['学号', '姓名', '班级', '经历1', '经历2', '经历3'],
    ['2023001', '张三', '软件2301', '参加ACM竞赛获奖', '实习于华为', '担任班级干部'],
    ['2023002', '李四', '软件2302', '参与开源项目', '志愿者服务', '发表论文']
  ]
  const csvContent = templateData.map(row => row.join(',')).join('\n')
  const blob = new Blob(['\ufeff' + csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)
  link.setAttribute('href', url)
  link.setAttribute('download', '生涯经历导入模板.csv')
  link.style.visibility = 'hidden'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}
</script>

<style scoped>
.career-ability-upload {
  padding: 20px;
}

.upload-progress {
  margin: 20px 0;
  text-align: center;
}

.template-download {
  margin-top: 30px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.template-download h4 {
  margin-top: 0;
  color: #333;
}

.template-download ul {
  margin: 10px 0;
  padding-left: 20px;
}

.template-download li {
  margin: 5px 0;
  color: #666;
}

.preview-section {
  margin-top: 30px;
}

.preview-section h4 {
  margin-bottom: 15px;
  color: #333;
}
</style> 