<template>
  <div class="ability-container">
    <div class="page-header">
      <h2>学生能力评估报告</h2>
      <p class="description">基于学生的多维能力数据自动评估</p>
    </div>
    <el-card class="chart-card">
      <div ref="radarChart" class="radar-chart-container"></div>
    </el-card>
    <el-card class="description-card">
      <div slot="header">能力维度说明</div>
      <div class="ability-descriptions">
        <div
            v-for="(item, index) in abilityItems"
            :key="index"
            class="ability-item"
            @click="navigateToDetail(item.type)"
        >
          <h3>{{ item.name }}</h3>
          <p>{{ item.description }}</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
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

use([
  CanvasRenderer,
  RadarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

export default {
  data() {
    return {
      chartOptions: {
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
      },
      chartInstance: null,
      abilityData: [],
      abilityItems: [
        { type: 'basic', name: '基础知识掌握', description: '...' },
        { type: 'coding', name: '算法实现能力', description: '...' },
        { type: 'modeling', name: '问题分析与建模能力', description: '...' },
        { type: 'initiative', name: '学习主动性', description: '...' },
        { type: 'innovation', name: '综合应用与创新能力', description: '...' }
      ]
    }
  },
  mounted() {
    this.initChart();
    this.loadAbilityData();
  },
  methods: {
    async loadAbilityData() {
      try {
        const studentId = 1001; // TODO: 替换为实际登录学生ID
        const res = await axios.get(`/api/student/ability/capability-map/${studentId}`);
        if (res.data.code === 200) {
          this.abilityData = res.data.data;
          const typeOrder = ['basic', 'coding', 'modeling', 'initiative', 'innovation'];
          const values = typeOrder.map(type => {
            const item = this.abilityData.find(a => a.abilityType === type);
            return item ? item.eloScore : 1500;
          });
          this.updateRadarChart(values);
        }
      } catch (error) {
        this.updateRadarChart([1500, 1500, 1500, 1500, 1500]);
      }
    },
    updateRadarChart(values) {
      if (!this.chartInstance) return;
      this.chartInstance.setOption({
        ...this.chartOptions,
        series: [{
          ...this.chartOptions.series[0],
          data: [
            {
              value: values,
              name: '当前能力'
            }
          ]
        }]
      });
    },
    initChart() {
      this.$nextTick(() => {
        if (this.$refs.radarChart) {
          this.chartInstance = echarts.init(this.$refs.radarChart)
          this.chartInstance.setOption(this.chartOptions)
          window.addEventListener('resize', this.handleResize)
        }
      })
    },
    handleResize() {
      if (this.chartInstance) {
        this.chartInstance.resize()
      }
    },
    navigateToDetail(type) {
      this.$router.push({
        path: '/ability/detail',
        query: { type }
      });
    }
  },
  destroyed() {
    window.removeEventListener('resize', this.handleResize)
  }
}
</script>