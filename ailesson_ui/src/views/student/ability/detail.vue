<template>
  <div class="ability-detail-container">
    <el-card>
      <div slot="header">
        <span>{{ abilityTitle }} 评分细则</span>
      </div>
      <div>
        <el-divider>能力成长曲线</el-divider>
        <div ref="growthChart" style="width: 100%; height: 300px; margin-bottom: 24px;"></div>
        <div v-if="detail">
          <h3>评分细则</h3>
          <ul>
            <li v-for="(rule, idx) in detail.rules" :key="idx">{{ rule }}</li>
          </ul>
          <h3>能力说明</h3>
          <p>{{ detail.description }}</p>
        </div>
        <div v-else>
          <el-alert title="未找到该能力维度的评分细则" type="warning" show-icon />
        </div>
      </div>
      <el-button type="primary" @click="$router.back()" style="margin-top: 20px;">返回</el-button>
    </el-card>
  </div>
</template>

<script>
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  GridComponent
} from 'echarts/components'
import * as echarts from 'echarts'

use([
  CanvasRenderer,
  LineChart,
  TitleComponent,
  TooltipComponent,
  GridComponent
])

export default {
  name: 'AbilityDetail',
  data() {
    return {
      abilityMap: {
        basic: {
          title: '基础知识掌握',
          description: '评估学生对数据结构与算法基础概念、定义、性质的理解和记忆。',
          rules: [
            '基础题正确率',
            'ELO能力分数',
            '课后练习完成度'
          ]
        },
        coding: {
          title: '算法实现能力',
          description: '评估学生将算法思想转化为代码的能力。',
          rules: [
            '编程题通过率',
            '代码规范性',
            'ELO能力分数'
          ]
        },
        modeling: {
          title: '问题分析与建模能力',
          description: '评估学生将实际问题抽象为数据结构与算法模型的能力。',
          rules: [
            '中高难度题正确率',
            '报告分析得分',
            'ELO能力分数'
          ]
        },
        initiative: {
          title: '学习主动性',
          description: '评估学生主动学习、查阅资料、观看教学视频的积极性。',
          rules: [
            '视频观看率',
            '自测题完成率'
          ]
        },
        innovation: {
          title: '综合应用与创新能力',
          description: '评估学生将所学知识综合应用于新问题、提出创新解法的能力。',
          rules: [
            '项目/报告创新评分',
            '高难度题得分'
          ]
        }
      },
      growthData: [
        { date: '2025-06-01', score: 1200 },
        { date: '2025-06-05', score: 1250 },
        { date: '2025-06-10', score: 1300 },
        { date: '2025-06-15', score: 1350 },
        { date: '2025-06-20', score: 1400 }
      ]
    }
  },
  computed: {
    type() {
      return this.$route.query.type
    },
    detail() {
      return this.abilityMap[this.type]
    },
    abilityTitle() {
      return this.detail ? this.detail.title : '未知能力'
    }
  },
  mounted() {
    this.initGrowthChart();
  },
  methods: {
    initGrowthChart() {
      const chart = echarts.init(this.$refs.growthChart);
      const option = {
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: this.growthData.map(item => item.date),
          boundaryGap: false
        },
        yAxis: {
          type: 'value',
          min: 1000,
          max: 2000,
          splitNumber: 5,
          axisLabel: {
            formatter: '{value}'
          },
          splitLine: {
            lineStyle: {
              type: 'dashed'
            }
          }
        },
        series: [{
          name: '能力分数',
          type: 'line',
          data: this.growthData.map(item => item.score),
          smooth: true,
          symbol: 'circle',
          symbolSize: 8,
          lineStyle: {
            color: '#1890ff',
            width: 3
          },
          itemStyle: {
            color: '#1890ff',
            borderWidth: 2
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(24,144,255,0.3)' },
              { offset: 1, color: 'rgba(24,144,255,0.1)' }
            ])
          }
        }]
      };
      chart.setOption(option);
      window.addEventListener('resize', () => chart.resize());
    }
  }
}
</script>

<style scoped>
.ability-detail-container {
  max-width: 700px;
  margin: 40px auto;
  padding: 20px;
}
</style>