<template>
  <div>
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
            :current-page="page[type.value as keyof typeof page]"
            @current-change="(p: number) => onPageChange(type.value, p)"
            layout="prev, pager, next"
            small
        />
      </div>
    </div>
    <el-dialog v-model="createDialogVisible" title="发布任务" width="500px">
      <el-form :model="createForm" label-width="90px">
        <el-form-item label="任务类型">
          <el-select v-model="createForm.selectedTaskType" placeholder="请选择任务类型" style="width: 100%;" clearable @change="onPublishTaskTypeChange">
            <el-option label="自主学习任务" value="self" />
            <el-option label="测验任务" value="quiz" />
            <el-option label="报告任务" value="report" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择任务">
          <div style="margin-bottom: 8px; color: #666; font-size: 12px;">
            {{ taskTypeFilterHint }}
          </div>
          <el-select v-model="createForm.selectedTask" placeholder="请选择任务" style="width: 100%;" :disabled="!createForm.selectedTaskType">
            <el-option
              v-for="item in filteredTaskNameOptions"
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
import { ref, computed, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { publishedMissionApi, taskTemplateApi } from '@/api/taskApi';

const props = defineProps<{ courseId: number | string }>();
const router = useRouter();

interface PublishedMission {
  id: number;
  name: string;
  description?: string;
  type: string;
  startTime: string;
  endTime: string;
  templateId: number;
}

interface TaskTemplate {
  missionId: number;
  missionName: string;
  missionDescription?: string;
  missionType: string;
  content: string;
  createTime?: string;
  teacherId: number;
  status: number;
}

const createDialogVisible = ref(false);
const createForm = reactive({
  selectedTask: '',
  selectedTaskType: '',
  startTime: '',
  endTime: ''
});

const allTasks = ref<PublishedMission[]>([]);
const taskTemplates = ref<TaskTemplate[]>([]);
const loading = ref(false);

const filteredTaskNameOptions = computed(() => {
  let filtered = taskTemplates.value;
  
  if (createForm.selectedTaskType) {
    filtered = filtered.filter(task => task.missionType === createForm.selectedTaskType);
  }
  
  console.log(`筛选后的任务模板数量: ${filtered.length}`);
  
  return filtered.map(task => ({ 
    label: `${task.missionName} (${getTaskTypeLabel(task.missionType)})`, 
    value: task.missionId.toString() 
  }));
});

const getTaskTypeLabel = (type: string) => {
  const typeMap: Record<string, string> = {
    'self': '自主学习',
    'quiz': '测验',
    'report': '报告'
  };
  return typeMap[type] || type;
};

// 显示筛选提示
const taskTypeFilterHint = computed(() => {
  if (!createForm.selectedTaskType) {
    return `请先选择任务类型，当前共有 ${taskTemplates.value.length} 个任务模板`;
  }
  const filteredCount = taskTemplates.value.filter(task => task.missionType === createForm.selectedTaskType).length;
  return `已筛选 ${getTaskTypeLabel(createForm.selectedTaskType)} 类型任务，共 ${filteredCount} 个`;
});

// 搜索表单
const searchForm = ref({
  name: '',
  dateRange: [] as string[]
});

// 任务类型
const taskTypes = [
  { label: '自主学习任务', value: 'self' },
  { label: '测验任务', value: 'quiz' },
  { label: '报告任务', value: 'report' }
];

// 分页
const pageSize = 4;
const page = ref({
  self: 1,
  quiz: 1,
  report: 1
});

// 获取已发布任务列表
const fetchPublishedMissions = async () => {
  try {
    loading.value = true;
    const response = await publishedMissionApi.getPublishedMissions();
    console.log('已发布任务API响应:', response);
    
    // 后端返回格式: { success: true, data: [...], message: "..." }
    if (response.success && response.data) {
      console.log('任务模板数据:', taskTemplates.value);
      console.log('原始已发布任务数据:', response.data);
      
      // 将后端数据转换为前端期望的格式
      allTasks.value = response.data.map((publishedMission: any) => {
        // 根据missionId查找对应的任务模板
        const template = taskTemplates.value.find(t => t.missionId === publishedMission.missionId);
        console.log(`查找任务模板 missionId=${publishedMission.missionId}:`, template);
        
        const convertedTask = {
          id: publishedMission.id,
          name: template ? template.missionName : `任务${publishedMission.missionId}`,
          description: template ? template.missionDescription : '',
          type: template ? template.missionType : 'unknown',
          startTime: publishedMission.startTime,
          endTime: publishedMission.endTime,
          templateId: publishedMission.missionId,
          status: publishedMission.status,
          createTime: publishedMission.createTime
        };
        
        console.log(`转换后的任务:`, convertedTask);
        return convertedTask;
      });
      console.log('最终转换后的任务数据:', allTasks.value);
    } else {
      allTasks.value = [];
    }
  } catch (error) {
    console.error('获取已发布任务失败:', error);
    ElMessage.error('获取已发布任务失败');
    allTasks.value = [];
  } finally {
    loading.value = false;
  }
};

// 获取任务模板列表
const fetchTaskTemplates = async () => {
  try {
    console.log('开始获取任务模板数据...');
    const response = await taskTemplateApi.getTaskTemplates();
    console.log('任务模板API响应:', response);
    
    // 后端返回格式: { success: true, data: [...], message: "..." }
    if (response.success && response.data) {
      taskTemplates.value = response.data;
      console.log('成功设置任务模板数据，总数:', taskTemplates.value.length);
      // 输出每个任务模板的信息用于调试
      taskTemplates.value.forEach((task, index) => {
        console.log(`任务模板${index + 1}:`, {
          id: task.missionId,
          name: task.missionName,
          type: task.missionType,
          description: task.missionDescription
        });
      });
    } else {
      console.warn('任务模板API返回的数据格式不正确:', response);
      taskTemplates.value = [];
    }
  } catch (error) {
    console.error('获取任务模板失败:', error);
    ElMessage.error('获取任务模板失败');
    taskTemplates.value = [];
  }
};

// 过滤任务
function filteredTasks(type: string) {
  return allTasks.value.filter(
      t =>
          t.type === type &&
          (!searchForm.value.name || t.name.includes(searchForm.value.name)) &&
          (!searchForm.value.dateRange.length ||
              (t.startTime >= searchForm.value.dateRange[0] &&
                  t.endTime <= searchForm.value.dateRange[1]))
  );
}

function pagedTasks(type: string) {
  const tasks = filteredTasks(type);
  const start = (page.value[type as keyof typeof page.value] - 1) * pageSize;
  return tasks.slice(start, start + pageSize);
}

function onPageChange(type: string, p: number) {
  page.value[type as keyof typeof page.value] = p;
}

// 搜索/重置/新建
function onSearch() {
  // 重置分页
  page.value.self = 1;
  page.value.quiz = 1;
  page.value.report = 1;
}

function onReset() {
  searchForm.value = { name: '', dateRange: [] };
  page.value.self = 1;
  page.value.quiz = 1;
  page.value.report = 1;
}

function onCreate() {
  createDialogVisible.value = true;
  createForm.selectedTask = '';
  createForm.selectedTaskType = '';
  createForm.startTime = '';
  createForm.endTime = '';
}

// 格式化日期为ISO格式
function formatDateToISO(date: Date | string, isEndTime: boolean = false): string {
  if (typeof date === 'string') {
    // 如果是字符串，直接添加时间部分
    return isEndTime ? date + 'T23:59:59' : date + 'T00:00:00';
  } else if (date instanceof Date) {
    // 如果是Date对象，转换为ISO格式
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const time = isEndTime ? '23:59:59' : '00:00:00';
    return `${year}-${month}-${day}T${time}`;
  }
  return '';
}

// 任务类型变化处理
function onPublishTaskTypeChange() {
  // 当任务类型改变时，清空已选择的任务
  createForm.selectedTask = '';
}

async function handleCreate() {
  if (!createForm.selectedTaskType) {
    ElMessage.warning('请选择任务类型');
    return;
  }
  if (!createForm.selectedTask) {
    ElMessage.warning('请选择任务');
    return;
  }
  if (!createForm.startTime || !createForm.endTime) {
    ElMessage.warning('请选择起止时间');
    return;
  }
  
  try {
    const selectedTemplate = taskTemplates.value.find(t => t.missionId.toString() === createForm.selectedTask);
    if (!selectedTemplate) {
      ElMessage.error('选择的任务模板不存在');
      return;
    }
    
    console.log('选中的任务模板:', selectedTemplate);
    console.log('表单数据:', createForm);
    
    const requestData = {
      missionId: selectedTemplate.missionId,
      startTime: formatDateToISO(createForm.startTime),
      endTime: formatDateToISO(createForm.endTime, true)
    };
    
    console.log('发送发布任务请求数据:', requestData);
    
    const response = await publishedMissionApi.publishMission(requestData);
    
    console.log('发布任务响应:', response);
    
    if (response.success) {
    ElMessage.success('任务发布成功');
    createDialogVisible.value = false;
    fetchPublishedMissions(); // 重新加载数据
    } else {
      ElMessage.error(response.message || '发布任务失败');
    }
  } catch (error) {
    console.error('发布任务失败:', error);
    if (error && typeof error === 'object' && 'response' in error) {
      console.error('错误详情:', (error as any).response?.data);
      console.error('请求URL:', (error as any).config?.url);
      console.error('请求方法:', (error as any).config?.method);
      console.error('请求头:', (error as any).config?.headers);
      console.error('请求体:', (error as any).config?.data);
    }
    ElMessage.error('发布任务失败');
  }
}

async function viewTask(task: PublishedMission) {
  try {
    console.log('查看任务详情，任务ID:', task.id, '任务模板ID:', task.templateId);
    
    // 从任务模板中获取详细信息
    const template = taskTemplates.value.find(t => t.missionId === task.templateId);
    console.log('找到的任务模板:', template);
    
    if (!template) {
      ElMessage.error('未找到对应的任务模板');
      return;
    }
    
    viewDialogVisible.value = true;
    
    // 填充任务基础信息
    viewForm.id = task.id.toString();
    viewForm.name = template.missionName;
    viewForm.desc = template.missionDescription || '';
    viewForm.type = template.missionType;
    viewForm.startTime = task.startTime;
    viewForm.endTime = task.endTime;
    
    console.log('填充的查看表单数据:', {
      id: viewForm.id,
      name: viewForm.name,
      desc: viewForm.desc,
      type: viewForm.type,
      startTime: viewForm.startTime,
      endTime: viewForm.endTime
    });
    
    // 根据任务类型加载相关资源
    if (template.missionType === 'self') {
      // 加载知识点数据 - 这里需要调用相应的API
      viewForm.knowledgePoints = [];
      console.log('自主学习任务，知识点待加载');
    } else if (template.missionType === 'quiz') {
      // 加载题目数据 - 这里需要调用相应的API
      viewForm.questions = [];
      console.log('测验任务，题目待加载');
    } else if (template.missionType === 'report') {
      // 加载文档数据 - 这里需要调用相应的API
      viewForm.document = null;
      console.log('报告任务，文档待加载');
    }
  } catch (error) {
    console.error('获取任务详情失败:', error);
    ElMessage.error('获取任务详情失败');
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

function editTask(task: PublishedMission) {
  console.log('编辑任务，任务ID:', task.id, '任务模板ID:', task.templateId);
  
  // 从任务模板中获取详细信息
  const template = taskTemplates.value.find(t => t.missionId === task.templateId);
  console.log('找到的任务模板:', template);
  
  if (!template) {
    ElMessage.error('未找到对应的任务模板');
    return;
  }
  
  editDialogVisible.value = true;
  
  // 填充现有任务信息
  editForm.id = task.id.toString();
  editForm.name = template.missionName;
  editForm.desc = template.missionDescription || '';
  editForm.type = template.missionType;
  editForm.startTime = task.startTime;
  editForm.endTime = task.endTime;
  
  console.log('填充的编辑表单数据:', {
    id: editForm.id,
    name: editForm.name,
    desc: editForm.desc,
    type: editForm.type,
    startTime: editForm.startTime,
    endTime: editForm.endTime
  });
}

async function handleEdit() {
  if (!editForm.name || !editForm.type) {
    ElMessage.warning('请填写任务名称和类型');
    return;
  }
  
  try {
    const response = await publishedMissionApi.updatePublishedMission(parseInt(editForm.id), {
      name: editForm.name,
      description: editForm.desc,
      type: editForm.type,
      startTime: editForm.startTime,
      endTime: editForm.endTime
    });
    
    if (response.success) {
    ElMessage.success('任务更新成功');
    editDialogVisible.value = false;
    fetchPublishedMissions(); // 重新加载数据
    } else {
      ElMessage.error(response.message || '更新任务失败');
    }
  } catch (error) {
    console.error('更新任务失败:', error);
    ElMessage.error('更新任务失败');
  }
}

function editKnowledge() {
  // 修改知识点功能
  ElMessage.info('修改知识点功能');
}

function editPaper() {
  // 修改试卷功能
  ElMessage.info('修改试卷功能');
}

function editDoc() {
  // 修改说明文档功能
  ElMessage.info('修改说明文档功能');
}

const viewDialogVisible = ref(false);
const viewForm = reactive({
  id: '',
  name: '',
  desc: '',
  type: '',
  startTime: '',
  endTime: '',
  knowledgePoints: [] as Array<{id: number, name: string}>,
  questions: [] as Array<{
    id: number;
    type: string;
    content: string;
    options?: Array<{key: string, content: string}>;
  }>,
  document: null as {name: string, content: string} | null
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
    ElMessage.warning('请选择任务类型和任务名称');
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

// 生命周期
onMounted(async () => {
  // 先加载任务模板，再加载已发布任务
  await fetchTaskTemplates();
  await fetchPublishedMissions();
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