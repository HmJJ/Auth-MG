<template>
    <div ref="dom"></div>
</template>

<script>
import echarts from 'echarts'
import { on, off } from '@/libs/tools'
export default {
  name: 'serviceRequests',
  option: {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985'
        }
      }
    },
    grid: {
      top: '3%',
      left: '1.2%',
      right: '1%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'category',
        boundaryGap: false,
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      }
    ],
    yAxis: [
      {
        type: 'value'
      }
    ],
    series: [
      {
        name: '支出',
        type: 'line',
        stack: '总量',
        areaStyle: {
          normal: {
            color: '#2d8cf0'
          }
        },
        data: [120, 132, 101, 134, 90, 230, 210]
      },
      {
        name: '银行/证券',
        type: 'line',
        stack: '总量',
        areaStyle: {
          normal: {
            color: '#10A6FF'
          }
        },
        data: [257, 358, 278, 234, 290, 330, 310]
      }
    ]
  },
  data () {
    return {
      dom: null
    }
  },
  methods: {
    resize () {
      this.dom = echarts.init(this.$refs.dom)
      this.dom.setOption(option)
      this.dom.resize()
    }
  },
  mounted () {
    on(window, 'resize', this.resize())
  },
  beforeDestroy () {
    off(window, 'resize', this.resize())
  }
}
</script>
