<template>
  <div>
    <Card style="width: auto;text-align: left;display: block;margin-left: auto;margin-right: auto;margin-bottom: 50px">
      <p slot="title">
        <Icon type="social-buffer"></Icon>
        {{$t('i18n_schedule_in_system')}}
      </p>
      <Table size='small' :data="data" border :columns="columns"></Table>
      <Spin size="large" fix v-if="spinShow"></Spin>
    </Card>

    <Page size='small' style="font-size:10px;" align="center" :total="pageTotal" :current="pageNum" :page-size="pageSize" :page-size-opts="[5, 10, 15]" show-elevator show-sizer show-total
      placement="top" @on-change="handlePage" @on-page-size-change="handlePageSize" />
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  data () {
    return {
      columns: [
        {
          title: this.$t('i18n_schedule_name'),
          key: 'name',
          // fixed: 'left',
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Icon', {
                props: {
                  type: 'flag'
                }
              }),
              h('strong', params.row.name)
            ])
          }
        },
        {
          title: this.$t('i18n_schedule_group'),
          key: 'group',
          align: 'center'
        },
        {
          title: this.$t('i18n_schedule_time'),
          key: 'cron',
          align: 'center'
        },
        {
          title: this.$t('i18n_common_status'),
          key: 'status',
          align: 'center'
        },
        {
          title: this.$t('i18n_common_modifydate'),
          key: 'modifydate',
          align: 'center'
        },
        {
          title: '#',
          key: 'action',
          width: 250,
          // fixed: 'right',
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h(
                'Button',
                {
                  props: {
                    type: 'info',
                    size: 'small'
                  },
                  on: {
                    click: () => {
                      this.detail(params.index)
                    }
                  }
                },
                this.$t('i18n_common_detail')
              ),
              h(
                'Button',
                {
                  props: {
                    type: 'success',
                    size: 'small'
                  },
                  style: {
                    marginLeft: '10px'
                  },
                  on: {
                    click: () => {
                      this.change(params.index)
                    }
                  }
                },
                this.$t('i18n_schedule_start_stop')
              ),
              h(
                'Button',
                {
                  props: {
                    type: 'warning',
                    size: 'small'
                  },
                  style: {
                    marginLeft: '10px'
                  },
                  on: {
                    click: () => {
                      this.modified(params.index)
                    }
                  }
                },
                this.$t('i18n_common_modify')
              )
            ])
          }
        }
      ],
      data: [],
      dataDef: [],
      pageTotal: 0,
      pageNum: 1,
      pageSize: 5,
      spinShow: false
    }
  },
  mounted () {
    this.getAllTasks()
  },
  methods: {
    ...mapActions(['pause', 'resumeJob', 'modify', 'getAll']),
    detail (index) {
      this.$Modal.info({
        title: this.$t('i18n_schedule_task_detail'),
        content: `${this.$t('i18n_schedule_name')}：${this.data[index].name}<br>${this.$t('i18n_schedule_group')}：${
          this.data[index].group
        }<br>${this.$t('i18n_schedule_time')}：${this.data[index].cron}<br>${this.$t('i18n_common_status')}：${this.data[index].status}<br>${this.$t('i18n_common_description')}：${this.data[index].detail}<br>
${this.$t('i18n_schedule_class')}：${this.data[index].classname}<br>${this.$t('i18n_common_createdate')}：${this.data[index].createdate}<br>${this.$t('i18n_common_modifydate')}：${this.data[index].modifydate}`
      })
    },
    change (index) {
      this.spinShow = true
      var position = (this.pageNum - 1) * this.pageSize + index
      const _this = this
      let param = {
        name: _this.data[index].name,
        group: _this.data[index].group
      }
      let status = _this.data[index].status
      if (status === this.$t('i18n_schedule_running')) {
        this.pause(param).then(res => {
          let result = res
          if (result.code === 200) {
            _this.data[index].status = this.$t('i18n_schedule_stop')
            _this.dataDef[position].status = this.$t('i18n_schedule_stop')
            this.$Message.success(this.$t('i18n_schedule_task') + ' ' + _this.data[index].name + ' ' + this.$t('i18n_schedule_stop'))
          }
        })
      } else {
        this.resumeJob(param).then(res => {
          let result = res
          if (result.code === 200) {
            _this.data[index].status = this.$t('i18n_schedule_running')
            _this.dataDef[position].status = this.$t('i18n_schedule_running')
            this.$Message.success(this.$t('i18n_schedule_task') + ' ' + _this.data[index].name + ' ' + this.$t('i18n_schedule_start'))
          }
        })
      }
      this.spinShow = false
    },
    modified (index) {
      this.$Modal.confirm({
        render: h => {
          return h('Input', {
            props: {
              // placeholder: '请输入新的时间周期'
              placeholder: this.data[index].cron
            },
            on: {
              input: val => {
                this.value = val
              }
            }
          })
        },
        onOk: () => {
          this.spinShow = true
          var position = (this.pageNum - 1) * this.pageSize + index
          if (this.value === undefined || this.value === '') {
            this.$Message.warning(this.$t('i18n_schedule_time_not_null'))
          } else {
            let _this = this
            let param = {
              name: _this.data[index].name,
              group: _this.data[index].group,
              cron: this.value,
              lang: this.$store.state.app.local
            }
            this.modify(param).then(res => {
              let result = res
              if (result.code === 200) {
                _this.data[index].status = result.data['quartzVOs'][index]['status']['status']
                _this.data[index].cron = result.data['quartzVOs'][index]['cron']
                _this.dataDef[position].status = result.data['quartzVOs'][index]['status'] === '0' ? this.$t('i18n_schedule_running') : this.$t('i18n_schedule_stop')
                _this.dataDef[position].cron = result.data['quartzVOs'][index]['cron']
                this.$Message.success(this.$t('i18n_schedule_task') + _this.data[index].name + this.$t('i18n_schedule_modified'))
              }
            })
            this.value = ''
            this.spinShow = false
          }
          this.spinShow = false
        }
      })
    },

    getAllTasks () {
      const _this = this
      this.spinShow = true
      this.getAll().then(res => {
        let result = res
        if (result.code === 200) {
          var quartzVOs = result.data['quartzVOs']
          var jsons = []
          _this.pageTotal = quartzVOs.length
          var begin = (_this.pageNum - 1) * _this.pageSize
          var end = (_this.pageNum - 1) * _this.pageSize + _this.pageSize
          if (end > quartzVOs.length) {
            end = quartzVOs.length
            for (let i = begin; i < end; i++) {
              let quartz = quartzVOs[i]
              let task = {}
              task.name = quartz.name
              task.group = quartz.group
              task.cron = quartz.cron
              task.status = (quartz.status === '0' ? this.$t('i18n_schedule_running') : this.$t('i18n_schedule_stop'))
              task.detail = quartz.detail
              task.classname = quartz.classname
              task.createdate = quartz.createdate
              task.modifydate = quartz.modifydate
              jsons.push(task)
            }
            _this.data = jsons
            _this.dataDef = jsons
          } else {
            for (let i = begin; i < end; i++) {
              let quartz = quartzVOs[i]
              let task = {}
              task.name = quartz.name
              task.group = quartz.group
              task.cron = quartz.cron
              task.status = (quartz.status === '0' ? this.$t('i18n_schedule_running') : this.$t('i18n_schedule_stop'))
              task.detail = quartz.detail
              task.classname = quartz.classname
              task.createdate = quartz.createdate
              task.modifydate = quartz.modifydate
              jsons.push(task)
            }
            _this.data = jsons
            jsons = []
            for (let i = 0; i < quartzVOs.length; i++) {
              let quartz = quartzVOs[i]
              let task = {}
              task.name = quartz.name
              task.group = quartz.group
              task.cron = quartz.cron
              task.status = (quartz.status === '0' ? this.$t('i18n_schedule_running') : this.$t('i18n_schedule_stop'))
              task.detail = quartz.detail
              task.classname = quartz.classname
              task.createdate = quartz.createdate
              task.modifydate = quartz.modifydate
              jsons.push(task)
            }
            _this.dataDef = jsons
          }
        }
        this.spinShow = false
      })
    },

    handlePage (value) {
      this.pageNum = value
      var pageNum = this.pageNum
      var pageSize = this.pageSize
      this.spinShow = true
      var jsons = this.dataDef
      var jsonsNew = []
      var begin = (pageNum - 1) * pageSize
      var end = (pageNum - 1) * pageSize + pageSize
      if (end > jsons.length) {
        end = jsons.length
      }
      for (var i = begin; i < end; i++) {
        var json = jsons[i]
        let task = {}
        task.name = json.name
        task.group = json.group
        task.cron = json.cron
        task.status = json.status
        task.detail = json.detail
        task.classname = json.classname
        task.createdate = json.createdate
        task.modifydate = json.modifydate
        jsonsNew.push(task)
      }
      this.data = jsonsNew
      this.spinShow = false
    },

    handlePageSize (value) {
      this.pageSize = value
      var pageNum = this.pageNum
      var pageSize = this.pageSize
      this.spinShow = true
      var jsons = this.dataDef
      var jsonsNew = []
      var begin = (pageNum - 1) * pageSize
      var end = (pageNum - 1) * pageSize + pageSize
      if (end > jsons.length) {
        end = jsons.length
      }
      for (var i = begin; i < end; i++) {
        var json = jsons[i]
        let task = {}
        task.name = json.name
        task.group = json.group
        task.cron = json.cron
        task.status = json.status
        task.detail = json.detail
        task.classname = json.classname
        task.createdate = json.createdate
        task.modifydate = json.modifydate
        jsonsNew.push(task)
      }
      this.data = jsonsNew
      this.spinShow = false
    }

  }
}
</script>
