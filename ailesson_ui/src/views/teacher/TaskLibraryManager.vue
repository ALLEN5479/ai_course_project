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
              <el-button class="delete-btn" size="small" @click.stop="deleteTask(task)">×</el-button>
              <div class="task-card-content">
                <div class="task-name">{{ task.name }}</div>
                <div class="task-desc">{{ task.desc }}</div>
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
const editDialogVisible = ref(false);
const viewDialogVisible = ref(false);
const editForm = reactive({
  id: '',
  name: '',
  desc: '',
  type: ''
});
const viewForm = reactive({
  id: '',
  name: '',
  desc: '',
  type: '',
  knowledgePoints: [],
  questions: [],
  document: null
});
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
function editTask(task) {
  editDialogVisible.value = true;
  // 填充现有任务信息
  editForm.id = task.id;
  editForm.name = task.name;
  editForm.desc = task.desc || '';
  editForm.type = task.type;
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
function viewTask(task) {
  viewDialogVisible.value = true;
  // 填充任务基础信息
  viewForm.id = task.id;
  viewForm.name = task.name;
  viewForm.desc = task.desc || '';
  viewForm.type = task.type;
  
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
function deleteTask(task) {
  // 删除任务
}
</script>

<style scoped>
.search-bar { margin-bottom: 20px; }
.task-card {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 180px;
}
.task-card-content { width: 100%; display: flex; flex-direction: column; align-items: center; justify-content: center; }
.task-name { font-size: 18px; font-weight: bold; margin-bottom: 8px; }
.task-desc { font-size: 14px; color: #888; margin-bottom: 16px; }
.task-actions { display: flex; gap: 12px; justify-content: center; }
.task-type-block { margin-bottom: 32px; }
.delete-btn {
  position: absolute;
  top: 8px;
  right: 8px;
  z-index: 2;
  background: #fff;
  border: 1px solid #dcdfe6;
  color: #000;
  box-shadow: none;
  border-radius: 4px;
  padding: 0;
  width: 28px;
  height: 28px;
  min-width: 28px;
}
.delete-btn:hover {
  background: #f5f7fa;
  border-color: #409EFF;
}
</style>