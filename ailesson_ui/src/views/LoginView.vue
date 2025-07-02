<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h1>智能课程系统</h1>
        <p>欢迎使用智能课程学习平台</p>
      </div>

      <!-- 切换按钮 -->
      <div class="tab-container">
        <el-button
            :type="activeTab === 'login' ? 'primary' : 'default'"
            @click="activeTab = 'login'"
            class="tab-btn"
        >
          登录
        </el-button>
        <el-button
            :type="activeTab === 'register' ? 'primary' : 'default'"
            @click="activeTab = 'register'"
            class="tab-btn"
        >
          注册
        </el-button>
      </div>

      <!-- 登录表单 -->
      <el-form
          v-if="activeTab === 'login'"
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
      >
        <el-form-item prop="userNumber">
          <el-input
              v-model="loginForm.userNumber"
              placeholder="请输入用户编号"
              prefix-icon="User"
              size="large"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="Lock"
              size="large"
              show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              size="large"
              class="submit-btn"
              @click="handleLogin"
              :loading="loading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 注册表单 -->
      <el-form
          v-if="activeTab === 'register'"
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          class="register-form"
      >
        <el-form-item prop="userNumber">
          <el-input
              v-model="registerForm.userNumber"
              placeholder="请输入学号/教师编号"
              prefix-icon="User"
              size="large"
          />
        </el-form-item>

        <el-form-item prop="realName">
          <el-input
              v-model="registerForm.realName"
              placeholder="请输入真实姓名"
              prefix-icon="UserFilled"
              size="large"
          />
        </el-form-item>

        <el-form-item prop="userType">
          <el-select
              v-model="registerForm.userType"
              placeholder="请选择用户类型"
              size="large"
              style="width: 100%"
          >
            <el-option label="学生" :value="1" />
            <el-option label="教师" :value="2" />
          </el-select>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="Lock"
              size="large"
              show-password
          />
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请确认密码"
              prefix-icon="Lock"
              size="large"
              show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              size="large"
              class="submit-btn"
              @click="handleRegister"
              :loading="loading"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const activeTab = ref('login')
const loading = ref(false)

// 登录表单
const loginFormRef = ref<FormInstance>()
const loginForm = reactive({
  userNumber: '',
  password: ''
})

// 注册表单
const registerFormRef = ref<FormInstance>()
const registerForm = reactive({
  userNumber: '',
  realName: '',
  userType: '',
  password: '',
  confirmPassword: ''
})

// 登录验证规则
const loginRules: FormRules = {
  userNumber: [
    { required: true, message: '请输入用户编号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 注册验证规则
const registerRules: FormRules = {
  userNumber: [
    { required: true, message: '请输入学号/教师编号', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  userType: [
    { required: true, message: '请选择用户类型', trigger: 'change' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  try {
    await loginFormRef.value.validate()
    loading.value = true
    const res = await axios.post('http://localhost:8080/login', null, {
      params: {
        user_id: loginForm.userNumber,
        password: loginForm.password
      }
    })
    console.log('login response:', res)
    if (res.data.code === 200) {
      localStorage.setItem('userInfo', JSON.stringify(res.data.data))
      localStorage.setItem('user_id', res.data.data.user_id)
      if (res.data.data.type === 1) {
        ElMessage.success(`欢迎学生 ${res.data.data.name} 登录！`)
        router.push({ path: '/student/dashboard', query: { name: res.data.data.name, user_id: res.data.data.user_id } })
      } else if (res.data.data.type === 2) {
        ElMessage.success(`欢迎教师 ${res.data.data.name} 登录！`)
        router.push({ 
          path: '/teacher/dashboard', 
          query: { 
            teacherId: loginForm.userNumber,
            teacherName: loginForm.password
          } 
        })
      } else {
        ElMessage.success('登录成功')
        router.push('/')
      }
    } else {
      ElMessage.error('用户名或密码错误')
    }
  } catch (error) {
    ElMessage.error('登录失败')
  } finally {
    loading.value = false
  }
}

// 处理注册
const handleRegister = async () => {
  if (!registerFormRef.value) return
  try {
    await registerFormRef.value.validate()
    loading.value = true
    const res = await axios.post('http://localhost:8080/register', {
      user_id: registerForm.userNumber,
      name: registerForm.realName,
      password: registerForm.password,
      type: registerForm.userType
    })
    if (res.data === true) {
      ElMessage.success('注册成功，请登录')
      activeTab.value = 'login'
    } else {
      ElMessage.error('注册失败，用户已存在或信息有误')
    }
  } catch (error) {
    ElMessage.error('注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #99dada 0%, #61ecdb 100%);
  padding: 20px;
}

.login-box {
  background: white;
  border-radius: 12px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h1 {
  color: #333;
  margin-bottom: 10px;
  font-size: 28px;
  font-weight: 600;
}

.login-header p {
  color: #666;
  font-size: 14px;
}

.tab-container {
  display: flex;
  margin-bottom: 30px;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e4e7ed;
}

.tab-btn {
  flex: 1;
  border-radius: 0;
  border: none;
}

.tab-btn:first-child {
  border-top-left-radius: 8px;
  border-bottom-left-radius: 8px;
}

.tab-btn:last-child {
  border-top-right-radius: 8px;
  border-bottom-right-radius: 8px;
}

.login-form,
.register-form {
  width: 100%;
}

.submit-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
}

:deep(.el-select .el-input__wrapper) {
  border-radius: 8px;
}
</style> 