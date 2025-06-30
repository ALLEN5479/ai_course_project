import axios from 'axios'

const BASE_URL = 'http://localhost:8080'

// 任务模板管理API
export const taskTemplateApi = {
  // 获取任务模板列表
  getTaskTemplates: async (params?: any) => {
    const response = await axios.get(`${BASE_URL}/api/task-library/missions`, { params })
    // 后端返回格式: { success: true, data: [...], message: "..." }
    return response.data
  },

  // 创建任务模板
  createTaskTemplate: async (data: any) => {
    const response = await axios.post(`${BASE_URL}/api/task-library/missions`, data)
    return response.data
  },

  // 更新任务模板
  updateTaskTemplate: async (id: number, data: any) => {
    const response = await axios.put(`${BASE_URL}/api/task-library/missions/${id}`, data)
    return response.data
  },

  // 删除任务模板
  deleteTaskTemplate: async (id: number) => {
    const response = await axios.delete(`${BASE_URL}/api/task-library/missions/${id}`)
    return response.data
  },

  // 获取任务模板详情
  getTaskTemplateDetail: async (id: number) => {
    const response = await axios.get(`${BASE_URL}/api/task-library/missions/${id}`)
    return response.data
  }
}

// 已发布任务管理API
export const publishedMissionApi = {
  // 获取已发布任务列表
  getPublishedMissions: async (params?: any) => {
    const response = await axios.get(`${BASE_URL}/api/task-manager/published-missions`, { params })
    return response.data
  },

  // 发布任务
  publishMission: async (data: any) => {
    const response = await axios.post(`${BASE_URL}/api/task-manager/publish-mission`, data)
    return response.data
  },

  // 更新已发布任务
  updatePublishedMission: async (id: number, data: any) => {
    const response = await axios.put(`${BASE_URL}/api/task-manager/published-missions/${id}`, data)
    return response.data
  },

  // 删除已发布任务
  deletePublishedMission: async (id: number) => {
    const response = await axios.delete(`${BASE_URL}/api/task-manager/published-missions/${id}`)
    return response.data
  },

  // 获取已发布任务详情
  getPublishedMissionDetail: async (id: number) => {
    const response = await axios.get(`${BASE_URL}/api/task-manager/published-missions/${id}`)
    return response.data
  }
}

// 任务提交管理API
export const missionSubmissionApi = {
  // 获取任务提交列表
  getMissionSubmissions: async (params?: any) => {
    const response = await axios.get(`${BASE_URL}/api/mission-submissions`, { params })
    return response.data
  },

  // 获取任务提交详情
  getMissionSubmissionDetail: async (id: number) => {
    const response = await axios.get(`${BASE_URL}/api/mission-submissions/${id}`)
    return response.data
  },

  // 提交任务
  submitMission: async (data: any) => {
    const response = await axios.post(`${BASE_URL}/api/mission-submissions`, data)
    return response.data
  },

  // 更新任务提交
  updateMissionSubmission: async (id: number, data: any) => {
    const response = await axios.put(`${BASE_URL}/api/mission-submissions/${id}`, data)
    return response.data
  }
}

// 任务批改API
export const taskGradingApi = {
  // 获取待批改任务列表
  getPendingGradingTasks: async (params?: any) => {
    const response = await axios.get(`${BASE_URL}/api/task-grading/submissions/${params?.missionId}/grade-status/pending`, { params })
    return response.data
  },

  // 获取任务批改详情
  getTaskGradingDetail: async (submissionId: number) => {
    const response = await axios.get(`${BASE_URL}/api/task-grading/submission/${submissionId}`)
    return response.data
  },

  // 提交批改结果
  submitGrading: async (submissionId: number, data: any) => {
    const response = await axios.put(`${BASE_URL}/api/task-grading/grade/${submissionId}`, data)
    return response.data
  },

  // 保存批改草稿
  saveGradingDraft: async (submissionId: number, data: any) => {
    const response = await axios.put(`${BASE_URL}/api/task-grading/grade/${submissionId}`, data)
    return response.data
  }
}

// 任务完成情况API
export const taskCompletionApi = {
  // 获取任务完成情况
  getTaskCompletion: async (missionId: number, params?: any) => {
    const response = await axios.get(`${BASE_URL}/api/mission/statistics/${missionId}`, { params })
    return response.data
  },

  // 获取任务统计信息
  getTaskStatistics: async (missionId: number) => {
    const response = await axios.get(`${BASE_URL}/api/mission/statistics/${missionId}`)
    return response.data
  },

  // 生成任务报告
  generateTaskReport: async (missionId: number) => {
    const response = await axios.post(`${BASE_URL}/api/mission/reports/generate/${missionId}`)
    return response.data
  }
}

// 试卷管理API
export const paperApi = {
  // 获取试卷列表
  getPapers: async (params?: any) => {
    const response = await axios.get(`${BASE_URL}/api/papers`, { params })
    return response.data
  },

  // 创建试卷
  createPaper: async (data: any) => {
    const response = await axios.post(`${BASE_URL}/api/papers`, data)
    return response.data
  },

  // 更新试卷
  updatePaper: async (id: number, data: any) => {
    const response = await axios.put(`${BASE_URL}/api/papers/${id}`, data)
    return response.data
  },

  // 删除试卷
  deletePaper: async (id: number) => {
    const response = await axios.delete(`${BASE_URL}/api/papers/${id}`)
    return response.data
  },

  // 获取试卷详情
  getPaperDetail: async (id: number) => {
    const response = await axios.get(`${BASE_URL}/api/papers/${id}`)
    return response.data
  }
}

// 试卷题目管理API
export const paperQuestionApi = {
  // 获取试卷题目列表
  getPaperQuestions: async (paperId: number) => {
    const response = await axios.get(`${BASE_URL}/api/paper-questions/paper/${paperId}`)
    return response.data
  },

  // 添加题目到试卷
  addQuestionToPaper: async (paperId: number, data: any) => {
    const response = await axios.post(`${BASE_URL}/api/paper-questions`, data)
    return response.data
  },

  // 从试卷移除题目
  removeQuestionFromPaper: async (paperId: number, questionId: number) => {
    const response = await axios.delete(`${BASE_URL}/api/paper-questions/${questionId}`)
    return response.data
  },

  // 更新试卷题目顺序
  updatePaperQuestionOrder: async (paperId: number, data: any) => {
    const response = await axios.put(`${BASE_URL}/api/paper-questions/${data.id}`, data)
    return response.data
  }
} 