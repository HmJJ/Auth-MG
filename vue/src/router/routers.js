export default [
  {
    path: '/',
    name: 'HelloWorld',
    meta: {
      title: 'Hello - 你好',
      hideInMenu: true
    },
    component: () => import('@/view/helloworld/HelloWorld.vue')
  }
]
