<template>
  <div class="course-browser">
    <el-header class="top-bar">
      <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      <span class="page-title">课程浏览</span>
    </el-header>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>我的课程</span>
      </div>
      <el-row :gutter="20">
        <el-col v-for="course in pagedCourses" :key="course.id" :span="8">
          <el-card class="course-card" @click="goToCourse(course.id)">
            <div class="course-title">{{ course.name }}</div>
            <div class="course-desc">{{ course.description }}</div>
          </el-card>
        </el-col>
      </el-row>
      <div class="pagination">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="courses.length"
          :page-size="pageSize"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

// 假数据，后续可用API替换
const courses = ref([
  { id: 1, name: '人工智能基础', description: 'AI入门课程' },
  { id: 2, name: '机器学习', description: '机器学习核心知识' },
  { id: 3, name: '深度学习', description: '深度学习与神经网络' },
  { id: 4, name: '自然语言处理', description: 'NLP基础与应用' },
  { id: 5, name: '计算机视觉', description: 'CV基础与项目' },
  { id: 6, name: '数据挖掘', description: '数据分析与挖掘' },
]);
const pageSize = 6;
const currentPage = ref(1);
const pagedCourses = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  return courses.value.slice(start, start + pageSize);
});
const router = useRouter();
function goToCourse(id: number) {
  router.push(`/teacher/courses/${id}`);
}
function handlePageChange(page: number) {
  currentPage.value = page;
}
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
.course-browser {
  padding: 24px;
}
.course-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: box-shadow 0.2s;
}
.course-card:hover {
  box-shadow: 0 2px 12px #aaa;
}
.course-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 8px;
}
.course-desc {
  color: #666;
  font-size: 14px;
}
.pagination {
  margin-top: 24px;
  text-align: center;
}
</style> 