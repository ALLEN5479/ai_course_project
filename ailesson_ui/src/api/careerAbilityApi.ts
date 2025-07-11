import request from '@/utils/request'

export function getStudentCareerAbility(studentId: string) {
  return request.get(`/career/ability/student/${studentId}`)
}

export function getCareerAbilityList(params: any) {
  return request.get('/career/ability/list', { params })
}

export function generateAiReport(studentId: string) {
  return request.post(`/career/ability/ai-report/${studentId}`)
}

export function uploadCareerAbilityData(file: File) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('type', 'career_ability')
  return request.post('/career/ability/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function deleteCareerAbility(studentId: string) {
  return request.delete(`/career/ability/delete/${studentId}`)
}