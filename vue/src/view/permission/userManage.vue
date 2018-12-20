<template>
  <Card >
    <div slot="title">
      <Icon type="person"></Icon>
      {{$t('i18n_user_manage')}}
    </div>
    <div>
      <Input v-model="username" placeholder="输入用户名" style="width: 200px" />
      <Input v-model="name" placeholder="输入用户名姓名" style="width: 200px" />
      <Button @click="handleSearch" type="primary" icon="search" >{{$t('i18n_common_search')}}</Button>
      <Button style="float: right;" @click="add" type="success"   icon="plus-round">{{$t('i18n_common_add')}}</Button>
      <Modal title="添加用户" v-model="userModalVisible" :mask-closable='false' :width="500" :styles="{top: '30px'}" :closable="false">
        <Form ref="userForm" :model="userForm" :label-width="70" :rules="userFormValidate">
          <FormItem :label="$t('i18n_permission_user_name')" prop="username">
            <Input v-model="userForm.username" placeholder="请输入用户名称" />
          </FormItem>
          <FormItem :label="$t('i18n_permission_user_password')" prop="password">
            <Input v-model="userForm.password" placeholder="请输入用户密码" />
          </FormItem>
          <FormItem :label="$t('i18n_permission_user_username')" prop="name">
            <Input v-model="userForm.name" placeholder="请输入用户姓名" />
          </FormItem>
          <FormItem :label="$t('i18n_employee_phone')" prop="phone">
            <Input v-model="userForm.phone" placeholder="请输入手机号码" />
          </FormItem>
          <FormItem label="E-Mail" prop="email">
            <Input v-model="userForm.email" type="email" placeholder="请输入用户邮箱" />
          </FormItem>
          <FormItem :label="$t('i18n_permission_user_type')" prop="type">
            <Input v-model="userForm.type" placeholder="请输入用户类型" />
          </FormItem>
          <FormItem :label="$t('i18n_permission_user_status')" prop="status">
            <RadioGroup v-model="userForm.status">
              <Radio label=0 >{{$t('i18n_common_enable')}}</Radio>
              <Radio label=1 >{{$t('i18n_common_disable')}}</Radio>
            </RadioGroup>
          </FormItem>
        </Form>
        <div slot="footer">
          <Button type="text" @click="cancelUser">{{$t('i18n_common_cancle')}}</Button>
          <Button type="primary" :loading="submitLoading" @click="submitUser">{{$t('i18n_common_submit')}}</Button>
        </div>
      </Modal>
      <Modal title="修改用户" v-model="userModalVisibleEdit" :mask-closable='false' :width="500" :styles="{top: '30px'}" :closable="false">
        <Form ref="userFormEdit" :model="userFormEdit" :label-width="70" :rules="userFormValidateEdit">
          <FormItem :label="$t('i18n_permission_user_name')" prop="username">
            <Input v-model="userFormEdit.username" placeholder="请输入用户名称" />
          </FormItem>
          <FormItem :label="$t('i18n_permission_user_username')" prop="name">
            <Input v-model="userFormEdit.name" placeholder="请输入用户姓名" />
          </FormItem>
          <FormItem :label="$t('i18n_employee_phone')" prop="phone">
            <Input v-model="userFormEdit.phone" placeholder="请输入手机号码" />
          </FormItem>
          <FormItem label="E-Mail" prop="email">
            <Input v-model="userFormEdit.email" type="email" placeholder="请输入用户邮箱" />
          </FormItem>
          <FormItem :label="$t('i18n_permission_user_type')" prop="type">
            <Input v-model="userFormEdit.type" placeholder="请输入用户类型" />
          </FormItem>
          <FormItem :label="$t('i18n_permission_user_status')" prop="status">
            <RadioGroup v-model="userFormEdit.status">
              <Radio label=0 >{{$t('i18n_common_enable')}}</Radio>
              <Radio label=1 >{{$t('i18n_common_disable')}}</Radio>
            </RadioGroup>
          </FormItem>
        </Form>
        <div slot="footer">
          <Button type="text" @click="cancelUserEdit">{{$t('i18n_common_cancle')}}</Button>
          <Button type="primary" :loading="submitLoadingEdit" @click="submitUserEdit">{{$t('i18n_common_submit')}}</Button>
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
    <Modal v-model="grandRoleModal" :mask-closable='false' :width="500" :styles="{top: '30px'}">
      <p slot="header">
        <span>{{$t('i18n_permission_user_grant')}}</span>
      </p>
      <div style="border-bottom: 1px solid #e9e9e9;padding-bottom:6px;margin-bottom:6px;">
        <Checkbox
          :indeterminate="indeterminate"
          :value="checkAll"
          @click.prevent.native="handleCheckAll">&nbsp;{{$t('i18n_common_check_all')}}</Checkbox>
      </div>
      <CheckboxGroup v-model="checkAllGroup" @on-change="checkAllGroupChange" >
          <Row v-for="item in roleList" :key="item.id">
            <i-col span="16">
              <Checkbox :label="item.id">&nbsp;&nbsp;<Icon :type="item.icon"></Icon>/{{item.name}}/{{item.type}}</Checkbox>
            </i-col>
          </Row>
      </CheckboxGroup>
      <div slot="footer">
        <Button type="text" @click="cancelUserRole">{{$t('i18n_common_cancle')}}</Button>
        <Button type="primary" :loading="roleSubmit" @click="submitUserRole">{{$t('i18n_common_submit')}}</Button>
      </div>
    </Modal>
    <Modal title="重置密码" v-model="modifyPasswordModal" :mask-closable='false' :width="400" :styles="{top: '30px'}" :closable='false'>
      <Form ref="newPasswordFrom" :model="newPasswordFrom" :label-width="80" :rules="passwordFormValidate">
        <FormItem :label="$t('i18n_permission_user_new_password')" prop="newPassword">
          <Input v-model="newPasswordFrom.newPassword" placeholder="请输入新的密码"/>
        </FormItem>
      </Form>
      <div slot="footer">
        <Button type="text" @click="cancelPasswordModal">{{$t('i18n_common_cancle')}}</Button>
        <Button type="primary" :loading="passwordSubmit" @click="submitUserPassword">{{$t('i18n_common_submit')}}</Button>
      </div>
    </Modal>
  </Card>
