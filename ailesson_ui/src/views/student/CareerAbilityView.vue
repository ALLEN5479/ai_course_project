<template>
  <div class="career-ability-view">
    <el-card class="main-card">
      <template #header>
        <div class="card-header">
          <span>我的能力图谱</span>
          <el-button type="primary" @click="refreshData">
            <el-icon><Refresh /></el-icon>
            刷新数据
          </el-button>
        </div>
      </template>

      <!-- 基本信息 -->
      <div class="basic-info">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="info-item">
              <label>姓名：</label>
              <span>{{ studentInfo.name }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>学号：</label>
              <span>{{ studentInfo.studentId }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>班级：</label>
              <span>{{ studentInfo.className }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 能力雷达图 -->
      <div class="radar-chart-section">
        <h3>能力雷达图</h3>
        <div ref="radarChartRef" class="radar-chart"></div>
      </div>

      <!-- 能力详情 -->
      <div class="ability-details">
        <h3>能力详情</h3>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card class="ability-card">
              <div class="ability-header">
                <h4>课程能力</h4>
                <span class="score">{{ abilityData.courseScore }}</span>
              </div>
              <el-progress :percentage="abilityData.courseScore" :color="getScoreColor(abilityData.courseScore)" />
              <p class="description">{{ getAbilityDescription('course', abilityData.courseScore) }}</p>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="ability-card">
              <div class="ability-header">
                <h4>实践能力</h4>
                <span class="score">{{ abilityData.practiceScore }}</span>
              </div>
              <el-progress :percentage="abilityData.practiceScore" :color="getScoreColor(abilityData.practiceScore)" />
              <p class="description">{{ getAbilityDescription('practice', abilityData.practiceScore) }}</p>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="ability-card">
              <div class="ability-header">
                <h4>综合素养</h4>
                <span class="score">{{ abilityData.qualityScore }}</span>
              </div>
              <el-progress :percentage="abilityData.qualityScore" :color="getScoreColor(abilityData.qualityScore)" />
              <p class="description">{{ getAbilityDescription('quality', abilityData.qualityScore) }}</p>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="8">
            <el-card class="ability-card">
              <div class="ability-header">
                <h4>创新能力</h4>
                <span class="score">{{ abilityData.innovationScore }}</span>
              </div>
              <el-progress :percentage="abilityData.innovationScore" :color="getScoreColor(abilityData.innovationScore)" />
              <p class="description">{{ getAbilityDescription('innovation', abilityData.innovationScore) }}</p>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="ability-card">
              <div class="ability-header">
                <h4>团队协作</h4>
                <span class="score">{{ abilityData.teamworkScore }}</span>
              </div>
              <el-progress :percentage="abilityData.teamworkScore" :color="getScoreColor(abilityData.teamworkScore)" />
              <p class="description">{{ getAbilityDescription('teamwork', abilityData.teamworkScore) }}</p>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="ability-card">
              <div class="ability-header">
                <h4>沟通表达</h4>
                <span class="score">{{ abilityData.communicationScore }}</span>
              </div>
              <el-progress :percentage="abilityData.communicationScore" :color="getScoreColor(abilityData.communicationScore)" />
              <p class="description">{{ getAbilityDescription('communication', abilityData.communicationScore) }}</p>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 总分和排名 -->
      <div class="total-score-section">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="total-card">
              <div class="total-content">
                <div class="total-number">{{ abilityData.totalScore }}</div>
                <div class="total-label">能力总分</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="rank-card">
              <div class="rank-content">
                <div class="rank-number">{{ abilityData.rank || '--' }}</div>
                <div class="rank-label">班级排名</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getStudentCareerAbility } from '../../api/careerAbilityApi'

// 响应式数据
const radarChartRef = ref()
const loading = ref(false)

// 学生信息
const studentInfo = ref({
  name: '',
  studentId: '',
  className: ''
})

// 能力数据
const abilityData = ref({
  courseScore: 0,
  practiceScore: 0,
  qualityScore: 0,
  innovationScore: 0,
  teamworkScore: 0,
  communicationScore: 0,
  totalScore: 0,
  rank: 0
})

// 方法
const loadAbilityData = async () => {
  loading.value = true
  try {
    const userId = localStorage.getItem('user_id') || ''
    const response: any = await getStudentCareerAbility(userId)
    if (response && response.code === 200) {
      studentInfo.value = response.data.studentInfo
      abilityData.value = response.data.abilityData
      // 初始化雷达图
      await nextTick()
      initRadarChart()
    }
  } catch (error) {
    ElMessage.error('加载能力数据失败')
  } finally {
    loading.value = false
  }
}

const initRadarChart = () => {
  const chartDom = radarChartRef.value
  if (!chartDom) return
  
  const myChart = echarts.init(chartDom)
  
  const option = {
    title: {
      text: '个人能力雷达图',
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'item'
    },
    radar: {
      indicator: [
        { name: '课程能力', max: 100 },
        { name: '实践能力', max: 100 },
        { name: '综合素养', max: 100 },
        { name: '创新能力', max: 100 },
        { name: '团队协作', max: 100 },
        { name: '沟通表达', max: 100 }
      ],
      radius: '65%'
    },
    series: [{
      name: '能力值',
      type: 'radar',
      data: [{
        value: [
          abilityData.value.courseScore,
          abilityData.value.practiceScore,
          abilityData.value.qualityScore,
          abilityData.value.innovationScore,
          abilityData.value.teamworkScore,
          abilityData.value.communicationScore
        ],
        name: '当前能力',
        areaStyle: { color: 'rgba(64,158,255,0.4)' }
      }]
    }]
  }
  
  myChart.setOption(option)
  
  // 响应式调整
  window.addEventListener('resize', () => {
    myChart.resize()
  })
}

const getScoreColor = (score: number) => {
  if (score >= 80) return '#67C23A'
  if (score >= 60) return '#E6A23C'
  return '#F56C6C'
}

const getAbilityDescription = (type: string, score: number) => {
  const descriptions: Record<string, Record<string, string>> = {
    course: {
      high: '课程学习表现优秀，基础知识扎实',
      medium: '课程学习表现良好，有一定基础',
      low: '课程学习需要加强，建议多复习'
    },
    practice: {
      high: '实践能力强，动手能力突出',
      medium: '实践能力良好，有一定动手经验',
      low: '实践能力需要提升，建议多参与项目'
    },
    quality: {
      high: '综合素养优秀，全面发展',
      medium: '综合素养良好，各方面均衡',
      low: '综合素养需要提升，建议全面发展'
    },
    innovation: {
      high: '创新能力突出，思维活跃',
      medium: '创新能力良好，有一定创新思维',
      low: '创新能力需要培养，建议多思考'
    },
    teamwork: {
      high: '团队协作能力强，善于合作',
      medium: '团队协作能力良好，能够配合',
      low: '团队协作需要加强，建议多参与团队活动'
    },
    communication: {
      high: '沟通表达能力优秀，表达清晰',
      medium: '沟通表达能力良好，能够表达',
      low: '沟通表达需要提升，建议多练习'
    }
  }
  
  const level = score >= 80 ? 'high' : score >= 60 ? 'medium' : 'low'
  return descriptions[type]?.[level] || '暂无描述'
}

const refreshData = () => {
  loadAbilityData()
}

// 生命周期
onMounted(() => {
  loadAbilityData()
})
</script>

<style scoped>
.career-ability-view {
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

.basic-info {
  margin-bottom: 30px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.info-item label {
  font-weight: bold;
  margin-right: 10px;
  color: #333;
}

.radar-chart-section {
  margin-bottom: 30px;
}

.radar-chart-section h3 {
  margin-bottom: 20px;
  color: #333;
}

.radar-chart {
  width: 100%;
  height: 400px;
}

.ability-details {
  margin-bottom: 30px;
}

.ability-details h3 {
  margin-bottom: 20px;
  color: #333;
}

.ability-card {
  height: 150px;
}

.ability-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.ability-header h4 {
  margin: 0;
  color: #333;
}

.score {
  font-size: 18px;
  font-weight: bold;
  color: #409EFF;
}

.description {
  margin-top: 10px;
  font-size: 12px;
  color: #666;
  line-height: 1.4;
}

.total-score-section {
  margin-top: 30px;
}

.total-card, .rank-card {
  text-align: center;
}

.total-content, .rank-content {
  padding: 20px;
}

.total-number, .rank-number {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
}

.total-label, .rank-label {
  font-size: 14px;
  color: #666;
  margin-top: 5px;
}
</style> 