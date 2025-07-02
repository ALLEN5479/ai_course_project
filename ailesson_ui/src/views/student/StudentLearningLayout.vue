<template>
  <el-container style="height: 100vh;">
    <!-- 侧边栏 -->
    <el-aside width="60px" style="background:#fff;">
      <el-menu
        :default-active="activeMenu"
        @select="handleMenuSelect"
        class="el-menu-vertical-demo"
        style="border:none"
      >
        <el-menu-item index="courseDetail">
          <el-tooltip content="课程详情" placement="right">
            <el-icon><Document /></el-icon>
          </el-tooltip>
        </el-menu-item>
        <el-menu-item index="knowledgeGraph">
          <el-tooltip content="知识图谱" placement="right">
            <el-icon><Connection /></el-icon>
          </el-tooltip>
        </el-menu-item>
        <el-menu-item index="mission">
          <el-tooltip content="任务完成" placement="right">
            <el-icon><Edit /></el-icon>
          </el-tooltip>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <!-- 右侧内容区 -->
    <el-main style="background:#f5f7fa;">
      <!-- 顶部课程信息卡片 -->
      <el-card class="course-info-card" style="margin-bottom: 24px;">
        <div style="font-size:20px;font-weight:600;">{{ courseName }}</div>
        <div style="color:#888;">教师：{{ teacherName }}</div>
        <div style="color:#888;">课程ID：{{ courseId }}</div>
      </el-card>
      <!-- 动态内容区 -->
      <component :is="currentComponent" :course-id="courseId" :user-id="userId" />
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { Document, Connection, Edit } from '@element-plus/icons-vue'
import StudentCourseDetail from './StudentCourseDetail.vue'
import KnowledgeGraph from './KnowledgeGraph.vue'
import MissionView from './MissionView.vue'

// 侧栏导航状态
const activeMenu = ref('courseDetail')

// 获取参数
const route = useRoute()
const courseId = route.query.courseId
const userId = route.query.user_id
const courseName = route.query.courseName as string || '课程名称'
const teacherName = route.query.teacherName as string || '教师姓名'

// 切换内容区组件
const currentComponent = computed(() => {
  switch (activeMenu.value) {
    case 'courseDetail': return StudentCourseDetail
    case 'knowledgeGraph': return KnowledgeGraph
    case 'mission': return MissionView
    default: return StudentCourseDetail
  }
})
const handleMenuSelect = (key: string) => {
  activeMenu.value = key
}
</script>

<style scoped>
.course-info-card {
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  padding: 18px 24px;
}
</style> 