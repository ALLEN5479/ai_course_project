<template>
  <div class="course-detail">
    <el-header class="top-bar">
      <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      <span class="page-title">课程详情</span>
    </el-header>
    <el-card>
      <div slot="header">
        <span>课程详情</span>
      </div>
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="课程简介" name="intro">
          <div>这里是课程简介内容（可后续完善）</div>
        </el-tab-pane>
        <el-tab-pane label="任务管理" name="manage">
          <TaskManager :courseId="1" />
        </el-tab-pane>
        <el-tab-pane label="任务库管理" name="tasklib">
          <TaskLibraryManager />
        </el-tab-pane>
        <el-tab-pane label="AI出题" name="quiz">
          <div class="quiz-section">
            <div class="quiz-header">
              <h3>AI智能出题</h3>
              <p class="quiz-description">基于课程内容，AI自动生成练习题，帮助教师快速创建题库</p>
            </div>
            
            <div class="quiz-controls">
              <el-button type="primary" @click="showQuizGenerator = true" :disabled="showQuizGenerator">
                生成新题目
              </el-button>
              <el-button @click="showQuizGenerator = false" :disabled="!showQuizGenerator">
                查看题目列表
              </el-button>
            </div>
            
            <!-- 生成题目区域 -->
            <div v-if="showQuizGenerator" class="quiz-generator">
              <el-card class="generator-card">
                <template #header>
                  <h4>生成编程选择题</h4>
                </template>
                
                <el-form :model="quizForm" label-width="100px">
                  <el-form-item label="生成数量">
                    <el-input-number 
                      v-model="quizForm.count" 
                      :min="1" 
                      :max="10" 
                      controls-position="right"
                    />
                  </el-form-item>
                  
                  <el-form-item label="难度级别">
                    <el-select v-model="quizForm.difficulty" placeholder="选择难度">
                      <el-option label="简单" value="简单" />
                      <el-option label="中等" value="中等" />
                      <el-option label="困难" value="困难" />
                    </el-select>
                  </el-form-item>
                  
                  <el-form-item label="额外要求">
                    <el-input 
                      v-model="quizForm.requirement" 
                      type="textarea" 
                      :rows="3"
                      placeholder="例如：关于Java集合框架，包含泛型应用"
                    />
                  </el-form-item>
                  
                  <el-form-item>
                    <el-button 
                      type="primary" 
                      @click="generateQuizQuestions"
                      :loading="isGeneratingQuiz"
                    >
                      {{ isGeneratingQuiz ? '生成中...' : '生成题目' }}
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-card>
              
              <!-- 生成结果展示 -->
              <div v-if="generatedQuizQuestions.length > 0" class="quiz-results">
                <h4>生成的题目</h4>
                <div v-for="(question, index) in generatedQuizQuestions" :key="index" class="quiz-question-card">
                  <el-card class="question-card">
                    <template #header>
                      <div class="question-header">
                        <span class="question-number">题目 {{ index + 1 }}</span>
                        <el-tag :type="getDifficultyTagType(question.difficulty)">
                          {{ question.difficulty || '未知' }}
                        </el-tag>
                      </div>
                    </template>
                    
                    <div class="question-content">
                      <p class="question-text"><strong>问题：</strong>{{ question.question_text || '（无）' }}</p>
                      <div class="question-options">
                        <p class="option"><strong>A:</strong> {{ question.option_a || '（无）' }}</p>
                        <p class="option"><strong>B:</strong> {{ question.option_b || '（无）' }}</p>
                        <p class="option"><strong>C:</strong> {{ question.option_c || '（无）' }}</p>
                        <p class="option"><strong>D:</strong> {{ question.option_d || '（无）' }}</p>
                      </div>
                      <p class="question-answer"><strong>正确答案：</strong>{{ question.correct_answer || '（无）' }}</p>
                    </div>
                    
                    <template #footer>
                      <el-button 
                        type="success" 
                        size="small"
                        @click="saveQuizQuestion(question)"
                        :loading="isSavingQuiz"
                      >
                        保存到题库
                      </el-button>
                    </template>
                  </el-card>
                </div>
              </div>
            </div>
            
            <!-- 题目列表区域 -->
            <div v-else class="quiz-list">
              <el-card>
                <template #header>
                  <div class="list-header">
                    <h4>已保存的题目</h4>
                    <el-button type="primary" size="small" @click="loadQuizQuestions">
                      刷新列表
                    </el-button>
                  </div>
                </template>
                
                <div v-if="isLoadingQuizList" class="quiz-loading">
                  <el-skeleton :rows="3" animated />
                </div>
                
                <div v-else-if="quizQuestions.length === 0" class="quiz-empty">
                  <el-empty description="暂无保存的题目，请生成题目并保存" />
                </div>
                
                <div v-else class="quiz-questions">
                  <div v-for="question in quizQuestions" :key="question.id" class="quiz-question-item">
                    <el-card class="question-item-card">
                      <template #header>
                        <div class="item-header">
                          <span class="item-id">#{{ question.id || '（无）' }}</span>
                          <el-tag :type="getDifficultyTagType(question.difficulty)">
                            {{ question.difficulty || '（无）' }}
                          </el-tag>
                        </div>
                      </template>
                      
                      <div class="item-content">
                        <p class="item-text"><strong>问题：</strong>{{ question.question_text || '（无）' }}</p>
                        <div class="item-options">
                          <p class="option"><strong>A:</strong> {{ question.option_a || '（无）' }}</p>
                          <p class="option"><strong>B:</strong> {{ question.option_b || '（无）' }}</p>
                          <p class="option"><strong>C:</strong> {{ question.option_c || '（无）' }}</p>
                          <p class="option"><strong>D:</strong> {{ question.option_d || '（无）' }}</p>
                        </div>
                        <p class="item-answer"><strong>正确答案：</strong>{{ question.correct_answer || '（无）' }}</p>
                      </div>
                      
                      <template #footer>
                        <el-button 
                          type="danger" 
                          size="small"
                          @click="deleteQuizQuestion(question.id)"
                          :loading="isDeletingQuiz"
                        >
                          删除
                        </el-button>
                      </template>
                    </el-card>
                  </div>
                </div>
              </el-card>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="试卷管理" name="paper">
          <PaperManager ref="paperManagerRef" :courseId="1" />
        </el-tab-pane>
        <el-tab-pane label="构建知识图谱" name="knowledge-graph">
          <KnowledgeGraphBuilder :courseId="1" />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import axios from 'axios';
