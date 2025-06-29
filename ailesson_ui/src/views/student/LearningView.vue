<template>
  <div class="learning-view">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>{{ courseTitle }}</h1>
          <div class="header-actions">
            <el-button @click="goBack">返回</el-button>
          </div>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <div class="learning-container">
          <!-- 课程信息卡片 -->
          <el-card class="course-info-card" v-if="courseInfo">
            <div class="course-header">
              <h2>{{ courseInfo.name }}</h2>
              <p class="course-description">{{ courseInfo.description }}</p>
              <div class="course-meta">
                <el-tag type="primary">课程ID: {{ courseInfo.id }}</el-tag>
                <el-tag type="success">教师: {{ courseInfo.teacher }}</el-tag>
                <el-progress :percentage="courseInfo.progress || 0" :stroke-width="8" />
              </div>
            </div>
          </el-card>

          <!-- 学习资源区域 -->
          <el-card class="learning-resources-card">
            <template #header>
              <div class="card-header">
                <span>学习资源</span>
                <el-button type="primary" size="small" @click="refreshResources">刷新资源</el-button>
              </div>
            </template>
            
            <div class="resources-grid">
              <!-- PDF资源 -->
              <el-card class="resource-item" shadow="hover">
                <div class="resource-content">
                  <el-icon class="resource-icon"><Document /></el-icon>
                  <h4>课程资料</h4>
                  <p>PDF格式的课程资料和讲义</p>
                  <el-button type="primary" @click="viewPDF">查看PDF</el-button>
                </div>
              </el-card>

              <!-- 视频资源 -->
              <el-card class="resource-item" shadow="hover">
                <div class="resource-content">
                  <el-icon class="resource-icon"><VideoPlay /></el-icon>
                  <h4>教学视频</h4>
                  <p>课程相关的教学视频内容</p>
                  <el-button type="primary" @click="viewVideo">观看视频</el-button>
                </div>
              </el-card>

              <!-- 练习题 -->
              <el-card class="resource-item" shadow="hover">
                <div class="resource-content">
                  <el-icon class="resource-icon"><Edit /></el-icon>
                  <h4>练习题</h4>
                  <p>课程相关的练习题和测试</p>
                  <el-button type="primary" @click="startExercise">开始练习</el-button>
                </div>
              </el-card>

              <!-- 学习进度 -->
              <el-card class="resource-item" shadow="hover">
                <div class="resource-content">
                  <el-icon class="resource-icon"><DataAnalysis /></el-icon>
                  <h4>学习进度</h4>
                  <p>查看您的学习进度和统计</p>
                  <el-button type="primary" @click="viewProgress">查看进度</el-button>
                </div>
              </el-card>
            </div>
          </el-card>

          <!-- 最近学习记录 -->
          <el-card class="recent-learning-card" v-if="recentLearning.length > 0">
            <template #header>
              <span>最近学习记录</span>
            </template>
            <el-timeline>
              <el-timeline-item
                v-for="(activity, index) in recentLearning"
                :key="index"
                :timestamp="activity.time"
                :type="activity.type"
              >
                {{ activity.content }}
              </el-timeline-item>
            </el-timeline>
          </el-card>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { VideoPlay, Document, Edit, DataAnalysis } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 课程信息
const courseInfo = ref<any>(null)
const recentLearning = ref<any[]>([])

// 计算属性：课程标题
const courseTitle = computed(() => {
  if (courseInfo.value) {
    return `学习 - ${courseInfo.value.name}`
  }
  return '学习界面'
})

// 获取课程信息
const fetchCourseInfo = async () => {
  const courseId = route.query.courseId || route.params.courseId
  const courseName = route.query.courseName
  const courseDescription = route.query.courseDescription
  const teacherName = route.query.teacherName
  
  if (courseId) {
    // 如果有课程ID，尝试从后端获取详细信息
    try {
      // 这里可以调用后端API获取课程详细信息
      // const response = await axios.get(`/api/courses/${courseId}`)
      // courseInfo.value = response.data
      
      // 暂时使用路由参数中的信息
      courseInfo.value = {
        id: courseId,
        name: courseName || `课程${courseId}`,
        description: courseDescription || '课程描述',
        teacher: teacherName || '教师姓名',
        progress: Math.floor(Math.random() * 100)
      }
    } catch (error) {
      ElMessage.error('获取课程信息失败')
    }
  } else {
    // 没有课程信息时的默认显示
    courseInfo.value = {
      id: 'default',
      name: '通用学习',
      description: '请选择具体课程进行学习',
      teacher: '系统',
      progress: 0
    }
  }
}

// 模拟最近学习记录
const generateRecentLearning = () => {
  const activities = [
    { content: '完成了第一章的学习', time: '2024-01-15 14:30', type: 'success' },
    { content: '观看了教学视频', time: '2024-01-15 10:20', type: 'primary' },
    { content: '完成了练习题', time: '2024-01-14 16:45', type: 'warning' },
    { content: '下载了课程资料', time: '2024-01-14 09:15', type: 'info' }
  ]
  recentLearning.value = activities.slice(0, 3) // 只显示最近3条
}

// 各种操作函数
const viewPDF = () => {
  ElMessage.info('PDF查看功能开发中...')
}

const viewVideo = () => {
  ElMessage.info('视频播放功能开发中...')
}

const startExercise = () => {
  ElMessage.info('练习题功能开发中...')
}

const viewProgress = () => {
  ElMessage.info('学习进度功能开发中...')
}

const refreshResources = () => {
  ElMessage.success('资源已刷新')
  generateRecentLearning()
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  fetchCourseInfo()
  generateRecentLearning()
})
</script>

<style scoped>
.learning-view {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.header {
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
}

.header-content h1 {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.main-content {
  padding: 30px;
}

.learning-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.course-info-card {
  margin-bottom: 20px;
}

.course-header h2 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 20px;
}

.course-description {
  margin: 0 0 15px 0;
  color: #666;
  font-size: 14px;
  line-height: 1.5;
}

.course-meta {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-wrap: wrap;
}

.learning-resources-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.resources-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.resource-item {
  cursor: pointer;
  transition: all 0.3s ease;
}

.resource-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.resource-content {
  text-align: center;
  padding: 20px;
}

.resource-icon {
  font-size: 48px;
  color: #409eff;
  margin-bottom: 15px;
}

.resource-content h4 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 16px;
}

.resource-content p {
  margin: 0 0 15px 0;
  color: #666;
  font-size: 14px;
  line-height: 1.4;
}

.recent-learning-card {
  margin-bottom: 20px;
}

.el-timeline {
  padding: 0;
}

.el-timeline-item {
  padding-bottom: 20px;
}

.el-timeline-item:last-child {
  padding-bottom: 0;
}
</style> 