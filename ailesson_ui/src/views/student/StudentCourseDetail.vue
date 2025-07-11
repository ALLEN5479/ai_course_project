<template>
  <div class="student-course-detail">
    <el-header class="top-bar">
      <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      <span class="page-title">{{ courseInfo.name }}</span>
    </el-header>
    
    <el-card class="course-card">
      <div class="course-header">
        <h2>{{ courseInfo.name }}</h2>
        <p class="course-description">{{ courseInfo.description }}</p>
        <div class="course-meta">
          <el-tag type="info">教师: {{ courseInfo.teacher }}</el-tag>
          <el-tag type="success">进度: {{ courseInfo.progress }}%</el-tag>
        </div>
      </div>
      
      <el-tabs v-model="activeTab" class="course-tabs">
        <el-tab-pane label="课程简介" name="intro">
          <div class="course-intro">
            <h3>课程介绍</h3>
            <div class="intro-content">
              <p>{{ courseInfo.description || '暂无课程介绍' }}</p>
            </div>
            
            <h3>学习目标</h3>
            <div class="learning-objectives">
              <ul>
                <li v-for="(objective, index) in courseInfo.objectives" :key="index">
                  {{ objective }}
                </li>
              </ul>
            </div>
            
            <h3>课程大纲</h3>
            <div class="course-outline">
              <el-timeline>
                <el-timeline-item
                  v-for="(chapter, index) in courseInfo.chapters"
                  :key="index"
                  :timestamp="chapter.title"
                  placement="top"
                >
                  <el-card>
                    <h4>{{ chapter.title }}</h4>
                    <p>{{ chapter.description }}</p>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="任务完成" name="missions">
          <div class="missions-section">
            <div class="missions-header">
              <h3>我的任务</h3>
              <el-button type="primary" @click="refreshMissions">刷新</el-button>
            </div>
            
            <div class="mission-types">
              <div
                v-for="type in missionTypes"
                :key="type.value"
                class="mission-type-block"
              >
                <h4>{{ type.label }}</h4>
                <el-row :gutter="20">
                  <el-col
                    v-for="mission in getMissionsByType(type.value)"
                    :key="mission.id"
                    :span="8"
                  >
                    <el-card class="mission-card" :class="getMissionStatusClass(mission.status)">
                      <div class="mission-content">
                        <div class="mission-header">
                          <h5>{{ mission.name }}</h5>
                          <el-tag :type="getStatusTagType(mission.status)">
                            {{ getStatusText(mission.status) }}
                          </el-tag>
                        </div>
                        <p class="mission-description">{{ mission.description }}</p>
                        <div class="mission-time">
                          <span>开始: {{ formatDate(mission.startTime) }}</span>
                          <span>结束: {{ formatDate(mission.endTime) }}</span>
                        </div>
                        <div class="mission-actions">
                          <el-button 
                            type="primary" 
                            size="small"
                            @click="startMission(mission)"
                            :disabled="mission.status === 'completed'"
                          >
                            {{ getActionButtonText(mission.status) }}
                          </el-button>
                          <el-button 
                            v-if="mission.status === 'in_progress'"
                            type="success" 
                            size="small"
                            @click="submitMission(mission)"
                          >
                            提交
                          </el-button>
                        </div>
                      </div>
                    </el-card>
                  </el-col>
                </el-row>
                <div v-if="getMissionsByType(type.value).length === 0" class="no-missions">
                  <el-empty description="暂无任务" />
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const route = useRoute()

// 路由参数
const routeParams = ref({
  user_id: route.query.user_id || '',
  name: route.query.name || ''
})

const courseId = route.params.id as string
const activeTab = ref('intro')

// 课程信息
const courseInfo = reactive({
  id: '',
  name: '',
  description: '',
  teacher: '',
  progress: 0,
  objectives: [
    '掌握课程基础知识',
    '完成相关实践任务',
    '提升综合应用能力'
  ],
  chapters: [
    {
      title: '第一章 基础知识',
      description: '学习课程的基础概念和理论'
    },
    {
      title: '第二章 实践应用',
      description: '通过实践项目巩固所学知识'
    },
    {
      title: '第三章 综合测试',
      description: '完成综合测试检验学习效果'
    }
  ]
})

// 任务类型
const missionTypes = [
  { label: '自主学习任务', value: 'self' },
  { label: '测验任务', value: 'quiz' },
  { label: '报告任务', value: 'report' }
]

// 任务列表
const missions = ref<any[]>([])

const userId = localStorage.getItem('user_id') || ''

// 获取课程信息
const fetchCourseInfo = async () => {
  try {
    // 获取课程基本信息
    const { data: courseData } = await axios.get(`http://localhost:8080/student/courses/${courseId}`, {
      params: { user_id: userId }
    })
    
    // 获取教师姓名
    const { data: teacherName } = await axios.get("http://localhost:8080/getTeacherName", {
      params: { teacher_id: courseData.teacher_id }
    })
    
    Object.assign(courseInfo, {
      id: courseData.course_id,
      name: courseData.course_name,
      description: courseData.description,
      teacher: teacherName,
      progress: courseData.progress || 0
    })
  } catch (error) {
    console.error('获取课程信息失败:', error)
    ElMessage.error('获取课程信息失败')
  }
}

