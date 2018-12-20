import Main from '@/view/main'
import parentView from '@/components/parent-view'

/**
 * iview-admin中meta除了原生参数外可配置的参数:
 * meta: {
 *  hideInMenu: (false) 设为true后在左侧菜单不会显示该页面选项
 *  notCache: (false) 设为true后页面不会缓存
 *  access: (null) 可访问该页面的权限数组，当前路由设置的权限会影响子路由
 *  icon: (-) 该页面在左侧菜单、面包屑和标签导航处显示的图标，如果是自定义图标，需要在图标名称前加下划线'_'
 * }
 */

export default [
  {
    path: '/login',
    name: 'login',
    meta: {
      title: 'Login - 登录',
      hideInMenu: true
    },
    component: () => import('@/view/login/login.vue')
  },
  {
    path: '/',
    name: 'i18n_home',
    redirect: '/home',
    component: Main,
    meta: {
      hideInMenu: true,
      notCache: true
    },
    children: [
      {
        path: 'home',
        name: 'home',
        meta: {
          hideInMenu: true,
          notCache: true
        },
        component: () => import('@/view/single-page/home')
      }
    ]
  },
  {
    path: '/permission',
    name: 'i18n_permission_manage',
    meta: {
      icon: 'key',
      title: '权限管理'
    },
    component: Main,
    children: [
      {
        path: 'characterManage',
        name: 'i18n_character_manage',
        meta: {
          icon: 'android-contacts',
          title: '角色管理'
        },
        component: () => import('@/view/permission/roleManage.vue')
      },
      {
        path: 'userManage',
        name: 'i18n_user_manage',
        meta: {
          icon: 'person',
          title: '用户管理'
        },
        component: () => import('@/view/permission/userManage.vue')
      },
      {
        path: 'resource',
        name: 'i18n_resource_manage',
        meta: {
          icon: 'navicon-round',
          title: '资源管理'
        },
        component: () => import('@/view/permission/menuManage.vue')
      }
    ]
  },
  {
    path: '/system',
    name: 'i18n_system_manage',
    meta: {
      icon: 'gear-b',
      title: '系统管理'
    },
    component: Main,
    children: [
      {
        path: '/schedule_page',
        name: 'i18n_schedule_manage',
        meta: {
          icon: 'ios-navigate',
          title: '定时任务管理'
        },
        component: () => import('@/view/schedule/schedule.vue')
      }
    ]
  }
]
