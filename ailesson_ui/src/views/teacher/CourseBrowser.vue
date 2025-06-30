<template>
  <div class="course-browser">
    <el-header class="top-bar">
      <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      <span class="page-title">课程浏览</span>
    </el-header>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>我的课程</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshCourses">刷新</el-button>
      </div>
      
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-loading-component />
      </div>
      
      <!-- 课程列表 -->
      <el-row v-else :gutter="20">
        <el-col v-for="course in pagedCourses" :key="course.course_id" :span="8">
          <el-card class="course-card" @click="goToCourse(course.course_id)">
            <div class="course-title">{{ course.course_name }}</div>
            <div class="course-desc">{{ course.description || '暂无描述' }}</div>
            <div class="course-info">
              <el-tag size="small" type="info">课程ID: {{ course.course_id }}</el-tag>
              <el-tag size="small" type="success">教师ID: {{ course.teacher_id }}</el-tag>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 空状态 -->
      <div v-if="!loading && courses.length === 0" class="empty-state">
        <el-empty description="暂无课程数据" />
      </div>
      
      <!-- 分页 -->
      <div v-if="courses.length > 0" class="pagination">
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
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import axios from 'axios';

const router = useRouter();
const route = useRoute();

// 教师信息
const teacherId = ref('');
const teacherName = ref('');

// 加载状态
const loading = ref(false);

// 课程数据
const courses = ref([]);
const pageSize = 6;
const currentPage = ref(1);

// 分页后的课程
const pagedCourses = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  return courses.value.slice(start, start + pageSize);
});

// 获取教师信息
const getTeacherInfo = () => {
  const id = route.query.teacherId as string;
  const name = route.query.teacherName as string;
  
  if (id) {
    teacherId.value = id;
  }
  
  if (name) {
    teacherName.value = name;
  }
};

// 获取教师课程数据
const fetchTeacherCourses = async () => {
  if (!teacherId.value) {
    ElMessage.warning('教师ID不存在，无法获取课程数据');
    return;
  }

  loading.value = true;
  try {
    // 调用getCoursesByTeacherId接口获取课程数据
    const { data: courseData } = await axios.get("http://localhost:8080/getCoursesByTeacherId", {
      params: {
        teacher_id: teacherId.value
      }
    });
    
    courses.value = courseData || [];
    ElMessage.success('课程数据加载成功');
  } catch (error) {
    console.error('获取课程数据失败:', error);
    ElMessage.error('获取课程数据失败，请检查网络连接');
  } finally {
    loading.value = false;
  }
};

// 刷新课程数据
const refreshCourses = () => {
  fetchTeacherCourses();
};

function goToCourse(id: number) {
  router.push({
    path: `/teacher/courses/${id}`,
    query: {
      teacherId: teacherId.value,
      teacherName: teacherName.value
    }
  });
}

function handlePageChange(page: number) {
  currentPage.value = page;
}

function goBack() {
  router.back();
}

// 组件挂载时获取教师信息和课程数据
onMounted(() => {
  getTeacherInfo();
  fetchTeacherCourses();
});
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
  margin-bottom: 12px;
}
.course-info {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
}
.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
}
.pagination {
  margin-top: 24px;
  text-align: center;
}
</style> 