// 获取任务列表
const fetchMissions = async () => {
  try {
    const { data: response } = await axios.get(`http://localhost:8080/api/task-manager/published-missions`, {
      params: { courseId: courseId }
    })
    
    if (response.success && response.data) {
      missions.value = response.data.map((mission: any) => ({
        id: mission.id,
        name: mission.name || `任务${mission.missionId}`,
        description: mission.description || '暂无描述',
        type: mission.type || 'unknown',
        startTime: mission.startTime,
        endTime: mission.endTime,
        status: mission.status || 'pending',
        templateId: mission.missionId
      }))
    }
  } catch (error) {
    console.error('获取任务列表失败:', error)
    ElMessage.error('获取任务列表失败')
  }
}

// 根据类型过滤任务
const getMissionsByType = (type: string) => {
  return missions.value.filter(mission => mission.type === type)
}

// 获取任务状态样式类
const getMissionStatusClass = (status: string) => {
  const statusMap: Record<string, string> = {
    'pending': 'mission-pending',
    'in_progress': 'mission-in-progress',
    'completed': 'mission-completed',
    'overdue': 'mission-overdue'
  }
  return statusMap[status] || 'mission-pending'
}

// 获取状态标签类型
const getStatusTagType = (status: string) => {
  const typeMap: Record<string, string> = {
    'pending': 'info',
    'in_progress': 'warning',
    'completed': 'success',
    'overdue': 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status: string) => {
  const textMap: Record<string, string> = {
    'pending': '待开始',
    'in_progress': '进行中',
    'completed': '已完成',
    'overdue': '已逾期'
  }
  return textMap[status] || '未知'
}

// 获取操作按钮文本
const getActionButtonText = (status: string) => {
  const textMap: Record<string, string> = {
    'pending': '开始任务',
    'in_progress': '继续任务',
    'completed': '已完成',
    'overdue': '重新开始'
  }
  return textMap[status] || '开始任务'
}

// 格式化日期
const formatDate = (dateString: string) => {
  if (!dateString) return '未设置'
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

// 开始任务
const startMission = (mission: any) => {
  console.log('开始任务:', mission)
  ElMessage.success(`开始任务: ${mission.name}`)
  
  // 根据任务类型跳转到不同的页面
  if (mission.type === 'self') {
    router.push({
      path: '/student/learning',
      query: {
        ...routeParams.value,
        missionId: mission.id,
        courseId: courseId
      }
    })
  } else if (mission.type === 'quiz') {
    router.push({
      path: '/student/missions',
      query: {
        ...routeParams.value,
        missionId: mission.id,
        courseId: courseId
      }
    })
  } else if (mission.type === 'report') {
    router.push({
      path: '/student/missions',
      query: {
        ...routeParams.value,
        missionId: mission.id,
        courseId: courseId
      }
    })
  }
}

// 提交任务
const submitMission = (mission: any) => {
  console.log('提交任务:', mission)
  ElMessage.success(`提交任务: ${mission.name}`)
}

// 刷新任务
const refreshMissions = () => {
  fetchMissions()
  ElMessage.success('任务列表已刷新')
}

// 返回上一页
const goBack = () => {
  router.push({
    path: '/student/courses',
    query: {
      ...routeParams.value
    }
  })
}

// 组件挂载时获取数据
onMounted(async () => {
  routeParams.value = {
    user_id: route.query.user_id || '',
    name: route.query.name || ''
  }
  
  await fetchCourseInfo()
  await fetchMissions()
})
</script>

<style scoped>
.student-course-detail {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;
}

.top-bar {
  display: flex;
  align-items: center;
  height: 56px;
  background: white;
  border-radius: 8px;
  margin-bottom: 20px;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-title {
  font-size: 20px;
  font-weight: bold;
  margin-left: 16px;
  color: #333;
}

.course-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.course-header {
  padding: 20px 0;
  border-bottom: 1px solid #eee;
  margin-bottom: 20px;
}

.course-header h2 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 24px;
}

.course-description {
  color: #666;
  margin: 0 0 15px 0;
  line-height: 1.6;
}

.course-meta {
  display: flex;
  gap: 10px;
}

.course-tabs {
  margin-top: 20px;
}

.course-intro h3 {
  color: #333;
  margin: 20px 0 10px 0;
  font-size: 18px;
}

.intro-content p {
  color: #666;
  line-height: 1.6;
  margin-bottom: 20px;
}

.learning-objectives ul {
  padding-left: 20px;
}

.learning-objectives li {
  color: #666;
  margin-bottom: 8px;
  line-height: 1.5;
}

.course-outline {
  margin-top: 20px;
}

.missions-section {
  padding: 20px 0;
}

.missions-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.missions-header h3 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.mission-type-block {
  margin-bottom: 30px;
}

.mission-type-block h4 {
  color: #333;
  margin-bottom: 15px;
  font-size: 16px;
}

.mission-card {
  margin-bottom: 15px;
  transition: all 0.3s ease;
}

.mission-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.mission-pending {
  border-left: 4px solid #909399;
}

.mission-in-progress {
  border-left: 4px solid #e6a23c;
}

.mission-completed {
  border-left: 4px solid #67c23a;
}

.mission-overdue {
  border-left: 4px solid #f56c6c;
}

.mission-content {
  padding: 10px 0;
}

.mission-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.mission-header h5 {
  margin: 0;
  color: #333;
  font-size: 14px;
}

.mission-description {
  color: #666;
  font-size: 12px;
  margin: 8px 0;
  line-height: 1.4;
}

.mission-time {
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin: 10px 0;
}

.mission-time span {
  font-size: 11px;
  color: #999;
}

.mission-actions {
  display: flex;
  gap: 8px;
  margin-top: 10px;
}

.no-missions {
  text-align: center;
  padding: 40px 0;
  color: #999;
}
</style> 