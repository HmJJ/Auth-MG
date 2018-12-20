<template>
  <div>
    <!-- <Dropdown trigger="click" @on-click="selectLang">
      <a href="javascript:void(0)">
        {{ title }}
        <Icon type="arrow-down-b"></Icon>
      </a>
      <DropdownMenu slot="list">
        <DropdownItem v-for="(value, key) in localList" :name="key" :key="`lang-${key}`">{{ value }}</DropdownItem>
      </DropdownMenu>
    </Dropdown> -->
  </div>
</template>

<script>
export default {
  name: 'Language',
  props: {
    lang: String
  },
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
      }
    }
  },
  watch: {
    lang (lang) {
      this.$i18n.locale = lang
    }
  },
  computed: {
    title () {
      return this.langList[this.lang]
    }
  },
  methods: {
    selectLang (name) {
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
