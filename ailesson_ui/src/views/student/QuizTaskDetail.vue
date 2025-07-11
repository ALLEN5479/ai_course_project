<template>
  <div class="task-detail-layout">
    <el-row :gutter="24" class="main-row">
      <el-col :span="10" class="left-col">
        <div class="left-panel">
          <el-collapse v-model="activeNames" class="left-collapse">
            <el-collapse-item name="1">
              <template #title>
                <el-tag type="info">测试任务</el-tag>
                <span class="task-title">{{ task.mission_name }}</span>
              </template>
              <div class="task-desc">{{ task.mission_description }}</div>
              <div class="task-meta">
                <span>开始时间：{{ formatDateTime(task.start_time) }}</span>
                <span>结束时间：{{ formatDateTime(task.end_time) }}</span>
              </div>
            </el-collapse-item>
          </el-collapse>
          <el-button class="back-btn" @click="goBack" type="primary" size="large" round>返回</el-button>
        </div>
      </el-col>
      <el-col :span="14" class="right-col">
        <el-card class="task-action-card quiz-card">
          <div class="quiz-header">
            <div class="quiz-nav">
              <span
                v-for="(q, idx) in questions"
                :key="idx"
                class="quiz-nav-item"
                :class="{ answered: answers[idx] !== undefined, active: idx === currentIndex }"
                @click="goToQuestion(idx)"
              >{{ idx + 1 }}</span>
            </div>
          </div>
          <div class="quiz-body" v-if="questions.length">
            <div class="quiz-question">{{ currentQuestion.question_text }}</div>
            <el-radio-group v-model="answers[currentIndex]" class="quiz-options">
              <el-radio label="A" class="quiz-option">
                <span class="option-key">A.</span> {{ currentQuestion.option_a }}
              </el-radio>
              <el-radio label="B" class="quiz-option">
                <span class="option-key">B.</span> {{ currentQuestion.option_b }}
              </el-radio>
              <el-radio label="C" class="quiz-option">
                <span class="option-key">C.</span> {{ currentQuestion.option_c }}
              </el-radio>
              <el-radio label="D" class="quiz-option">
                <span class="option-key">D.</span> {{ currentQuestion.option_d }}
              </el-radio>
            </el-radio-group>
          </div>
          <div v-else class="quiz-loading">正在加载题目...</div>
          <div class="quiz-footer">
            <el-button :disabled="currentIndex === 0" @click="prevQuestion" icon="el-icon-arrow-left" round>上一题</el-button>
            <el-button
              v-if="currentIndex !== questions.length - 1"
              :disabled="currentIndex === questions.length - 1"
              @click="nextQuestion"
              icon="el-icon-arrow-right"
              round
            >下一题</el-button>
            <el-button
              v-else
              type="primary"
              @click="submitQuiz"
              round
            >提交</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const studentId = route.query.user_id || ''

const task = ref({
  mission_id: route.query.mission_id || '',
  mission_name: route.query.mission_name || '',
  mission_description: route.query.mission_description || '',
  start_time: route.query.start_time || '',
  end_time: route.query.end_time || '',
})

const content = ref('')
const activeNames = ref(['1', '2'])

const questions = ref<any[]>([])
const currentIndex = ref(0)
const answers = ref<(string|undefined)[]>([])

const currentQuestion = computed(() => questions.value[currentIndex.value] || {})

function goBack() {
  router.back()
}

function formatDateTime(dt: string) {
  if (!dt) return ''
  return dt.replace('T', ' ').slice(0, 16)
}

function goToQuestion(idx: number) {
  currentIndex.value = idx
}
function prevQuestion() {
  if (currentIndex.value > 0) currentIndex.value--
}
function nextQuestion() {
  if (currentIndex.value < questions.value.length - 1) currentIndex.value++
}

function submitQuiz() {
  let totalscore = 0
  questions.value.forEach((q, idx) => {
    if (answers.value[idx] && answers.value[idx] === q.correct_answer) {
      totalscore += Number(q.score || 0)
    }
  })
  // 调用后端接口更新分数
  axios.get('http://localhost:8080/api/task-manager/published-missions/updatescore', {
    params: {
      mission_id: task.value.mission_id,
      student_id: studentId,
      score: totalscore
    }
  }).then(() => {
    ElMessage.success('答题已提交，分数已同步！')
    setTimeout(() => {
      router.back()
    }, 800)
  }).catch(() => {
    ElMessage.error('分数同步失败')
  })
}

