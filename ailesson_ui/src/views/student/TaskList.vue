<template>
  <div class="task-list-container">
    <el-card class="search-bar-card">
      <div class="search-bar">
        <el-input
          v-model="searchName"
          placeholder="搜索任务名称"
          clearable
          class="search-input"
          @input="filterTasks"
        />
        <el-select
          v-model="selectedType"
          placeholder="选择任务类型"
          clearable
          class="type-select"
          @change="filterTasks"
        >
          <el-option label="全部类型" :value="''" />
          <el-option label="报告任务" value="report" />
          <el-option label="自主学习" value="self" />
          <el-option label="测试任务" value="quiz" />
        </el-select>
      </div>
    </el-card>

    <el-row :gutter="20" class="task-cards-row">
      <el-col :span="8" v-for="task in filteredTasks" :key="task.mission_id">
        <el-card class="task-card" @click="goToTaskDetail(task)">
          <div class="task-header">
            <el-tag :type="typeTagType(task.mission_type)">
              {{ typeLabel(task.mission_type) }}
            </el-tag>
            <span class="task-title">{{ task.mission_name }}</span>
          </div>
          <div class="task-desc">{{ task.mission_description }}</div>
          <div class="task-meta">
            <span>开始时间：{{ formatDateTime(task.start_time) }}</span>
            <span>结束时间：{{ formatDateTime(task.end_time) }}</span>
          </div>
        </el-card>
      </el-col>
      <div v-if="filteredTasks.length === 0" class="no-tasks">
        <el-empty description="暂无任务" />
      </div>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, defineProps } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const props = defineProps({
  userId: {
    type: String,
    required: false
  }
})

const router = useRouter()
// 任务数据
const tasks = ref<any[]>([])
const searchName = ref('')
const selectedType = ref('')

const filteredTasks = computed(() => {
  return tasks.value.filter(task => {
    const matchType = !selectedType.value || task.mission_type === selectedType.value
    const matchName = !searchName.value || (task.mission_name && task.mission_name.includes(searchName.value))
    return matchType && matchName
  })
})

function filterTasks() {
  // 由于 filteredTasks 是 computed，输入变化会自动刷新
}

function typeLabel(type: string) {
  switch (type) {
    case 'report': return '报告任务'
    case 'self': return '自主学习'
    case 'quiz': return '测试任务'
    default: return '未知类型'
  }
}

function typeTagType(type: string) {
  switch (type) {
    case 'report': return 'warning'
    case 'self': return 'success'
    case 'quiz': return 'info'
    default: return 'default'
  }
}

function formatDateTime(dt: string) {
  if (!dt) return ''
  // 兼容后端返回的 ISO 格式
  return dt.replace('T', ' ').slice(0, 16)
}

async function fetchTasks() {
  try {
    const res = await axios.get('http://localhost:8080/api/task-manager/published-missions/show')
    tasks.value = res.data || []
  } catch (e) {
    tasks.value = []
  }
}

function goToTaskDetail(task: any) {
  let routeName = ''
  if (task.mission_type === 'quiz') routeName = 'student-quiz-task-detail'
  else if (task.mission_type === 'self') routeName = 'student-self-task-detail'
  else if (task.mission_type === 'report') routeName = 'student-report-task-detail'
  if (routeName) {
    router.push({
      name: routeName,
      query: {
        mission_id: task.mission_id,
        mission_name: task.mission_name,
        mission_description: task.mission_description,
        start_time: task.start_time,
        end_time: task.end_time,
        user_id: props.userId
      }
    })
  }
}

onMounted(() => {
  fetchTasks()
})
</script>

<style scoped>
.task-list-container {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 30px 40px;
}
.search-bar-card {
  margin-bottom: 24px;
}
.search-bar {
  display: flex;
  gap: 16px;
  align-items: center;
}
.search-input {
  width: 260px;
}
.type-select {
  width: 180px;
}
.task-cards-row {
  margin-top: 10px;
}
.task-card {
  margin-bottom: 24px;
  min-height: 180px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.task-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}
.task-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}
.task-desc {
  color: #666;
  margin-bottom: 12px;
  font-size: 15px;
}
.task-meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 13px;
  color: #999;
}
.no-tasks {
  width: 100%;
  margin-top: 40px;
  display: flex;
  justify-content: center;
}
</style> 