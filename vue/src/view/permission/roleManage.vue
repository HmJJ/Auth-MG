<template>
  <Card >
    <div slot="title">
      <Icon type="android-contacts"></Icon>
      {{$t('i18n_character_manage')}}
    </div>
    <div>
      <Input v-model="roleName" placeholder="输入角色名称" style="width: 200px" />
      <Input v-model="roleCode" placeholder="输入角色代码" style="width: 200px" />
      <Button @click="handleSearch" type="primary" icon="search" >{{$t('i18n_common_search')}}</Button>
      <Button style="float: right;"  @click="add" type="success"   icon="plus-round">{{$t('i18n_common_add')}}</Button>
      <Modal :title="modalTitle" v-model="roleModalVisible" :mask-closable='false' :width="500" :styles="{top: '30px'}">
        <Form ref="roleForm" :model="roleForm" :label-width="70" :rules="roleFormValidate">
          <FormItem :label="$t('i18n_permission_role_name')" prop="name">
            <Input v-model="roleForm.name" placeholder="按照Spring Security约定建议以‘ROLE_’开头" />
          </FormItem>
          <FormItem :label="$t('i18n_permission_role_code')" prop="code">
            <Input v-model="roleForm.code" placeholder="请输入角色代码" />
          </FormItem>
          <FormItem :label="$t('i18n_permission_role_icon')" prop="icon">
            <Input v-model="roleForm.icon" placeholder="请参考下方链接，填入图标名称" />
            <span>
                      {{$t('i18n_permission_icon_tips')}} <a target="_blank" href="http://v2.iviewui.com/components/icon"><Icon type="ionic"></Icon> ionicons</a>
                    </span>
          </FormItem>
          <FormItem :label="$t('i18n_permission_role_type')" prop="type">
            <Input v-model="roleForm.type" placeholder="请输入角色类型" />
          </FormItem>
          <FormItem :label="$t('i18n_permission_role_status')" >
            <RadioGroup v-model="roleForm.status">
              <Radio label='0' >{{$t('i18n_common_enable')}}</Radio>
              <Radio label='1' >{{$t('i18n_common_disable')}}</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem :label="$t('i18n_permission_role_sort')" prop="sort">
            <Input v-model="roleForm.sort" placeholder="请输入角色序号" />
          </FormItem>
          <FormItem :label="$t('i18n_permission_role_description')" prop="description">
            <Input v-model="roleForm.description"  type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder=" 请输入..." />
          </FormItem>
        </Form>
        <div slot="footer">
          <Button type="text" @click="cancelRole" >{{$t('i18n_common_cancle')}}</Button>
          <Button type="primary" :loading="submitLoading" @click="submitRole" >{{$t('i18n_common_submit')}}</Button>
        </div>
      </Modal>
    </div>
    <br />
    <div>
      <Table border size="small" :loading="loading" :columns="columns" :data="data"></Table>
      <br />
      <div style="text-align: center">
        <Page :v-model="page" style="font-size: 10px;" size="small" :total="page.pageTotal" :current="page.pageNum" :page-size="page.pageSize" show-elevator show-total show-sizer @on-change="handlePage" @on-page-size-change='handlePageSize'/>
      </div>
    </div>
    <Modal v-model="permModalVisible" :mask-closable='false' :width="500" :styles="{top: '30px'}">
      <p slot="header">
        <span>{{$t('i18n_permission_role_allocate_resource_title')}}</span>
      </p>
      <Tree ref="tree" :data="permData" multiple></Tree>
      <Spin size="large" v-if="treeLoading"></Spin>
      <div slot="footer">
        <Button type="text" @click="cancelPermEdit">{{$t('i18n_common_cancle')}}</Button>
        <Button @click="selectTreeAll">{{$t('i18n_permission_role_check_all')}}</Button>
        <Button type="primary" :loading="submitPermLoading" @click="submitPermEdit">{{$t('i18n_common_submit')}}</Button>
      </div>
    </Modal>
  </Card>
