<template>
  <div class="layout">
    <Layout>
        <Header :style="{position: 'fixed', width: '100%',zIndex:'1000'}">
          <Menu mode="horizontal" theme="dark" active-name="1">
            <div class="layout-logo">
              <img src="../assets/chain_logo.png" style="width: 120px;height: 40px"/>
            </div>
            <div class="layout-nav">
              <MenuItem name="1">
                <Icon type="ios-navigate"></Icon>
                Item 1
              </MenuItem>
              <MenuItem name="2">
                <Icon type="ios-keypad"></Icon>
                Item 2
              </MenuItem>
              <MenuItem name="3">
                <Icon type="ios-analytics"></Icon>
                Item 3
              </MenuItem>
              <MenuItem name="4">
                <Icon type="ios-paper"></Icon>
                Item 4
              </MenuItem>
            </div>
          </Menu>
        </Header>
      <Layout >
        <Content :style="{padding: '88px 0  24px 0', minHeight: '280px', background: '#e9eaec'}">
          <Layout>
            <Sider :style="{background: '#fff'}">
              <Menu active-name="1-2" theme="light" width="auto" :open-names="['1']">
                <Submenu name="1">
                  <template slot="title">
                    <Icon type="email"></Icon>
                    邮件模板管理
                  </template>
                  <MenuItem name="1-1"><router-link to="/emailManager/show">模板展示</router-link></MenuItem>
                  <MenuItem name="1-2"><router-link to="/emailManager/add">添加模板</router-link></MenuItem>
                  <MenuItem name="1-3"><router-link to="/emailManager/modify">修改模板</router-link></MenuItem>
                  <MenuItem name="1-4"><router-link to="/emailManager/send">群发邮件</router-link></MenuItem>
                </Submenu>
                <Submenu name="2">
                  <template slot="title">
                    <Icon type="ios-eye"></Icon>
                    账号明细
                  </template>
                  <MenuItem name="2-1"><router-link to="/Systemaccount">系统账号明细</router-link></MenuItem>
                  <MenuItem name="2-2"><router-link to="/Infosearch">信息搜索</router-link></MenuItem>
                </Submenu>
                <Submenu name="3">
                  <template slot="title">
                    <Icon type="ios-analytics"></Icon>
                    Item 3
                  </template>
                  <MenuItem name="3-1">Option 1</MenuItem>
                  <MenuItem name="3-2">Option 2</MenuItem>
                </Submenu>
                <Submenu name="4">
                  <template slot="title">
                    <Icon type="settings"></Icon>
                    系统设置
                  </template>
                  <MenuItem name="4-1"><router-link to="/system/dictionary">数据字典</router-link></MenuItem>
                  <MenuItem name="4-2"><router-link to="/system/i18n-language">国际化/语言</router-link></MenuItem>
                  <MenuItem name="4-3"><router-link to="/system/i18n-code">国际化/配置</router-link></MenuItem>
                </Submenu>
              </Menu>
            </Sider>
            <Content :style="{padding: '24px', minHeight: '560px', background: '#fff'}">
              <router-view/>
            </Content>
          </Layout>
        </Content>
      </Layout>
      <Footer>Footer</Footer>
    </Layout>
  </div>
</template>
<script>
  export default {
      name : 'Home'
  }
</script>
<style scoped>
  .layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    position: relative;
    border-radius: 4px;
    overflow: hidden;
  }
  .layout-logo{
    width: 100px;
    height: 30px;
    border-radius: 3px;
    float: left;
    position: relative;
    top: 15px;
    left: 20px;
  }
  .layout-nav{
    width: 420px;
    margin: 0 auto;
    margin-right: 20px;
  }
  .layout-footer-center{
    text-align: center;
  }
</style>
