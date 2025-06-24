<template>
  <div class="knowledge-graph">
    <el-header class="top-bar">
      <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      <span class="page-title">知识图谱管理</span>
    </el-header>
    <el-card>
      <div slot="header">
        <span>知识图谱管理</span>
      </div>
      <div style="height: 400px; display: flex; align-items: center; justify-content: center;">
        <div>（此处为知识图谱可视化区域，后续可用ECharts等实现）</div>
      </div>
      <el-button type="primary" @click="showUpload = true">点击节点上传资源（示例）</el-button>
      <el-dialog v-model="showUpload" title="上传资源" width="400px">
        <el-form>
          <el-form-item label="资源名称">
            <el-input v-model="resourceName" />
          </el-form-item>
          <el-form-item label="资源文件">
            <el-upload action="#" :auto-upload="false">
              <el-button>选择文件</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showUpload = false">取消</el-button>
          <el-button type="primary" @click="handleUpload">上传</el-button>
        </template>
      </el-dialog>
      <el-button style="margin-top: 16px;" @click="showEdit = true">编辑节点内容（示例）</el-button>
      <el-dialog v-model="showEdit" title="编辑节点" width="400px">
        <el-form>
          <el-form-item label="节点名称">
            <el-input v-model="nodeName" />
          </el-form-item>
          <el-form-item label="节点描述">
            <el-input v-model="nodeDesc" type="textarea" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showEdit = false">取消</el-button>
          <el-button type="primary" @click="handleEdit">保存</el-button>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const showUpload = ref(false);
const showEdit = ref(false);
const resourceName = ref('');
const nodeName = ref('');
const nodeDesc = ref('');
const router = useRouter();
function handleUpload() {
  showUpload.value = false;
}
function handleEdit() {
  showEdit.value = false;
}
function goBack() {
  router.back();
}
</script>

<style scoped>
.top-bar {
  display: flex;
  align-items: center;
  height: 56px;
  background: #fff;
  border-bottom: 1px solid #eee;
  padding: 0 24px;
  margin-bottom: 16px;
}
.page-title {
  font-size: 20px;
  font-weight: bold;
  margin-left: 16px;
}
.knowledge-graph {
  padding: 24px;
}
</style> 