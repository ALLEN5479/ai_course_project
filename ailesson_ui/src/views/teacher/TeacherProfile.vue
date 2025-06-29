<template>
  <div class="teacher-profile">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>教师个人中心</h1>
          <el-button @click="goBack">返回主页</el-button>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <!-- 个人信息卡片 -->
        <el-card class="profile-card">
          <div class="profile-header">
            <el-avatar :size="100" :src="teacherInfo.avatar" />
            <div class="profile-info">
              <h2>{{ teacherInfo.name }}</h2>
              <p class="teacher-title">{{ teacherInfo.title }}</p>
              <p class="teacher-department">{{ teacherInfo.department }}</p>
              <p class="teacher-email">{{ teacherInfo.email }}</p>
            </div>
            <div class="profile-stats">
              <div class="stat-item">
                <div class="stat-number">{{ teacherInfo.totalCourses }}</div>
                <div class="stat-label">教授课程</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ teacherInfo.totalStudents }}</div>
                <div class="stat-label">学生总数</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">{{ teacherInfo.teachingYears }}</div>
                <div class="stat-label">教龄(年)</div>
              </div>
            </div>
          </div>
          
          <!-- 个人介绍 -->
          <div class="profile-description">
            <h3>个人介绍</h3>
            <p>{{ teacherInfo.description }}</p>
          </div>
          
          <!-- 研究方向 -->
          <div class="research-areas">
            <h3>研究方向</h3>
            <div class="tags-container">
              <el-tag 
                v-for="area in teacherInfo.researchAreas" 
                :key="area"
                type="primary"
                effect="light"
                class="research-tag"
              >
                {{ area }}
              </el-tag>
            </div>
          </div>
        </el-card>

        <!-- 当前教授课程 -->
        <el-card class="courses-card">
          <div class="card-header">
            <h3>当前教授课程</h3>
            <el-button type="primary" @click="goToCourseManagement">课程管理</el-button>
          </div>
          
          <el-row :gutter="20">
            <el-col :span="8" v-for="course in currentCourses" :key="course.id">
              <el-card class="course-item" @click="viewCourseDetail(course)">
                <div class="course-header">
                  <h4>{{ course.name }}</h4>
                  <el-tag :type="course.status === 'active' ? 'success' : 'info'" size="small">
                    {{ course.status === 'active' ? '进行中' : '已结束' }}
                  </el-tag>
                </div>
                <p class="course-description">{{ course.description }}</p>
                <div class="course-stats">
                  <div class="course-stat">
                    <span class="stat-icon">👥</span>
                    <span>{{ course.studentCount }}人</span>
                  </div>
                  <div class="course-stat">
                    <span class="stat-icon">📚</span>
                    <span>{{ course.classCount }}个班</span>
                  </div>
                </div>
                <div class="course-actions">
                  <el-button size="small" @click.stop="viewClasses(course)">查看班级</el-button>
                  <el-button size="small" type="primary" @click.stop="manageCourse(course)">管理</el-button>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-card>

        <!-- 教学成就 -->
        <el-card class="achievements-card">
          <h3>教学成就</h3>
          <el-row :gutter="20">
            <el-col :span="6" v-for="achievement in achievements" :key="achievement.id">
              <div class="achievement-item">
                <div class="achievement-icon">
                  <i :class="achievement.icon"></i>
                </div>
                <div class="achievement-content">
                  <div class="achievement-title">{{ achievement.title }}</div>
                  <div class="achievement-desc">{{ achievement.description }}</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
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
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

// 教师信息
const teacherInfo = ref({
  name: '李教授',
  title: '教授',
  department: '计算机科学与技术学院',
  email: 'li.professor@university.edu.cn',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
  totalCourses: 5,
  totalStudents: 156,
  teachingYears: 8,
  description: '专注于人工智能和机器学习领域的研究与教学，具有丰富的教学经验和深厚的学术背景。致力于培养学生的创新思维和实践能力，在多个国际顶级期刊发表论文，主持多项国家级科研项目。',
  researchAreas: ['人工智能', '机器学习', '深度学习', '计算机视觉', '自然语言处理']
})

