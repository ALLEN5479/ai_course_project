<template>
  <div>
    <el-button type="primary" @click="showCreateDialog = true">新建试卷</el-button>
    <el-table :data="paperList" style="margin-top: 20px;">
      <el-table-column prop="paperName" label="试卷名称" />
      <el-table-column prop="paperDescription" label="描述" />
      <el-table-column prop="totalScore" label="总分" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="viewPaper(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新建试卷弹窗 -->
    <el-dialog v-model="showCreateDialog" title="新建试卷" width="500px">
      <el-form :model="newPaper" label-width="90px">
        <el-form-item label="试卷名称">
          <el-input v-model="newPaper.name" />
        </el-form-item>
        <el-form-item label="试卷简介">
          <el-input v-model="newPaper.description" type="textarea" />
        </el-form-item>
        <el-form-item label="选择题目">
          <el-button @click="showQuestionDialog = true" type="primary">选择题目</el-button>
          <span v-if="selectedQuestions.length" style="margin-left: 10px;">
            已选 {{ selectedQuestions.length }} 题，总分 {{ totalScore }}
          </span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="createPaper">保存</el-button>
      </template>
    </el-dialog>

    <!-- 题库选择弹窗（分页多选） -->
    <el-dialog v-model="showQuestionDialog" title="选择题目" width="900px">
      <el-table
        :data="pagedQuestionList"
        @selection-change="handleSelectionChange"
        :row-key="(row: any) => row.id"
        border
        height="400"
        ref="questionTable"
        :default-selection="selectedQuestions"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="question_text" label="题目" />
        <el-table-column prop="difficulty" label="难度" width="80" />
        <el-table-column prop="category" label="分类" width="100" />
      </el-table>
      <el-pagination
        style="margin-top: 10px; text-align: right;"
        background
        layout="prev, pager, next, jumper"
        :total="questionList.length"
        :page-size="pageSize"
        :current-page.sync="currentPage"
        @current-change="onPageChange"
      />
      <template #footer>
        <el-button @click="showQuestionDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmSelectQuestions">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch, nextTick } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const props = defineProps<{ courseId: string | number }>()
const showCreateDialog = ref(false)
const showQuestionDialog = ref(false)
const paperList = ref<any[]>([])
const questionList = ref<any[]>([])
const selectedQuestions = ref<any[]>([])
const selectedQuestionIds = ref<Set<number>>(new Set())
const tempSelectedQuestions = ref<any[]>([])
const newPaper = ref({ name: '', description: '' })
const totalScore = ref(0)
const pageSize = 10
const currentPage = ref(1)
const refs = ref({})

// 分页后的题库数据
const pagedQuestionList = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return questionList.value.slice(start, start + pageSize)
})

const onPageChange = (page: number) => {
  currentPage.value = page
}

// 获取试卷列表
const fetchPapers = async () => {
  const { data } = await axios.get('http://localhost:8080/api/papers/list', { params: { courseId: props.courseId } })
  console.log('[fetchPapers] 后端返回:', data)
  paperList.value = Array.isArray(data.data) ? data.data.filter((item: any) => item) : []
  console.log('[fetchPapers] paperList:', paperList.value)
}

// 获取题库题目（一次性全部拉取，前端分页）
const fetchQuestions = async () => {
  const { data } = await axios.get('http://localhost:8080/api/quiz/all')
  console.log('[fetchQuestions] 后端返回:', data)
  questionList.value = Array.isArray(data.data) ? data.data : (Array.isArray(data) ? data : [])
  console.log('[fetchQuestions] questionList:', questionList.value)
}

// 选择题目弹窗的多选
const handleSelectionChange = (val: any[]) => {
  // 只更新当前页的勾选项
  const currentPageIds = pagedQuestionList.value.map((q: any) => q.id)
  // 先移除当前页所有id
  currentPageIds.forEach(id => selectedQuestionIds.value.delete(id))
  // 再添加当前页新勾选id
  val.forEach((q: any) => selectedQuestionIds.value.add(q.id))
  // 临时保存当前页勾选项
  tempSelectedQuestions.value = val
}

// 确认选择题目
const confirmSelectQuestions = () => {
  // 用Set生成所有已选题目
  selectedQuestions.value = questionList.value.filter((q: any) => selectedQuestionIds.value.has(q.id))
  totalScore.value = selectedQuestions.value.length * 5
  showQuestionDialog.value = false
}

// 创建试卷
const createPaper = async () => {
  if (!newPaper.value.name || selectedQuestions.value.length === 0) {
    ElMessage.error('请填写试卷名称并选择题目')
    return
  }
  try {
    await axios.post('http://localhost:8080/api/papers', {
      paper_name: newPaper.value.name,
      paper_description: newPaper.value.description,
      total_score: totalScore.value,
      courseId: props.courseId,
      questions: selectedQuestions.value.map((q, idx) => ({
        question_id: q.id,
        score: 5,
        sort_order: idx + 1,
        question_type: 'single'
      }))
    })
    ElMessage.success('试卷创建成功')
    showCreateDialog.value = false
    selectedQuestions.value = []
    totalScore.value = 0
    fetchPapers()
  } catch (e) {
    ElMessage.error('试卷保存失败，请检查网络或后端接口')
  }
}

const viewPaper = (paper: any) => {
  ElMessage.info(`查看试卷：${paper.paper_name || paper.name}`)
}

onMounted(() => {
  console.log('[PaperManager] onMounted');
  fetchPapers()
  fetchQuestions()
})

// 暴露refresh方法供父组件调用
const refresh = () => {
  console.log('[PaperManager] refresh called');
  fetchPapers()
  fetchQuestions()
}

defineExpose({ refresh });

// 打开选择题目弹窗时，恢复所有已选题目
watch(showQuestionDialog, (val) => {
  if (val) {
    nextTick(() => {
      const table = (refs as any).value.questionTable;
      if (table && table.toggleRowSelection) {
        pagedQuestionList.value.forEach((row: any) => {
          table.toggleRowSelection(row, selectedQuestionIds.value.has(row.id));
        });
      }
    })
  }
})

// 分页切换后自动高亮
watch(currentPage, () => {
  nextTick(() => {
    const table = (refs as any).value.questionTable;
    if (table && table.toggleRowSelection) {
      pagedQuestionList.value.forEach((row: any) => {
        table.toggleRowSelection(row, selectedQuestionIds.value.has(row.id));
      });
    }
  });
});
</script> 