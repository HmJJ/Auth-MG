<template>
  <div class='login'>
    <Card class='loginPart' icon="md-log-in" title="欢迎登录" :bordered="true">
      <div class='loginInfo'>
        <Row>
          <Form ref="formInline" :model="formInline" :rules="ruleInline" inline>
            <i-Col span='24'>
              <FormItem prop="user">
                <i-Input type="text" style="width: 250px" v-model="formInline.user" placeholder="Username">
                  <Icon type="ios-person-outline" slot="prepend"></Icon>
                </i-Input>
              </FormItem>
            </i-Col>
            <i-Col span='24'>
              <FormItem prop="password">
                <i-Input type="password" style="width: 250px" v-model="formInline.password" placeholder="Password">
                  <Icon type="ios-lock-outline" slot="prepend"></Icon>
                </i-Input>
              </FormItem>
            </i-Col>
            <i-Col span='24'>
              <FormItem>
                <Button type="primary" long @click="handleSubmit('formInline')">登录</Button>
                <a type="primary" @click="register()">去注册</a>
              </FormItem>
            </i-Col>
          </Form>
        </Row>
      </div>
    </Card>
  </div>
</template>
<script>
export default {
  data() {
    return {
      formInline: {
        user: '',
        password: ''
      },
      ruleInline: {
        user: [
          {
            required: true,
            message: '请输入用户名.',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '请输入密码.',
            trigger: 'blur'
          },
          {
            type: 'string',
            min: 6,
            message: '密码不得少于6位.',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          this.$Message.success('Success!')
        } else {
          this.$Message.error('Fail!')
        }
      })
    },
    register() {
      this.$router.push({ path: '/register' })
    }
  }
}
</script>

<style lang='less' scoped>
  @import './login.less';
</style>