// 当前教授课程
const currentCourses = ref([
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
  },
  {
    id: 4,
    name: '人工智能导论',
    description: '人工智能基础理论与应用，包括机器学习、深度学习等前沿技术',
    status: 'active',
    studentCount: 35,
    classCount: 2
  },
  {
    id: 5,
    name: '软件工程',
    description: '软件开发的工程化方法，包括需求分析、设计、实现、测试等',
    status: 'active',
    studentCount: 28,
    classCount: 1
  }
])

// 教学成就
const achievements = ref([
  {
    id: 1,
    title: '优秀教师奖',
    description: '2023年度校级优秀教师',
    icon: 'el-icon-trophy'
  },
  {
    id: 2,
    title: '教学成果奖',
    description: '省级教学成果二等奖',
    icon: 'el-icon-medal'
  },
  {
    id: 3,
    title: '科研项目',
    description: '主持国家级项目3项',
    icon: 'el-icon-research'
  },
  {
    id: 4,
    title: '论文发表',
    description: '发表SCI论文20余篇',
    icon: 'el-icon-document'
  }
])

// 班级选择相关
const classDialogVisible = ref(false)
const selectedCourseClasses = ref([])
const selectedCourse = ref(null)

// 获取路由参数中的教师信息
const getTeacherInfo = () => {
  const id = route.query.teacher_id as string
  const name = route.query.name as string
  
  if (id) {
    // 可以在这里设置teacherId，如果需要的话
    console.log('Teacher ID:', id)
  }
  
  if (name) {
    teacherInfo.value.name = name
  }
}

const goBack = () => {
  router.push({
    path: '/teacher/dashboard',
    query: {
      teacherId: route.query.teacher_id,
      teacherName: route.query.name
    }
  })
}

const goToCourseManagement = () => {
  router.push('/teacher/courses')
}

const viewCourseDetail = (course: any) => {
  router.push(`/teacher/courses/${course.id}`)
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

// 组件挂载时获取教师信息
onMounted(() => {
  getTeacherInfo()
})
</script>

<style scoped>
.teacher-profile {
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

.profile-card {
  margin-bottom: 30px;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.profile-header {
  display: flex;
  align-items: flex-start;
  gap: 24px;
  margin-bottom: 30px;
}

.profile-info {
  flex: 1;
}

.profile-info h2 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 24px;
  font-weight: bold;
}

.teacher-title {
  margin: 0 0 4px 0;
  color: #409eff;
  font-size: 16px;
  font-weight: 500;
}

.teacher-department {
  margin: 0 0 4px 0;
  color: #666;
  font-size: 14px;
}

.teacher-email {
  margin: 0;
  color: #999;
  font-size: 14px;
}

.profile-stats {
  display: flex;
  gap: 20px;
}

.stat-item {
  text-align: center;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  min-width: 80px;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #666;
}

.profile-description {
  margin-bottom: 24px;
}

.profile-description h3 {
  margin: 0 0 12px 0;
  color: #333;
  font-size: 18px;
}

.profile-description p {
  margin: 0;
  color: #666;
  line-height: 1.6;
  font-size: 14px;
}

.research-areas {
  margin-bottom: 0;
}

.research-areas h3 {
  margin: 0 0 12px 0;
  color: #333;
  font-size: 18px;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.research-tag {
  margin: 0;
}

.courses-card {
  margin-bottom: 30px;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-header h3 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.course-item {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.course-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.course-header h4 {
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
  gap: 16px;
  margin-bottom: 16px;
}

.course-stat {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #666;
}

.stat-icon {
  font-size: 16px;
}

.course-actions {
  display: flex;
  gap: 8px;
}

.achievements-card {
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.achievements-card h3 {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 18px;
}

.achievement-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 16px;
}

.achievement-icon {
  width: 40px;
  height: 40px;
  background: #409eff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.achievement-content {
  flex: 1;
}

.achievement-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 4px;
}

.achievement-desc {
  font-size: 12px;
  color: #666;
}
</style> 