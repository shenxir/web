import { getItem } from '@/utils/storage'
import { ElMessage } from 'element-plus'

/**
 * 路由守卫配置
 * 处理权限控制、页面标题等
 */

/**
 * 检查用户是否已登录
 * @returns {object|null} 用户对象或null
 */
function getUser() {
  return getItem('currentUser')
}

/**
 * 检查是否为管理员
 * @param {object} user - 用户对象
 * @returns {boolean}
 */
function isAdmin(user) {
  return user && user.role === 'admin'
}

/**
 * 设置页面标题
 * @param {object} to - 路由目标
 */
function setPageTitle(to) {
  const title = to.meta.title
  if (title) {
    document.title = `${title} - 校园失物招领系统`
  } else {
    document.title = '校园失物招领系统'
  }
}

/**
 * 路由前置守卫
 * @param {object} to - 目标路由
 * @param {object} from - 来源路由
 * @param {function} next - 下一步函数
 */
export function beforeEachGuard(to, from, next) {
  const user = getUser()

  // 设置页面标题
  setPageTitle(to)

  // 1. 处理游客专用页面（如登录页）
  // 已登录用户访问登录页，重定向到首页
  if (to.meta.guestOnly && user) {
    ElMessage.info('您已登录，无需再次登录')
    return next({ name: 'Home' })
  }

  // 2. 处理需要登录的页面
  if (to.meta.requiresAuth && !user) {
    ElMessage.warning('请先登录')
    return next({
      name: 'Login',
      query: { redirect: to.fullPath }
    })
  }

  // 3. 处理需要管理员权限的页面
  if (to.meta.requiresAdmin && !isAdmin(user)) {
    ElMessage.error('没有权限访问该页面')
    return next({ name: 'Home' })
  }

  // 4. 处理普通用户专用页面（管理员不能访问）
  if (to.meta.userOnly && isAdmin(user)) {
    ElMessage.warning('管理员不能访问该页面')
    return next({ name: 'AdminDashboard' })
  }

  // 5. 所有条件通过，放行
  next()
}

/**
 * 路由后置守卫
 * @param {object} to - 目标路由
 * @param {object} from - 来源路由
 */
export function afterEachGuard(to, from) {
  // 可以在这里添加页面加载完成后的逻辑
  // 例如：隐藏loading、滚动到顶部等
}

/**
 * 安装路由守卫
 * @param {object} router - Vue Router实例
 */
export function setupRouterGuards(router) {
  router.beforeEach(beforeEachGuard)
  router.afterEach(afterEachGuard)
}

export default {
  beforeEachGuard,
  afterEachGuard,
  setupRouterGuards
}
