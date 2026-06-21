# 校园失物招领管理系统

> Campus Lost & Found Management System — 基于 Vue 3 + Spring Boot 的校园失物招领线上平台

## 项目简介

本系统旨在搭建统一的校园失物招领线上平台，支持用户发布失物/拾物信息、在线预约认领、管理员审核、状态更新、过期下架，实现校园物品互助寻回规范化、数字化。

### 核心业务功能

| 功能 | 说明 |
|------|------|
| **认领申请审核流程** | 用户提交认领 → 管理员审核（通过/拒绝） → 物品状态自动更新 |
| **物品状态自动流转** | 待认领 → 已认领 / 已过期，前后端自动检查并更新过期状态 |
| **信息重复发布校验** | 同一用户 24 小时内不可发布相同标题的物品，防止刷屏 |
| **时效自动下架逻辑** | 失物信息 30 天过期、拾物信息 15 天过期，自动下架 |
| **用户认领权限控制** | 未登录不可认领、不可认领自己发布的物品、管理员不可参与认领 |

## 技术栈

| 层级 | 技术 | 版本 | 说明 |
|------|------|------|------|
| **前端** | Vue | 3.5 | 渐进式 JavaScript 框架，`<script setup>` 语法 |
| | Vite | 8.0 | 前端构建工具，内置代理转发 |
| | Element Plus | 2.14 | Vue 3 UI 组件库 |
| | Pinia | 3.0 | 状态管理 |
| | Vue Router | 4.6 | 路由管理 + 导航守卫 |
| **后端** | Spring Boot | 3.2 | Java Web 框架 |
| | Java | 21 | 运行环境 |
| | Maven | 3.9 | 项目构建 |

> 测试数据使用静态数据，不依赖数据库。

## 快速开始

### 环境要求

- Java >= 21
- Node.js >= 18
- Maven >= 3.9

### 启动后端

```bash
# 在项目根目录
mvn spring-boot:run
# 后端运行在 http://localhost:8080
```

### 启动前端

```bash
# 进入前端目录
cd frontend

# 安装依赖（首次）
npm install

# 启动开发服务器
npm run dev
# 前端运行在 http://localhost:5173
# /api 请求自动代理到后端 8080 端口
```

### 演示账号

| 角色 | 用户名 | 密码 | 权限 |
|------|--------|------|------|
| 管理员 | `admin` | `admin123` | 物品管理、认领审核、数据概览 |
| 普通用户 | `zhangsan` | `123456` | 发布物品、申请认领、管理个人信息 |
| 普通用户 | `lisi` | `123456` | 同上 |
| 普通用户 | `wangwu` | `123456` | 同上 |
| 普通用户 | `zhaoliu` | `123456` | 同上 |

## 项目结构

```
webwork/
├── pom.xml                              # Maven 配置（Spring Boot 3.2）
├── src/main/java/org/example/lostfound/
│   ├── LostFoundApplication.java        # Spring Boot 启动类
│   ├── config/
│   │   └── CorsConfig.java              # 跨域配置
│   ├── model/
│   │   ├── User.java                    # 用户模型
│   │   ├── Item.java                    # 物品模型
│   │   └── Claim.java                   # 认领模型
│   ├── service/
│   │   ├── UserService.java             # 用户服务（登录）
│   │   ├── ItemService.java             # 物品服务（CRUD/过期/统计）
│   │   └── ClaimService.java            # 认领服务（CRUD/审核/统计）
│   └── controller/
│       ├── AuthController.java          # 认证接口
│       ├── ItemController.java          # 物品接口
│       └── ClaimController.java         # 认领接口
│
└── frontend/
    ├── index.html                       # HTML 入口
    ├── package.json                     # 前端依赖
    ├── vite.config.js                   # Vite 配置（含 API 代理）
    └── src/
        ├── main.js                      # 应用入口
        ├── App.vue                      # 根组件
        ├── style.css                    # 全局样式
        ├── mock/data.js                 # 前端静态 mock 数据
        ├── utils/helpers.js             # 工具函数
        ├── stores/                      # Pinia 状态管理
        │   ├── auth.js
        │   ├── items.js
        │   └── claims.js
        ├── router/index.js              # 路由 + 导航守卫
        ├── components/                  # 公共组件
        │   ├── AppNavbar.vue
        │   ├── AdminSidebar.vue
        │   └── ItemCard.vue
        └── views/                       # 页面视图
            ├── LoginView.vue
            ├── HomeView.vue
            ├── ItemListView.vue
            ├── ItemDetailView.vue
            ├── PublishView.vue
            ├── MyItemsView.vue
            ├── MyClaimsView.vue
            └── admin/
                ├── AdminDashboard.vue
                ├── AdminItems.vue
                └── AdminClaims.vue
```

## 后端 API 接口

### 认证

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| `POST` | `/api/auth/login` | 用户登录 | `{ "username", "password" }` |

