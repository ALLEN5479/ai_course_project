<template>
  <div class="task-detail-layout">
    <el-row :gutter="24" class="main-row">
      <el-col :span="10" class="left-col">
        <div class="left-panel">
          <el-collapse v-model="activeNames" class="left-collapse">
            <el-collapse-item name="1">
              <template #title>
                <el-tag type="success">自主学习</el-tag>
                <span class="task-title">{{ task.mission_name }}</span>
              </template>
              <div class="task-desc">{{ task.mission_description }}</div>
              <div class="task-meta">
                <span>开始时间：{{ formatDateTime(task.start_time) }}</span>
                <span>结束时间：{{ formatDateTime(task.end_time) }}</span>
              </div>
            </el-collapse-item>
            <el-collapse-item title="任务内容" name="2">
              <div v-html="content" style="white-space: pre-line;"></div>
            </el-collapse-item>
          </el-collapse>
          <el-button class="back-btn" @click="goBack" type="primary" size="large" round>返回</el-button>
        </div>
      </el-col>
      <el-col :span="14" class="right-col">
        <el-card class="task-action-card">
          <div class="action-title">任务完成区域</div>
          <div class="action-placeholder">（此处可放答题、提交等功能）</div>
          <el-button type="primary" @click="handleSubmit" size="large" round style="margin-top: 24px;">完成任务</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { ref, onMounted } from 'vue'
import axios from 'axios'

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
  } catch (e) {
    content.value = '任务内容加载失败'
  }
}

async function handleSubmit() {
  try {
    await axios.get('http://localhost:8080/api/task-manager/published-missions/updatescore', {
      params: {
        mission_id: task.value.mission_id,
        student_id: studentId,
        score: 0
      }
    })
    window.ElMessage && window.ElMessage.success('任务完成，分数已同步！')
  } catch (e) {
    window.ElMessage && window.ElMessage.error('分数同步失败')
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
  min-height: 350px;
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
  min-height: 350px;
  width: 100%;
  max-width: 520px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
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
.action-placeholder {
  color: #bbb;
  font-size: 16px;
  letter-spacing: 1px;
}
@media (max-width: 900px) {
  .main-row { flex-direction: column; }
  .left-col, .right-col { width: 100% !important; max-width: 100% !important; }
  .task-action-card { max-width: 100%; }
  .left-panel { min-height: 350px; }
}
</style> 