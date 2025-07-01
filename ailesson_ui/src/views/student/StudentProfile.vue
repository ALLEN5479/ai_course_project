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
        <div class="profile-wrapper">
          <el-card class="profile-card">
            <div class="profile-header">
              <el-avatar :size="80" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
              <div class="profile-info">
                <h2>{{ userInfo.name }}</h2>
                <p>学号：{{ userInfo.user_id }}</p>
                <p>已选课程：{{ userInfo.courseCount }}门</p>
              </div>
            </div>
            <div class="profile-courses">
              <h3>已选课程</h3>
              <!-- <ul>
                <li v-for="course in userInfo.courses" :key="course">{{ course }}</li>
              </ul> -->
            </div>
            <div style="display: flex; justify-content: center;">
              <AbilityRadarChart :userId="userId" />
            </div>
          </el-card>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import AbilityRadarChart from './ability/AbilityRadarChart.vue'
import { ref, onMounted } from 'vue'

const router = useRouter()
const goBack = () => {
  router.push('/student/dashboard')
}

// 读取用户信息
const userInfo = ref({ name: '', user_id: '', courseCount: 0 })

onMounted(() => {
  const info = localStorage.getItem('userInfo')
  if (info) {
    userInfo.value = JSON.parse(info)
  }
})

const userId = userInfo.value.user_id || 
  (JSON.parse(localStorage.getItem('userInfo') || '{}').user_id) || 
  ''
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
  width: 100vw;
  min-height: 100vh;
  padding: 0;
  position: static;
}
.profile-wrapper {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  position: static;
  top: unset;
  right: unset;
}
.profile-card {
  width: 100%;
  max-width: none;
  padding: 40px 60px;
  min-height: 600px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: stretch;
}
.ability-btn-wrapper {
  margin-top: 16px;
  width: 100%;
  display: flex;
  justify-content: flex-end;
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