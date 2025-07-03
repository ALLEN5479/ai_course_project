<template>
  <div class="career-ability-manager">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span>生涯能力管理</span>
          <el-button type="primary" @click="showUploadDialog = true">
            <el-icon><Upload /></el-icon>
            导入能力数据
          </el-button>
        </div>
      </template>

      <!-- 统计概览 -->
      <el-row :gutter="20" class="stats-row">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ stats.totalStudents }}</div>
              <div class="stat-label">总学生数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ stats.analyzedStudents }}</div>
              <div class="stat-label">已分析学生</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ stats.reportCount }}</div>
              <div class="stat-label">AI报告数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-content">
              <div class="stat-number">{{ stats.avgScore }}</div>
              <div class="stat-label">平均能力分</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 学生能力列表 -->
      <div class="student-list-section">
        <div class="section-header">
          <h3>学生能力列表</h3>
          <el-input
            v-model="searchKeyword"
            placeholder="搜索学生姓名"
            style="width: 200px"
            clearable
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>

        <el-table :data="filteredStudents" style="width: 100%" v-loading="loading">
          <el-table-column prop="studentId" label="学号" width="120" />
          <el-table-column prop="name" label="姓名" width="120" />
          <el-table-column prop="className" label="班级" width="120" />
          <el-table-column prop="courseScore" label="课程能力" width="100">
            <template #default="scope">
              <el-progress :percentage="scope.row.courseScore" :color="getScoreColor(scope.row.courseScore)" />
            </template>
          </el-table-column>
          <el-table-column prop="practiceScore" label="实践能力" width="100">
            <template #default="scope">
              <el-progress :percentage="scope.row.practiceScore" :color="getScoreColor(scope.row.practiceScore)" />
            </template>
          </el-table-column>
          <el-table-column prop="qualityScore" label="综合素养" width="100">
            <template #default="scope">
              <el-progress :percentage="scope.row.qualityScore" :color="getScoreColor(scope.row.qualityScore)" />
            </template>
          </el-table-column>
          <el-table-column prop="totalScore" label="总分" width="80">
            <template #default="scope">
              <span class="total-score">{{ scope.row.totalScore }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button size="small" @click="viewAbilityMap(scope.row)">查看图谱</el-button>
              <el-button size="small" type="primary" @click="generateReport(scope.row)">生成报告</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          class="pagination"
        />
      </div>
    </el-card>

    <!-- Excel上传对话框 -->
    <el-dialog v-model="showUploadDialog" title="导入能力数据" width="500px">
      <CareerAbilityUpload @upload-success="handleUploadSuccess" />
    </el-dialog>

    <!-- 能力图谱对话框 -->
    <el-dialog v-model="showAbilityMapDialog" title="学生能力图谱" width="800px">
      <div ref="radarChartRef" style="width: 100%; height: 400px;"></div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Upload, Search } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import CareerAbilityUpload from './CareerAbilityUpload.vue'
import { getCareerAbilityList, generateAiReport } from '../../api/careerAbilityApi'

// 响应式数据
const loading = ref(false)
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showUploadDialog = ref(false)
const showAbilityMapDialog = ref(false)
const radarChartRef = ref()

// 统计数据
const stats = ref({
  totalStudents: 0,
  analyzedStudents: 0,
  reportCount: 0,
  avgScore: 0
})

// 学生列表数据
const students = ref<any[]>([])

// 计算属性
const filteredStudents = computed(() => {
  if (!searchKeyword.value) return students.value
  return students.value.filter(student => 
    student.name.includes(searchKeyword.value) || 
    student.studentId.includes(searchKeyword.value)
  )
})

// 方法
const loadStudents = async () => {
  loading.value = true
  try {
    const response: any = await getCareerAbilityList({
      page: currentPage.value,
      size: pageSize.value
    })
    students.value = response.data.list
    total.value = response.data.total
    stats.value = response.data.stats
  } catch (error) {
    ElMessage.error('加载学生数据失败')
  } finally {
    loading.value = false
  }
}

const getScoreColor = (score: number) => {
  if (score >= 80) return '#67C23A'
  if (score >= 60) return '#E6A23C'
  return '#F56C6C'
}

const viewAbilityMap = (student: any) => {
  showAbilityMapDialog.value = true
  // 在下一个tick中初始化图表
  setTimeout(() => {
    initRadarChart(student)
  }, 100)
}

const initRadarChart = (student: any) => {
  const chartDom = radarChartRef.value
  const myChart = echarts.init(chartDom)
  
  const option = {
    title: {
      text: `${student.name}的能力图谱`,
      left: 'center'
    },
    tooltip: {},
    radar: {
      indicator: [
        { name: '课程能力', max: 100 },
        { name: '实践能力', max: 100 },
        { name: '综合素养', max: 100 },
        { name: '创新能力', max: 100 },
        { name: '团队协作', max: 100 },
        { name: '沟通表达', max: 100 }
      ]
    },
    series: [{
      name: '能力值',
      type: 'radar',
      data: [{
        value: [
          student.courseScore,
          student.practiceScore,
          student.qualityScore,
          student.innovationScore || 70,
          student.teamworkScore || 75,
          student.communicationScore || 80
        ],
        name: '当前能力',
        areaStyle: { color: 'rgba(64,158,255,0.4)' }
      }]
    }]
  }
  
  myChart.setOption(option)
}

const generateReport = async (student: any) => {
  try {
    const response = await generateAiReport(student.studentId)
    ElMessage.success('AI能力报告生成成功')
    // 可选：弹窗展示报告内容
  } catch (error) {
    ElMessage.error('生成AI报告失败')
  }
}

const handleUploadSuccess = () => {
  showUploadDialog.value = false
  loadStudents()
  ElMessage.success('数据导入成功')
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  loadStudents()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loadStudents()
}

// 生命周期
onMounted(() => {
  loadStudents()
})
</script>

<style scoped>
.career-ability-manager {
  padding: 20px;
}

.main-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stats-row {
  margin-bottom: 30px;
}

.stat-card {
  text-align: center;
}

.stat-content {
  padding: 10px;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-top: 5px;
}

.student-list-section {
  margin-top: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  margin: 0;
  color: #333;
}

.total-score {
  font-weight: bold;
  color: #409EFF;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style> 