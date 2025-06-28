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
    <el-dialog v-model="editDialogVisible" title="修改任务" width="500px">
      <el-form :model="editForm" label-width="90px">
        <el-form-item label="任务名称">
          <el-input v-model="editForm.name" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务简介">
          <el-input
            v-model="editForm.desc"
            type="textarea"
            placeholder="请输入任务简介"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="任务类型">
          <el-select v-model="editForm.type" placeholder="请选择任务类型" style="width: 100%;">
            <el-option label="自主学习任务" value="self" />
            <el-option label="测验任务" value="quiz" />
            <el-option label="报告任务" value="report" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="editForm.startTime"
            type="date"
            placeholder="选择开始日期"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="editForm.endTime"
            type="date"
            placeholder="选择结束日期"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item v-if="editForm.type === 'self'">
          <el-button @click="editKnowledge">修改知识点</el-button>
        </el-form-item>
        <el-form-item v-if="editForm.type === 'quiz'">
          <el-button @click="editPaper">修改试卷</el-button>
        </el-form-item>
        <el-form-item v-if="editForm.type === 'report'">
          <el-button @click="editDoc">修改说明文档</el-button>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEdit">确定</el-button>
      </template>
    </el-dialog>
    <el-dialog v-model="viewDialogVisible" title="查看任务" width="600px">
      <el-form :model="viewForm" label-width="90px" disabled>
        <el-form-item label="任务名称">
          <el-input v-model="viewForm.name" />
        </el-form-item>
        <el-form-item label="任务简介">
          <el-input
            v-model="viewForm.desc"
            type="textarea"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="任务类型">
          <el-select v-model="viewForm.type" style="width: 100%;">
            <el-option label="自主学习任务" value="self" />
            <el-option label="测验任务" value="quiz" />
            <el-option label="报告任务" value="report" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="viewForm.startTime"
            type="date"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="viewForm.endTime"
            type="date"
            style="width: 100%;"
          />
        </el-form-item>
      </el-form>
      
      <!-- 任务资源展示区域 -->
      <div class="task-resources">
        <h4>任务资源</h4>
        <div v-if="viewForm.type === 'self'" class="resource-content">
          <h5>知识点</h5>
          <div class="knowledge-points">
            <el-tag v-for="point in viewForm.knowledgePoints" :key="point.id" class="knowledge-tag">
              {{ point.name }}
            </el-tag>
          </div>
        </div>
        <div v-if="viewForm.type === 'quiz'" class="resource-content">
          <h5>题目列表</h5>
          <div class="question-list">
            <div v-for="(question, index) in viewForm.questions" :key="question.id" class="question-item">
              <div class="question-header">
                <span class="question-number">{{ index + 1 }}.</span>
                <span class="question-type">{{ question.type === 'single' ? '单选题' : question.type === 'multiple' ? '多选题' : '简答题' }}</span>
              </div>
              <div class="question-content">{{ question.content }}</div>
              <div v-if="question.options" class="question-options">
                <div v-for="option in question.options" :key="option.key" class="option-item">
                  {{ option.key }}. {{ option.content }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-if="viewForm.type === 'report'" class="resource-content">
          <h5>说明文档</h5>
          <div class="document-content">
            <div class="document-info">
              <el-icon><Document /></el-icon>
              <span class="document-name">{{ viewForm.document?.name || '未上传文档' }}</span>
            </div>
            <div v-if="viewForm.document?.content" class="document-preview">
              {{ viewForm.document.content }}
            </div>
          </div>
        </div>
      </div>
      
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
    <el-dialog v-model="completionDialogVisible" title="查看任务完成情况" width="400px">
      <el-form :model="completionForm" label-width="90px">
        <el-form-item label="任务类型">
          <el-select v-model="completionForm.taskType" placeholder="请选择任务类型" style="width: 100%;" @change="onTaskTypeChange">
            <el-option label="自主学习任务" value="self" />
            <el-option label="测验任务" value="quiz" />
            <el-option label="报告任务" value="report" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务名称">
          <el-select v-model="completionForm.taskName" placeholder="请选择任务名称" style="width: 100%;" :disabled="!completionForm.taskType">
            <el-option
              v-for="task in filteredTaskOptions"
              :key="task.id"
              :label="task.name"
              :value="task.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="completionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleViewCompletion" :disabled="!completionForm.taskName">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
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
  viewDialogVisible.value = true;
  // 填充任务基础信息
  viewForm.id = task.id;
  viewForm.name = task.name;
  viewForm.desc = task.desc || '';
  viewForm.type = task.type;
  viewForm.startTime = task.startTime;
  viewForm.endTime = task.endTime;
  
  // 根据任务类型加载相关资源
  if (task.type === 'self') {
    // 模拟知识点数据
    viewForm.knowledgePoints = [
      { id: 1, name: '数据结构基础' },
      { id: 2, name: '算法复杂度分析' },
      { id: 3, name: '排序算法' }
    ];
  } else if (task.type === 'quiz') {
    // 模拟题目数据
    viewForm.questions = [
      {
        id: 1,
        type: 'single',
        content: '以下哪种数据结构是线性的？',
        options: [
          { key: 'A', content: '树' },
          { key: 'B', content: '图' },
          { key: 'C', content: '数组' },
          { key: 'D', content: '堆' }
        ]
      },
      {
        id: 2,
        type: 'multiple',
        content: '以下哪些是排序算法？',
        options: [
          { key: 'A', content: '冒泡排序' },
          { key: 'B', content: '快速排序' },
          { key: 'C', content: '深度优先搜索' },
          { key: 'D', content: '归并排序' }
        ]
      }
    ];
  } else if (task.type === 'report') {
    // 模拟文档数据
    viewForm.document = {
      name: '项目报告要求.pdf',
      content: '请根据所学知识，完成一个关于数据结构的项目报告。报告应包含：\n1. 项目背景介绍\n2. 技术方案设计\n3. 实现过程\n4. 结果分析\n5. 总结与反思'
    };
  }
}
const editDialogVisible = ref(false);
const editForm = reactive({
  id: '',
  name: '',
  desc: '',
  type: '',
  startTime: '',
  endTime: ''
});
function editTask(task: any) {
  editDialogVisible.value = true;
  // 填充现有任务信息
  editForm.id = task.id;
  editForm.name = task.name;
  editForm.desc = task.desc || '';
  editForm.type = task.type;
  editForm.startTime = task.startTime;
  editForm.endTime = task.endTime;
}
function handleEdit() {
  if (!editForm.name || !editForm.type) {
    alert('请填写任务名称和类型');
    return;
  }
  // 这里可以添加实际的任务修改逻辑
  editDialogVisible.value = false;
}
function editKnowledge() {
  // 修改知识点功能
  alert('修改知识点功能');
}
function editPaper() {
  // 修改试卷功能
  alert('修改试卷功能');
}
function editDoc() {
  // 修改说明文档功能
  alert('修改说明文档功能');
}

// 切换班级时重置分页
watch(activeClassId, () => {
  page.value = { self: 1, quiz: 1, report: 1 };
});

const viewDialogVisible = ref(false);
const viewForm = reactive({
  id: '',
  name: '',
  desc: '',
  type: '',
  startTime: '',
  endTime: '',
  knowledgePoints: [],
  questions: [],
  document: null
});

const completionDialogVisible = ref(false);
const completionForm = reactive({
  taskType: '',
  taskName: ''
});

const filteredTaskOptions = computed(() => {
  if (!completionForm.taskType) return [];
  return allTasks.value.filter(task => task.type === completionForm.taskType);
});

function onViewCompletion() {
  completionDialogVisible.value = true;
  completionForm.taskType = '';
  completionForm.taskName = '';
}

function onTaskTypeChange() {
  completionForm.taskName = '';
}

function handleViewCompletion() {
  if (!completionForm.taskType || !completionForm.taskName) {
    alert('请选择任务类型和任务名称');
    return;
  }
  // 跳转到任务完成情况页面
  router.push({
    name: 'class_mission_student',
    query: {
      taskType: completionForm.taskType,
      taskId: completionForm.taskName
    }
  });
  completionDialogVisible.value = false;
}
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