async function fetchContentAndQuiz() {
  if (!task.value.mission_id) return
  try {
    // 获取content内容
    const res = await axios.get('http://localhost:8080/api/task-manager/published-missions/showcontent', {
      params: { mission_id: task.value.mission_id }
    })
    content.value = res.data || ''
    // 用content内容请求题目
    const quizRes = await axios.get('http://localhost:8080/api/paper-questions/showQuizs', {
      params: { paper_id: content.value }
    })
    questions.value = quizRes.data || []
    answers.value = Array(questions.value.length).fill(undefined)
  } catch (e) {
    content.value = '题目加载失败'
    questions.value = []
  }
}

onMounted(() => {
  fetchContentAndQuiz()
})
</script>

<style scoped>
.task-detail-layout {
  min-height: 100vh;
  background: linear-gradient(120deg, #f5f7fa 60%, #e0e7ff 100%);
  padding: 30px 40px;
}
.main-row {
  min-height: 80vh;
}
.left-col, .right-col {
  display: flex;
  align-items: stretch;
}
.left-panel {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
}
.left-collapse {
  flex: 1 1 0;
  background: linear-gradient(135deg, #fff 80%, #e0e7ff 100%);
  border-radius: 18px;
  margin-bottom: 18px;
  box-shadow: 0 4px 24px 0 rgba(80,120,255,0.08);
  transition: box-shadow 0.2s;
  min-height: 350px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.left-collapse:hover {
  box-shadow: 0 8px 32px 0 rgba(80,120,255,0.16);
}
.task-title {
  font-size: 20px;
  font-weight: 700;
  margin-left: 12px;
  color: #2d3a4b;
}
.task-desc {
  color: #666;
  margin-bottom: 14px;
  font-size: 16px;
  line-height: 1.7;
}
.task-meta {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 14px;
  color: #999;
}
.back-btn {
  margin-top: 22px;
  width: 100%;
  font-size: 16px;
  letter-spacing: 2px;
}
.right-col {
  align-items: stretch;
  justify-content: center;
}
.task-action-card.quiz-card {
  min-height: 350px;
  width: 100%;
  max-width: 520px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  background: linear-gradient(135deg, #fafdff 60%, #e0e7ff 100%);
  border-radius: 18px;
  box-shadow: 0 4px 24px 0 rgba(80,120,255,0.10);
  padding: 40px 32px 32px 32px;
}
.quiz-header {
  width: 100%;
  display: flex;
  justify-content: center;
  margin-bottom: 18px;
}
.quiz-nav {
  display: flex;
  gap: 8px;
  background: #f0f4ff;
  border-radius: 12px;
  padding: 8px 16px;
  box-shadow: 0 2px 8px rgba(80,120,255,0.06);
}
.quiz-nav-item {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #fff;
  color: #333;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 16px;
  cursor: pointer;
  border: 2px solid #e0e7ff;
  transition: background 0.2s, color 0.2s, border 0.2s;
}
.quiz-nav-item.answered {
  background: #409eff;
  color: #fff;
  border: 2px solid #409eff;
}
.quiz-nav-item.active {
  box-shadow: 0 0 0 2px #a5bfff;
  border: 2px solid #409eff;
}
.quiz-body {
  width: 100%;
  margin: 24px 0 16px 0;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.quiz-question {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 18px;
  color: #2d3a4b;
}
.quiz-options {
  display: flex;
  flex-direction: column;
  gap: 16px;
  width: 100%;
}
.quiz-option {
  font-size: 16px;
  padding: 8px 0 8px 0;
  display: flex;
  align-items: center;
}
.option-key {
  font-weight: 700;
  margin-right: 8px;
  color: #409eff;
}
.quiz-footer {
  width: 100%;
  display: flex;
  justify-content: space-between;
  margin-top: 24px;
}
.quiz-loading {
  width: 100%;
  text-align: center;
  color: #999;
  font-size: 18px;
  margin: 40px 0;
}
@media (max-width: 900px) {
  .main-row { flex-direction: column; }
  .left-col, .right-col { width: 100% !important; max-width: 100% !important; }
  .task-action-card { max-width: 100%; }
  .left-panel { min-height: 350px; }
}
</style> 