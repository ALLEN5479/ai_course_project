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
import { ref, nextTick, onMounted, onBeforeUnmount } from 'vue'
import type { Ref } from 'vue'
import type { ECharts } from 'echarts'
import { abilityApi } from '@/api/abilityApi'

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

const indicatorNames = [
  '基础知识掌握',
  '算法实现能力',
  '问题分析与建模能力',
  '学习主动性',
  '综合应用与创新能力'
]

const chartOptions = {
  radar: {
    indicator: indicatorNames.map(name => ({ name, max: 2000 })),
    splitNumber: 5,
    splitLine: {
      lineStyle: {
        width: 2,
        color: [
          '#e0e6f1', // 最外圈
          '#f3f6fa',
          '#f8fafc',
          '#f3f6fa',
          '#e0e6f1'  // 最内圈
        ]
      }
    },
    splitArea: {
      areaStyle: {
        color: [
          'rgba(24,144,255,0.08)',
          'rgba(24,144,255,0.04)',
          'rgba(24,144,255,0.02)',
          'rgba(24,144,255,0.01)',
          'rgba(24,144,255,0.00)'
        ]
      }
    },
    axisLine: {
      lineStyle: {
        color: '#e0e6f1'
      }
    },
    name: {
      textStyle: {
        color: '#333',
        fontSize: 16
      }
    }
  },
  tooltip: {
    show: true,
    formatter: function (params: any) {
      if (params.dimensionIndex !== undefined) {
        return `${indicatorNames[params.dimensionIndex]}<br/>分数: ${params.value[params.dimensionIndex]}`
      }
      return params.name + '<br/>' + params.value.map((v: number, i: number) => `${indicatorNames[i]}: ${v}`).join('<br/>')
    }
  },
  series: [{
    name: '当前能力',
    type: 'radar',
    data: [
      { value: [1500, 1500, 1500, 1500, 1500], name: '当前能力', areaStyle: { color: 'rgba(24, 144, 255, 0.8)' } }
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

const getFixedFakeAbility = (userId: string | number) => {
  // 尝试从 localStorage 取
  const key = `fakeAbility_${userId}`
  const cached = localStorage.getItem(key)
  if (cached) {
    return JSON.parse(cached)
  }
  // 没有就生成
  const fake = {
    coding: 1500 + Math.floor(Math.random() * 200 - 100),
    modeling: 1500 + Math.floor(Math.random() * 200 - 100),
    initiative: 1500 + Math.floor(Math.random() * 200 - 100),
    innovation: 1500 + Math.floor(Math.random() * 200 - 100)
  }
  localStorage.setItem(key, JSON.stringify(fake))
  return fake
}

const loadAbilityData = async () => {
  try {
    const res = await abilityApi.getCapabilityMap(Number(props.userId))
    let abilityData = []
    if (res.code === 200) {
      abilityData = res.data
    }
    const typeOrder = ['basic', 'coding', 'modeling', 'initiative', 'innovation']
    const values = typeOrder.map(type => {
      const item = abilityData.find((a: { abilityType: string; eloScore: number }) => a.abilityType === type)
      return item ? item.eloScore : 1500
    })
    updateRadarChart(values)
  } catch (error) {
    updateRadarChart([1500, 1260, 1380, 1500, 1220])
  }
}

const updateRadarChart = (values: number[]) => {
  if (!chartInstance.value) return
  chartInstance.value.setOption({
    ...chartOptions,
    series: [{
      ...chartOptions.series[0],
      data: [
        {
          value: values,
          name: '当前能力',
          areaStyle: { color: 'rgba(24, 144, 255, 0.8)' }
        }
      ]
    }]
  })
}

const handleResize = () => {
  if (chartInstance.value) {
    chartInstance.value.resize()
  }
}

onMounted(() => {
  initChart()
  loadAbilityData()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<template>
  <el-card class="chart-card" style="margin: 40px auto; max-width: 700px;">
    <div style="text-align:center; font-size:20px; font-weight:bold; margin-bottom:10px;">课程能力图谱</div>
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