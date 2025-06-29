<template>
  <div class="student-management">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>学生管理</h1>
          <div class="header-actions">
            <el-button type="primary" @click="addStudent">添加学生</el-button>
            <el-button @click="goBack">返回主页</el-button>
          </div>
        </div>
      </el-header>

      <el-main class="main-content">
        <!-- 搜索和筛选 -->
        <el-card class="search-card">
          <div class="search-section">
            <el-form :inline="true" :model="searchForm" class="search-form">
              <el-form-item label="学号">
                <el-input
                    v-model="searchForm.studentId"
                    placeholder="请输入学号"
                    clearable
                    style="width: 150px"
                />
              </el-form-item>
              <el-form-item label="姓名">
                <el-input
                    v-model="searchForm.name"
                    placeholder="请输入姓名"
                    clearable
                    style="width: 150px"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSearch">搜索</el-button>
                <el-button @click="handleReset">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>

        <!-- 学生列表 -->
        <el-card class="student-list-card">
          <div class="card-header">
            <h3>学生列表</h3>
            <div class="list-stats">
              <span>共 {{ filteredStudents.length }} 名学生</span>
            </div>
          </div>

          <el-table
              :data="pagedStudents"
              style="width: 100%"
              v-loading="loading"
              stripe
          >
            <el-table-column prop="studentId" label="学号" width="180" />
            <el-table-column prop="name" label="姓名" width="180" />
            <el-table-column prop="subjects" label="科目" width="280">
              <template #default="scope">
                <div class="subject-list">
                  <el-tag
                      v-for="(subject, index) in scope.row.subjects"
                      :key="index"
                      type="info"
                      class="subject-tag"
                  >
                    {{ subject }}
                  </el-tag>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="140">
              <template #default="scope">
                <el-tag type="success">在读</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="240" fixed="right">
              <template #default="scope">
                <el-button
                    size="small"
                    type="primary"
                    @click="viewStudentProgress(scope.row)"
                >
                  查看学习状况
                </el-button>
                <el-button
                    size="small"
                    type="danger"
                    @click="deleteStudent(scope.row)"
                >
                  删除学生
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <div class="pagination-container">
            <el-pagination
                v-model:current-page="currentPage"
                v-model:page-size="pageSize"
                :page-sizes="[10, 20, 50, 100]"
                :total="filteredStudents.length"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
            />
          </div>
        </el-card>
      </el-main>
    </el-container>

    <!-- 添加学生对话框 -->
    <el-dialog v-model="addStudentDialogVisible" title="添加学生" width="500px">
      <el-form :model="addStudentForm" :rules="addStudentRules" ref="addStudentFormRef" label-width="80px">
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="addStudentForm.studentId" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="addStudentForm.name" placeholder="请输入姓名" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addStudentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddStudent">确定</el-button>
      </template>
    </el-dialog>

    <!-- 删除确认对话框 -->
    <el-dialog v-model="deleteDialogVisible" title="确认删除" width="400px">
      <p>确定要删除学生 <strong>{{ selectedStudent?.name }}</strong> ({{ selectedStudent?.studentId }}) 吗？</p>
      <p style="color: #f56c6c; font-size: 14px;">此操作不可恢复，请谨慎操作！</p>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmDeleteStudent">确定删除</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import axios from 'axios'

const router = useRouter()
const route = useRoute()
const teacherId = ref(route.query.teacherId || '')

// 加载状态
const loading = ref(false)

// 搜索表单
const searchForm = ref({
  studentId: '',
  name: ''
})

// 学生列表数据
const students = ref<Array<{
  id: string
  studentId: string
  name: string
  subjects: string[]
  status: string
}>>([])

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)

// 过滤后的学生列表
const filteredStudents = computed(() => {
  return students.value.filter(student => {
    const matchStudentId = !searchForm.value.studentId ||
        student.studentId.includes(searchForm.value.studentId)
    const matchName = !searchForm.value.name ||
        student.name.includes(searchForm.value.name)
    return matchStudentId && matchName
  })
})

// 分页后的学生列表
const pagedStudents = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredStudents.value.slice(start, end)
})

// 添加学生相关
const addStudentDialogVisible = ref(false)
const addStudentFormRef = ref<FormInstance>()
const addStudentForm = ref({
  studentId: '',
  name: ''
})

// 添加学生表单验证规则
const addStudentRules: FormRules = {
  studentId: [
    { required: true, message: '请输入学号', trigger: 'blur' },
    { min: 3, max: 20, message: '学号长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度在 2 到 10 个字符', trigger: 'blur' }
  ]
}

// 删除学生相关
const deleteDialogVisible = ref(false)
const selectedStudent = ref<any>(null)

// 搜索和重置
const handleSearch = () => {
  currentPage.value = 1
  ElMessage.success('搜索完成')
}

