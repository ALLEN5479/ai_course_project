<script setup lang="ts">
import { computed, ref, onMounted, watch, nextTick, onBeforeUnmount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Document, VideoCamera, Files, Monitor, ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import axios from 'axios'

const router = useRouter()
const route = useRoute()
const goBack = async () => {
  await reportRecord()
  router.back()
}

// 资源类型图标
const typeIcon = (type: string) => {
  switch (type) {
    case 'pdf': return Document
    case 'video': return VideoCamera
    case 'word': return Files
    case 'ppt': return Monitor
    default: return Document
  }
}

const collapsed = ref(false)
const leftPanelMode = ref<'list' | 'desc'>('list')
const toggleCollapse = () => {
  collapsed.value = !collapsed.value
}

// 资源数据
const resources = ref<any[]>([])
const selectedId = ref(0)
const selectedResource = computed(() => resources.value.find(r => r.resource_id === selectedId.value))

// 获取资源ID列表 - 返回多个资源ID
const fetchResourceIds = async (nodeId: number): Promise<number[]> => {
  try {
    const response = await axios.get('http://localhost:8080/getResourceId', {
      params: { nodeId }
    })
    return response.data || []
  } catch (error) {
    console.error('获取资源ID失败:', error)
    return []
  }
}

// 获取单个资源详情 - 使用现有的getresources接口
const fetchSingleResource = async (resourceId: number) => {
  try {
    const response = await axios.get('http://localhost:8080/getresources', {
      params: { resource_id: resourceId }
    })
    // 假设返回的是资源对象列表，取第一个
    return response.data && response.data.length > 0 ? response.data[0] : null
  } catch (error) {
    console.error(`获取资源 ${resourceId} 详情失败:`, error)
    return null
  }
}

// 加载资源 - 处理多个资源ID
const loadResources = async (nodeId: number) => {
  if (!nodeId) {
    resources.value = []
    return
  }

  try {
    // 第一步：获取多个资源ID
    const resourceIds = await fetchResourceIds(nodeId)
    if (resourceIds.length === 0) {
      resources.value = []
      return
    }

    // 第二步：使用现有的getresources接口逐个获取资源详情
    const resourceDetails = []
    for (const id of resourceIds) {
      const resource = await fetchSingleResource(id)
      if (resource) {
        resourceDetails.push(resource)
      }
    }

    resources.value = resourceDetails

    // 设置默认选中的资源（如果有资源）
    if (resourceDetails.length > 0) {
      selectedId.value = resourceDetails[0].resource_id
    } else {
      selectedId.value = 0
    }
  } catch (error) {
    console.error('加载资源失败:', error)
    resources.value = []
    selectedId.value = 0
  }
}

// 监听路由参数变化
watch(() => route.query.nodeId, (newNodeId) => {
  if (newNodeId) {
    loadResources(Number(newNodeId))
  } else {
    resources.value = []
    selectedId.value = 0
  }
})

// 页面加载时初始化
onMounted(() => {
  const nodeId = route.query.nodeId ? Number(route.query.nodeId) : 0
  if (nodeId) {
    loadResources(nodeId)
  }
  // 监听页面关闭
  window.addEventListener('beforeunload', handleBeforeUnload)
})

// 计时相关变量
const isHovering = ref(false)
const hoverStartTime = ref<number | null>(null)
const totalHoverTime = ref(0)

// 鼠标进入资源区
const handleMouseEnter = () => {
  isHovering.value = true
  hoverStartTime.value = Date.now()
}
// 鼠标离开资源区
const handleMouseLeave = () => {
  isHovering.value = false
  if (hoverStartTime.value) {
    const duration = Date.now() - hoverStartTime.value
    totalHoverTime.value += duration
    // 只对非视频资源计时
    if (selectedResource.value && selectedResource.value.res_type !== 'video') {
      currentStudyTime.value += duration
    }
    hoverStartTime.value = null
  }
}

const videoPlayer = ref<HTMLVideoElement | null>(null)

// 跳看统计相关变量
const seekCount = ref(0)
const totalSeekTime = ref(0)
let lastCurrentTime = 0

// 统计相关变量
const currentStudyTime = ref(0) // 当前资源学习时长（ms）
const currentSeekCount = ref(0) // 当前资源跳看次数
let lastResourceId = ref<number | null>(null)

// 记录上报函数
const reportRecord = async () => {
  console.log(userId.value+','+lastResourceId.value+','+currentStudyTime.value+','+currentSeekCount.value)
  if (!userId.value || !lastResourceId.value) return
  try {
    await axios.post('http://localhost:8080/updateRecord', null, {
      params: {
        student_id: userId.value,
        resource_id: lastResourceId.value,
        actual_time: (currentStudyTime.value/1000).toFixed(2),
        jump_time: currentSeekCount.value
      }
    })
  } catch (e) {
    console.error('上报学习记录失败', e)
  }
}

// 重置统计
const resetStats = () => {
  currentStudyTime.value = 0
  currentSeekCount.value = 0
  hoverStartTime.value = null
  totalHoverTime.value = 0
  seekCount.value = 0
  totalSeekTime.value = 0
  lastCurrentTime = 0
}

// 切换资源时上报并重置
watch(selectedId, async (newId, oldId) => {
  if (oldId && lastResourceId.value) {
    // 先上报旧资源
    await reportRecord()
    resetStats()
  }
  lastResourceId.value = newId
})

// 页面关闭/刷新时上报
const handleBeforeUnload = async (e: BeforeUnloadEvent) => {
  await reportRecord()
}

// 页面卸载时移除事件
onBeforeUnmount(() => {
  window.removeEventListener('beforeunload', handleBeforeUnload)
  reportRecord()
})

// 监听selectedResource变化，若为视频则绑定ratechange和seeked事件
watch(selectedResource, async (newVal) => {
  await nextTick()
  if (newVal && newVal.res_type === 'video' && videoPlayer.value) {
    // 绑定事件前先移除旧事件，防止重复
    videoPlayer.value.onratechange = null
    videoPlayer.value.onseeked = null
    lastCurrentTime = 0
    seekCount.value = 0
    totalSeekTime.value = 0
    // 限速
    videoPlayer.value.onratechange = () => {
      if (videoPlayer.value && videoPlayer.value.playbackRate > 1) {
        videoPlayer.value.playbackRate = 1
      }
    }
    // 跳看统计
    videoPlayer.value.onseeked = () => {
      if (videoPlayer.value) {
        const seekedTime = Math.abs(videoPlayer.value.currentTime - lastCurrentTime)
        // 跳看时间大于1秒才计入（防止误差）
        if (seekedTime > 1) {
          seekCount.value++
          totalSeekTime.value += seekedTime
          currentSeekCount.value = seekCount.value
        }
        lastCurrentTime = videoPlayer.value.currentTime
      }
    }
    // 计时：每秒累加实际观看时长
    let lastTime = Date.now()
    const timer = setInterval(() => {
      if (videoPlayer.value && !videoPlayer.value.paused && !videoPlayer.value.ended) {
        const now = Date.now()
        currentStudyTime.value += now - lastTime
        lastTime = now
      } else {
        lastTime = Date.now()
      }
    }, 1000)
    // 组件卸载/切换资源时清理
    onBeforeUnmount(() => clearInterval(timer))
    watch(selectedId, () => clearInterval(timer), { once: true })
    // 初始化时也强制为1倍速
    videoPlayer.value.playbackRate = 1
    lastCurrentTime = 0
  } else if (newVal && newVal.res_type !== 'video') {
    // 非视频资源，跳看次数为0
    currentSeekCount.value = 0
  }
})

const userId = computed(() => route.query.user_id ? Number(route.query.user_id) : 0)
</script>

<template>
  <div class="study-resources">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <h1>学习资源</h1>
          <el-button @click="goBack">返回</el-button>
        </div>
      </el-header>
      <el-main class="main-content">
        <el-row gutter="20">
          <!-- 左侧资源选择栏 -->
          <el-col :span="collapsed ? 1 : 6" class="resource-col">
            <el-card class="resource-list" :class="{ collapsed }">
              <div class="collapse-btn-wrapper" :class="{ collapsed }">
                <el-button circle size="small" @click="toggleCollapse" class="collapse-btn">
                  <el-icon>
                    <component :is="collapsed ? ArrowRight : ArrowLeft" />
                  </el-icon>
                </el-button>
              </div>
              <div v-if="!collapsed" style="display:flex;justify-content:center;margin-bottom:10px;">
                <el-button-group>
                  <el-button :type="leftPanelMode==='list' ? 'primary' : 'default'" size="small" @click="leftPanelMode='list'">资源列表</el-button>
                  <el-button :type="leftPanelMode==='desc' ? 'primary' : 'default'" size="small" @click="leftPanelMode='desc'" :disabled="!selectedResource">资源简介</el-button>
                </el-button-group>
              </div>
              <template v-if="leftPanelMode==='list'">
                <template v-if="resources.length > 0">
                  <el-menu
                      v-if="!collapsed"
                      :default-active="selectedId + ''"
                      class="resource-menu"
                      @select="(key:string) => selectedId = Number(key)"
                  >
                    <el-menu-item v-for="item in resources" :key="item.resource_id" :index="item.resource_id + ''">
                      <el-icon :size="18"><component :is="typeIcon(item.res_type)" /></el-icon>
                      <span style="margin-left:8px">{{ item.resource_name }}</span>
                    </el-menu-item>
                  </el-menu>
                  <div v-show="collapsed" class="icon-list">
                    <div v-for="item in resources" :key="item.resource_id" class="icon-item" @click="selectedId = item.resource_id">
                      <el-tooltip :content="item.resource_name" placement="right">
                        <el-icon :size="20" :class="{'active': selectedId === item.resource_id}"><component :is="typeIcon(item.res_type)" /></el-icon>
                      </el-tooltip>
                    </div>
                  </div>
                </template>
                <div v-else class="empty-resources">
                  <el-empty description="暂无资源数据" />
                </div>
              </template>
              <template v-else-if="leftPanelMode==='desc'">
                <div v-if="selectedResource">
                  <h3 style="margin:10px 0 8px 0;">资源简介</h3>
                  <div style="white-space:pre-line;word-break:break-all;min-height:80px;">{{ selectedResource.res_description || '暂无简介' }}</div>
                </div>
                <div v-else class="empty-resources">
                  <el-empty description="暂无资源数据" />
                </div>
              </template>
            </el-card>
          </el-col>
          <!-- 右侧资源浏览区 -->
          <el-col :span="collapsed ? 23 : 18">
            <el-card class="resource-viewer" @mouseenter="handleMouseEnter" @mouseleave="handleMouseLeave">
              <template v-if="selectedResource">
                <template v-if="selectedResource.res_type === 'pdf'">
                  <iframe :src="selectedResource.resource_url" width="100%" height="600px" style="border:none;" />
                </template>
                <template v-else-if="selectedResource.res_type === 'video'">
                  <video ref="videoPlayer" :src="selectedResource.resource_url" width="100%" height="600px" controls style="background:#000" />
                </template>
                <template v-else-if="selectedResource.res_type === 'word'">
                  <div style="text-align:center;margin-top:20px">
                    <el-link :href="selectedResource.resource_url" target="_blank">下载Word文档</el-link>
                  </div>
                </template>
                <template v-else-if="selectedResource.res_type === 'ppt'">
                  <div style="text-align:center;margin-top:20px">
                    <el-link :href="selectedResource.resource_url" target="_blank">下载PPT文件</el-link>
                  </div>
                </template>
                <template v-else>
                  <el-empty description="暂不支持该类型预览" />
                  <div style="text-align:center;margin-top:20px">
                    <el-link :href="selectedResource.resource_url" target="_blank">下载资源</el-link>
                  </div>
                </template>
              </template>
              <template v-else-if="resources.length > 0">
                <el-empty description="请选择左侧资源" />
              </template>
              <template v-else>
                <el-empty description="暂无资源数据" />
              </template>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>
/* 样式保持不变 */
.study-resources {
  min-height: 100vh;
  background-color: #f5f7fa;
}
.header {
  background: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
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
.main-content {
  padding: 30px;
}
.resource-col {
  transition: width 0.3s;
}
.resource-list {
  width: 320px;
  min-width: 320px;
  max-width: 320px;
  transition: width 0.3s, min-width 0.3s, max-width 0.3s;
}
.resource-list.collapsed {
  width: 48px !important;
  min-width: 48px !important;
  max-width: 48px !important;
  padding: 0;
  overflow: hidden;
}
.collapse-btn-wrapper {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 4px 4px 0 0;
}
.collapse-btn-wrapper.collapsed {
  justify-content: center;
}
.collapse-btn {
  margin-bottom: 8px;
}
.icon-list {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 16px;
}
.icon-item {
  margin: 10px 0;
  cursor: pointer;
}
.icon-item .el-icon.active {
  color: #409EFF;
}
.resource-menu {
  border: none;
}
.resource-viewer {
  min-height: 600px;
  background: #fff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: stretch;
}
.empty-resources {
  padding: 20px;
  text-align: center;
}
</style>