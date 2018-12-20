<template>
  <div>
    <Row :gutter="20">
      <i-col span="8" v-for="(infor, i) in inforCardData1" :key="`infor-${i}`" style="height: 120px;color:#666666;">
        <infor-card shadow :color="infor.color" :icon="infor.icon" :icon-size="36">
          <count-to :end="infor.count" count-class="count-style"/>
          <p>{{ infor.title }}</p>
        </infor-card>
      </i-col>
    </Row>
    <br/>
    <Row :gutter="20">
      <i-col span="8" v-for="(infor, i) in inforCardData2" :key="`infor-${i}`" style="height: 120px;color:#666666;">
        <infor-card shadow :color="infor.color" :icon="infor.icon" :icon-size="36">
          <count-to :end="infor.count" count-class="count-style"/>
          <p>{{ infor.title }}</p>
        </infor-card>
      </i-col>
    </Row>
    <Row :gutter="20" style="margin-top: 20px;">
      <i-col span="12">
        <Card shadow style="width: 100%">
          <!--<p slot="title">-->
            <!--各产品本季度出货量百分比-->
          <!--</p>-->
          <div id="pie" style="height: 300px;width: 470px;"></div>
        </Card>
      </i-col>
      <i-col span="12">
        <Card shadow style="width: 100%;">
          <div id="bar" style="height: 300px;width: 470px;"></div>
        </Card>
      </i-col>
      <br />
      <br />
    </Row>
    <Row :gutter="20" style="margin-top: 20px;">
      <i-col span="24">
        <Card shadow>
          <Row>
            <i-col span="4" style="padding-top: 5px;">
              <h4>{{$t('i18n_home_select_year')}}</h4>
            </i-col>
            <i-col span="4" style="padding-right:10px">
              <Select v-model="years" >
                <Option v-for="item in year" :value="item" :key="item">{{ item }}</Option>
              </Select>
            </i-col>
            <i-col span="4" offset="7" style="padding-top: 5px;">
              <h4>{{$t('i18n_home_select_product')}}</h4>
            </i-col>
            <i-col span="4" style="padding-right:10px">
              <Select v-model="products" >
                <Option v-for="item in product" :value="item.value" :key="item.value">{{ item.label }}</Option>
              </Select>
            </i-col>
          </Row>
          <br />
          <Row>
            <i-col span="24">
              <div id="bar2" style="height: 400px;width: 990px;"></div>
            </i-col>
          </Row>
        </Card>
      </i-col>
    </Row>
    <br />
  </div>
