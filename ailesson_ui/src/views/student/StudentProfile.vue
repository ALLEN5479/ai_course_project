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
              <h2>张三</h2>
              <p>学号：20240001</p>
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
          <el-card class="ability-card" @click="goToAbility">
            <div class="ability-header">
              <el-icon style="font-size: 32px; color: #409eff;"><i class="el-icon-data-analysis"></i></el-icon>
              <span style="font-size: 18px; font-weight: bold; margin-left: 10px;">能力图谱</span>
            </div>
            <div style="margin-top: 8px; color: #666;">多维度能力评估，点击查看详情</div>
          </el-card>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
const router = useRouter()
const goBack = () => {
  router.push('/student/dashboard')
}
const goToAbility = () => {
  router.push('/student/ability')
}
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
.ability-card {
  margin-top: 24px;
  cursor: pointer;
  transition: box-shadow 0.2s;
}
.ability-card:hover {
  box-shadow: 0 4px 16px rgba(64,158,255,0.15);
}
.ability-header {
  display: flex;
  align-items: center;
}
</style> 