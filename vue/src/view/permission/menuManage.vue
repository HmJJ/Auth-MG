<style lang="less">
@import "./menuManage.less";
textarea{
  resize: none;
}
</style>
<template>
    <div class="search">
        <Card>
          <div slot="title">
            <Icon type="navicon-round"></Icon>
            {{$t('i18n_resource_manage')}}
          </div>
          <Row class="operation">
            <Button @click="addRootMenu" type="primary" icon="android-add">{{$t('i18n_resource_add_parent')}}</Button>
            <Button @click="addMenu" type="primary" icon="android-add">{{$t('i18n_resource_add_child')}}</Button>
            <Button @click="del" icon="trash-b">{{$t('i18n_resource_batch_delete')}}</Button>
            <Dropdown @on-click="handleDropdown">
              <Button>
                {{$t('i18n_resource_more_operation')}}
                <Icon type="android-arrow-dropdown"></Icon>
              </Button>
              <DropdownMenu slot="list">
                <DropdownItem name="refresh">{{$t('i18n_common_refresh')}}</DropdownItem>
                <DropdownItem name="expandOne">{{$t('i18n_resource_unwind_1')}}</DropdownItem>
                <DropdownItem name="expandTwo">{{$t('i18n_resource_unwind_2')}}</DropdownItem>
                <DropdownItem name="expandAll">{{$t('i18n_resource_unwind_all')}}</DropdownItem>
              </DropdownMenu>
            </Dropdown>
          </Row>
          <Row type="flex" justify="start" class="code-row-bg">
            <i-col span="6">
              <Alert show-icon>
                {{$t('i18n_resource_select_edit')}}： <span class="select-count">{{ editTitle }}</span>
                <a class="select-clear" v-if="menuForm.id" @click="canelEdit">{{$t('i18n_resource_select_cancle')}}</a>
              </Alert>
              <Tree :data="data" show-checkbox @on-check-change="changeSelect" @on-select-change="selectTree"></Tree>
              <Spin size="large" fix v-if="loading"></Spin>
            </i-col>
            <i-col span="9">
              <Form ref="menuForm" :model="menuForm" :label-width="85" :rules="menuFormValidate">
                <FormItem :label="$t('i18n_common_type')" prop="type">
                  <RadioGroup v-model="menuForm.type">
                    <Radio :label="'TYPE_MENU'" :disabled="isButton">
                      <Icon type="ios-list-outline"></Icon>
                      <span>{{$t('i18n_resource_page_menu')}}</span>
                    </Radio>
                    <Radio :label="'TYPE_BUTTON'" :disabled="isMenu">
                      <Icon type="log-in"></Icon>
                      <span>{{$t('i18n_resource_operate_button')}}</span>
                    </Radio>
                  </RadioGroup>
                </FormItem>
                <FormItem :label="$t('i18n_resource_name')" prop="title">
                  <Input v-model="menuForm.title"/>
                </FormItem>
                <FormItem :label="$t('i18n_resource_code')" prop="code">
                  <Input v-model="menuForm.code"/>
                </FormItem>
                <FormItem label="URL" prop="url">
                  <Input v-model="menuForm.url"/>
                </FormItem>
                <FormItem :label="$t('i18n_resource_sort')" prop="sort">
                  <InputNumber :max="10000" :min="0" v-model="menuForm.sort"></InputNumber>
                  <span style="margin-left:5px">{{$t('i18n_resource_sort_info')}}</span>
                </FormItem>
                <FormItem :label="$t('i18n_resource_whether_enable')" prop="status">
                  <i-switch size="large" v-model="editStatus" @on-change="changeEditSwitch">
                    <span slot="open">{{$t('i18n_common_enable')}}</span>
                    <span slot="close">{{$t('i18n_common_disable')}}</span>
                  </i-switch>
                </FormItem>
                <FormItem :label="$t('i18n_resource_note')" prop="description">
                  <Input v-model="menuForm.description" @input="descInput" type="textarea" :maxlength="64" :rows="4" />
                  <div style="text-align: right;">
                    <span>{{ remnant }}/64</span>
                  </div>
                </FormItem>
                <Form-item>
                  <Button @click="submitEdit" :loading="submitLoading" type="primary" icon="ios-create-outline">{{$t('i18n_resource_modify_save')}}</Button>
                  <Button @click="handleReset" >{{$t('i18n_common_reset')}}</Button>
                </Form-item>
              </Form>
            </i-col>
          </Row>
        </Card>

        <Modal draggable :title="modalTitle" v-model="menuModalVisible" :mask-closable='false' :width="500" :styles="{top: '30px'}">
          <Form ref="menuFormAdd" :model="menuFormAdd" :label-width="85" :rules="menuFormValidate">
            <div v-if="showParent">
              <FormItem :label="$t('i18n_resource_superior_node') + ': '">
                {{parentTitle}}
              </FormItem>
            </div>
            <FormItem :label="$t('i18n_common_type')" prop="type">
              <RadioGroup v-model="menuFormAdd.type">
                <Radio :label="'TYPE_MENU'" :disabled="isButtonAdd">
                  <Icon type="ios-list-outline"></Icon>
                  <span>{{$t('i18n_resource_page_menu')}}</span>
                </Radio>
                <Radio :label="'TYPE_BUTTON'" :disabled="isMenuAdd">
                  <Icon type="log-in"></Icon>
                  <span>{{$t('i18n_resource_operate_button')}}</span>
                </Radio>
              </RadioGroup>
            </FormItem>
            <FormItem :label="$t('i18n_resource_name')" prop="title">
              <Input v-model="menuFormAdd.title" placeholder="请输入资源名称"/>
            </FormItem>
            <FormItem :label="$t('i18n_resource_code')" prop="code">
              <Input v-model="menuFormAdd.code" placeholder="请以menu_或button_开头"/>
            </FormItem>
            <FormItem label="URL" prop="url">
              <Input v-model="menuFormAdd.url" placeholder="请输入URL"/>
            </FormItem>
            <FormItem :label="$t('i18n_resource_sort')" prop="sort">
              <InputNumber :max="10000" :min="0" v-model="menuFormAdd.sort"></InputNumber>
              <span style="margin-left:5px">{{$t('i18n_resource_sort_info')}}</span>
            </FormItem>
            <FormItem :label="$t('i18n_resource_whether_enable')" prop="status">
              <i-switch size="large" v-model="addStatus" @on-change="changeAddSwitch">
                <span slot="open">{{$t('i18n_common_enable')}}</span>
                <span slot="close">{{$t('i18n_common_disable')}}</span>
              </i-switch>
            </FormItem>
            <FormItem :label="$t('i18n_resource_note')" prop="description">
              <Input v-model="menuFormAdd.description" @input="descInputAdd" type="textarea" :maxlength="64" :rows="4" />
              <div style="text-align: right;">
                <span>{{ remnantAdd }}/64</span>
              </div>
            </FormItem>
          </Form>
          <div slot="footer">
            <Button type="text" @click="cancelAdd">{{$t('i18n_common_cancle')}}</Button>
            <Button type="primary" :loading="submitLoading" @click="submitAdd">{{$t('i18n_common_submit')}}</Button>
          </div>
        </Modal>
        <Modal v-model="deleteModal" width="360">
          <p slot="header" style="color:#f60;text-align:center">
            <Icon type="information-circled" />
            <span>{{$t('i18n_common_confirm_delete')}}</span>
          </p>
          <div style="text-align:center">
            <p>{{$t('i18n_resource_delete_info1')}} {{ selectCount }} {{$t('i18n_resource_delete_info2')}}?</p>
            <!-- <p>是否继续删除?</p> -->
          </div>
          <div slot="footer">
            <Button type="error" size="large" long :loading="modal_loading" @click="delAll">{{$t('i18n_common_delete')}}</Button>
          </div>
        </Modal>
    </div>
