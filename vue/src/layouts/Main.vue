<template>
  <div class="container">
    <ul>
      <li>
        <v-link href="/">HelloWorld</v-link>
        <v-link href="/login">Login</v-link>
      </li>
    </ul>
    <slot></slot>
  </div>
</template>

<script>
import VLink from '../components/VLink'
export default {
  name: 'Main',
  components: {
    VLink
  }
}
</script>

<style scoped>
  .container {
    max-width: 600px;
    margin: 0 auto;
    padding: 15px 30px;
    background: #f9f7f5;
  }
</style>