</template>

<script>
import { mapActions } from 'vuex'
import ICol from 'iview/src/components/grid/col'
export default {
  components: { ICol },
  name: 'user-manage',
  data () {
    return {
      disabledSingle: true,
      newPasswordFrom: {
        newPassword: ''
      },
      passwordSubmit: false,
      userModalVisibleEdit: false,
      submitLoadingEdit: false,
      grandRoleModal: false,
      userModalVisible: false,
      modifyPasswordModal: false,
      submitLoading: false,
      roleSubmit: false,
      loading: true,
      username: '',
      name: '',
      indeterminate: true,
      checkAll: false,
      checkAllGroup: [],
      page: {
        pageTotal: 0,
        pageNum: 1,
        pageSize: 10
      },
      userForm: {
        id: '',
        username: '',
        password: '',
        name: '',
        phone: '',
        email: '',
        type: '',
        status: '0'
      },
      userFormEdit: {
        id: '',
        username: '',
        name: '',
        phone: '',
        email: '',
        type: '',
        status: ''
      },
      roleList: [],
      errorPass: '',
      columns: [
        {
          title: this.$t('i18n_permission_user_name'),
          key: 'username',
          width: 100,
          align: 'center'
        },
        {
          title: this.$t('i18n_permission_user_username'),
          key: 'name',
          width: 100,
          align: 'center'
        },
        {
          title: this.$t('i18n_permission_user_type'),
          key: 'type',
          width: 100,
          align: 'center'
        },
        {
          title: this.$t('i18n_common_createdate'),
          key: 'createDate',
          align: 'center',
          width: 150
        },
        {
          title: this.$t('i18n_employee_phone'),
          key: 'phone',
          align: 'center',
          width: 130
        },
        {
          title: this.$t('i18n_common_status'),
          key: 'status',
          width: 130,
          align: 'center',
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
          width: 300,
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
                        this.grand(params.row)
                      }
                    }
                  },
                  this.$t('i18n_permission_user_grant')
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
                ),
                h(
                  'Button',
                  {
                    props: {
                      type: 'info',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
                        this.modifyPassword(params.row)
                      }
                    }
                  },
                  this.$t('i18n_permission_user_reset_password')
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
                        this.grand(params.row)
                      }
                    }
                  },
                  this.$t('i18n_permission_user_grant')
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
                ),
                h(
                  'Button',
                  {
                    props: {
                      type: 'info',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
                        this.modifyPassword(params.row)
                      }
                    }
                  },
                  this.$t('i18n_permission_user_reset_password')
                )
              ])
            }
          }
        }
      ],
      data: [],
      passwordFormValidate: {
        newPassword: [
          {required: true, message: '用户密码不能为空', trigger: 'blur'}
        ]
      },
      userFormValidate: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' }
        ],
        password: [
          {required: true, message: '用户密码不能为空', trigger: 'blur'}
        ],
        name: [
          { required: true, message: '用户姓名不能为空', trigger: 'blur' }
        ],
        phone: [
          {
            validator: (rule, value, callback) => {
              if (/(^1[3|4|5|7|8]\d{9}$)|(^09\d{8}$)/.test(value) === false) {
                callback(new Error('请输入有效的手机号码'))
              } else {
                callback()
              }
            }
          }
        ],
        type: [
          { required: true, message: '用户类型不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '用户状态不能为空', trigger: 'blur' }
        ]
      },
      userFormValidateEdit: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '用户姓名不能为空', trigger: 'blur' }
        ],
        phone: [
          {
            validator: (rule, value, callback) => {
              if (/(^1[3|4|5|7|8]\d{9}$)|(^09\d{8}$)/.test(value) === false) {
                callback(new Error('请输入有效的手机号码'))
              } else {
                callback()
              }
            }
          }
        ],
        type: [
          { required: true, message: '用户类型不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '用户状态不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  mounted () {
    this.init()
    this.getUserRoles()
  },
  methods: {
    ...mapActions([
      'addUser',
      'getRoles',
      'getUser',
      'deleteUser',
      'modifyUser',
      'modifyUserStatus',
      'getUserRole',
      'grandRole',
      'modifyUserPassword'
    ]),
    getUserRoles () {
      this.getRoles().then(res => {
        console.log(res.data)
        this.roleList = res.data
      })
    },
    init () {
      var params = { username: '', name: '', pageNum: this.page.pageNum, pageSize: this.page.pageSize }
      this.paging(params)
    },
    handlePage (value) {
      var params = { username: this.username, name: this.name, pageNum: value, pageSize: this.page.pageSize }
      this.paging(params)
    },
    handlePageSize (value) {
      this.page.pageSize = value
      var params = { username: this.username, name: this.name, pageNum: 1, pageSize: this.page.pageSize }
      this.paging(params)
    },
    handleSearch () {
      var params = {username: this.username, name: this.name, pageNum: this.page.pageNum, pageSize: this.page.pageSize}
      this.paging(params)
    },
    paging (params) {
      this.loading = true
      this.getUser(params).then(res => {
        this.data = res.data.content
        this.page.pageTotal = res.data.totalElements
        this.page.pageNum = res.data.number + 1
        this.page.pageSize = res.data.size
        this.loading = false
      })
    },
    cancelUser () {
      this.userModalVisible = false
      this.$refs.userForm.resetFields()
    },
    submitUser () {
      this.$refs.userForm.validate(valid => {
        if (valid) {
          // 添加用户 避免编辑后传入id
          delete this.userForm.id
          this.submitLoading = true
          this.addUser(this.userForm).then(res => {
            this.submitLoading = false
            if (res.data === true) {
              this.$Message.success('操作成功')
              this.init()
              this.userModalVisible = false
            } else {
              this.$Message.error(res.msg)
              this.userModalVisible = false
            }
          })
        }
      })
    },
    cancelUserEdit () {
      this.userModalVisibleEdit = false
      this.$refs.userFormEdit.resetFields()
      this.$refs.userForm.resetFields()
    },
    submitUserEdit () {
      this.$refs.userFormEdit.validate(valid => {
        if (valid) {
          this.submitLoadingEdit = true
          this.modifyUser(this.userFormEdit).then(res => {
            this.submitLoadingEdit = false
            this.$refs.userForm.resetFields()
            if (res.data === true) {
              this.$Message.success('修改成功')
              this.init()
              this.userModalVisibleEdit = false
            } else {
              this.$Message.error(res.msg)
              this.userModalVisibleEdit = false
            }
          })
        }
      })
    },
    add () {
      this.$refs.userForm.resetFields()
      this.userModalVisible = true
    },
    edit (v) {
      this.$refs.userFormEdit.resetFields()
      // // 转换null为''
      for (let attr in v) {
        if (v[attr] === null) {
          v[attr] = ''
        }
      }
      let str = JSON.stringify(v)
      let userInfo = JSON.parse(str)
      this.userForm = userInfo
      this.userFormEdit.id = this.userForm.id
      this.userFormEdit.email = this.userForm.email
      this.userFormEdit.name = this.userForm.name
      this.userFormEdit.phone = this.userForm.phone
      this.userFormEdit.status = this.userForm.status
      this.userFormEdit.username = this.userForm.username
      this.userFormEdit.type = this.userForm.type
      this.userModalVisibleEdit = true
    },
    grand (v) {
      this.grandRoleModal = true
      this.$refs.userForm.resetFields()
      this.checkAllGroup = []
      // // 转换null为''
      for (let attr in v) {
        if (v[attr] === null) {
          v[attr] = ''
        }
      }
      let str = JSON.stringify(v)
      let userInfo = JSON.parse(str)
      this.userForm = userInfo
      this.getUserRole({id: v.id}).then(res => {
        for (let i in res.data) {
          this.checkAllGroup.push(res.data[i].id)
        }
      })
    },
    cancelUserRole () {
      this.grandRoleModal = false
    },
    submitUserRole () {
      this.roleSubmit = true
      console.log(this.checkAllGroup)
      this.grandRole({id: this.userForm.id, checkAllGroup: this.checkAllGroup}).then(res => {
        this.roleSubmit = false
        if (res.data === true) {
          this.$Message.success('添加成功')
          this.grandRoleModal = false
          this.checkAllGroup = []
        }
      })
    },
    modifyPassword (v) {
      this.$refs.userForm.resetFields()
      // // 转换null为''
      for (let attr in v) {
        if (v[attr] === null) {
          v[attr] = ''
        }
      }
      let str = JSON.stringify(v)
      let userInfo = JSON.parse(str)
      this.userForm = userInfo
      this.modifyPasswordModal = true
    },
    cancelPasswordModal () {
      this.modifyPasswordModal = false
      this.passwordSubmit = false
      this.newPasswordFrom.newPassword = ''
    },
    submitUserPassword () {
      this.passwordSubmit = true
      this.modifyUserPassword({id: this.userForm.id, password: this.newPasswordFrom.newPassword}).then(res => {
        this.passwordSubmit = false
        if (res.data === true) {
          this.$Message.success('修改成功')
          this.modifyPasswordModal = false
          this.newPassword = ''
        } else {
          this.$Message.success('修改失败')
          this.modifyPasswordModal = false
          this.newPassword = ''
        }
      })
    },
    enable (v) {
      this.$Modal.confirm({
        title: this.$t('i18n_common_affirm') + this.$t('i18n_common_enable'),
        content: this.$t('i18n_permission_user_confirm_start_info') + v.username + ' ?',
        onOk: () => {
          this.modifyUserStatus({id: v.id, status: '0'}).then(res => {
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
        content: this.$t('i18n_permission_user_confirm_disable_info') + v.username + ' ?',
        onOk: () => {
          this.modifyUserStatus({id: v.id, status: '1'}).then(res => {
            if (res.data === true) {
              this.$Message.success('操作成功')
              this.init()
            }
          })
        }
      })
    },
    // remove (v) {
    //   this.$Modal.confirm({
    //     title: '确认删除',
    //     content: '您确认要删除用户 ' + v.username + ' ?',
    //     onOk: () => {
    //       this.deleteUser({id: v.id}).then(res => {
    //         if (res.data === true) {
    //           this.$Message.success('删除成功')
    //           this.init()
    //         }
    //       })
    //     }
    //   })
    // },
    handleCheckAll () {
      if (this.indeterminate) {
        this.checkAll = false
      } else {
        this.checkAll = !this.checkAll
      }
      this.indeterminate = false

      if (this.checkAll) {
        for (let i in this.roleList) {
          this.checkAllGroup.push(this.roleList[i].id)
        }
      } else {
        this.checkAllGroup = []
      }
    },
    checkAllGroupChange (data) {
      if (data.length === this.roleList.length) {
        this.indeterminate = false
        this.checkAll = true
      } else if (data.length > 0) {
        this.indeterminate = true
        this.checkAll = false
      } else {
        this.indeterminate = false
        this.checkAll = false
      }
    }
  }
}
</script>

<style lang="less" scoped>
  .vertical-center-modal{
    display: flex;
    align-items: center;
    justify-content: center;

    .ivu-modal{
      top: 0;
    }
  }
</style>
