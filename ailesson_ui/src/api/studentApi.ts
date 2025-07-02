import axios from 'axios'

// 获取学生扩展信息
export const getStudentInfo = (userId: string) => {
  return axios.get('http://localhost:8080/studentInfo/get', { params: { user_id: userId } })
}

// 更新学生扩展信息
export const updateStudentInfo = (data: any) => {
  return axios.post('http://localhost:8080/studentInfo/update', data)
}
