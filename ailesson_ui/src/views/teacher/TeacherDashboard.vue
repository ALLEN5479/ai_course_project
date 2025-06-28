<template>
  <div class="teacher-dashboard">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>教师端 - 智能课程系统</h1>
          <div class="user-info">
            <el-avatar :size="40" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
            <span class="username">李老师</span>
            <el-button type="text" @click="logout">退出登录</el-button>
          </div>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <!-- 工作概览 -->
        <div class="overview-section">
          <h2>工作概览</h2>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-number">{{ stats.totalCourses }}</div>
                  <div class="stat-label">总课程数</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-number">{{ stats.totalStudents }}</div>
                  <div class="stat-label">总学生数</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-number">{{ stats.pendingTasks }}</div>
                  <div class="stat-label">待处理任务</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="stat-card">
                <div class="stat-content">
                  <div class="stat-number">{{ stats.activeClasses }}</div>
                  <div class="stat-label">活跃班级</div>
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
            <el-col :span="8" v-for="course in myCourses" :key="course.id">
              <el-card class="course-card" @click="selectCourse(course)">
                <div class="course-header">
                  <h3>{{ course.name }}</h3>
                  <el-tag :type="course.status === 'active' ? 'success' : 'info'">
                    {{ course.status === 'active' ? '进行中' : '已结束' }}
                  </el-tag>
                </div>
                <p class="course-description">{{ course.description }}</p>
                <div class="course-stats">
                  <div class="stat-item">
                    <span class="stat-label">学生数:</span>
                    <span class="stat-value">{{ course.studentCount }}</span>
                  </div>
                  <div class="stat-item">
                    <span class="stat-label">班级数:</span>
                    <span class="stat-value">{{ course.classCount }}</span>
                  </div>
                </div>
                <div class="course-actions">
                  <el-button size="small" @click.stop="viewClasses(course)">查看班级</el-button>
                  <el-button size="small" type="primary" @click.stop="manageCourse(course)">管理课程</el-button>
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

    <!-- 班级选择对话框 -->
    <el-dialog v-model="classDialogVisible" title="选择教学班" width="600px">
      <el-table :data="selectedCourseClasses" style="width: 100%">
        <el-table-column prop="className" label="班级名称" />
        <el-table-column prop="studentCount" label="学生数量" />
        <el-table-column prop="status" label="状态" />
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="enterClass(scope.row)">进入班级</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 统计数据
const stats = ref({
  totalCourses: 5,
  totalStudents: 156,
  pendingTasks: 8,
  activeClasses: 12
})

// 我的课程
const myCourses = ref([
  {
    id: 1,
    name: '数据结构与算法',
    description: '学习基本的数据结构和算法知识，包括数组、链表、栈、队列、树、图等',
    status: 'active',
    studentCount: 45,
    classCount: 3
  },
  {
    id: 2,
    name: '计算机网络',
    description: '深入了解网络协议和通信原理，掌握网络编程基础',
    status: 'active',
    studentCount: 38,
    classCount: 2
  },
  {
    id: 3,
    name: '操作系统',
    description: '学习操作系统的基本概念和原理，进程管理、内存管理等',
    status: 'active',
    studentCount: 42,
    classCount: 2
  }
])

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

// 班级选择相关
const classDialogVisible = ref(false)
const selectedCourseClasses = ref<Array<{
  className: string;
  studentCount: number;
  status: string;
}>>([])
const selectedCourse = ref<any>(null)

const goToCourses = () => {
  router.push('/teacher/courses')
}

const selectCourse = (course: any) => {
  selectedCourse.value = course
  // 模拟获取班级数据
  selectedCourseClasses.value = [
    { className: `${course.name}-1班`, studentCount: 15, status: '活跃' },
    { className: `${course.name}-2班`, studentCount: 16, status: '活跃' },
    { className: `${course.name}-3班`, studentCount: 14, status: '活跃' }
  ]
  classDialogVisible.value = true
}

const viewClasses = (course: any) => {
  selectCourse(course)
}

const manageCourse = (course: any) => {
  router.push(`/teacher/courses/${course.id}`)
}

const enterClass = (classInfo: any) => {
  ElMessage.success(`进入班级: ${classInfo.className}`)
  classDialogVisible.value = false
  // 这里可以跳转到具体的班级管理页面
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
</style> 