import TaskManager from './TaskManager.vue';
import TaskLibraryManager from './TaskLibraryManager.vue';
import KnowledgeGraphBuilder from './KnowledgeGraphBuilder.vue';
import PaperManager from './PaperManager.vue';

const activeTab = ref('manage'); // 默认显示任务管理tab
const router = useRouter();

// Quiz相关状态
const showQuizGenerator = ref(true);
const isGeneratingQuiz = ref(false);
const isSavingQuiz = ref(false);
const isLoadingQuizList = ref(false);
const isDeletingQuiz = ref(false);
const generatedQuizQuestions = ref<any[]>([]);
const quizQuestions = ref<any[]>([]);

// Quiz表单数据
const quizForm = ref({
  count: 3,
  difficulty: '中等',
  requirement: ''
});

// Quiz题目类型定义
interface QuizQuestion {
  id?: number;
  question_text: string;
  option_a: string;
  option_b: string;
  option_c: string;
  option_d: string;
  correct_answer: string;
  category?: string;
  difficulty: string;
  created_at?: string;
}

const paperManagerRef = ref();

function goBack() {
  router.back();
}

// Quiz相关方法
// 生成Quiz题目
const generateQuizQuestions = async () => {
  isGeneratingQuiz.value = true;
  try {
    const response = await axios.post('http://localhost:8080/api/quiz/generate', quizForm.value);
    generatedQuizQuestions.value = response.data.map((q: any) => ({
      ...q,
      category: q.category || '编程'
    }));
    ElMessage.success('题目生成成功！');
  } catch (error: any) {
    console.error('生成题目失败', error);
    ElMessage.error('生成题目失败，请检查网络连接');
  } finally {
    isGeneratingQuiz.value = false;
  }
};

