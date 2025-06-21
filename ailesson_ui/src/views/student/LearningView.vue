<template>
  <div class="learning-view">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>学习界面</h1>
          <el-button @click="goBack">返回</el-button>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <div class="learning-container">
          <div class="placeholder">
            <el-icon class="placeholder-icon"><VideoPlay /></el-icon>
            <h3>学习资源</h3>
            <p>这里将显示PDF、视频、纯文本等学习资源，支持下载和查看</p>
            <el-button type="primary">查看资源</el-button>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { VideoPlay } from '@element-plus/icons-vue'

const router = useRouter()

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.learning-view {
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

.learning-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  min-height: 600px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.placeholder {
  text-align: center;
  padding: 40px;
}

.placeholder-icon {
  font-size: 64px;
  color: #409eff;
  margin-bottom: 20px;
}

.placeholder h3 {
  margin: 0 0 16px 0;
  color: #333;
  font-size: 24px;
}

.placeholder p {
  margin: 0 0 24px 0;
  color: #666;
  font-size: 16px;
  line-height: 1.5;
}
</style> 