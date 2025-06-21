<template>
  <div class="student-courses">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>我的课程</h1>
          <el-button @click="goBack">返回主页</el-button>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <el-row :gutter="20">
          <el-col :span="8" v-for="course in courses" :key="course.id">
            <el-card class="course-card" @click="viewCourse(course)">
              <div class="course-info">
                <h3>{{ course.name }}</h3>
                <p>{{ course.description }}</p>
                <div class="course-meta">
                  <span>教师: {{ course.teacher }}</span>
                  <el-progress :percentage="course.progress" :stroke-width="6" />
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const courses = ref([
  {
    id: 1,
    name: '数据结构与算法',
    description: '学习基本的数据结构和算法知识',
    teacher: '李老师',
    progress: 75
  },
  {
    id: 2,
    name: '计算机网络',
    description: '深入了解网络协议和通信原理',
    teacher: '王老师',
    progress: 45
  },
  {
    id: 3,
    name: '操作系统',
    description: '学习操作系统的基本概念和原理',
    teacher: '张老师',
    progress: 30
  }
])

const viewCourse = (course: any) => {
  router.push(`/student/courses/${course.id}`)
}

const goBack = () => {
  router.push('/student/dashboard')
}
</script>

<style scoped>
.student-courses {
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

.course-card {
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 20px;
}

.course-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.course-info h3 {
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