<template>
  <div class="mission-view">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>任务完成</h1>
          <el-button @click="goBack">返回</el-button>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <div class="mission-container">
          <!-- 任务列表 -->
          <div v-if="missions.length > 0" class="mission-list">
            <h3>我的任务</h3>
            <el-row :gutter="20">
              <el-col 
                v-for="mission in missions" 
                :key="mission.id" 
                :span="8"
              >
                <el-card class="mission-card" :class="{ 'completed': mission.isCompleted }">
                  <div class="mission-header">
                    <h4>{{ mission.name }}</h4>
                    <el-tag :type="mission.isCompleted ? 'success' : 'warning'">
                      {{ mission.isCompleted ? '已完成' : '进行中' }}
                    </el-tag>
                  </div>
                  <div class="mission-info">
                    <p>{{ mission.description }}</p>
                    <div class="mission-time">
                      <span>开始时间：{{ mission.startTime }}</span>
                      <span>结束时间：{{ mission.endTime }}</span>
                    </div>
                    <div v-if="mission.isCompleted && mission.score !== null" class="mission-score">
                      <span>得分：{{ mission.score }}分</span>
                    </div>
                  </div>
                  <div class="mission-actions">
                    <el-button 
                      v-if="!mission.isCompleted" 
                      type="primary" 
                      @click="startMission(mission)"
                    >
                      开始任务
                    </el-button>
                    <el-button 
                      v-else 
                      type="info" 
                      @click="viewSubmission(mission)"
                    >
                      查看提交
                    </el-button>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
          
          <!-- 空状态 -->
          <div v-else class="placeholder">
            <el-icon class="placeholder-icon"><List /></el-icon>
            <h3>暂无任务</h3>
            <p>当前没有需要完成的任务</p>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { List } from '@element-plus/icons-vue'
import { publishedMissionApi, missionSubmissionApi } from '@/api/taskApi'

const router = useRouter()

interface Mission {
  id: number;
  name: string;
  description?: string;
  type: string;
  startTime: string;
  endTime: string;
  isCompleted: boolean;
  score: number | null;
  submissionId?: number;
}

const missions = ref<Mission[]>([]);
const loading = ref(false);

// 获取学生任务列表
const fetchStudentMissions = async () => {
  try {
    loading.value = true;
    const response = await publishedMissionApi.getPublishedMissions();
    const allMissions = response.data || [];
    
    // 获取每个任务的提交状态
    const missionsWithStatus = await Promise.all(
      allMissions.map(async (mission: any) => {
        try {
          const submissionResponse = await missionSubmissionApi.getMissionSubmissions({
            missionId: mission.id,
            studentId: 'current' // 这里应该传入当前学生ID
          });
          
          const submissions = submissionResponse.data || [];
          const latestSubmission = submissions[0]; // 假设按时间倒序排列
          
          return {
            ...mission,
            isCompleted: submissions.length > 0,
            score: latestSubmission?.score || null,
            submissionId: latestSubmission?.id
          };
        } catch (error) {
          console.error(`获取任务${mission.id}提交状态失败:`, error);
          return {
            ...mission,
            isCompleted: false,
            score: null
          };
        }
      })
    );
    
    missions.value = missionsWithStatus;
  } catch (error) {
    console.error('获取学生任务列表失败:', error);
    ElMessage.error('获取任务列表失败');
  } finally {
    loading.value = false;
  }
};

const goBack = () => {
  router.back()
}

const startMission = (mission: Mission) => {
  // 根据任务类型跳转到不同的任务页面
  if (mission.type === 'quiz') {
    router.push({
      name: 'quiz_task',
      params: { missionId: mission.id }
    });
  } else if (mission.type === 'report') {
    router.push({
      name: 'report_task',
      params: { missionId: mission.id }
    });
  } else {
    // 自主学习任务
    router.push({
      name: 'self_learning_task',
      params: { missionId: mission.id }
    });
  }
}

const viewSubmission = (mission: Mission) => {
  if (mission.submissionId) {
    router.push({
      name: 'submission_detail',
      params: { submissionId: mission.submissionId }
    });
  }
}

onMounted(() => {
  fetchStudentMissions();
});
</script>

<style scoped>
.mission-view {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.header {
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
}

.header-content h1 {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.main-content {
  padding: 30px;
}

.mission-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  min-height: 600px;
  padding: 30px;
}

.mission-list h3 {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 20px;
}

.mission-card {
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.mission-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.mission-card.completed {
  border-left: 4px solid #67C23A;
}

.mission-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.mission-header h4 {
  margin: 0;
  color: #333;
  font-size: 16px;
}

.mission-info {
  margin-bottom: 16px;
}

.mission-info p {
  color: #666;
  margin: 0 0 12px 0;
  line-height: 1.5;
}

.mission-time {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 12px;
  color: #999;
}

.mission-score {
  margin-top: 8px;
  font-weight: bold;
  color: #67C23A;
}

.mission-actions {
  display: flex;
  justify-content: center;
}

.placeholder {
  text-align: center;
  padding: 40px;
}

.placeholder-icon {
  font-size: 64px;
  color: #409eff;
  margin-bottom: 20px;
}

.placeholder h3 {
  margin: 0 0 16px 0;
  color: #333;
  font-size: 24px;
}

.placeholder p {
  margin: 0;
  color: #666;
  font-size: 16px;
  line-height: 1.5;
}
</style> 