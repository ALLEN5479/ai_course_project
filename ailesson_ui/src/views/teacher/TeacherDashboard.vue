<template>
  <div class="teacher-dashboard">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>教师端 - 智能课程系统</h1>
          <div class="user-info">
            <el-avatar
              :size="40"
              src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
              @click="goToProfile"
              class="clickable-avatar"
            />
            <span class="username clickable-username" @click="goToProfile">{{ teacherName }}</span>
            <el-button type="text" @click="logout">退出登录</el-button>
          </div>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <!-- 工作概览 -->
        <div class="overview-section">
          <h2>工作概览</h2>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-number">{{ stats.totalCourses }}</div>
                  <div class="stat-label">总课程数</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-number">{{ stats.totalStudents }}</div>
                  <div class="stat-label">总学生数</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-number">{{ stats.pendingTasks }}</div>
                  <div class="stat-label">待处理任务</div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <!-- 我的课程 -->
        <div class="courses-section">
          <div class="section-header">
            <h2>我的课程</h2>
            <el-button type="primary" @click="goToCourses">查看所有课程</el-button>
          </div>
          
          <el-row :gutter="20">
            <el-col :span="8" v-for="course in myCourses" :key="course.course_id">
              <el-card class="course-card" @click="manageCourse(course)">
                <div class="course-header">
                  <h3>{{ course.course_name }}</h3>
                  <el-tag type="success">进行中</el-tag>
                </div>
                <p class="course-description">{{ course.description || '暂无描述' }}</p>
                <div class="course-stats">
                  <div class="stat-item">
                    <span class="stat-label">课程ID:</span>
                    <span class="stat-value">{{ course.course_id }}</span>
                  </div>
                  <div class="stat-item">
                    <span class="stat-label">教师ID:</span>
                    <span class="stat-value">{{ course.teacher_id }}</span>
                  </div>
                </div>
                <div class="course-actions">
                  <el-button size="small" type="primary">管理课程</el-button>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <!-- 加载状态 -->
          <div v-if="loading" class="loading-container">
            <el-loading-component />
          </div>

          <!-- 空状态 -->
          <div v-if="!loading && myCourses.length === 0" class="empty-state">
            <el-empty description="暂无课程数据" />
          </div>
        </div>

        <!-- 快速操作 -->
        <div class="quick-actions-section">
          <h2>快速操作</h2>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card class="action-card" @click="goToStudentManagement">
                <div class="action-content">
                  <div class="action-icon">👥</div>
                  <div class="action-title">学生管理</div>
                  <div class="action-desc">管理学生信息</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="action-card" @click="goToTaskLibrary">
                <div class="action-content">
                  <div class="action-icon">📚</div>
                  <div class="action-title">任务库</div>
                  <div class="action-desc">管理教学任务</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="action-card" @click="goToKnowledgeGraph">
                <div class="action-content">
                  <div class="action-icon">🧠</div>
                  <div class="action-title">知识图谱</div>
                  <div class="action-desc">查看知识结构</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="action-card" @click="goToClassTaskManager">
                <div class="action-content">
                  <div class="action-icon">📋</div>
                  <div class="action-title">班级任务</div>
                  <div class="action-desc">管理班级任务</div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <!-- 待完成工作 -->
        <div class="tasks-section">
          <h2>待完成工作</h2>
          <el-table :data="pendingTasks" style="width: 100%">
            <el-table-column prop="courseName" label="课程名称" />
            <el-table-column prop="taskType" label="任务类型" />
            <el-table-column prop="deadline" label="截止时间" />
            <el-table-column prop="priority" label="优先级">
              <template #default="scope">
                <el-tag :type="getPriorityType(scope.row.priority)">
                  {{ scope.row.priority }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button size="small" @click="handleTask(scope.row)">处理</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const route = useRoute()

// 教师信息
const teacherId = ref('')
const teacherName = ref('李老师') // 默认值

// 加载状态
const loading = ref(false)

// 获取路由参数中的教师信息
const getTeacherInfo = () => {
  const id = route.query.teacherId as string
  const name = route.query.teacherName as string

  if (id) {
    teacherId.value = id
  }

  if (name) {
    teacherName.value = name
  }
}

// 统计数据
const stats = ref({
  totalCourses: 0,
  totalStudents: 0,
  pendingTasks: 8
})

// 我的课程
const myCourses = ref([])

// 获取教师课程数据
const fetchTeacherCourses = async () => {
  if (!teacherId.value) {
    ElMessage.warning('教师ID不存在，无法获取课程数据')
    return
  }

  loading.value = true
  try {
    // 调用getCoursesByTeacherId接口获取课程数据
    const { data: courses } = await axios.get("http://localhost:8080/getCoursesByTeacherId", {
      params: {
        teacher_id: teacherId.value
      }
    })

    myCourses.value = courses || []
    stats.value.totalCourses = courses ? courses.length : 0

    // 如果教师姓名为默认值，尝试从API获取
    if (teacherName.value === '李老师') {
      try {
        const { data: name } = await axios.get("http://localhost:8080/getTeacherName", {
          params: {
            teacher_id: teacherId.value
          }
        })
        if (name) {
          teacherName.value = name
        }
      } catch (error) {
        console.error('获取教师姓名失败:', error)
      }
    }

    ElMessage.success('课程数据加载成功')
  } catch (error) {
    console.error('获取课程数据失败:', error)
    ElMessage.error('获取课程数据失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

// 获取教师教导的学生数量
const fetchStudentCount = async () => {
  if (!teacherId.value) {
    ElMessage.warning('教师ID不存在，无法获取学生数据')
    return
  }

  try {
    const response = await axios.get('http://localhost:8080/student/course/count', {
      params: {
        teacher_id: teacherId.value
      }
    })

    if (response.data !== undefined && response.data !== null) {
      stats.value.totalStudents = response.data
      console.log('获取到学生数量:', response.data)
    } else {
      stats.value.totalStudents = 0
      console.log('未获取到学生数量数据')
    }
  } catch (error) {
    console.error('获取学生数量失败:', error)
    ElMessage.error('获取学生数量失败')
    stats.value.totalStudents = 0
  }
}

// 待处理任务
const pendingTasks = ref([
  {
    id: 1,
    courseName: '数据结构与算法',
    taskType: '批改作业',
    deadline: '2024-01-15',
    priority: '高'
  },
  {
    id: 2,
    courseName: '计算机网络',
    taskType: '上传资源',
    deadline: '2024-01-16',
    priority: '中'
  },
  {
    id: 3,
    courseName: '操作系统',
    taskType: '布置任务',
    deadline: '2024-01-17',
    priority: '低'
  }
])

const goToCourses = () => {
  router.push({
    path: '/teacher/courses',
    query: {
      teacherId: teacherId.value,
      teacherName: teacherName.value
    }
  })
}

const manageCourse = (course: any) => {
  router.push({
    path: `/teacher/courses/${course.course_id}`,
    query: {
      teacherId: teacherId.value,
      teacherName: teacherName.value
    }
  })
}

const handleTask = (task: any) => {
  if (task.taskType === '批改作业') {
    // 跳转到课程提交列表页面，传递任务ID
    router.push({
      path: '/teacher/course-submission-list',
      query: { taskId: task.id }
    });
  } else {
    ElMessage.info(`处理任务: ${task.taskType}`);
    // 这里可以跳转到具体的任务处理页面
  }
}

const getPriorityType = (priority: string) => {
  switch (priority) {
    case '高': return 'danger'
    case '中': return 'warning'
    case '低': return 'info'
    default: return 'info'
  }
}

const logout = () => {
  ElMessage.success('已退出登录')
  router.push('/login')
}

const goToProfile = () => {
  router.push({
    path: '/teacher/profile',
    query: {
      teacher_id: teacherId.value,
      name: teacherName.value
    }
  })
}

const goToStudentManagement = () => {
  const { teacher_id, ...rest } = route.query;
  router.push({
    path: '/teacher/student-management',
    query: {
      ...rest,
      teacherName: teacherName.value
    }
  })
}

const goToTaskLibrary = () => {
  router.push('/teacher/task-library')
}

const goToKnowledgeGraph = () => {
  router.push('/teacher/knowledge-graph')
}

const goToClassTaskManager = () => {
  router.push('/teacher/class-task-manager')
}

// 组件挂载时获取教师信息和课程数据
onMounted(() => {
  getTeacherInfo()
  fetchTeacherCourses()
  fetchStudentCount()
})
</script>

<style scoped>
.teacher-dashboard {
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

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.username {
  font-weight: 500;
  color: #333;
}

.clickable-avatar {
  cursor: pointer;
  transition: transform 0.2s ease;
}

.clickable-avatar:hover {
  transform: scale(1.05);
}

.clickable-username {
  cursor: pointer;
  transition: color 0.2s ease;
}

.clickable-username:hover {
  color: #409eff;
}

.main-content {
  padding: 30px;
}

.overview-section {
  margin-bottom: 40px;
}

.overview-section h2 {
  margin-bottom: 20px;
  color: #333;
  font-size: 20px;
}

.stat-card {
  text-align: center;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-content {
  padding: 20px;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 8px;
}

.stat-label {
  color: #666;
  font-size: 14px;
}

.courses-section {
  margin-bottom: 40px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h2 {
  margin: 0;
  color: #333;
  font-size: 20px;
}

.course-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.course-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.course-header h3 {
  margin: 0;
  color: #333;
  font-size: 16px;
}

.course-description {
  color: #666;
  font-size: 14px;
  line-height: 1.4;
  margin-bottom: 16px;
}

.course-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 16px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-label {
  font-size: 12px;
  color: #999;
}

.stat-value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.course-actions {
  display: flex;
  gap: 8px;
}

.tasks-section h2 {
  margin-bottom: 20px;
  color: #333;
  font-size: 20px;
}

.quick-actions-section {
  margin-bottom: 40px;
}

.quick-actions-section h2 {
  margin-bottom: 20px;
  color: #333;
  font-size: 20px;
}

.action-card {
  text-align: center;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

.action-content {
  padding: 20px;
}

.action-icon {
  font-size: 24px;
  margin-bottom: 8px;
}

.action-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.action-desc {
  font-size: 12px;
  color: #666;
}
</style>