/**
 * sessionStorage 工具封装
 * 提供类型安全的存储操作
 */

const STORAGE_PREFIX = 'lostfound_'

/**
 * 获取存储的值
 * @param {string} key - 存储键名
 * @returns {*} 解析后的值，不存在返回null
 */
export function getItem(key) {
  try {
    const value = sessionStorage.getItem(STORAGE_PREFIX + key)
    return value ? JSON.parse(value) : null
  } catch (error) {
    console.error(`获取存储项 ${key} 失败:`, error)
    return null
  }
}

/**
 * 设置存储的值
 * @param {string} key - 存储键名
 * @param {*} value - 要存储的值
 */
export function setItem(key, value) {
  try {
    sessionStorage.setItem(STORAGE_PREFIX + key, JSON.stringify(value))
  } catch (error) {
    console.error(`设置存储项 ${key} 失败:`, error)
  }
}

/**
 * 移除存储的值
 * @param {string} key - 存储键名
 */
export function removeItem(key) {
  try {
    sessionStorage.removeItem(STORAGE_PREFIX + key)
  } catch (error) {
    console.error(`移除存储项 ${key} 失败:`, error)
  }
}

/**
 * 清空所有存储
 */
export function clear() {
  try {
    // 只清除带前缀的项
    const keys = Object.keys(sessionStorage).filter(k => k.startsWith(STORAGE_PREFIX))
    keys.forEach(k => sessionStorage.removeItem(k))
  } catch (error) {
    console.error('清空存储失败:', error)
  }
}

/**
 * 检查是否存在某个键
 * @param {string} key - 存储键名
 * @returns {boolean}
 */
export function hasItem(key) {
  return sessionStorage.getItem(STORAGE_PREFIX + key) !== null
}

export default {
  getItem,
  setItem,
  removeItem,
  clear,
  hasItem
}
