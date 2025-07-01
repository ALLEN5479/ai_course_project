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
        <el-form :model="form" label-width="80px" style="max-width:400px;margin:auto;">
          <el-form-item label="姓名">
            <el-input v-model="form.name" disabled />
          </el-form-item>
          <el-form-item label="学号">
            <el-input v-model="form.user_id" disabled />
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="form.gender" placeholder="请选择">
              <el-option label="男" value="男" />
              <el-option label="女" value="女" />
            </el-select>
          </el-form-item>
          <el-form-item label="年级">
            <el-input v-model="form.grade" />
          </el-form-item>
          <el-form-item label="班级">
            <el-input v-model="form.class_name" />
          </el-form-item>
          <el-form-item label="专业">
            <el-input v-model="form.major" />
          </el-form-item>
          <el-form-item label="学校">
            <el-input v-model="form.school" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveProfile">保存</el-button>
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router' // 添加 useRoute
import { getStudentInfo, updateStudentInfo } from '@/api/studentApi'
import { ElMessage } from 'element-plus'

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

const form = ref({
  user_id: '',
  name: '',
  gender: '',
  grade: '',
  class_name: '',
  major: '',
  school: ''
})

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
onMounted(async () => {
  // 确保参数更新
  routeParams.value = {
    user_id: route.query.user_id || '',
    name: route.query.name || ''
  }

  // 更新学生信息
  studentName.value = route.query.name || '学生'
  studentId.value = route.query.user_id || '未获取学号'

  form.value.user_id = route.query.user_id as string
  form.value.name = route.query.name as string
  // 获取扩展信息
  const res = await getStudentInfo(form.value.user_id)
  if (res.data) {
    Object.assign(form.value, res.data)
    // 保证 name 字段不被覆盖
    form.value.name = route.query.name as string
  }
})

const saveProfile = async () => {
  await updateStudentInfo(form.value)
  ElMessage.success('保存成功')
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
.avatar-uploader .avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: block;
}
</style>