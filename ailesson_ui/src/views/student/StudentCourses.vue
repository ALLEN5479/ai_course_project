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
        <div v-if="courses.length === 0" class="no-courses">
          <el-empty description="暂无课程数据" />
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

const router = useRouter()
const route = useRoute()

// 存储从路由中获取的参数
const routeParams = ref({
  user_id: route.query.user_id || '',
  name: route.query.name || ''
})

// 课程数据（初始为空数组）
const courses = ref<any[]>([])

// 获取课程数据
const fetchCourses = async () => {
  if (!routeParams.value.user_id) {
    ElMessage.error('用户ID缺失')
    return
  }

  try {
    // 1. 获取学生课程列表
    const { data: courseMsgs } = await axios.get("http://localhost:8080/student/courses", {
      params: { user_id: routeParams.value.user_id }
    })

    // 2. 为每个课程获取教师姓名
    const coursesWithTeachers = await Promise.all(
        courseMsgs.map(async (course: any) => {
          try {
            const { data: teacherName } = await axios.get("http://localhost:8080/getTeacherName", {
              params: { teacher_id: course.teacher_id }
            })
            return {
              id: course.course_id,
              name: course.course_name,
              description: course.description,
              teacher: teacherName,
              progress: course.progress || 0
            }
          } catch (error) {
            console.error("获取教师姓名失败:", error)
            return {
              id: course.course_id,
              name: course.course_name,
              description: course.description,
              teacher: "未知教师",
              progress: course.progress || 0
            }
          }
        })
    )

    courses.value = coursesWithTeachers
  } catch (error) {
    console.error("获取课程数据失败:", error)
    ElMessage.error('获取课程数据失败')
    courses.value = []
  }
}

// 跳转到课程详情页时保留参数
const viewCourse = (course: any) => {
  router.push({
    path: '/student/learning',
    query: {
      ...routeParams.value,
      courseId: course.id,
      courseName: course.name,
      courseDescription: course.description,
      teacherName: course.teacher
    }
  })
}

// 返回主页时保留参数
const goBack = () => {
  router.push({
    path: '/student/dashboard',
    query: {
      ...routeParams.value
    }
  })
}

// 组件挂载时获取课程数据
onMounted(() => {
  // 更新路由参数
  routeParams.value = {
    user_id: route.query.user_id || '',
    name: route.query.name || ''
  }

  // 获取课程数据
  fetchCourses()
})
</script>

<style scoped>
/* 保持原有样式不变 */
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

.no-courses {
  margin-top: 50px;
  text-align: center;
}
</style>