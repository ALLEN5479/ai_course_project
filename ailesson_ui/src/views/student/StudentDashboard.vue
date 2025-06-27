<template>
  <div class="student-dashboard">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>学生端 - 智能课程系统</h1>
          <div class="user-info">
            <el-avatar :size="40" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
            <span class="username">{{ studentName }}</span>
            <el-button type="text" @click="logout">退出登录</el-button>
          </div>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <div class="dashboard-grid">
          <!-- 我的课程 -->
          <el-card class="dashboard-card" @click="goToCourses">
            <div class="card-content">
              <el-icon class="card-icon"><Reading /></el-icon>
              <h3>我的课程</h3>
              <p>查看和管理您的所有课程</p>
            </div>
          </el-card>
          
          <!-- 个人学习状况 -->
          <el-card class="dashboard-card" @click="goToProfile">
            <div class="card-content">
              <el-icon class="card-icon"><DataAnalysis /></el-icon>
              <h3>个人学习状况</h3>
              <p>查看您的学习进度和统计</p>
            </div>
          </el-card>
          
          <!-- 自主学习 -->
          <el-card class="dashboard-card" @click="goToSelfStudy">
            <div class="card-content">
              <el-icon class="card-icon"><Edit /></el-icon>
              <h3>自主学习</h3>
              <p>进行自主答题和学习</p>
            </div>
          </el-card>
          
          <!-- 测试学习资源 -->
          <el-card class="dashboard-card" @click="goToStudyResources(1)">
            <div class="card-content">
              <el-icon class="card-icon"><Document /></el-icon>
              <h3>测试学习资源</h3>
              <p>测试nodeId=1的学习资源</p>
            </div>
          </el-card>
          
          <!-- 测试学习资源2 -->
          <el-card class="dashboard-card" @click="goToStudyResources(2)">
            <div class="card-content">
              <el-icon class="card-icon"><Document /></el-icon>
              <h3>测试学习资源2</h3>
              <p>测试nodeId=2的学习资源</p>
            </div>
          </el-card>
        </div>
        
        <!-- 我的课程展示 -->
        <div class="recent-courses">
          <h2>我的课程</h2>
          <el-row :gutter="20">
            <el-col :span="8" v-for="course in courses" :key="course.id">
              <el-card class="course-card" @click="goToCourse(course.id)">
                <div class="course-info">
                  <h4>{{ course.course_name }}</h4>
                  <p>{{ course.description }}</p>
                  <div class="course-meta">
                    <span>教师: {{ course.teacher }}</span>
                    <el-progress :percentage="course.progress" :stroke-width="6" />
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <div v-if="courses.length === 0" style="text-align:center;color:#999;margin-top:20px;">
            暂无课程
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { Reading, DataAnalysis, Edit, Document } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

// 获取学生真实姓名
const studentName = ref(route.query.name || '学生')
// 获取user_id
const userId = ref(route.query.user_id || '')

// 课程数据
const courses = ref<any[]>([])

const navigateWithParams = (path: string, additionalParams: Record<string, any> = {}) => {
  router.push({
    path,
    query: {
      ...route.query, // 保留现有所有查询参数
      ...additionalParams // 添加额外参数（如有）
    }
  })
}

const goToCourses = () => navigateWithParams('/student/courses')
const goToProfile = () => navigateWithParams('/student/profile')
const goToSelfStudy = () => navigateWithParams('/student/self-study')
const goToCourse = (courseId: number) => navigateWithParams(`/student/courses/${courseId}`)
const goToStudyResources = (nodeId: number) => navigateWithParams('/student/study-resources', { nodeId })

const logout = () => {
  ElMessage.success('已退出登录')
  router.push('/login')
}

// 获取学生课程信息
const fetchStudentCourses = async () => {
  if (!userId.value) {
    ElMessage.error('未获取到用户ID')
    return
  }
  try {
    // 1. 获取课程列表
    const { data: courseMsgs } = await axios.get("http://localhost:8080/student/courses", {
      params: {
        user_id: userId.value
      }
    });

    // 2. 获取每个课程的教师姓名
    const coursesWithTeachers = await Promise.all(
        courseMsgs.map(async (course: any) => {
          console.log(course.teacher_id);
          try {
            // 调用获取教师姓名的接口
            const { data: teacherName } = await axios.get("http://localhost:8080/getTeacherName", {
              params: {
                teacher_id: course.teacher_id
              }
            });
            console.log("获取教师姓名成功:", teacherName);

            return {
              ...course,
              teacher: teacherName // 添加教师姓名到课程对象
            };
          } catch (error) {
            console.error("获取教师姓名失败:", error);
            return {
              ...course,
              teacher: "未知教师" // 错误时使用默认值
            };
          }
        })
    );

    courses.value = coursesWithTeachers;
  } catch (e) {
    ElMessage.error('获取课程信息失败')
    courses.value = []
  }
}

onMounted(() => {
  fetchStudentCourses()
})
</script>

<style scoped>
.student-dashboard {
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

.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.dashboard-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.dashboard-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.card-content {
  text-align: center;
  padding: 20px;
}

.card-icon {
  font-size: 48px;
  color: #409eff;
  margin-bottom: 16px;
}

.card-content h3 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 18px;
}

.card-content p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.recent-courses {
  margin-top: 40px;
}

.recent-courses h2 {
  margin-bottom: 20px;
  color: #333;
  font-size: 20px;
}

.course-card {
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 20px;
}

.course-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.course-info h4 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 16px;
}

.course-info p {
  margin: 0 0 12px 0;
  color: #666;
  font-size: 14px;
  line-height: 1.4;
}

.course-meta {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.course-meta span {
  font-size: 12px;
  color: #999;
}
</style> 