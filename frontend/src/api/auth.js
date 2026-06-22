import api from './index'

/**
 * 用户登录
 * @param {object} data - { username, password }
 * @returns {Promise}
 */
export function login(data) {
  return api.post('/auth/login', data)
}

/**
 * 用户登出
 * @returns {Promise}
 */
export function logout() {
  return api.post('/auth/logout')
}

/**
 * 获取当前用户信息
 * @returns {Promise}
 */
export function getUserInfo() {
  return api.get('/auth/userinfo')
}

/**
 * 更新用户信息
 * @param {object} data - 用户信息
 * @returns {Promise}
 */
export function updateUserInfo(data) {
  return api.put('/auth/userinfo', data)
}

/**
 * 修改密码
 * @param {object} data - { oldPassword, newPassword }
 * @returns {Promise}
 */
export function changePassword(data) {
  return api.post('/auth/password', data)
}

/**
 * 用户注册
 * @param {object} data - { username, password, realName, phone }
 * @returns {Promise}
 */
export function register(data) {
  return api.post('/auth/register', data)
}

export default {
  login,
  logout,
  getUserInfo,
  updateUserInfo,
  changePassword,
  register
}
