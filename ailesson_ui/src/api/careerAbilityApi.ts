import request from '@/utils/request'

// 生涯能力数据接口
export interface CareerAbilityData {
  studentId: string
  name: string
  className: string
  courseScore: number
  practiceScore: number
  qualityScore: number
  innovationScore: number
  teamworkScore: number
  communicationScore: number
  totalScore: number
  rank?: number
}

// 获取生涯能力列表（教师端）
export const getCareerAbilityList = (params: {
  page: number
  size: number
  keyword?: string
}) => {
  return request({
    url: '/career/ability/list',
    method: 'get',
    params
  })
}

// 获取学生生涯能力数据（学生端）
export const getStudentCareerAbility = (studentId: string) => {
  return request({
    url: `/career/ability/student/${studentId}`,
    method: 'get'
  })
}

// 上传生涯能力数据
export const uploadCareerAbilityData = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('type', 'career_ability')
  
  return request({
    url: '/career/ability/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 生成AI能力报告
export const generateAiReport = (studentId: string) => {
  return request({
    url: '/career/ability/report/generate',
    method: 'post',
    data: { studentId }
  })
}

// 获取AI能力报告
export const getAiReport = (studentId: string) => {
  return request({
    url: `/career/ability/report/${studentId}`,
    method: 'get'
  })
}

// 更新生涯能力数据
export const updateCareerAbility = (data: CareerAbilityData) => {
  return request({
    url: '/career/ability/update',
    method: 'put',
    data
  })
}

// 删除生涯能力数据
export const deleteCareerAbility = (studentId: string) => {
  return request({
    url: `/career/ability/delete/${studentId}`,
    method: 'delete'
  })
}

// 获取能力统计数据
export const getAbilityStats = () => {
  return request({
    url: '/career/ability/stats',
    method: 'get'
  })
}

// 导出能力数据
export const exportAbilityData = (params: {
  studentIds?: string[]
  format: 'excel' | 'csv'
}) => {
  return request({
    url: '/career/ability/export',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
} 