### 物品

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| `GET` | `/api/items` | 查询物品列表 | `?keyword=&category=&location=&status=&ownerType=` |
| `GET` | `/api/items/all` | 获取所有物品 | - |
| `GET` | `/api/items/{id}` | 物品详情 | - |
| `GET` | `/api/items/user/{userId}` | 用户发布的物品 | - |
| `GET` | `/api/items/stats` | 物品统计数据 | - |
| `POST` | `/api/items` | 发布物品 | Item JSON Body |
| `DELETE` | `/api/items/{id}` | 删除物品 | - |
| `PUT` | `/api/items/{id}/status` | 更新状态 | `{ "status" }` |

### 认领

| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| `GET` | `/api/claims` | 查询认领列表 | `?status=` |
| `GET` | `/api/claims/my?userId=` | 我的认领记录 | `userId` |
| `GET` | `/api/claims/pending` | 待审核认领 | - |
| `GET` | `/api/claims/stats` | 认领统计数据 | - |
| `POST` | `/api/claims` | 提交认领 | Claim JSON Body |
| `PUT` | `/api/claims/{id}/review` | 审核认领 | `{ "status", "adminNote" }` |
| `DELETE` | `/api/claims/{id}` | 删除认领记录 | - |

## 数据模型

### 用户 (User)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 用户 ID |
| username | String | 用户名 |
| password | String | 密码 |
| realName | String | 真实姓名 |
| role | String | 角色：`admin` / `user` |
| phone | String | 联系电话 |
| email | String | 邮箱 |
| studentId | String | 学号 |
| department | String | 院系 |

### 物品 (Item)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 物品 ID |
| title | String | 物品标题 |
| description | String | 详细描述 |
| category | String | 物品分类 |
| location | String | 相关地点 |
| publishDate | String | 发布日期 |
| expiryDate | String | 过期日期 |
| status | String | `pending` / `claimed` / `expired` |
| ownerType | String | `lost`（失物）/ `found`（拾物） |
| userId | int | 发布者 ID |
| userName | String | 发布者姓名 |
| phone | String | 联系电话 |
| views | int | 浏览次数 |

### 认领 (Claim)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 认领 ID |
| itemId | int | 物品 ID |
| itemTitle | String | 物品标题 |
| claimerId | int | 认领人 ID |
| claimerName | String | 认领人姓名 |
| ownerId | int | 物品主人 ID |
| ownerName | String | 物品主人姓名 |
| reason | String | 认领理由 |
| status | String | `pending` / `approved` / `rejected` / `expired` |
| adminNote | String | 管理员审核备注 |
| createdAt | String | 提交时间 |
| reviewedAt | String | 审核时间 |

## 业务流程

```
┌─────────────────────────────────────────────────────────┐
│                     物品发布流程                          │
├─────────────────────────────────────────────────────────┤
│  用户填写表单 → 重复发布校验 → 发布成功 → 状态: pending   │
│                                       ↓                  │
│                              30天/15天后自动过期           │
│                              状态: expired               │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                     认领审核流程                          │
├─────────────────────────────────────────────────────────┤
│  用户提交认领 → 状态: pending → 管理员审核                │
│                                      ↓         ↓        │
│                                 通过: approved  拒绝: rejected
│                                      ↓                  │
│                          物品状态 → claimed              │
└─────────────────────────────────────────────────────────┘
```

## 路由权限

| 路径 | 需要登录 | 需要管理员 | 说明 |
|------|:--------:|:----------:|------|
| `/` | ✗ | ✗ | 首页，所有人可访问 |
| `/login` | ✗ | ✗ | 登录页 |
| `/items` | ✗ | ✗ | 物品列表，所有人可访问 |
| `/item/:id` | ✗ | ✗ | 物品详情，所有人可访问 |
| `/publish` | ✓ | ✗ | 发布信息，需登录 |
| `/my/items` | ✓ | ✗ | 我的发布，需登录 |
| `/my/claims` | ✓ | ✗ | 我的认领，需登录 |
| `/admin` | ✓ | ✓ | 管理后台，需管理员 |
| `/admin/items` | ✓ | ✓ | 物品管理，需管理员 |
| `/admin/claims` | ✓ | ✓ | 认领管理，需管理员 |

## 页面功能说明

### 登录页 `/login`

- 用户名密码登录，支持回车提交
- 演示账号快速填充
- 登录后根据角色跳转（管理员 → 管理后台，普通用户 → 首页）

### 首页 `/`

- 系统欢迎横幅 + 快捷操作入口
- 实时统计数据（待认领/已认领/待审核/已过期）
- 最新发布物品卡片展示、系统公告

### 物品列表 `/items`

- 关键词搜索、分类筛选、地点筛选
- 状态标签页切换（待认领/已认领/已过期/全部）
- 类型快捷筛选（失物/拾物）

### 物品详情 `/item/:id`

- 完整物品信息 + 联系信息 + 剩余有效天数
- **认领申请**：填写理由 → 提交 → 等待管理员审核

### 发布信息 `/publish`

- 选择类型（失物/拾物）→ 表单填写 → 重复发布检测 → 发布

### 管理后台 `/admin/*`

- 数据概览、物品管理（强制下架/删除）、认领管理（审核通过/拒绝）

## 样式说明

- UI 框架：Element Plus
- 布局：Flexbox + CSS Grid
- 响应式：支持移动端自适应（768px 断点）
- 主题色：渐变紫 `#667eea → #764ba2`

## License

MIT
