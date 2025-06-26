<template>
  <div>
    <el-tabs v-model="activeClassId" type="card">
      <el-tab-pane
          v-for="clazz in classList"
          :key="clazz.id"
          :label="clazz.name"
          :name="clazz.id"
      >
        <div class="search-bar">
          <el-form :inline="true" :model="searchForm">
            <el-form-item label="任务名称">
              <el-input v-model="searchForm.name" placeholder="请输入任务名称" />
            </el-form-item>
            <el-form-item label="起止时间">
              <el-date-picker
                  v-model="searchForm.dateRange"
                  type="daterange"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSearch">搜索</el-button>
              <el-button @click="onReset">重置</el-button>
              <el-button type="success" @click="onCreate">发布任务</el-button>
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
                    <div class="task-time">{{ task.startTime }} ~ {{ task.endTime }}</div>
                    <div class="task-actions">
                      <el-button @click="viewTask(task)">查看任务</el-button>
                      <el-button @click="editTask(task)">修改任务</el-button>
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
      </el-tab-pane>
    </el-tabs>
  <el-dialog v-model="createDialogVisible" title="发布新任务" width="400px">
    <el-form :model="createForm" label-width="90px">
      <el-form-item label="选择任务">
        <el-select v-model="createForm.selectedTask" placeholder="请选择任务" style="width: 100%;">
          <el-option
            v-for="item in taskNameOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="createForm.startTime"
            type="date"
            placeholder="选择开始日期"
            style="width: 100%;"
          />
      </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="createForm.endTime"
            type="date"
            placeholder="选择结束日期"
            style="width: 100%;"
          />
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
import { ref, computed, watch } from 'vue';
import { reactive } from 'vue';
const props = defineProps<{ courseId: number | string }>();
const createDialogVisible = ref(false);
const createForm = reactive({
  selectedTask: '',
  startTime: '',
  endTime: ''
});
const taskNameOptions = computed(() =>
  allTasks.value.map(task => ({ label: task.name, value: task.id }))
);
const availableTasks = [
  { label: '自主学习任务', value: 'self' },
  { label: '测验任务', value: 'quiz' },
  { label: '报告任务', value: 'report' }
];

// 班级列表（实际应通过API获取）
const classList = ref([
  { id: '1', name: '一班' },
  { id: '2', name: '二班' }
]);
const activeClassId = ref(classList.value[0]?.id || '');

// 搜索表单
const searchForm = ref({
  name: '',
  dateRange: []
});

// 任务类型
const taskTypes = [
  { label: '自主学习任务', value: 'self' },
  { label: '测验任务', value: 'quiz' },
  { label: '报告任务', value: 'report' }
];

// 假数据：任务列表（实际应通过API获取）
const allTasks = ref([
  { id: 1, classId: '1', name: '自主学习1', type: 'self', startTime: '2024-01-01', endTime: '2024-01-10' },
  { id: 2, classId: '1', name: '测验1', type: 'quiz', startTime: '2024-01-05', endTime: '2024-01-12' },
  { id: 3, classId: '1', name: '报告1', type: 'report', startTime: '2024-01-08', endTime: '2024-01-15' },
  { id: 4, classId: '2', name: '自主学习2', type: 'self', startTime: '2024-01-02', endTime: '2024-01-11' },
  { id: 5, classId: '2', name: '测验2', type: 'quiz', startTime: '2024-01-06', endTime: '2024-01-13' },
  { id: 6, classId: '2', name: '报告2', type: 'report', startTime: '2024-01-09', endTime: '2024-01-16' }
]);
// 分页
const pageSize = 4;
const page = ref({
  self: 1,
  quiz: 1,
  report: 1
});

// 过滤任务
function filteredTasks(type: string) {
  return allTasks.value.filter(
      t =>
          t.classId === activeClassId.value &&
          t.type === type &&
          (!searchForm.value.name || t.name.includes(searchForm.value.name)) &&
          (!searchForm.value.dateRange.length ||
              (t.startTime >= searchForm.value.dateRange[0] &&
                  t.endTime <= searchForm.value.dateRange[1]))
  );
}
function pagedTasks(type: string) {
  const tasks = filteredTasks(type);
  const start = (page.value[type] - 1) * pageSize;
  return tasks.slice(start, start + pageSize);
}
function onPageChange(type: string, p: number) {
  page.value[type] = p;
}

// 搜索/重置/新建
function onSearch() {
  // 触发过滤
}
function onReset() {
  searchForm.value = { name: '', dateRange: [] };
}
function onCreate() {
  createDialogVisible.value = true;
  createForm.selectedTask = '';
  createForm.startTime = '';
  createForm.endTime = '';
}

function handleCreate() {
  if (!createForm.selectedTask) {
    alert('请选择任务');
    return;
  }
  if (!createForm.startTime || !createForm.endTime) {
    alert('请选择起止时间');
    return;
  }
  createDialogVisible.value = false;
  // 这里可以添加实际的任务发布逻辑
}

function viewTask(task: any) {
  // 跳转或弹窗
}
function editTask(task: any) {
  // 跳转或弹窗
}

// 切换班级时重置分页
watch(activeClassId, () => {
  page.value = { self: 1, quiz: 1, report: 1 };
});
</script>

<style scoped>
.search-bar { margin-bottom: 20px; }
.task-type-block { margin-bottom: 32px; }
.task-card {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 180px;
}
.task-card-content {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.task-name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 8px;
}
.task-time {
  font-size: 14px;
  color: #888;
  margin-bottom: 16px;
}
.task-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}
</style>