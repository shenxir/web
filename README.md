# 🎓 校园失物招领系统

一个基于 Spring Boot + Vue 3 的全栈校园失物招领管理平台，帮助学生和教职工高效地发布、搜索和认领丢失物品。

## 📋 目录

- [项目简介](#项目简介)
- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
  - [1. 数据库初始化](#1-数据库初始化)
  - [2. 后端启动](#2-后端启动)
  - [3. 前端启动](#3-前端启动)
- [功能模块](#功能模块)
- [API 接口](#api-接口)
- [数据库设计](#数据库设计)
- [配置说明](#配置说明)
- [常见问题](#常见问题)
- [许可证](#许可证)

---

## 项目简介

校园失物招领系统是一个为学校师生设计的在线平台，旨在简化失物招领流程。系统支持：

- 📝 **发布失物/招领信息**：支持多图上传、分类选择、地点标注
- 🔍 **智能搜索**：按关键词、分类、地点、状态等多维度筛选
- ✅ **认领流程**：完整的认领申请、审核、批准/拒绝流程
- 👨‍💼 **管理后台**：管理员可审核认领、管理物品、发布公告
- 📊 **数据统计**：实时查看物品统计信息

## 功能特性

### 普通用户
- ✅ 注册/登录账户
- ✅ 发布丢失物品信息
- ✅ 发布捡到物品信息（招领）
- ✅ 搜索和筛选物品
- ✅ 查看物品详情
- ✅ 申请认领物品
- ✅ 查看我的物品和认领记录
- ✅ 上传物品图片（支持多图）

### 管理员
- ✅ 管理后台仪表盘
- ✅ 审核认领申请
- ✅ 管理所有物品信息
- ✅ 发布和管理公告
- ✅ 管理物品分类和地点
- ✅ 用户管理

## 技术栈

### 后端
| 技术 | 版本 | 说明 |
|------|------|------|
| **Java** | 21 | 开发语言 |
| **Spring Boot** | 3.2.5 | 框架 |
| **MyBatis** | 3.0.3 | ORM 框架 |
| **MySQL** | 8.0 | 数据库 |
| **Druid** | 1.2.21 | 数据库连接池 |
| **Lombok** | - | 简化代码 |

### 前端
| 技术 | 版本 | 说明 |
|------|------|------|
| **Vue** | 3.5.34 | 渐进式 JavaScript 框架 |
| **Vite** | 8.0.12 | 构建工具 |
| **Element Plus** | 2.14.1 | UI 组件库 |
| **Pinia** | 3.0.4 | 状态管理 |
| **Vue Router** | 4.6.4 | 路由管理 |
| **Axios** | 1.17.0 | HTTP 客户端 |

## 项目结构

```
webwork/
├── frontend/                    # 前端项目
│   ├── src/
│   │   ├── api/                 # API 接口封装
│   │   ├── assets/              # 静态资源
│   │   ├── components/          # 公共组件
│   │   │   ├── layout/          # 布局组件
│   │   │   ├── AdminSidebar.vue # 管理后台侧边栏
│   │   │   ├── AppNavbar.vue    # 导航栏
│   │   │   └── ItemCard.vue     # 物品卡片
│   │   ├── constants/           # 常量定义
│   │   ├── mock/                # 模拟数据
│   │   ├── router/              # 路由配置
│   │   ├── stores/              # Pinia 状态管理
│   │   ├── utils/               # 工具函数
│   │   └── views/               # 页面视图
│   │       ├── admin/           # 管理后台页面
│   │       ├── auth/            # 认证页面
│   │       ├── error/           # 错误页面
│   │       ├── home/            # 首页
│   │       └── items/           # 物品相关页面
│   ├── package.json
│   └── vite.config.js
│
├── src/                         # 后端项目
│   ├── main/
│   │   ├── java/org/example/lostfound/
│   │   │   ├── config/          # 配置类
│   │   │   │   ├── CorsConfig.java      # 跨域配置
│   │   │   │   └── WebConfig.java       # Web 配置
│   │   │   ├── controller/      # 控制器层
│   │   │   │   ├── AuthController.java        # 认证控制器
│   │   │   │   ├── ItemController.java        # 物品控制器
│   │   │   │   ├── ClaimController.java       # 认领控制器
│   │   │   │   ├── CategoryController.java    # 分类控制器
│   │   │   │   ├── LocationController.java    # 地点控制器
│   │   │   │   └── AnnouncementController.java # 公告控制器
│   │   │   ├── mapper/          # MyBatis Mapper 接口
│   │   │   ├── model/           # 实体类
│   │   │   └── service/         # 服务层
│   │   └── resources/
│   │       ├── mapper/          # MyBatis XML 映射文件
│   │       ├── application.yml  # 应用配置
│   │       └── schema.sql       # 数据库初始化脚本
│   └── pom.xml                  # Maven 配置
│
├── picture/                     # 上传图片存储目录
└── README.md                    # 项目说明文档
```

## 环境要求

### 必需环境
- **JDK** 21 或更高版本
- **Node.js** 18 或更高版本
- **MySQL** 8.0 或更高版本
- **Maven** 3.8+ (可选，也可使用 Maven Wrapper)

### 推荐 IDE
- **后端**: IntelliJ IDEA 2023+
- **前端**: VS Code + Volar 插件

---

## 快速开始

### 1. 数据库初始化

```bash
# 1. 登录 MySQL
mysql -u root -p

# 2. 执行数据库脚本
source /path/to/webwork/src/main/resources/schema.sql
```

或者直接在 MySQL 客户端中运行 `src/main/resources/schema.sql` 文件。

**默认管理员账号**：
- 用户名: `admin`
- 密码: `admin123`

### 2. 后端启动

```bash
# 进入后端目录（项目根目录）
cd webwork

# 方式1: 使用 Maven 命令
mvn spring-boot:run

# 方式2: 使用 Maven Wrapper (Windows)
mvnw spring-boot:run

# 方式3: 使用 Maven Wrapper (Linux/Mac)
./mvnw spring-boot:run

# 方式4: 在 IDE 中运行
# 打开 LostFoundApplication.java，点击运行按钮
```

后端服务启动后，默认运行在 `http://localhost:8080`

### 3. 前端启动

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端服务启动后，默认运行在 `http://localhost:5173`

### 4. 访问系统

打开浏览器访问: **http://localhost:5173**

使用测试账号登录：
- **管理员**: admin / admin123
- **普通用户**: zhangsan / 123456

---

## 功能模块

### 🏠 首页
- 系统介绍和统计数据
- 最新发布物品展示
- 快速搜索入口

### 📦 物品管理
- 物品列表：支持关键词、分类、地点、状态筛选
- 物品详情：查看完整信息、图片、发布者联系方式
- 发布物品：填写物品信息、上传图片、选择分类和地点

### ✋ 认领功能
- 申请认领：填写认领理由
- 我的认领：查看认领状态（待审核/已通过/已拒绝）

### 👨‍💼 管理后台
- **仪表盘**：数据统计、快捷操作
- **物品管理**：查看、删除、修改物品状态
- **认领管理**：审核认领申请、添加审核备注
- **公告管理**：发布、编辑、删除公告

---

## API 接口

### 认证接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/login` | 用户登录 |
| POST | `/api/auth/register` | 用户注册 |

### 物品接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/items` | 搜索物品 |
| GET | `/api/items/all` | 获取所有物品 |
| GET | `/api/items/{id}` | 获取物品详情 |
| GET | `/api/items/user/{userId}` | 获取用户物品 |
| POST | `/api/items` | 发布物品 |
| POST | `/api/items/upload` | 上传图片 |
| DELETE | `/api/items/{id}` | 删除物品 |
| PUT | `/api/items/{id}/status` | 更新物品状态 |
| GET | `/api/items/stats` | 获取统计数据 |

### 认领接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/claims` | 获取认领列表 |
| GET | `/api/claims/item/{itemId}` | 获取物品认领列表 |
| GET | `/api/claims/user/{userId}` | 获取用户认领列表 |
| POST | `/api/claims` | 申请认领 |
| PUT | `/api/claims/{id}/review` | 审核认领 |

### 分类/地点接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/categories` | 获取所有分类 |
| GET | `/api/locations` | 获取所有地点 |

### 公告接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/announcements` | 获取公告列表 |
| POST | `/api/announcements` | 发布公告 |
| PUT | `/api/announcements/{id}` | 更新公告 |
| DELETE | `/api/announcements/{id}` | 删除公告 |

---

## 数据库设计

### 数据库表结构

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `sys_user` | 用户表 | id, username, password, role, real_name, phone, email |
| `sys_category` | 分类表 | id, name, icon, sort_order |
| `sys_location` | 地点表 | id, name, sort_order |
| `lost_found_item` | 物品表 | id, title, description, category_id, location_id, status, owner_type |
| `item_image` | 图片表 | id, item_id, image_url, original_name |
| `item_claim` | 认领表 | id, item_id, claimer_id, reason, status, admin_note |
| `sys_announcement` | 公告表 | id, title, content, type, publish_by |

### 物品状态流转

```
pending (待认领) ──┬──> claimed (已认领)
                  ├──> expired (已过期)
                  └──> deleted (已删除)
```

### 认领状态流转

```
pending (待审核) ──┬──> approved (已通过)
                  └──> rejected (已拒绝)
```

---

## 配置说明

### 后端配置 (`application.yml`)

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lostfound_db
    username: root
    password: 123456
```

**修改数据库连接**：
- 修改 `spring.datasource.url` 中的数据库地址
- 修改 `spring.datasource.username` 和 `spring.datasource.password`

**文件上传配置**：
- 最大文件大小: 10MB
- 最大请求大小: 50MB
- 上传目录: `./picture`

### 前端配置

如需修改后端 API 地址，编辑 `frontend/src/api/index.js`：

```javascript
const api = axios.create({
  baseURL: 'http://localhost:8080',  // 修改此处
  timeout: 10000
})
```

---

## 常见问题

### Q1: 数据库连接失败
**错误**: `Communications link failure`

**解决方案**:
1. 确认 MySQL 服务已启动
2. 检查 `application.yml` 中的数据库连接信息
3. 确认 MySQL 用户名和密码正确

### Q2: 端口被占用
**错误**: `Web server failed to start. Port 8080 was already in use`

**解决方案**:
- 修改 `application.yml` 中的 `server.port` 为其他端口
- 或者停止占用该端口的进程

### Q3: 前端无法连接后端
**错误**: `Network Error` 或 `CORS error`

**解决方案**:
1. 确认后端服务已启动
2. 检查前端 API 地址配置
3. 确认后端跨域配置正确（`CorsConfig.java`）

### Q4: 图片上传失败
**解决方案**:
1. 确认 `picture/` 目录存在且有写入权限
2. 检查文件大小是否超过限制（10MB）
3. 检查文件格式是否支持（jpg, png, gif 等）

### Q5: 如何重置数据库
```bash
# 重新执行数据库脚本
mysql -u root -p lostfound_db < src/main/resources/schema.sql
```

---

## 开发说明

### 代码规范
- 后端遵循 RESTful API 设计规范
- 前端使用 Vue 3 Composition API
- 使用 ESLint 和 Prettier 进行代码格式化

### 添加新功能
1. 后端：创建 Model → Mapper → Service → Controller
2. 前端：创建 API → Store → View → 路由配置

### 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 用户 | zhangsan | 123456 |
| 用户 | lisi | 123456 |
| 用户 | wangwu | 123456 |
| 用户 | zhaoliu | 123456 |

---

## 许可证

本项目仅供学习和研究使用。

---

## 致谢

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [Element Plus](https://element-plus.org/)
- [MyBatis](https://mybatis.org/)

---

**📧 如有问题，请联系项目维护者**