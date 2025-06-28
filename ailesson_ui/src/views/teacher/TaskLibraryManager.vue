<template>
  <div>
    <div class="search-bar">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="任务名称">
          <el-input v-model="searchForm.name" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
              v-model="searchForm.createTimeRange"
              type="daterange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSearch">搜索</el-button>
          <el-button @click="onReset">重置</el-button>
          <el-button type="success" @click="onCreate">新建任务</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="task-types">
      <div
          v-for="type in taskTypes"
          :key="type.value"
          class="task-type-block"
      >
        <h3>{{ type.label }}</h3>
        <el-row :gutter="20">
          <el-col
              v-for="task in pagedTasks(type.value)"
              :key="task.id"
              :span="6"
          >
            <el-card class="task-card">
              <div class="task-card-content">
                <div class="task-name">{{ task.name }}</div>
                <div class="task-desc">{{ task.desc }}</div>
                <div class="task-actions">
                  <el-button @click="viewTask(task)">查看任务</el-button>
                  <el-button @click="editTask(task)">修改任务</el-button>
                  <el-button type="danger" @click="deleteTask(task)">删除任务</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-pagination
            v-if="filteredTasks(type.value).length > pageSize"
            :total="filteredTasks(type.value).length"
            :page-size="pageSize"
            :current-page="page[type.value]"
            @current-change="p => onPageChange(type.value, p)"
            layout="prev, pager, next"
            small
        />
      </div>
    </div>
    <el-pagination
        v-if="filteredTasks.length > pageSize"
        :total="filteredTasks.length"
        :page-size="pageSize"
        :current-page="page"
        @current-change="onPageChange"
        layout="prev, pager, next"
        small
    />
    <el-dialog v-model="createDialogVisible" title="新建任务" width="500px">
      <el-form :model="createForm" label-width="90px">
        <el-form-item label="任务名称">
          <el-input v-model="createForm.name" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务简介">
          <el-input
              v-model="createForm.desc"
              type="textarea"
              placeholder="请输入任务简介"
              :rows="3"
          />
        </el-form-item>
        <el-form-item label="任务类型">
          <el-select v-model="createForm.type" placeholder="请选择任务类型" style="width: 100%;">
            <el-option label="自主学习任务" value="self" />
            <el-option label="测验任务" value="quiz" />
            <el-option label="报告任务" value="report" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="createForm.type === 'self'">
          <el-button @click="addKnowledge">添加知识点</el-button>
        </el-form-item>
        <el-form-item v-if="createForm.type === 'quiz'">
          <el-button @click="addPaper">添加试卷</el-button>
        </el-form-item>
        <el-form-item v-if="createForm.type === 'report'">
          <el-button @click="addDoc">添加说明文档</el-button>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed,reactive  } from 'vue';

const searchForm = ref({
  name: '',
  createTimeRange: []
});

const allTasks = ref([
  { id: 1, name: '任务A', desc: '简介A', type: 'self', createTime: '2024-07-01' },
  { id: 2, name: '任务B', desc: '简介B', type: 'quiz', createTime: '2024-07-02' },
  { id: 3, name: '任务C', desc: '简介C', type: 'report', createTime: '2024-07-03' },
  // ...更多任务
]);
const createDialogVisible = ref(false);
const pageSize = 4;
const page = reactive({ self: 1, quiz: 1, report: 1 });
const createForm = reactive({
  name: '',
  desc: '',
  type: ''
});
const taskTypes = [
  { label: '自主学习任务', value: 'self' },
  { label: '测验任务', value: 'quiz' },
  { label: '报告任务', value: 'report' }
];
function filteredTasks(type) {
  return allTasks.value.filter(t =>
      t.type === type &&
      (!searchForm.value.name || t.name.includes(searchForm.value.name)) &&
      (!searchForm.value.createTimeRange.length ||
          (t.createTime >= searchForm.value.createTimeRange[0] &&
              t.createTime <= searchForm.value.createTimeRange[1]))
  );
}
function pagedTasks(type) {
  const tasks = filteredTasks(type);
  const start = (page[type] - 1) * pageSize;
  return tasks.slice(start, start + pageSize);
}
function onPageChange(type, p) {
  page[type] = p;
}
function onSearch(type, p) {
  page[type] = p;
}
function onReset() {
  page.self = 1;
  page.quiz = 1;
  page.report = 1;
}
function onCreate() {
  createDialogVisible.value = true;
  createForm.name = '';
  createForm.desc = '';
  createForm.type = '';
}

function handleCreate() {
  if (!createForm.name || !createForm.type) {
    alert('请填写任务名称和类型');
    return;
  }
  // 这里可以添加实际的任务保存逻辑
  createDialogVisible.value = false;
}

function addKnowledge() {
  // 打开知识点选择弹窗或逻辑
  alert('添加知识点功能');
}
function addPaper() {
  // 打开试卷选择弹窗或逻辑
  alert('添加试卷功能');
}
function addDoc() {
  // 打开说明文档上传或编辑逻辑
  alert('添加说明文档功能');
}
function viewTask(task) {
  // 查看任务详情
}
function editTask(task) {
  // 编辑任务
}
function deleteTask(task) {
  // 删除任务
}
</script>

<style scoped>
.search-bar { margin-bottom: 20px; }
.task-card { display: flex; align-items: center; justify-content: center; min-height: 180px; }
.task-card-content { width: 100%; display: flex; flex-direction: column; align-items: center; justify-content: center; }
.task-name { font-size: 18px; font-weight: bold; margin-bottom: 8px; }
.task-desc { font-size: 14px; color: #888; margin-bottom: 16px; }
.task-actions { display: flex; gap: 12px; justify-content: center; }
.task-type-block { margin-bottom: 32px; }

</style>