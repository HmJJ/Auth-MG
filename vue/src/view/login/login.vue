<style lang="less">
  @import './login.less';
</style>

<template>
  <div class="login" @keydown.enter="handleLogin">
    <div class="login-con">
      <Card icon="log-in" title="欢迎登录" :bordered="false">
        <div class="form-con">
          <login-form @on-success-valid="handleSubmit"></login-form>
        </div>
        <Dropdown trigger="click" @on-click="selectLang">
          <a href="javascript:void(0)">
            {{title}}:{{languageType}}
            <Icon type="arrow-down-b"></Icon>
          </a>
          <DropdownMenu slot="list">
            <DropdownItem v-for="(value, key) in localList" :name="key" :key="`lang-${key}`">{{ value }}</DropdownItem>
          </DropdownMenu>
        </Dropdown>
      </Card>
    </div>
  </div>
</template>

<script>
import LoginForm from '_c/login-form'
import { mapActions } from 'vuex'
export default {
  data () {
    return {
      langList: {
        'ja_JP': '言語',
        'en_US': 'Lang',
        'zh_CN': '语言'
      },
      localList: {
        'ja_JP': '日本語',
        'en_US': 'English',
        'zh_CN': '中文简体'
      },
      title: 'language',
      languageType: 'Default(中文简体)'
    }
  },
  components: {
    LoginForm,
    init () {
      this.$i18n.locale = 'zh_CN'
    }
  },
  methods: {
    ...mapActions([
      'handleLogin',
      'getUserInfo'
    ]),
    handleSubmit ({ username, password }) {
      this.handleLogin({ username, password }).then(res => {
        this.getUserInfo().then(res => {
          this.$router.push({
            name: 'home'
          })
        })
      })
    },
    selectLang (name) {
      this.title = this.langList[name]
      this.languageType = this.localList[name]
      this.$i18n.locale = name
      if (name === 'zh_CN') {
        this.$Message.success('部分元素需要页面刷新后改变语言，但不影响使用。')
      } else if (name === 'ja_JP') {
        this.$Message.success('部分元素需要页面刷新后改变语言，但不影响使用。')
      } else {
        this.$Message.success('Some elements will not change their language before page refreshing, do not affect usage effect.')
      }
      this.$emit('on-lang-change', name)
    }
  }
}
</script>

<style>

</style>
