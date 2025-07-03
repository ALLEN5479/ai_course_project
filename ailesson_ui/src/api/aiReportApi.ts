import request from '@/utils/request'

// AI报告接口
export interface AiReport {
  id: string
  studentId: string
  studentName: string
  reportContent: string
  analysisDate: string
  abilityScores: {
    courseScore: number
    practiceScore: number
    qualityScore: number
    innovationScore: number
    teamworkScore: number
    communicationScore: number
  }
  suggestions: string[]
  strengths: string[]
  weaknesses: string[]
  improvementPlan: string
}

// 生成AI能力报告
export const generateAiReport = (studentId: string) => {
  return request({
    url: '/ai/report/generate',
    method: 'post',
    data: { studentId }
  })
}

// 获取AI能力报告
export const getAiReport = (studentId: string) => {
  return request({
    url: `/ai/report/${studentId}`,
    method: 'get'
  })
}

// 获取AI报告列表
export const getAiReportList = (params: {
  page: number
  size: number
  studentId?: string
}) => {
  return request({
    url: '/ai/report/list',
    method: 'get',
    params
  })
}

// 删除AI报告
export const deleteAiReport = (reportId: string) => {
  return request({
    url: `/ai/report/delete/${reportId}`,
    method: 'delete'
  })
}

// 更新AI报告
export const updateAiReport = (reportId: string, data: Partial<AiReport>) => {
  return request({
    url: `/ai/report/update/${reportId}`,
    method: 'put',
    data
  })
}

// 批量生成AI报告
export const batchGenerateAiReport = (studentIds: string[]) => {
  return request({
    url: '/ai/report/batch-generate',
    method: 'post',
    data: { studentIds }
  })
}

// 获取AI配置
export const getAiConfig = () => {
  return request({
    url: '/ai/config',
    method: 'get'
  })
}

// 更新AI配置
export const updateAiConfig = (config: {
  apiUrl: string
  apiKey: string
  model: string
  temperature: number
}) => {
  return request({
    url: '/ai/config',
    method: 'put',
    data: config
  })
}

// 测试AI连接
export const testAiConnection = () => {
  return request({
    url: '/ai/test-connection',
    method: 'post'
  })
} 