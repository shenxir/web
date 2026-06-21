# 校园失物招领系统 - MySQL数据库使用说明

## 1. 数据库概述

本项目使用MySQL 8.0数据库，包含4张核心数据表，支持完整的失物招领业务流程。

### 数据表结构

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| sys_user | 用户表 | id, username, password, real_name, role |
| lost_found_item | 失物招领物品表 | id, title, category, location, status, user_id |
| item_image | 物品图片表 | id, item_id, image_url |
| item_claim | 认领记录表 | id, item_id, claimer_id, reason, status |

### 关系图

```
sys_user (1) ---< (many) lost_found_item        [lost_found_item.user_id -> sys_user.id]
sys_user (1) ---< (many) item_claim             [item_claim.claimer_id -> sys_user.id]
sys_user (1) ---< (many) item_claim             [item_claim.reviewed_by -> sys_user.id]
lost_found_item (1) ---< (many) item_claim      [item_claim.item_id -> lost_found_item.id]
lost_found_item (1) ---< (many) item_image      [item_image.item_id -> lost_found_item.id]
```

## 2. 数据库初始化

### 2.1 创建数据库和表

```bash
# 方法1：使用MySQL命令行
mysql -u root -p123456 < src/main/resources/schema.sql

# 方法2：登录MySQL后执行
mysql -u root -p
source D:/study/idea/webwork/src/main/resources/schema.sql
```

### 2.2 验证数据库

```sql
-- 查看数据库
SHOW DATABASES;

-- 使用数据库
USE lostfound_db;

-- 查看表
SHOW TABLES;

-- 查看表结构
DESCRIBE sys_user;
DESCRIBE lost_found_item;
DESCRIBE item_image;
DESCRIBE item_claim;
```

## 3. 测试数据

### 3.1 用户数据

| 用户名 | 密码 | 姓名 | 角色 | 学号 | 院系 |
|--------|------|------|------|------|------|
| admin | admin123 | 系统管理员 | admin | - | 信息中心 |
| zhangsan | 123456 | 张三 | user | 2021001 | 计算机学院 |
| lisi | 123456 | 李四 | user | 2021002 | 计算机学院 |
| wangwu | 123456 | 王五 | user | 2021003 | 数学学院 |
| zhaoliu | 123456 | 赵六 | user | 2021004 | 文学院 |

### 3.2 物品数据

- 10个物品，包含失物和拾物
- 不同状态：pending（待认领）、claimed（已认领）、expired（已过期）
- 不同分类：电子产品、证件卡类、钥匙、书籍文具、生活用品等

### 3.3 认领数据

- 6个认领记录
- 不同状态：pending（待审核）、approved（已通过）、rejected（已拒绝）

## 4. MyBatis Mapper使用

### 4.1 Mapper接口

| Mapper | 说明 | 主要方法 |
|--------|------|----------|
| UserMapper | 用户操作 | login, getUserById, insertUser |
| ItemMapper | 物品操作 | getItemById, searchItems, insertItem |
| ClaimMapper | 认领操作 | getClaimById, insertClaim, reviewClaim |

### 4.2 关键业务SQL

#### 多表联查示例

**物品详情查询（联表查询用户信息）：**
```xml
<select id="getItemById" resultMap="ItemResultMap">
    SELECT
        i.id, i.title, i.description, i.category, i.location,
        i.publish_date, i.expiry_date, i.status, i.owner_type,
        i.user_id, u.real_name AS user_name, i.phone, i.views
    FROM lost_found_item i
    LEFT JOIN sys_user u ON i.user_id = u.id AND u.is_deleted = 0
    WHERE i.id = #{itemId} AND i.is_deleted = 0
</select>
```

**认领审核（多表联动）：**
```xml
<!-- 更新认领状态 -->
<update id="reviewClaim">
    UPDATE item_claim
    SET status = #{status}, admin_note = #{adminNote},
        reviewed_by = #{reviewedBy}, reviewed_at = NOW()
    WHERE id = #{claimId} AND status = 'pending'
</update>

<!-- 审核通过时更新物品状态 -->
<update id="updateItemStatusOnApproved">
    UPDATE lost_found_item
    SET status = 'claimed', updated_at = NOW()
    WHERE id = #{itemId} AND status = 'pending'
</update>
```

**批量过期处理（多表联动）：**
```xml
<update id="autoExpireItems">
    UPDATE lost_found_item
    SET status = 'expired', updated_at = NOW()
    WHERE status = 'pending' AND expiry_date < CURDATE() AND is_deleted = 0
</update>

<update id="autoExpireClaims">
    UPDATE item_claim
    SET status = 'expired', updated_at = NOW()
    WHERE status = 'pending'
    AND created_at < DATE_SUB(NOW(), INTERVAL 7 DAY)
</update>
```