// 保存Quiz题目
const saveQuizQuestion = async (question: QuizQuestion) => {
  isSavingQuiz.value = true;
  try {
    const { id, created_at, ...toSave } = question;
    await axios.post('http://localhost:8080/api/quiz', toSave);
    ElMessage.success('题目保存成功！');
    loadQuizQuestions(); // 保存后刷新列表
  } catch (error: any) {
    console.error('保存题目失败', error);
    ElMessage.error('保存题目失败，请重试');
  } finally {
    isSavingQuiz.value = false;
  }
};

// 加载Quiz题目列表
const loadQuizQuestions = async () => {
  isLoadingQuizList.value = true;
  try {
    const response = await axios.get('http://localhost:8080/api/quiz/all');
    quizQuestions.value = response.data;
  } catch (error: any) {
    console.error('加载题目失败', error);
    ElMessage.error('加载题目失败，请重试');
  } finally {
    isLoadingQuizList.value = false;
  }
};

// 删除Quiz题目
const deleteQuizQuestion = async (id?: number) => {
  if (!id) return;
  if (!confirm('确定要删除这道题目吗？')) return;

  isDeletingQuiz.value = true;
  try {
    await axios.delete(`http://localhost:8080/api/quiz/${id}`);
    ElMessage.success('题目删除成功！');
    loadQuizQuestions(); // 删除后刷新列表
  } catch (error: any) {
    console.error('删除题目失败', error);
    ElMessage.error('删除题目失败，请重试');
  } finally {
    isDeletingQuiz.value = false;
  }
};

// 获取难度标签类型
const getDifficultyTagType = (difficulty: string) => {
  const typeMap: Record<string, string> = {
    '简单': 'success',
    '中等': 'warning',
    '困难': 'danger'
  };
  return typeMap[difficulty] || 'info';
};

const handleTabClick = (tab: any) => {
  if (tab.paneName === 'paper') {
    paperManagerRef.value?.refresh && paperManagerRef.value.refresh();
  }
};

// 组件挂载时加载Quiz题目列表
onMounted(async () => {
  await loadQuizQuestions();
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
.course-detail {
  padding: 24px;
}

/* Quiz相关样式 */
.quiz-section {
  padding: 20px 0;
}

.quiz-header {
  margin-bottom: 20px;
}

.quiz-header h3 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.quiz-description {
  color: #666;
  margin: 10px 0;
  line-height: 1.6;
}

.quiz-controls {
  margin-bottom: 20px;
}

.quiz-generator {
  margin-bottom: 20px;
}

.generator-card {
  padding: 20px;
}

.generator-card h4 {
  margin-bottom: 20px;
}

.generator-card .el-form {
  margin-bottom: 20px;
}

.generator-card .el-form .el-form-item {
  margin-bottom: 10px;
}

.generator-card .el-form .el-form-item .el-input-number {
  width: 100%;
}

.generator-card .el-form .el-form-item .el-select {
  width: 100%;
}

.generator-card .el-form .el-form-item .el-input {
  width: 100%;
}

.generator-card .el-form .el-form-item .el-textarea {
  width: 100%;
}

.generator-card .el-form .el-form-item .el-button {
  width: 100%;
}

.quiz-results {
  margin-bottom: 20px;
}

.quiz-results h4 {
  margin-bottom: 10px;
}

.quiz-question-card {
  margin-bottom: 10px;
}

.question-card {
  padding: 10px;
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.question-header .question-number {
  font-weight: bold;
}

.question-content {
  margin-bottom: 10px;
}

.question-text {
  margin-bottom: 10px;
}

.question-options {
  margin-bottom: 10px;
}

.question-answer {
  margin-top: 10px;
}

.quiz-list {
  margin-bottom: 20px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.list-header h4 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.quiz-loading {
  text-align: center;
  padding: 40px 0;
}

.quiz-empty {
  text-align: center;
  padding: 40px 0;
  color: #999;
}

.quiz-questions {
  margin-bottom: 20px;
}

.quiz-question-item {
  margin-bottom: 10px;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-id {
  font-weight: bold;
}

.item-content {
  margin-bottom: 10px;
}

.item-text {
  margin-bottom: 10px;
}

.item-options {
  margin-bottom: 10px;
}

.item-answer {
  margin-top: 10px;
}
</style>