<template>
  <div class="student-list">
    <el-header class="top-bar">
      <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      <span class="page-title">学生管理</span>
    </el-header>
    <el-card>
      <div slot="header">
        <span>学生管理</span>
      </div>
      <el-table :data="students" style="width: 100%">
        <el-table-column prop="id" label="学号" width="120" />
        <el-table-column prop="name" label="姓名" width="150" />
        <el-table-column prop="progress" label="完成度" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewAbility(scope.row)">能力图谱</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
const students = [
  { id: '2023001', name: '张三', progress: '80%' },
  { id: '2023002', name: '李四', progress: '90%' },
  { id: '2023003', name: '王五', progress: '70%' },
];
const router = useRouter();
function viewAbility(student: any) {
  router.push(`/teacher/ability-map/${student.id}`);
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
.student-list {
  padding: 24px;
}
</style> 