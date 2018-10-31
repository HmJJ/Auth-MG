// import Main from '@/view/main'
// import parentView from '@/components/parent-view'

export default [
  {
    path: '/',
    name: 'Login',
    component: () => import('@/view/login/login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/view/login/register.vue')
  }
]
