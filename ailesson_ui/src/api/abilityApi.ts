import axios from 'axios'

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error('API请求错误:', error)
    return Promise.reject(error)
  }
)

// 能力图谱相关API
export const abilityApi = {
  // 获取学生能力图谱数据
  getCapabilityMap(studentId: number) {
    return api.get(`/student/ability/capability-map/${studentId}`)
  },

  // 获取所有学生的能力数据
  getAllStudentsAbility() {
    return api.get('/student/ability/all-students')
  },

  // 更新学生能力数据
  updateStudentAbility(ability: any) {
    return api.put('/student/ability/update', ability)
  },

  // 获取学生语法能力
  getGrammarAbility(studentId: number) {
    return api.get(`/student/ability/grammar/${studentId}`)
  },

  // 添加问题记录
  addQuestionRecord(record: any) {
    return api.post('/student/ability/record', record)
  }
}

// 模拟数据API（用于开发测试）
export const mockAbilityApi = {
  // 获取模拟的能力图谱数据
  getMockCapabilityData() {
    return Promise.resolve({
      code: 200,
      msg: '获取成功',
      data: [
        { abilityType: 'basic', eloScore: 1530, studentId: 1001 },
        { abilityType: 'coding', eloScore: 1480, studentId: 1001 },
        { abilityType: 'modeling', eloScore: 1450, studentId: 1001 },
        { abilityType: 'initiative', eloScore: 1600, studentId: 1001 },
        { abilityType: 'innovation', eloScore: 1400, studentId: 1001 }
      ]
    })
  },

  // 生成随机能力数据
  generateRandomData() {
    const names = ['张三', '李四', '王五', '赵六', '钱七', '孙八', '周九', '吴十']
    const data = names.map((name, index) => {
      const programmingScore = Math.floor(Math.random() * 30) + 70;
      const aiScore = Math.floor(Math.random() * 30) + 70;
      const mathScore = Math.floor(Math.random() * 30) + 70;
      const creativityScore = Math.floor(Math.random() * 30) + 70;
      const communicationScore = Math.floor(Math.random() * 30) + 70;
      const averageScore = (programmingScore + aiScore + mathScore + creativityScore + communicationScore) / 5;
      return {
        id: index + 1,
        studentId: 1001 + index,
        studentName: name,
        programmingScore,
        aiScore,
        mathScore,
        creativityScore,
        communicationScore,
        averageScore
      }
    })

    return Promise.resolve({
      code: 200,
      msg: '生成成功',
      data
    })
  }
}

export default api 