import { createRouter, createWebHistory } from 'vue-router'
import { setupRouterGuards } from './guards'

/**
 * 路由配置
 * meta字段说明：
 * - title: 页面标题
 * - requiresAuth: 是否需要登录
 * - requiresAdmin: 是否需要管理员权限
 * - guestOnly: 是否仅游客可访问
 * - userOnly: 是否仅普通用户可访问
 * - layout: 布局类型（default/admin/blank）
 */
const routes = [
  // ===== 公开页面 =====
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/home/HomeView.vue'),
    meta: {
      title: '首页',
      requiresAuth: false,
      layout: 'default'
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/auth/LoginView.vue'),
    meta: {
      title: '登录',
      requiresAuth: false,
      guestOnly: true,
      layout: 'blank'
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/auth/RegisterView.vue'),
    meta: {
      title: '注册',
      requiresAuth: false,
      guestOnly: true,
      layout: 'blank'
    }
  },
  {
    path: '/change-password',
    name: 'ChangePassword',
    component: () => import('../views/auth/ChangePasswordView.vue'),
    meta: {
      title: '修改密码',
      requiresAuth: false,
      layout: 'blank'
    }
  },
  {
    path: '/items',
    name: 'ItemList',
    component: () => import('../views/items/ItemListView.vue'),
    meta: {
      title: '物品列表',
      requiresAuth: false,
      layout: 'default'
    }
  },
  {
    path: '/item/:id',
    name: 'ItemDetail',
    component: () => import('../views/items/ItemDetailView.vue'),
    meta: {
      title: '物品详情',
      requiresAuth: false,
      layout: 'default'
    }
  },

  // ===== 需要登录的页面 =====
  {
    path: '/publish',
    name: 'Publish',
    component: () => import('../views/items/PublishView.vue'),
    meta: {
      title: '发布物品',
      requiresAuth: true,
      layout: 'default'
    }
  },
  {
    path: '/my/items',
    name: 'MyItems',
    component: () => import('../views/items/MyItemsView.vue'),
    meta: {
      title: '我的物品',
      requiresAuth: true,
      userOnly: true,
      layout: 'default'
    }
  },
  {
    path: '/my/claims',
    name: 'MyClaims',
    component: () => import('../views/items/MyClaimsView.vue'),
    meta: {
      title: '我的认领',
      requiresAuth: true,
      userOnly: true,
      layout: 'default'
    }
  },

  // ===== 管理员路由 =====
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: () => import('../views/admin/AdminDashboard.vue'),
    meta: {
      title: '管理后台',
      requiresAuth: true,
      requiresAdmin: true,
      layout: 'admin'
    }
  },
  {
    path: '/admin/items',
    name: 'AdminItems',
    component: () => import('../views/admin/AdminItems.vue'),
    meta: {
      title: '物品管理',
      requiresAuth: true,
      requiresAdmin: true,
      layout: 'admin'
    }
  },
  {
    path: '/admin/claims',
    name: 'AdminClaims',
    component: () => import('../views/admin/AdminClaims.vue'),
    meta: {
      title: '认领管理',
      requiresAuth: true,
      requiresAdmin: true,
      layout: 'admin'
    }
  },
  {
    path: '/admin/announcements',
    name: 'AdminAnnouncement',
    component: () => import('../views/admin/AdminAnnouncement.vue'),
    meta: {
      title: '公告管理',
      requiresAuth: true,
      requiresAdmin: true,
      layout: 'admin'
    }
  },

  // ===== 404页面 =====
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/error/NotFound.vue'),
    meta: {
      title: '页面不存在',
      layout: 'blank'
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  // 滚动行为
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 安装路由守卫
setupRouterGuards(router)

export default router
