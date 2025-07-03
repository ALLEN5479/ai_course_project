<template>
  <div class="ai-chat-modal" v-if="isVisible">
    <div class="modal-overlay" @click="closeModal"></div>
    <div class="modal-container">
      <!-- 模态框头部 -->
      <div class="modal-header">
        <h3 class="modal-title">
          <i class="ai-icon">🤖</i>
          AI智能助手
        </h3>
        <button class="close-btn" @click="closeModal">
          <span>&times;</span>
        </button>
      </div>

      <!-- 聊天消息区域 -->
      <div class="chat-container" ref="chatContainer">
        <div class="messages" ref="messagesContainer">
          <!-- 欢迎消息 -->
          <div class="message system-message">
            <div class="message-content">
              <p>👋 你好！我是AI智能助手，可以帮你解答编程相关的问题。</p>
              <p>你可以问我：</p>
              <ul>
                <li>编程概念和原理</li>
                <li>代码实现和优化</li>
                <li>技术选型建议</li>
                <li>学习路径指导</li>
              </ul>
            </div>
          </div>

          <!-- 用户消息 -->
          <div
            v-for="(message, index) in messages"
            :key="index"
            class="message"
            :class="message.role === 'user' ? 'user-message' : 'ai-message'"
          >
            <div class="message-avatar">
              <span v-if="message.role === 'user'">👤</span>
              <span v-else>🤖</span>
            </div>
            <div class="message-content">
              <div class="message-text" v-html="formatMessage(message.content)"></div>
              <div class="message-time">{{ formatTime(message.timestamp) }}</div>
              <div v-if="message.fileName" class="uploaded-file-in-msg">
                <span>📎 已上传文件：{{ message.fileName }}</span>
              </div>
            </div>
          </div>

          <!-- 加载状态 -->
          <div v-if="isLoading" class="message ai-message">
            <div class="message-avatar">
              <span>🤖</span>
            </div>
            <div class="message-content">
              <div class="typing-indicator">
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="input-container">
        <div class="input-wrapper">
          <!-- 美化后的文件选择按钮 -->
          <input
            ref="fileInput"
            type="file"
            @change="handleFileChange"
            :disabled="isLoading || fileUploading"
            style="display: none;"
          />
          <button
            class="custom-file-btn"
            :disabled="isLoading || fileUploading"
            @click="triggerFileSelect"
            type="button"
          >
            <svg style="vertical-align: middle; margin-right: 6px;" width="18" height="18" fill="none" viewBox="0 0 24 24"><path fill="#fff" d="M16.5 13.5a4.5 4.5 0 1 1-9 0 4.5 4.5 0 0 1 9 0Z"/><path stroke="#fff" stroke-width="2" d="M12 3v10.5M12 13.5l3-3m-3 3-3-3"/></svg>
            选择文件
          </button>
          <div v-if="uploadedFileName" class="uploaded-file" style="margin-right: 12px;">
            <span>{{ uploadedFileName }}</span>
            <button @click="cancelUpload" class="cancel-upload-btn">取消上传</button>
          </div>
          <textarea
            v-model="inputMessage"
            @keydown.enter.prevent="handleEnterKey"
            @keydown.ctrl.enter="sendMessage"
            placeholder="输入你的问题... (Enter发送，Ctrl+Enter换行)"
            class="message-input"
            :disabled="isLoading"
            ref="messageInput"
          ></textarea>
          <button
            @click="sendMessage"
            class="send-btn"
            :disabled="isLoading || !inputMessage.trim()"
          >
            <span v-if="!isLoading">发送</span>
            <span v-else>发送中...</span>
          </button>
        </div>
        <div class="input-tips">
          <span>💡 提示：可以上传代码文件并结合问题提问</span>
        </div>
      </div>
      
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick, watch, onMounted } from 'vue';
import axios from 'axios';

// 消息类型定义
interface ChatMessage {
  role: 'user' | 'assistant' | 'system';
  content: string;
  timestamp: Date;
  fileName?: string;
}

// Props
interface Props {
  isVisible: boolean;
}

