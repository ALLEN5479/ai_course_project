<template>
  <div class="course-detail">
    <el-header class="top-bar">
      <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      <span class="page-title">课程详情</span>
    </el-header>
    <el-card>
      <div slot="header">
        <span>课程详情</span>
      </div>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="课程简介" name="intro">
          <div>这里是课程简介内容（可后续完善）</div>
        </el-tab-pane>
        <el-tab-pane label="任务布置" name="assign">
          <div>这里是任务布置页面（可后续完善）</div>
        </el-tab-pane>
        <el-tab-pane label="任务管理" name="manage">
          <div>这里是任务管理页面（可后续完善）</div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const activeTab = ref('intro');
const router = useRouter();
function goBack() {
  router.back();
}
</script>

<style scoped>
.top-bar {
  display: flex;
  align-items: center;
  height: 56px;
  background: #fff;
  border-bottom: 1px solid #eee;
  padding: 0 24px;
  margin-bottom: 16px;
}
.page-title {
  font-size: 20px;
  font-weight: bold;
  margin-left: 16px;
}
.course-detail {
  padding: 24px;
}
</style> 