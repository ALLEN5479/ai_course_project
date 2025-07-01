<script setup lang="ts">
import { ref } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

// 定义组件接收的属性
const props = defineProps({
  courseId: {
    type: Number,
    required: true
  }
});

const file = ref<File | null>(null);
const description = ref('');
const isLoading = ref(false);
const uploadStatus = ref({ success: false, message: '' });

const handleFileChange = (e: Event) => {
  const target = e.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    file.value = target.files[0];
    uploadStatus.value = { success: false, message: '' }; // 重置状态
  }
};

const uploadResource = async () => {
  if (!file.value) {
    ElMessage.warning('请选择文件');
    return;
  }

  isLoading.value = true;
  uploadStatus.value = { success: false, message: '' };

  const formData = new FormData();
  formData.append('file', file.value);
  formData.append('description', description.value);
  formData.append('courseId', props.courseId.toString()); // 添加课程ID

  try {
    const response = await axios.post('http://localhost:8080/uploadResource', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });

    if (response.status === 200) {
      uploadStatus.value = {
        success: true,
        message: '文件上传成功！'
      };
      ElMessage.success('文件上传成功');
      // 清空表单
      file.value = null;
      description.value = '';
    }
  } catch (error) {
    console.error('上传失败:', error);
  } finally {
    isLoading.value = false;
  }
};

const resetForm = () => {
  file.value = null;
  description.value = '';
  uploadStatus.value = { success: false, message: '' };
};
</script>

<template>
  <div class="resource-upload">
    <div class="upload-header">
      <h2>课程资源上传</h2>
    </div>

    <div class="upload-container">
      <div class="upload-area" @click="$refs.fileInput.click()">
        <input
            type="file"
            ref="fileInput"
            @change="handleFileChange"
            class="file-input"
        />
        <div v-if="file" class="file-info">
          <p class="file-name">{{ file.name }}</p>
          <p class="file-size">大小: {{ (file.size / 1024 / 1024).toFixed(2) }} MB</p>
        </div>
        <div v-else class="upload-prompt">
          <el-icon class="upload-icon"><Upload /></el-icon>
          <p>点击此处上传文件</p>
          <p class="file-types">支持所有文件类型</p>
        </div>
      </div>

      <div class="description-area">
        <label>资源描述:</label>
        <el-input
            type="textarea"
            v-model="description"
            placeholder="请输入资源描述..."
            :rows="4"
        />
      </div>

      <div class="status-message" :class="{ success: uploadStatus.success, error: !uploadStatus.success }">
        {{ uploadStatus.message }}
      </div>

      <div class="action-buttons">
        <el-button @click="resetForm" class="cancel-button">重置</el-button>
        <el-button
            type="primary"
            @click="uploadResource"
            :loading="isLoading"
            :disabled="!file"
            class="upload-button"
        >
          {{ isLoading ? '上传中...' : '确认上传' }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.resource-upload {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.upload-header {
  text-align: center;
  margin-bottom: 25px;
}

.upload-header h2 {
  margin: 0 0 10px 0;
  color: #2c3e50;
  font-size: 24px;
}

.upload-header p {
  color: #7f8c8d;
  font-size: 14px;
}

.upload-container {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.upload-area {
  border: 2px dashed #7ec8e1;
  border-radius: 8px;
  padding: 40px 20px;
  text-align: center;
  background: #f9fcff;
  transition: all 0.3s;
  cursor: pointer;
}

.upload-area:hover {
  background: #e6f4ff;
  border-color: #0088b4;
}

.file-input {
  display: none;
}

.file-info, .upload-prompt {
  color: #7ec8e1;
}

.upload-icon {
  font-size: 60px;
  margin-bottom: 15px;
  color: #7ec8e1;
}

.file-name {
  font-weight: bold;
  margin-bottom: 5px;
  color: #333;
  font-size: 16px;
}

.file-size {
  color: #777;
  font-size: 14px;
}

.file-types {
  color: #999;
  font-size: 14px;
  margin-top: 8px;
}

.description-area label {
  font-weight: 500;
  color: #444;
  display: block;
  margin-bottom: 8px;
}

.status-message {
  text-align: center;
  padding: 10px;
  border-radius: 4px;
  margin-top: 10px;
  font-size: 14px;
}

.status-message.success {
  background: #f0f9eb;
  color: #67c23a;
}

.status-message.error {
  background: #fef0f0;
  color: #f56c6c;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 15px;
}

.cancel-button {
  padding: 12px 30px;
}

.upload-button {
  padding: 12px 30px;
}
</style>