</template>
<script>
import { mapActions } from 'vuex'
import InforCard from '_c/info-card'
import CountTo from '_c/count-to'
import ICol from 'iview/src/components/grid/col'
export default {
  name: '首页',
  components: {
    ICol,
    InforCard,
    CountTo
  },
  data () {
    return {
      year: this.getYear(),
      products: '',
      years: '',
      product: [
        {
          label: '南汇苹果',
          value: '001'
        },
        {
          label: '南汇葡萄',
          value: '002'
        },
        {
          label: '普陀大虾',
          value: '003'
        }
      ],
      inforCardData1: [
        { title: this.$t('i18n_home_today_visitors'), icon: 'egg', count: 803, color: '#6666CC' },
        { title: this.$t('i18n_home_total_number_of_users'), icon: 'person-stalker', count: 23432, color: '#66CC66' },
        { title: this.$t('i18n_home_new_users'), icon: 'person-add', count: 142, color: '#CCFF66' }
      ],
      inforCardData2: [
        { title: this.$t('i18n_home_total_number_of_products'), icon: 'star', count: 657, color: '#FF6666' },
        { title: this.$t('i18n_home_yesterday_shipment'), icon: 'ios-filing', count: 12, color: '#FFCC99' },
        { title: this.$t('i18n_home_yesterday_packaged'), icon: 'ios-box', count: 14, color: '#FFFF99' }
      ],
      barData: {
        Mon: 40,
        Tue: 50,
        Wed: 70,
        Thu: 80,
        Fri: 50,
        Sat: 40,
        Sun: 20
      },
      pieData: [
        {value: 335, name: '南汇水蜜桃'},
        {value: 225, name: '南汇大虾'},
        {value: 234, name: '普陀小青龙'},
        {value: 135, name: '浦东青蟹'},
        {value: 120, name: '普陀青提'}
      ]
    }
  },
  methods: {
    ...mapActions([
      'getHomeData'
    ]),
    getYear: function () {
      let arr = []
      for (var i = 1990; i < 2200; i++) {
        arr.push(i)
      }
      return arr
    },
    getOverviewData: function () {
      this.drawPie()
      this.drawBar(this.$data.barData)
      this.drawBar2()
    },
    drawPie: function () {
      let charts = this.$echarts.init(document.getElementById('pie'))
      charts.on('click', this.eConsole)
      charts.setOption({
        color: ['#99FFCC', '#CCFFCC', '#FF99CC', '#FFCCCC', '#99CCFF'],
        title: {
          text: this.$t('i18n_home_percentage_of_each_product_shipped_this_quarter'),
          x: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          show: false,
          orient: 'vertical',
          x: 'left',
          top: '30%',
          data: ['南汇水蜜桃', '南汇大虾', '普陀小青龙', '浦东青蟹', '普陀青提']
        },
        series: [
          {
            name: this.$t('i18n_home_name_of_products'),
            type: 'pie',
            center: ['50%', '50%'],
            radius: ['30%', '50%'],
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: true,
                position: 'outside'
              },
              emphasis: {
                show: true,
                textStyle: {
                  fontSize: '14',
                  fontWeight: 'bold'
                }
              }
            },
            labelLine: {
              normal: {
                show: true
              }
            },
            data: this.$data.pieData,
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      })
    },
    drawBar: function (dataes) {
      let charts = this.$echarts.init(document.getElementById('bar'))
      let seriesData = Object.values(dataes)
      charts.setOption({
        color: ['#669999'],
        title: {
          text: this.$t('i18n_home_user_scan_statistics_this_week'),
          x: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'line'
          }
        },
        legend: {
          show: true,
          orient: 'vertical',
          left: '5%',
          top: '10%',
          data: [this.$t('i18n_home_scan_times')]
        },
        grid: {
          left: '1%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            axisTick: {
              alignWithLabel: true
            }
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: this.$t('i18n_home_scan_times'),
            type: 'bar',
            barWidth: '60%',
            data: seriesData
          }
        ]
      })
    },
    drawBar2: function () {
      let charts = this.$echarts.init(document.getElementById('bar2'))
      charts.setOption({
        color: ['#9966FF', '#6699FF'],
        title: {
          text: this.$t('i18n_home_annual_product_data_display'),
          x: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        toolbox: {
          top: '1%',
          right: '5%',
          fontSize: '12',
          feature: {
            dataView: {show: true, readOnly: false},
            magicType: {show: true, type: ['line', 'bar']},
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        legend: {
          top: '2%',
          fontSize: '12',
          data: [this.$t('i18n_home_shipments'), this.$t('i18n_home_scan_number')],
          left: '5%'
        },
        xAxis: [
          {
            type: 'category',
            data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
            axisPointer: {
              type: 'shadow'
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: '#9999FF'
              }
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: this.$t('i18n_home_box_number'),
            nameGap: '20',
            splitNumber: 10,
            minInterval: 100,
            maxInterval: 1000,
            axisLine: {
              show: true,
              lineStyle: {
                color: '#9999FF'
              }
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              show: true,
              margin: 1,
              fontSize: 12
            }
          }
        ],
        series: [
          {
            name: this.$t('i18n_home_shipments'),
            type: 'bar',
            data: [300, 400, 300, 200, 100, 700, 800, 900, 200, 300, 500, 200]
          },
          {
            name: this.$t('i18n_home_scan_number'),
            type: 'bar',
            data: [100, 200, 500, 200, 300, 600, 900, 1000, 1400, 300, 300, 200]
          }
        ]
      })
    }
  },
  mounted () {
    this.getOverviewData()
  }
}
</script>

<style lang="less">
  .count-style{
    font-size: 50px;
  }
</style>
