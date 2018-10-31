// import Main from '@/view/main'
// import parentView from '@/components/parent-view'

export default [
  {
    path: '/',
    name: 'HelloWorld',
    component: () => import('@/view/hello/HelloWorld.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/view/login/login.vue')
  }
]
