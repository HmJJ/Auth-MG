<template>
  <header class="example-header">
    <span>{{tip}}</span>
  </header>
</template>

<script>
export default {
  name: 'HeaderTip',
  props: ['tip']
}
</script>

<style scoped>
  .example-header {
    font-weight: 500;
    margin: 30px 0 10px;
    position: relative;
  }
  .example-header span {
    display: inline-block;
    background: #fff;
    padding: 0 5px 0 30px;
    position: relative;
    margin-right: 80%;
    font-size: 16px;
  }
  .example-header:before {
    content: "";
    display: block;
    width: 100%;
    height: 1px;
    background: #eee;
    position: absolute;
    top: 10px;
    left: 0;
  }
</style>