</template>

<script>
import { mapActions } from 'vuex'
export default {
  name: 'menu-manage',
  data () {
    return {
      remnant: 0,
      remnantAdd: 0,
      loading: true,
      expandLevel: 1,
      menuModalVisible: false,
      selectList: [],
      selectCount: 0,
      showParent: false,
      parentTitle: '',
      isButtonAdd: false,
      isMenuAdd: false,
      isMenu: false,
      isButton: false,
      editStatus: true,
      addStatus: true,
      deleteModal: false,
      modal_loading: false,
      editTitle: '',
      modalTitle: '',
      menuForm: {
        id: '',
        parentId: '',
        title: '',
        code: '',
        type: 'TYPE_MENU',
        sort: null,
        status: '0',
        url: '',
        description: ''
      },
      menuFormAdd: {
        type: 'TYPE_MENU'
      },
      menuFormValidate: {
        title: [{ required: true, message: '资源名称不能为空', trigger: 'blur' }],
        code: [{ required: true, message: '资源代码不能为空', trigger: 'blur' }],
        url: [{ required: true, message: 'url不能为空', trigger: 'blur' }]
      },
      submitLoading: false,
      data: []
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    ...mapActions([
      'addResource',
      'getAllList',
      'deletePermission'
    ]),
    init () {
      this.loading = true
      this.getAllList().then(res => {
        this.loading = false
        if (res.data !== undefined) {
          // 仅展开指定级数
          let expandLevel = this.expandLevel
          res.data.forEach(function (e) {
            if (expandLevel === 1) {
              if (e.children && e.children.length > 0) {
                e.children.forEach(function (c) {
                  c.expand = false
                })
              }
            } else if (expandLevel === 2) {
              e.expand = true
            }
          })
          if (expandLevel === 3) {
            this.expandTreeAll(res.data, true)
          }
        }
        this.data = res.data
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
    handleDropdown (name) {
      if (name === 'expandOne') {
        this.expandLevel = 1
        this.init()
      } else if (name === 'expandTwo') {
        this.expandLevel = 2
        this.init()
      } else if (name === 'expandAll') {
        this.expandLevel = 3
        this.init()
      } else if (name === 'refresh') {
        this.init()
      }
    },
    descInput () {
      var txtVal = this.menuForm.description.length
      this.remnant = txtVal
    },
    descInputAdd () {
      var txtVal = this.menuFormAdd.description.length
      this.remnantAdd = txtVal
    },
    submitEdit () {
      this.$refs.menuForm.validate(valid => {
        if (valid) {
          if (!this.menuForm.id) {
            this.$Message.warning('请先点击选择要修改的资源节点')
            return
          }
          const param = this.menuForm
          this.submitLoading = true
          this.addResource(param).then(res => {
            if (res.data !== undefined) {
              this.$Message.success('操作成功')
              this.submitLoading = false
              this.init()
            } else {
              this.$Message.error(res.msg)
            }
          })
        }
      })
    },
    handleReset () {
      this.$refs.menuForm.resetFields()
      this.editStatus = true
      this.menuForm.status = '0'
      this.remnant = 0
    },
    changeEditSwitch (v) {
      if (v) {
        this.menuForm.status = '0'
      } else {
        this.menuForm.status = '1'
      }
    },
    changeAddSwitch (v) {
      if (v) {
        this.menuFormAdd.status = '0'
      } else {
        this.menuFormAdd.status = '1'
      }
    },
    selectTree (v) {
      if (v.length > 0) {
        if (v[0].type === 'TYPE_MENU') {
          this.isButton = false
          this.isMenu = true
        } else {
          this.isButton = true
          this.isMenu = false
        }
        if (v[0].status === '0') {
          this.editStatus = true
        } else {
          this.editStatus = false
        }
        // 转换null为""
        for (let attr in v[0]) {
          if (v[0][attr] === null) {
            v[0][attr] = ''
          }
        }
        let str = JSON.stringify(v[0])
        let menu = JSON.parse(str)
        this.menuForm = menu
        this.menuForm.code = menu.code
        this.editTitle = menu.title
        this.remnant = this.menuForm.description.length
      }
    },
    canelEdit () {
      this.isMenu = false
      this.isButton = false
      this.$refs.menuForm.resetFields()
      delete this.menuForm.id
      this.editTitle = ''
      this.menuForm.id = ''
      this.editStatus = true
      this.selectedTreeAll(this.data, false)
    },
    // 递归取消节点
    selectedTreeAll (permData, select) {
      let that = this
      permData.forEach(function (e) {
        e.selected = select
        if (e.children && e.children.length > 0) {
          that.selectedTreeAll(e.children, select)
        }
      })
    },
    addMenu () {
      if (this.menuForm.id === '' || this.menuForm.id === null) {
        this.$Message.warning('请先点击选择一个菜单资源节点')
        return
      }
      let type = 'TYPE_MENU'
      this.parentTitle = this.menuForm.title
      this.modalTitle = this.$t('i18n_resource_menu_permission')
      this.showParent = true
      this.menuFormAdd = {
        type: type,
        parentId: this.menuForm.id,
        title: '',
        code: '',
        sort: null,
        status: '0',
        url: '',
        description: ''
      }
      this.menuModalVisible = true
    },
    cancelAdd () {
      this.menuModalVisible = false
      this.remnantAdd = 0
    },
    submitAdd () {
      this.$refs.menuFormAdd.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.menuFormAdd.sort === null) {
            this.menuFormAdd.sort = null
          }
          if (this.menuFormAdd.description === undefined || this.menuFormAdd.description === null) {
            this.menuFormAdd.description = ''
          }
          this.addResource(this.menuFormAdd).then(res => {
            this.submitLoading = false
            this.$Message.success('添加成功')
            this.init()
            this.menuModalVisible = false
          })
        }
      })
    },
    addRootMenu () {
      this.modalTitle = this.$t('i18n_resource_add_parent')
      this.isMenuAdd = true
      this.isButtonAdd = false
      this.showParent = false
      this.menuFormAdd = {
        type: 'TYPE_MENU',
        sort: null,
        status: '0'
      }
      this.menuModalVisible = true
    },
    changeSelect (v) {
      this.selectCount = v.length
      this.selectList = v
    },
    del () {
      if (this.selectCount <= 0) {
        this.$Message.warning('您还未勾选要删除的数据')
        return
      }
      this.deleteModal = true
    },
    delAll () {
      this.modal_loading = true
      var jsons = []
      if (this.selectList !== undefined) {
        this.selectList.forEach(function (e) {
          let temp = {}
          temp = e.id
          jsons.push(temp)
        })
      }
      var params = { 'selectList': jsons }
      this.deletePermission(params).then(res => {
        if (res.data !== null) {
          this.$Message.success('删除成功')
          this.modal_loading = false
          this.selectList = []
          this.selectCount = 0
          this.canelEdit()
          this.init()
        }
        this.deleteModal = false
      })
    }
  }
}
</script>
