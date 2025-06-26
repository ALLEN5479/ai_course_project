<template>
  <div class="student-profile">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>个人中心</h1>
          <el-button @click="goBack">返回主页</el-button>
        </div>
      </el-header>
      <el-main class="main-content">
        <el-card class="profile-card">
          <div class="profile-header">
            <el-avatar :size="80" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
            <div class="profile-info">
              <h2>{{ studentName }}</h2> <!-- 使用路由参数中的姓名 -->
              <p>学号：{{ studentId }}</p>
              <p>已选课程：3门</p>
            </div>
          </div>
          <div class="profile-courses">
            <h3>已选课程</h3>
            <ul>
              <li>数据结构与算法</li>
              <li>计算机网络</li>
              <li>操作系统</li>
            </ul>
          </div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router' // 添加 useRoute

const router = useRouter()
const route = useRoute() // 获取当前路由信息

// 存储从路由中获取的参数
const routeParams = ref({
  user_id: route.query.user_id || '',
  name: route.query.name || ''
})

// 学生信息（使用路由参数）
const studentName = ref(route.query.name || '学生')
const studentId = ref(route.query.user_id || '未获取学号')

// 返回主页时保留参数
const goBack = () => {
  router.push({
    path: '/student/dashboard',
    query: {
      ...routeParams.value // 保留当前所有参数
    }
  })
}

// 监听路由参数变化
onMounted(() => {
  // 确保参数更新
  routeParams.value = {
    user_id: route.query.user_id || '',
    name: route.query.name || ''
  }

  // 更新学生信息
  studentName.value = route.query.name || '学生'
  studentId.value = route.query.user_id || '未获取学号'
})
</script>

<style scoped>
.student-profile {
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
  max-width: 500px;
  margin: 0 auto;
  padding: 30px 40px;
}
.profile-header {
  display: flex;
  align-items: center;
  gap: 24px;
  margin-bottom: 24px;
}
.profile-info h2 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 22px;
}
.profile-info p {
  margin: 0 0 4px 0;
  color: #666;
  font-size: 14px;
}
.profile-courses h3 {
  margin: 0 0 8px 0;
  color: #409eff;
  font-size: 18px;
}
.profile-courses ul {
  padding-left: 20px;
  margin: 0;
}
.profile-courses li {
  color: #333;
  font-size: 15px;
  margin-bottom: 4px;
}
</style>