// Emits
interface Emits {
  (e: 'close'): void;
}

const props = defineProps<Props>();
const emit = defineEmits<Emits>();

// 响应式数据
const messages = ref<ChatMessage[]>([]);
const inputMessage = ref('');
const isLoading = ref(false);
const chatContainer = ref<HTMLElement>();
const messagesContainer = ref<HTMLElement>();
const messageInput = ref<HTMLTextAreaElement>();

const uploadedFileName = ref('');
const uploadedFileContent = ref('');
const fileUploading = ref(false);
const fileInput = ref<HTMLInputElement | null>(null)

// API配置
const API_URL = 'http://localhost:8080/api/chat';

// 关闭模态框
const closeModal = () => {
  emit('close');
};

// 发送消息
const sendMessage = async () => {
  const message = inputMessage.value.trim();
  if (!message || isLoading.value) return;

  // 添加用户消息
  const userMessage: ChatMessage = {
    role: 'user',
    content: message,
    timestamp: new Date(),
    fileName: uploadedFileName.value || undefined
  };
  messages.value.push(userMessage);

  // 清空输入框
  inputMessage.value = '';
  isLoading.value = true;

  try {
    // 调用AI API
    const payload: any = {
      message: message,
      conversationId: generateConversationId()
    };
    if (uploadedFileContent.value) {
      payload.fileContent = uploadedFileContent.value;
    }
    const response = await axios.post(`${API_URL}/send`, payload);

    // 添加AI回复
    const aiMessage: ChatMessage = {
      role: 'assistant',
      content: response.data.response || '抱歉，我现在无法回答这个问题。',
      timestamp: new Date()
    };
    messages.value.push(aiMessage);

  } catch (error: any) {
    console.error('发送消息失败:', error);
    // 添加错误消息
    const errorMessage: ChatMessage = {
      role: 'assistant',
      content: '抱歉，网络连接出现问题，请稍后重试。',
      timestamp: new Date()
    };
    messages.value.push(errorMessage);
  } finally {
    // 自动清空上传文件历史
    uploadedFileName.value = '';
    uploadedFileContent.value = '';
    // 清空文件input
    if (fileInput.value) fileInput.value.value = '';
    isLoading.value = false;
    await nextTick();
    scrollToBottom();
  }
};

// 处理Enter键
const handleEnterKey = (event: KeyboardEvent) => {
  if (event.ctrlKey) {
    // Ctrl+Enter 换行
    return;
  }
  // Enter 发送消息
  sendMessage();
};

// 文件上传
const handleFileChange = async (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0];
  if (!file) return;
  fileUploading.value = true;
  const formData = new FormData();
  formData.append('file', file);
  try {
    const res = await axios.post(`${API_URL}/upload`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    uploadedFileName.value = res.data.fileName;
    uploadedFileContent.value = res.data.fileContent;
  } catch (e) {
    alert('文件上传失败');
  } finally {
    fileUploading.value = false;
  }
};

// 取消上传
const cancelUpload = () => {
  uploadedFileName.value = '';
  uploadedFileContent.value = '';
};

// 格式化消息内容（支持换行和代码块）
const formatMessage = (content: string) => {
  return content
    .replace(/\n/g, '<br>')
    .replace(/```(\\w+)?\\n([\\s\\S]*?)```/g, '<pre><code>$2</code></pre>')
    .replace(/`([^`]+)`/g, '<code>$1</code>');
};

// 格式化时间
const formatTime = (timestamp: Date) => {
  return timestamp.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  });
};

// 滚动到底部
const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
  }
};

// 生成会话ID
const generateConversationId = () => {
  return `chat_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
};

// 监听消息变化，自动滚动
watch(messages, () => {
  nextTick(() => {
    scrollToBottom();
  });
});

// 监听模态框显示状态
watch(() => props.isVisible, (newValue) => {
  if (newValue) {
    // 模态框打开时，聚焦到输入框
    nextTick(() => {
      messageInput.value?.focus();
    });
  }
});