const handleReset = () => {
  searchForm.value = {
    studentId: '',
    name: ''
  }
  currentPage.value = 1
  ElMessage.success('已重置搜索条件')
}

// 分页处理
const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
}

// 添加学生
const addStudent = () => {
  addStudentForm.value = {
    studentId: '',
    name: ''
  }
  addStudentDialogVisible.value = true
}

const handleAddStudent = async () => {
  if (!addStudentFormRef.value) return

  try {
    await addStudentFormRef.value.validate()

    // 检查学号是否已存在
    const existingStudent = students.value.find(s => s.studentId === addStudentForm.value.studentId)
    if (existingStudent) {
      ElMessage.error('该学号已存在')
      return
    }

    // 添加新学生
    const newStudent = {
      id: `new_${Date.now()}`,
      studentId: addStudentForm.value.studentId,
      name: addStudentForm.value.name,
      subjects: [],
      status: 'active'
    }

    students.value.push(newStudent)
    addStudentDialogVisible.value = false
    ElMessage.success('学生添加成功')
  } catch (error) {
    ElMessage.error('请检查表单信息')
  }
}

// 查看学生学习状况
const viewStudentProgress = (student: any) => {
  router.push({
    path: `/teacher/ability-map/${student.id}`,
    query: {
      studentId: student.studentId,
      studentName: student.name
    }
  })
}

// 删除学生
const deleteStudent = (student: any) => {
  selectedStudent.value = student
  deleteDialogVisible.value = true
}

const confirmDeleteStudent = () => {
  if (!selectedStudent.value) return

  const index = students.value.findIndex(s => s.id === selectedStudent.value.id)
  if (index > -1) {
    students.value.splice(index, 1)
    ElMessage.success('学生删除成功')
  }

  deleteDialogVisible.value = false
  selectedStudent.value = null
}

// 返回主页
const goBack = () => {
  router.push({
    path: '/teacher/dashboard',
    query: {
      ...route.query
    }
  })
}

// 处理学生数据，合并同一学生的多个科目
const processStudentData = (data: any[]) => {
  const studentMap = new Map()

  data.forEach(item => {
    const { student_id, name, course_name } = item
    const studentId = student_id

    if (studentMap.has(studentId)) {
      // 如果学生已存在，添加科目
      const existingStudent = studentMap.get(studentId)
      if (course_name && !existingStudent.subjects.includes(course_name)) {
        existingStudent.subjects.push(course_name)
      }
    } else {
      // 创建新学生记录
      studentMap.set(studentId, {
        id: studentId,
        studentId: studentId,
        name: name,
        subjects: course_name ? [course_name] : [],
        status: 'active'
      })
    }
  })

  return Array.from(studentMap.values())
}

// 获取学生数据
const fetchStudents = async () => {
  if (!teacherId.value) {
    ElMessage.error('缺少教师ID')
    return
  }
  console.log('teacherId:', teacherId.value)
  loading.value = true
  try {
    const response = await axios.get('http://localhost:8080/getStudentNameAndId', {
      params: { teacher_id: teacherId.value }
    })

    console.log('response.data:', response.data)
    if (response.data && Array.isArray(response.data)) {
      students.value = processStudentData(response.data)
    } else {
      ElMessage.warning('未获取到学生数据')
    }
  } catch (error) {
    console.error('获取学生数据失败:', error)
    ElMessage.error('获取学生数据失败')
  } finally {
    loading.value = false
  }
}

// 组件挂载时获取数据
onMounted(() => {
  fetchStudents()
})
</script>

<style scoped>
.student-management {
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

.header-actions {
  display: flex;
  gap: 12px;
}

.main-content {
  padding: 30px;
}

.search-card {
  margin-bottom: 20px;
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.search-section {
  padding: 20px 0;
}

.search-form {
  margin: 0;
}

.student-list-card {
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  max-width: 900px;
  margin: 0 auto;
  padding: 24px 32px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.card-header h3 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.list-stats {
  color: #666;
  font-size: 14px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.subject-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.subject-tag {
  margin-right: 6px;
  margin-bottom: 6px;
}

/* 表格样式优化 */
:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table th),
:deep(.el-table td) {
  font-size: 16px;
}

/* 按钮样式优化 */
:deep(.el-button--small) {
  padding: 6px 12px;
  font-size: 12px;
}

/* 对话框样式 */
:deep(.el-dialog) {
  border-radius: 8px;
}

:deep(.el-dialog__header) {
  border-bottom: 1px solid #eee;
  padding: 20px 20px 15px;
}

:deep(.el-dialog__body) {
  padding: 20px;
}

:deep(.el-dialog__footer) {
  border-top: 1px solid #eee;
  padding: 15px 20px 20px;
}
</style>