</template>

<script>
import { mapActions } from 'vuex'
export default {
  name: 'character-manage',
  data () {
    return {
      roleName: '',
      roleCode: '',
      roleModalVisible: false,
      submitLoading: false,
      modalType: 0,
      modalTitle: '',
      permModalVisible: false,
      editRolePermId: '',
      treeLoading: true,
      permData: [],
      selectAllFlag: false,
      select: false,
      submitPermLoading: false,
      permData2: [],
      loading: true,
      roleForm: {
        id: '',
        name: '',
        code: '',
        sort: '',
        type: '',
        icon: '',
        status: '0',
        description: ''
      },
      page: {
        pageTotal: 0,
        pageNum: 1,
        pageSize: 10
      },
      roleFormValidate: {
        name: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '角色代码不能为空', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '角色序号不能为空', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '角色类型不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '角色状态不能为空', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入该角色的简单描述说明', trigger: 'blur' },
          { type: 'string', min: 5, message: '最少5个单词', trigger: 'blur' }
        ]
      },
      columns: [
        {
          title: this.$t('i18n_permission_role_icon'),
          key: 'icon',
          width: 100,
          align: 'center',
          render: (h, params) => {
            return h('Icon', {
              props: {
                type: params.row.icon
              }
            })
          }
        },
        {
          title: this.$t('i18n_permission_role_name'),
          key: 'name',
          align: 'center'
        },
        {
          title: this.$t('i18n_permission_role_code'),
          key: 'code',
          align: 'center'
        },
        {
          title: this.$t('i18n_permission_role_createdate'),
          key: 'createDate',
          width: 150,
          align: 'center'
        },
        {
          title: this.$t('i18n_permission_role_sort'),
          key: 'sort',
          width: 150,
          align: 'center'
        },
        {
          title: this.$t('i18n_permission_role_status'),
          key: 'status',
          align: 'center',
          width: 140,
          render: (h, params) => {
            if (params.row.status === '0') {
              return h('div', [
                h(
                  'Tag',
                  {
                    props: {
                      type: 'dot',
                      color: 'green'
                    }
                  },
                  this.$t('i18n_common_enable')
                )
              ])
            } else if (params.row.status === '1') {
              return h('div', [
                h(
                  'Tag',
                  {
                    props: {
                      type: 'dot',
                      color: 'red'
                    }
                  },
                  this.$t('i18n_common_disable')
                )
              ])
            }
          },
          filters: [
            {
              label: this.$t('i18n_common_enable'),
              value: '0'
            },
            {
              label: this.$t('i18n_common_disable'),
              value: '1'
            }
          ],
          filterMultiple: false,
          filterMethod (value, row) {
            if (value === '0') {
              return row.status === '0'
            } else if (value === '1') {
              return row.status === '1'
            }
          }
        },
        {
          title: '#',
          key: 'action',
          width: 250,
          align: 'center',
          render: (h, params) => {
            if (params.row.status === '0') {
              return h('div', [
                h(
                  'Button',
                  {
                    props: {
                      type: 'warning',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
                        this.editPerm(params.row)
                      }
                    }
                  },
                  this.$t('i18n_permission_role_allocate_resource')
                ),
                h(
                  'Button',
                  {
                    props: {
                      type: 'primary',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
                        this.edit(params.row)
                      }
                    }
                  },
                  this.$t('i18n_common_edit')
                ),
                h(
                  'Button',
                  {
                    props: {
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
                        this.disable(params.row)
                      }
                    }
                  },
                  this.$t('i18n_common_disable')
                )
              ])
            } else {
              return h('div', [
                h(
                  'Button',
                  {
                    props: {
                      type: 'warning',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
                        this.editPerm(params.row)
                      }
                    }
                  },
                  this.$t('i18n_permission_role_allocate_resource')
                ),
                h(
                  'Button',
                  {
                    props: {
                      type: 'primary',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
                        this.edit(params.row)
                      }
                    }
                  },
                  this.$t('i18n_common_edit')
                ),
                h(
                  'Button',
                  {
                    props: {
                      type: 'success',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
                        this.enable(params.row)
                      }
                    }
                  },
                  this.$t('i18n_common_enable')
                )
              ])
            }
          }
        }
      ],
      data: []
    }
  },
  mounted () {
    this.init()
    this.getPermList()
  },
  methods: {
    ...mapActions([
      'addRole',
      'getRole',
      'deleteRole',
      'modifyRole',
      'getRoleById',
      'modifyRoleStatus',
      'getAllList',
      'addOrModifyPermission',
      'getUserResource'
    ]),
    init () {
      var params = { name: '', code: '', pageNum: this.page.pageNum, pageSize: this.page.pageSize }
      this.paging(params)
    },
    handlePage (value) {
      var params = { name: this.roleName, code: this.roleCode, pageNum: value, pageSize: this.page.pageSize }
      this.paging(params)
    },
    handlePageSize (value) {
      this.page.pageSize = value
      var params = { name: this.roleName, code: this.roleCode, pageNum: 1, pageSize: this.page.pageSize }
      this.paging(params)
    },
    handleSearch () {
      var params = {name: this.roleName, code: this.roleCode, pageNum: this.page.pageNum, pageSize: this.page.pageSize}
      this.paging(params)
    },
    paging (params) {
      this.loading = true
      this.getRole(params).then(res => {
        this.data = res.data.content
        this.page.pageTotal = res.data.totalElements
        this.page.pageNum = res.data.number + 1
        this.page.pageSize = res.data.size
        this.loading = false
      })
    },
    cancelRole () {
      this.roleModalVisible = false
    },
    submitRole () {
      this.$refs.roleForm.validate(valid => {
        if (valid) {
          if (this.modalType === 0) {
            // 添加用户 避免编辑后传入id
            delete this.roleForm.id
            this.submitLoading = true
            this.addRole(this.roleForm).then(res => {
              this.submitLoading = false
              if (res.data === true) {
                this.$Message.success('操作成功')
                this.init()
                this.roleModalVisible = false
              } else {
                this.$Message.error(res.msg)
                this.roleModalVisible = false
              }
            })
          } else {
            // 编辑
            this.submitLoading = true
            this.modifyRole(this.roleForm).then(res => {
              this.submitLoading = false
              if (res.data === true) {
                this.$Message.success('操作成功')
                this.init()
                this.roleModalVisible = false
              } else {
                this.$Message.error(res.msg)
                this.roleModalVisible = false
              }
            })
          }
        }
      })
    },
    add () {
      this.modalType = 0
      this.modalTitle = this.$t('i18n_permission_role_add')
      this.$refs.roleForm.resetFields()
      this.roleModalVisible = true
    },
    edit (v) {
      this.modalType = 1
      this.modalTitle = this.$t('i18n_permission_role_edit')
      this.$refs.roleForm.resetFields()
      for (let attr in v) {
        if (v[attr] === null) {
          v[attr] = ''
        }
      }
      let str = JSON.stringify(v)
      let roleInfo = JSON.parse(str)
      this.roleForm = roleInfo
      this.roleModalVisible = true
    },
    enable (v) {
      this.$Modal.confirm({
        title: this.$t('i18n_common_affirm') + this.$t('i18n_common_enable'),
        content: this.$t('i18n_permission_role_confirm_start_info') + v.name + ' ?',
        onOk: () => {
          this.modifyRoleStatus({id: v.id, status: '0'}).then(res => {
            if (res.data === true) {
              this.$Message.success('操作成功')
              this.init()
            }
          })
        }
      })
    },
    disable (v) {
      this.$Modal.confirm({
        title: this.$t('i18n_common_affirm') + this.$t('i18n_common_disable'),
        content: this.$t('i18n_permission_role_confirm_disable_info') + v.name + ' ?',
        onOk: () => {
          this.modifyRoleStatus({id: v.id, status: '1'}).then(res => {
            if (res.data === true) {
              this.$Message.success('操作成功')
              this.init()
            } else {
              this.$Message.error(res.msg)
              this.roleModalVisible = false
            }
          })
        }
      })
    },
    // remove (v) {
    //   this.$Modal.confirm({
    //     title: '确认删除',
    //     content: '您确认要删除该角色 ' + v.name + ' ?',
    //     onOk: () => {
    //       this.deleteRole({id: v.id}).then(res => {
    //         if (res.data === true) {
    //           this.$Message.success('删除成功')
    //           this.init()
    //         } else {
    //           this.$Message.error(res.msg)
    //           this.roleModalVisible = false
    //         }
    //       })
    //     }
    //   })
    // },
    editPerm (v) {
      this.editRolePermId = v.id
      // 匹配勾选
      var params = { 'id': this.editRolePermId }
      this.getUserResource(params).then(res => {
        if (res.data !== undefined) {
          let rolePerms = res.data
          // 递归判断子节点
          this.checkPermTree(this.permData, rolePerms)
          this.permModalVisible = true
        } else {
          this.permModalVisible = true
        }
      })
    },
    // 递归判断子节点
    checkPermTree (permData, rolePerms) {
      let that = this
      permData.forEach(function (p) {
        if (that.hasPerm(p, rolePerms)) {
          p.selected = true
        } else {
          p.selected = false
        }
        if (p.children && p.children.length > 0) {
          that.checkPermTree(p.children, rolePerms)
        }
      })
    },
    // 判断角色拥有的权限节点勾选
    hasPerm (p, rolePerms) {
      let flag = false
      for (let i = 0; i < rolePerms.length; i++) {
        if (p.id === rolePerms[i].id) {
          flag = true
          p.selected = true
          break
        }
      }
      if (flag) {
        return true
      }
      return false
    },
    getPermList () {
      this.treeLoading = true
      this.getAllList().then(res => {
        this.treeLoading = false
        if (res.data !== undefined) {
          this.expandTreeAll(res.data, true)
          this.permData = res.data
        }
      })
    },
    expandTreeAll (data, expand) {
      let that = this
      data.forEach(function (e) {
        e.expand = expand
        if (e.children && e.children.length > 0) {
          that.expandTreeAll(e.children, expand)
        }
      })
    },
    selectTreeAll () {
      this.selectAllFlag = !this.selectAllFlag
      let select = this.selectAllFlag
      this.permData2 = this.permData
      this.selectedTreeAll(this.permData2, select)
      this.permData = this.permData2
    },
    // 递归全选节点
    selectedTreeAll (permData, select) {
      let that = this
      permData.forEach(function (e) {
        e.selected = select
        if (e.children && e.children.length > 0) {
          that.selectedTreeAll(e.children, select)
        }
      })
    },
    cancelPermEdit () {
      this.permModalVisible = false
    },
    submitPermEdit () {
      let selectedNodes = this.$refs.tree.getSelectedNodes()
      this.submitPermLoading = true
      var jsons = []
      if (selectedNodes === undefined || selectedNodes.length <= 0) {
        jsons = ['']
      } else {
        selectedNodes.forEach(function (e) {
          let temp = {}
          temp = e.id
          jsons.push(temp)
        })
      }
      var params = { 'id': this.editRolePermId, 'list': jsons }
      this.addOrModifyPermission(params).then(res => {
        if (res.data !== undefined) {
          this.$Message.success('操作成功')
          this.submitPermLoading = false
          this.permModalVisible = false
        }
      })
    }
  }
}
</script>

<style lang="less" >
  .vertical-center-modal{
    display: flex;
    align-items: center;
    justify-content: center;

    .ivu-modal{
      top: 0;
    }
  }
  textarea{
    resize: none;
  }
</style>
