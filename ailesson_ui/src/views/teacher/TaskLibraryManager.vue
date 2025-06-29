<template>
    <div class="task-library-manager">
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="任务名称">
            <el-input v-model="searchForm.name" placeholder="请输入任务名称" clearable />
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="searchForm.createTimeRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSearch">搜索</el-button>
            <el-button @click="onReset">重置</el-button>
            <el-button type="success" @click="onCreate">新建任务</el-button>
          </el-form-item>
        </el-form>
      </div>
  
      <!-- 任务类型块 -->
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
              :key="task.missionId"
              :span="6"
            >
              <el-card class="task-card">
                <el-button class="delete-btn" size="small" @click.stop="deleteTask(task)">×</el-button>
                <div class="task-card-content">
                  <div class="task-name">{{ task.missionName }}</div>
                  <div class="task-desc">{{ task.missionDescription }}</div>
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
            :current-page="getCurrentPage(type.value)"
            @current-change="(p: number) => onPageChange(type.value, p)"
            layout="prev, pager, next"
            small
          />
        </div>
      </div>
  
      <el-dialog v-model="createDialogVisible" title="新建任务" width="500px" :close-on-click-modal="false" :close-on-press-escape="true">
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
          <el-button @click="cancelCreate">取消</el-button>
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
  import { ref, computed, reactive, onMounted } from 'vue';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { taskTemplateApi } from '@/api/taskApi';
  
  interface TaskTemplate {
    missionId: number;
    missionName: string;
    missionDescription?: string;
    content: string;
    createTime?: string;
    teacherId: number;
    missionType: string;
    status: number;
    knowledgePoints?: Array<{id: number, name: string}>;
    questions?: Array<{
      id: number;
      type: string;
      content: string;
      options?: Array<{key: string, content: string}>;
    }>;
    document?: {name: string, content: string} | null;
  }
  
  const searchForm = ref({
    name: '',
    createTimeRange: [] as string[]
  });
  
  const allTasks = ref<TaskTemplate[]>([]);
  const loading = ref(false);
  
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
    knowledgePoints: [] as Array<{id: number, name: string}>,
    questions: [] as Array<{
      id: number;
      type: string;
      content: string;
      options?: Array<{key: string, content: string}>;
    }>,
    document: null as {name: string, content: string} | null
  });
  
  // 获取任务模板列表
  async function fetchTaskTemplates() {
    try {
      loading.value = true;
      console.log('开始获取任务模板数据...');
      const response = await taskTemplateApi.getTaskTemplates();
      console.log('API响应:', response);
      
      // 后端返回格式: { success: true, data: [...], message: "..." }
      if (response.success && response.data && Array.isArray(response.data)) {
        allTasks.value = response.data;
        console.log('成功设置任务数据，总数:', allTasks.value.length);
        // 输出每个任务的信息用于调试
        allTasks.value.forEach((task, index) => {
          console.log(`任务${index + 1}:`, {
            id: task.missionId,
            name: task.missionName,
            type: task.missionType,
            description: task.missionDescription
          });
        });
      } else {
        console.warn('API返回的数据格式不正确:', response);
        allTasks.value = [];
      }
    } catch (error) {
      console.error('获取任务模板失败:', error);
      ElMessage.error('获取任务模板失败');
      allTasks.value = [];
    } finally {
      loading.value = false;
    }
  }
  
  function filteredTasks(type: string) {
    const filtered = allTasks.value.filter(t =>
        t.missionType === type &&
        (!searchForm.value.name || t.missionName.includes(searchForm.value.name)) &&
        (!searchForm.value.createTimeRange.length ||
            (t.createTime && t.createTime >= searchForm.value.createTimeRange[0] &&
                t.createTime <= searchForm.value.createTimeRange[1]))
    );
    console.log(`过滤后的${type}类型任务数量:`, filtered.length);
    return filtered;
  }
  
  function pagedTasks(type: string) {
    const tasks = filteredTasks(type);
    const start = (page[type as keyof typeof page] - 1) * pageSize;
    const paged = tasks.slice(start, start + pageSize);
    console.log(`${type}类型分页数据:`, paged);
    return paged;
  }
  
  function getCurrentPage(type: string): number {
    return page[type as keyof typeof page] || 1;
  }
  
  function onPageChange(type: string, p: number) {
    page[type as keyof typeof page] = p;
  }
  
  function onSearch() {
    // 重置分页
    page.self = 1;
    page.quiz = 1;
    page.report = 1;
  }
  
  function onReset() {
    searchForm.value = { name: '', createTimeRange: [] };
    page.self = 1;
    page.quiz = 1;
    page.report = 1;
  }
  
  function onCreate() {
    console.log('点击新建任务按钮');
    console.log('createDialogVisible当前值:', createDialogVisible.value);
    createDialogVisible.value = true;
    console.log('createDialogVisible设置后值:', createDialogVisible.value);
    createForm.name = '';
    createForm.desc = '';
    createForm.type = '';
  }
  
  function cancelCreate() {
    console.log('取消创建任务');
    console.log('createDialogVisible当前值:', createDialogVisible.value);
    createDialogVisible.value = false;
    console.log('createDialogVisible设置后值:', createDialogVisible.value);
    createForm.name = '';
    createForm.desc = '';
    createForm.type = '';
  }
  
  async function handleCreate() {
    console.log('开始创建任务');
    console.log('createForm数据:', createForm);
    
    if (!createForm.name || !createForm.type) {
      ElMessage.warning('请填写任务名称和类型');
      return;
    }
    
    try {
      const requestData = {
        missionName: createForm.name,
        missionDescription: createForm.desc,
        missionType: createForm.type,
        content: createForm.desc, // 暂时用描述作为内容
        teacherId: 1 // 临时设置，实际应该从用户信息中获取
      };
      
      console.log('发送创建请求数据:', requestData);
      const response = await taskTemplateApi.createTaskTemplate(requestData);
      console.log('创建任务响应:', response);
      
      if (response.success) {
        ElMessage.success('任务模板创建成功');
        console.log('createDialogVisible当前值:', createDialogVisible.value);
        createDialogVisible.value = false;
        console.log('createDialogVisible设置后值:', createDialogVisible.value);
        
        // 重置表单
        createForm.name = '';
        createForm.desc = '';
        createForm.type = '';
        
        // 重新加载数据
        await fetchTaskTemplates();
      } else {
        ElMessage.error(response.message || '创建任务模板失败');
      }
    } catch (error) {
      console.error('创建任务模板失败:', error);
      ElMessage.error('创建任务模板失败');
    }
  }
  
  function addKnowledge() {
    // 打开知识点选择弹窗或逻辑
    ElMessage.info('添加知识点功能');
  }
  
  function addPaper() {
    // 打开试卷选择弹窗或逻辑
    ElMessage.info('添加试卷功能');
  }
  
  function addDoc() {
    // 打开说明文档上传或编辑逻辑
    ElMessage.info('添加说明文档功能');
  }
  
  function editTask(task: TaskTemplate) {
    editDialogVisible.value = true;
    // 填充现有任务信息
    editForm.id = task.missionId.toString();
    editForm.name = task.missionName;
    editForm.desc = task.missionDescription || '';
    editForm.type = task.missionType;
  }
  
  async function handleEdit() {
    if (!editForm.name || !editForm.type) {
      ElMessage.warning('请填写任务名称和类型');
      return;
    }
    
    try {
      const response = await taskTemplateApi.updateTaskTemplate(parseInt(editForm.id), {
        missionName: editForm.name,
        missionDescription: editForm.desc,
        missionType: editForm.type,
        content: editForm.desc
      });
      
      if (response.success) {
        ElMessage.success('任务模板更新成功');
        editDialogVisible.value = false;
        fetchTaskTemplates(); // 重新加载数据
      } else {
        ElMessage.error(response.message || '更新任务模板失败');
      }
    } catch (error) {
      console.error('更新任务模板失败:', error);
      ElMessage.error('更新任务模板失败');
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
  
  async function viewTask(task: TaskTemplate) {
    try {
      const response = await taskTemplateApi.getTaskTemplateDetail(task.missionId);
      
      if (response.success && response.data) {
        const taskDetail = response.data;
        
        viewDialogVisible.value = true;
        // 填充任务基础信息
        viewForm.id = taskDetail.missionId.toString();
        viewForm.name = taskDetail.missionName;
        viewForm.desc = taskDetail.missionDescription || '';
        viewForm.type = taskDetail.missionType;
        
        // 根据任务类型加载相关资源
        if (taskDetail.missionType === 'self') {
          // 加载知识点数据
          viewForm.knowledgePoints = taskDetail.knowledgePoints || [];
        } else if (taskDetail.missionType === 'quiz') {
          // 加载题目数据
          viewForm.questions = taskDetail.questions || [];
        } else if (taskDetail.missionType === 'report') {
          // 加载文档数据
          viewForm.document = taskDetail.document || null;
        }
      } else {
        ElMessage.error(response.message || '获取任务详情失败');
      }
    } catch (error) {
      console.error('获取任务详情失败:', error);
      ElMessage.error('获取任务详情失败');
    }
  }
  
  async function deleteTask(task: TaskTemplate) {
    try {
      await ElMessageBox.confirm(
        `确定要删除任务"${task.missionName}"吗？`,
        '确认删除',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      );
      
      const response = await taskTemplateApi.deleteTaskTemplate(task.missionId);
      
      if (response.success) {
        ElMessage.success('任务模板删除成功');
        fetchTaskTemplates(); // 重新加载数据
      } else {
        ElMessage.error(response.message || '删除任务模板失败');
      }
    } catch (error) {
      if (error !== 'cancel') {
        console.error('删除任务模板失败:', error);
        ElMessage.error('删除任务模板失败');
      }
    }
  }
  
  // 生命周期
  onMounted(() => {
    fetchTaskTemplates();
  });
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