// 组件挂载时初始化
onMounted(() => {
  if (props.isVisible) {
    messageInput.value?.focus();
  }
});

function triggerFileSelect() {
  fileInput.value?.click()
}
</script>

<style scoped>
.ai-chat-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
}

.modal-container {
  position: relative;
  width: 90%;
  max-width: 800px;
  height: 80%;
  max-height: 600px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.modal-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
}

.ai-icon {
  font-size: 20px;
}

.close-btn {
  background: none;
  border: none;
  color: white;
  font-size: 24px;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.close-btn:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.chat-container {
  flex: 1;
  overflow: hidden;
  background-color: #f8f9fa;
}

.messages {
  height: 100%;
  overflow-y: auto;
  padding: 20px;
  scroll-behavior: smooth;
}

.message {
  display: flex;
  margin-bottom: 16px;
  gap: 12px;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
}

.user-message {
  flex-direction: row-reverse;
}

.user-message .message-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.ai-message .message-avatar {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.message-content {
  max-width: 70%;
  background: white;
  padding: 12px 16px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: relative;
}

.user-message .message-content {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.system-message .message-content {
  background: #e3f2fd;
  border-left: 4px solid #2196f3;
}

.message-text {
  line-height: 1.5;
  margin-bottom: 4px;
}

.message-text pre {
  background: #f5f5f5;
  padding: 8px;
  border-radius: 4px;
  overflow-x: auto;
  margin: 8px 0;
}

.message-text code {
  background: #f0f0f0;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: 'Courier New', monospace;
}

.user-message .message-text pre {
  background: rgba(255, 255, 255, 0.1);
}

.user-message .message-text code {
  background: rgba(255, 255, 255, 0.2);
}

.message-time {
  font-size: 12px;
  color: #666;
  opacity: 0.8;
}

.user-message .message-time {
  color: rgba(255, 255, 255, 0.8);
}

.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 8px 0;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #ccc;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-indicator span:nth-child(1) { animation-delay: -0.32s; }
.typing-indicator span:nth-child(2) { animation-delay: -0.16s; }

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.input-container {
  padding: 20px;
  background: white;
  border-top: 1px solid #e0e0e0;
}

.input-wrapper {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.message-input {
  flex: 1;
  min-height: 60px;
  max-height: 120px;
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  line-height: 1.5;
  resize: vertical;
  transition: border-color 0.2s;
  font-family: inherit;
}

.message-input:focus {
  outline: none;
  border-color: #667eea;
}

.message-input:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.send-btn {
  padding: 12px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  min-width: 80px;
}

.send-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.send-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.input-tips {
  margin-top: 8px;
  font-size: 12px;
  color: #666;
  text-align: center;
}

/* 滚动条样式 */
.messages::-webkit-scrollbar {
  width: 6px;
}

.messages::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.messages::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.messages::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .modal-container {
    width: 95%;
    height: 90%;
  }
  
  .message-content {
    max-width: 85%;
  }
  
  .input-wrapper {
    flex-direction: column;
  }
  
  .send-btn {
    align-self: flex-end;
  }
}

.uploaded-file {
  display: inline-flex;
  align-items: center;
  background: #f0f0f0;
  border-radius: 4px;
  padding: 2px 8px;
  font-size: 13px;
  margin-right: 8px;
}
.cancel-upload-btn {
  background: none;
  border: none;
  color: #f5576c;
  margin-left: 6px;
  cursor: pointer;
  font-size: 13px;
}

.custom-file-btn {
  background: linear-gradient(90deg, #1C64F2 0%, #67e8f9 100%);
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 8px 20px;
  font-size: 16px;
  cursor: pointer;
  transition: box-shadow 0.2s;
  display: flex;
  align-items: center;
  font-weight: 500;
}
.custom-file-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}
.custom-file-btn:hover:not(:disabled) {
  box-shadow: 0 2px 8px rgba(28, 100, 242, 0.18);
}

.uploaded-file-in-msg {
  margin-top: 6px;
  font-size: 13px;
  color: #409eff;
  word-break: break-all;
}
</style> 