#### 统计查询示例

**首页统计数据：**
```xml
<select id="getItemStats" resultType="java.util.Map">
    SELECT
        (SELECT COUNT(*) FROM lost_found_item WHERE is_deleted = 0) AS totalItems,
        (SELECT COUNT(*) FROM lost_found_item WHERE status = 'pending' AND is_deleted = 0) AS pendingItems,
        (SELECT COUNT(*) FROM lost_found_item WHERE status = 'claimed' AND is_deleted = 0) AS claimedItems,
        (SELECT COUNT(*) FROM lost_found_item WHERE status = 'expired' AND is_deleted = 0) AS expiredItems,
        (SELECT COUNT(*) FROM lost_found_item WHERE owner_type = 'lost' AND is_deleted = 0) AS lostItems,
        (SELECT COUNT(*) FROM lost_found_item WHERE owner_type = 'found' AND is_deleted = 0) AS foundItems,
        (SELECT COUNT(*) FROM item_claim WHERE status = 'pending') AS pendingClaims
</select>
```

## 5. 索引说明

### 5.1 联合索引

| 表名 | 索引名 | 字段 | 用途 |
|------|--------|------|------|
| lost_found_item | idx_user_status | user_id, status | 查询用户物品 |
| lost_found_item | idx_type_status | owner_type, status | 按类型和状态筛选 |
| item_claim | idx_item_claimer | item_id, claimer_id | 检查重复认领 |
| item_claim | idx_item_status | item_id, status | 查询物品认领 |

### 5.2 唯一约束

| 表名 | 约束名 | 字段 | 说明 |
|------|--------|------|------|
| sys_user | uk_username | username | 用户名唯一 |
| sys_user | uk_student_id | student_id | 学号唯一 |
| item_claim | uk_item_claimer_pending | item_id, claimer_id, status | 同一用户对同一物品不能有多个pending认领 |

## 6. 外键约束

| 表名 | 外键名 | 字段 | 关联表 | 级联操作 |
|------|--------|------|--------|----------|
| lost_found_item | fk_item_user | user_id | sys_user.id | RESTRICT, CASCADE |
| item_image | fk_image_item | item_id | lost_found_item.id | CASCADE, CASCADE |
| item_claim | fk_claim_item | item_id | lost_found_item.id | RESTRICT, CASCADE |
| item_claim | fk_claim_claimer | claimer_id | sys_user.id | RESTRICT, CASCADE |
| item_claim | fk_claim_reviewer | reviewed_by | sys_user.id | SET NULL, CASCADE |

## 7. Spring Boot配置

### 7.1 数据源配置

```yaml
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/lostfound_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
```

### 7.2 MyBatis配置

```yaml
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: org.example.lostfound.model
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

## 8. 常用查询

### 8.1 查询用户物品列表

```sql
SELECT i.*, u.real_name AS user_name
FROM lost_found_item i
LEFT JOIN sys_user u ON i.user_id = u.id
WHERE i.user_id = ? AND i.is_deleted = 0
ORDER BY i.created_at DESC;
```

### 8.2 查询物品认领记录

```sql
SELECT c.*, i.title AS item_title,
       claimer.real_name AS claimer_name,
       owner.real_name AS owner_name
FROM item_claim c
LEFT JOIN lost_found_item i ON c.item_id = i.id
LEFT JOIN sys_user claimer ON c.claimer_id = claimer.id
LEFT JOIN sys_user owner ON i.user_id = owner.id
WHERE c.item_id = ?
ORDER BY c.created_at DESC;
```

### 8.3 统计每日新增物品

```sql
SELECT DATE(created_at) AS date, COUNT(*) AS count
FROM lost_found_item
WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
GROUP BY DATE(created_at)
ORDER BY date DESC;
```

## 9. 注意事项

1. **软删除** - 所有表都使用 `is_deleted` 字段进行软删除，物理删除很少使用
2. **时间字段** - 使用 `created_at` 和 `updated_at` 记录时间，由数据库自动维护
3. **状态字段** - 物品状态：pending/claimed/expired，认领状态：pending/approved/rejected/expired
4. **级联操作** - 删除物品时，关联的图片会级联删除，但认领会受到RESTRICT约束
5. **唯一约束** - 同一用户不能对同一物品提交多个pending状态的认领

## 10. 性能优化建议

1. **索引优化** - 根据实际查询需求调整索引
2. **连接池配置** - 根据并发量调整Druid连接池参数
3. **查询优化** - 避免N+1查询，使用联表查询代替多次单表查询
4. **缓存策略** - 对热点数据（如分类、地点）使用Redis缓存
