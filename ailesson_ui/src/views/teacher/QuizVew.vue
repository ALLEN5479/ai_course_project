<template>
  <div class="ai-quiz-app">
    <!-- 导航栏 -->
    <nav class="navbar">
      <div class="container">
        <h1 class="app-title">AI编程题目生成器</h1>
        <div class="nav-buttons">
          <button @click="showGenerator = true" class="nav-btn" :class="{ active: showGenerator }">
            生成题目
          </button>
          <button @click="showGenerator = false" class="nav-btn" :class="{ active: !showGenerator }">
            题目列表
          </button>
        </div>
      </div>
    </nav>

    <!-- 生成题目区域 -->
    <div v-if="showGenerator" class="generator-section">
      <h2 class="section-title">生成编程选择题</h2>
      <div class="form-container">
        <div class="form-group">
          <label for="count">生成数量</label>
          <input
              id="count"
              v-model.number="formData.count"
              type="number"
              min="1"
              max="10"
              class="form-control"
          >
        </div>
        <div class="form-group">
          <label for="difficulty">难度级别</label>
          <select
              id="difficulty"
              v-model="formData.difficulty"
              class="form-control"
          >
            <option value="简单">简单</option>
            <option value="中等">中等</option>
            <option value="困难">困难</option>
          </select>
        </div>
        <div class="form-group">
          <label for="requirement">额外要求</label>
          <textarea
              id="requirement"
              v-model="formData.requirement"
              rows="3"
              class="form-control"
              placeholder="例如：关于Java集合框架，包含泛型应用"
          ></textarea>
        </div>
        <button
            @click="generateQuestions"
            class="btn primary"
            :disabled="isLoading"
        >
          {{ isLoading ? '生成中...' : '生成题目' }}
        </button>
      </div>

      <!-- 生成结果展示 -->
      <div v-if="generatedQuestions.length > 0" class="results-container">
        <h3 class="results-title">生成的题目</h3>
        <div v-for="(question, index) in generatedQuestions" :key="index" class="result-item">
          <div class="question-card">
            <div class="card-header">
              <span class="card-difficulty">{{ question.difficulty || '（无）' }}</span>
            </div>
            <div class="card-body">
              <p class="question-text"><strong>问题：</strong>{{ question.questionText || '（无）' }}</p>
              <div class="options">
                <p class="option"><strong>A:</strong> {{ question.optionA || '（无）' }}</p>
                <p class="option"><strong>B:</strong> {{ question.optionB || '（无）' }}</p>
                <p class="option"><strong>C:</strong> {{ question.optionC || '（无）' }}</p>
                <p class="option"><strong>D:</strong> {{ question.optionD || '（无）' }}</p>
              </div>
              <p class="answer"><strong>正确答案：</strong>{{ question.correctAnswer || '（无）' }}</p>
            </div>
            <div class="card-footer">
              <button
                  @click="saveQuestion(question)"
                  class="btn secondary"
                  :disabled="isSaving"
              >
                {{ isSaving ? '保存中...' : '保存到数据库' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 题目列表区域 -->
    <div v-else class="list-section">
      <h2 class="section-title">已保存的题目</h2>
      <div v-if="isLoadingList" class="loading">
        <div class="spinner"></div>
        <p>加载题目中...</p>
      </div>

      <div v-else-if="questions.length === 0" class="empty-state">
        <p>暂无保存的题目，请生成题目并保存</p>
      </div>

      <div v-else class="questions-container">
        <div v-for="question in questions" :key="question.id" class="question-card">
          <div class="card-header">
            <span class="card-id">#{{ question.id || '（无）' }}</span>
            <span class="card-difficulty">{{ question.difficulty || '（无）' }}</span>
          </div>
          <div class="card-body">
            <p class="question-text"><strong>问题：</strong>{{ question.questionText || '（无）' }}</p>
            <div class="options">
              <p class="option"><strong>A:</strong> {{ question.optionA || '（无）' }}</p>
              <p class="option"><strong>B:</strong> {{ question.optionB || '（无）' }}</p>
              <p class="option"><strong>C:</strong> {{ question.optionC || '（无）' }}</p>
              <p class="option"><strong>D:</strong> {{ question.optionD || '（无）' }}</p>
            </div>
            <p class="answer"><strong>正确答案：</strong>{{ question.correctAnswer || '（无）' }}</p>
          </div>
          <div class="card-footer">
            <button
                @click="deleteQuestion(question.id)"
                class="btn danger"
                :disabled="isDeleting || !question.id"
            >
              {{ isDeleting ? '删除中...' : '删除' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import axios from 'axios';

// 题目类型定义
interface QuizQuestion {
  id?: number;
  questionText: string;
  optionA: string;
  optionB: string;
  optionC: string;
  optionD: string;
  correctAnswer: string;
  category?: string;
  difficulty: string;
  createdAt?: string;
}

// 生成请求参数
interface GenerateRequest {
  count: number;
  difficulty: '简单' | '中等' | '困难';
  requirement?: string;
}

// 组件状态
const showGenerator = ref(true);
const formData = ref<GenerateRequest>({
  count: 3, // 默认生成3道题
  difficulty: '中等',
  requirement: '',
});
const generatedQuestions = ref<QuizQuestion[]>([]);
const questions = ref<QuizQuestion[]>([]);
const isLoading = ref(false);
const isSaving = ref(false);
const isLoadingList = ref(false);
const isDeleting = ref(false);

// API配置
const API_URL = 'http://localhost:8080/api/quiz';

// 生成题目
const generateQuestions = async () => {
  isLoading.value = true;
  try {
    const response = await axios.post<QuizQuestion[]>(`${API_URL}/generate`, formData.value);
    // 补全category字段
    generatedQuestions.value = response.data.map(q => ({
      ...q,
      category: q.category || '编程',
    }));
  } catch (error: any) {
    console.error('生成题目失败', error);
    alert('生成题目失败，请检查网络连接');
  } finally {
    isLoading.value = false;
  }
};

// 保存题目到数据库
const saveQuestion = async (question: QuizQuestion) => {
  isSaving.value = true;
  try {
    // 只发送后端需要的字段
    const { id, createdAt, ...toSave } = question;
    await axios.post<QuizQuestion>(`${API_URL}`, toSave);
    alert('题目保存成功！');
    loadSavedQuestions(); // 保存后刷新列表
  } catch (error: any) {
    console.error('保存题目失败', error);
    alert('保存题目失败，请重试');
  } finally {
    isSaving.value = false;
  }
};

// 加载已保存的题目
const loadSavedQuestions = async () => {
  isLoadingList.value = true;
  try {
    const response = await axios.get<QuizQuestion[]>(`${API_URL}/all`);
    questions.value = response.data;
  } catch (error: any) {
    console.error('加载题目失败', error);
    alert('加载题目失败，请重试');
  } finally {
    isLoadingList.value = false;
  }
};

// 删除题目
const deleteQuestion = async (id?: number) => {
  if (!id) return;
  if (!confirm('确定要删除这道题目吗？')) return;

  isDeleting.value = true;
  try {
    await axios.delete(`${API_URL}/${id}`);
    loadSavedQuestions(); // 删除后刷新列表
  } catch (error: any) {
    console.error('删除题目失败', error);
    alert('删除题目失败，请重试');
  } finally {
    isDeleting.value = false;
  }
};

// 组件挂载时加载已保存的题目
onMounted(() => {
  loadSavedQuestions();
});
</script>

<style scoped>
.ai-quiz-app {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  color: #333;
}

/* 导航栏样式 */
.navbar {
  background-color: #4285f4;
  color: white;
  padding: 15px 0;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 30px;
}
.container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.app-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}
.nav-buttons {
  display: flex;
}
.nav-btn {
  background: none;
  border: none;
  color: white;
  padding: 8px 15px;
  margin-left: 10px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 14px;
}
.nav-btn.active {
  background-color: rgba(255,255,255,0.2);
}

/* 表单样式 */
.form-container {
  background-color: white;
  border-radius: 4px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  margin-bottom: 30px;
}
.form-group {
  margin-bottom: 15px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
}
.form-control {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
  font-size: 14px;
}
textarea.form-control {
  resize: vertical;
}
.btn {
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  width: 100%;
  font-size: 14px;
  transition: background-color 0.3s;
}
.primary {
  background-color: #4285f4;
  color: white;
}
.secondary {
  background-color: #4caf50;
  color: white;
  margin-top: 10px;
  width: auto;
}
.danger {
  background-color: #f44336;
  color: white;
  width: auto;
}

/* 题目卡片样式 */
.question-card {
  background-color: white;
  border: 1px solid #eee;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 15px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}
.card-id {
  font-weight: bold;
  color: #333;
}
.card-difficulty {
  font-size: 12px;
  color: #666;
  background-color: #f0f7ff;
  padding: 3px 8px;
  border-radius: 4px;
}
.question-text {
  margin-bottom: 10px;
  line-height: 1.5;
}
.options {
  margin-bottom: 10px;
  padding-left: 20px;
}
.option {
  margin-bottom: 5px;
}
.answer {
  color: #4caf50;
  font-weight: bold;
  margin-top: 10px;
}
.card-footer {
  margin-top: 15px;
  text-align: right;
}

/* 加载状态样式 */
.loading {
  text-align: center;
  padding: 30px 0;
}
.spinner {
  display: inline-block;
  width: 30px;
  height: 30px;
  border: 3px solid rgba(66, 133, 244, 0.3);
  border-radius: 50%;
  border-top-color: #4285f4;
  animation: spin 1s ease-in-out infinite;
  margin-bottom: 10px;
}
.empty-state {
  text-align: center;
  padding: 30px 0;
  color: #666;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 响应式样式 */
@media (max-width: 576px) {
  .container {
    flex-direction: column;
    text-align: center;
  }
  .nav-buttons {
    margin-top: 10px;
    width: 100%;
    justify-content: center;
  }
  .nav-btn {
    margin: 0 5px;
  }
}
</style>