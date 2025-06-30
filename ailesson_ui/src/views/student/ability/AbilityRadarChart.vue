<script setup lang="ts">
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { RadarChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import * as echarts from 'echarts'
import axios from 'axios'
import { ref, nextTick, onMounted, onBeforeUnmount } from 'vue'
import type { Ref } from 'vue'
import type { ECharts } from 'echarts'

use([
  CanvasRenderer,
  RadarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

const props = defineProps({
  userId: {
    type: [String, Number],
    required: true
  }
})

const radarChart = ref(null)

const chartOptions = {
  radar: {
    indicator: [
      { name: '基础知识掌握', max: 2000 },
      { name: '算法实现能力', max: 2000 },
      { name: '问题分析与建模能力', max: 2000 },
      { name: '学习主动性', max: 2000 },
      { name: '综合应用与创新能力', max: 2000 }
    ]
  },
  series: [{
    name: '能力评分',
    type: 'radar',
    data: [
      { value: [1500, 1500, 1500, 1500, 1500], name: '当前能力' }
    ]
  }]
}

const chartInstance: Ref<ECharts | null> = ref(null)

const initChart = () => {
  nextTick(() => {
    if (radarChart.value) {
      chartInstance.value = echarts.init(radarChart.value)
      chartInstance.value.setOption(chartOptions)
      window.addEventListener('resize', handleResize)
    }
  })
}

const loadAbilityData = async () => {
  try {
    const res = await axios.get(`/api/student/ability/capability-map/${props.userId}`);
    if (res.data.code === 200) {
      const abilityData = res.data.data;
      const typeOrder = ['basic', 'coding', 'modeling', 'initiative', 'innovation'];
      const values = typeOrder.map(type => {
        const item = abilityData.find((a: any) => a.abilityType === type);
        return item ? item.eloScore : 1500;
      });
      updateRadarChart(values);
    }
  } catch (error) {
    updateRadarChart([1500, 1500, 1500, 1500, 1500]);
  }
}

const updateRadarChart = (values: number[]) => {
  if (!chartInstance.value) return;
  chartInstance.value.setOption({
    ...chartOptions,
    series: [{
      ...chartOptions.series[0],
      data: [
        {
          value: values,
          name: '当前能力'
        }
      ]
    }]
  });
}

const handleResize = () => {
  if (chartInstance.value) {
    chartInstance.value.resize()
  }
}

onMounted(() => {
  initChart();
  loadAbilityData();
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<template>
  <el-card class="chart-card" style="margin: 40px auto; max-width: 700px;">
    <div ref="radarChart" class="radar-chart-container"></div>
  </el-card>
</template>

<style scoped>
.radar-chart-container {
  width: 600px;
  height: 500px;
  margin: 0 auto;
}
</style>