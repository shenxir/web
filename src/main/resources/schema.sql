-- =====================================================
-- 校园失物招领系统 - MySQL 8.0 数据库脚本
-- =====================================================

CREATE DATABASE IF NOT EXISTS lostfound_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE lostfound_db;

DROP TABLE IF EXISTS sys_announcement;
DROP TABLE IF EXISTS item_claim;
DROP TABLE IF EXISTS item_image;
DROP TABLE IF EXISTS lost_found_item;
DROP TABLE IF EXISTS sys_location;
DROP TABLE IF EXISTS sys_category;
DROP TABLE IF EXISTS sys_user;

-- 用户表
CREATE TABLE sys_user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    real_name VARCHAR(50),
    role VARCHAR(20) NOT NULL DEFAULT 'user',
    phone VARCHAR(20),
    email VARCHAR(100),
    student_id VARCHAR(50),
    department VARCHAR(100),
    avatar VARCHAR(500),
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_username (username),
    INDEX idx_role (role),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 分类表
CREATE TABLE sys_category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    icon VARCHAR(50),
    sort_order INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 地点表
CREATE TABLE sys_location (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 物品表
CREATE TABLE lost_found_item (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    category_id INT NOT NULL,
    location_id INT NOT NULL,
    publish_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    owner_type VARCHAR(10) NOT NULL,
    user_id INT NOT NULL,
    phone VARCHAR(20),
    views INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    FOREIGN KEY (category_id) REFERENCES sys_category(id),
    FOREIGN KEY (location_id) REFERENCES sys_location(id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_owner_type (owner_type),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 图片表
CREATE TABLE item_image (
    id INT PRIMARY KEY AUTO_INCREMENT,
    item_id INT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    original_name VARCHAR(200),
    file_size BIGINT,
    sort_order INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (item_id) REFERENCES lost_found_item(id) ON DELETE CASCADE,
    INDEX idx_item_id (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 认领表
CREATE TABLE item_claim (
    id INT PRIMARY KEY AUTO_INCREMENT,
    item_id INT NOT NULL,
    claimer_id INT NOT NULL,
    reason TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    admin_note VARCHAR(500),
    reviewed_by INT,
    reviewed_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (item_id) REFERENCES lost_found_item(id),
    FOREIGN KEY (claimer_id) REFERENCES sys_user(id),
    FOREIGN KEY (reviewed_by) REFERENCES sys_user(id),
    INDEX idx_item_id (item_id),
    INDEX idx_claimer_id (claimer_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 公告表
CREATE TABLE sys_announcement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    type VARCHAR(20) NOT NULL DEFAULT 'normal',
    status TINYINT NOT NULL DEFAULT 1,
    publish_by INT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    FOREIGN KEY (publish_by) REFERENCES sys_user(id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 测试数据
INSERT INTO sys_user (username, password, real_name, role, phone, email, student_id, department) VALUES
('admin', 'admin123', '系统管理员', 'admin', '13800138000', 'admin@school.edu', NULL, '信息中心'),
('zhangsan', '123456', '张三', 'user', '13800138001', 'zhangsan@school.edu', '2021001', '计算机学院'),
('lisi', '123456', '李四', 'user', '13800138002', 'lisi@school.edu', '2021002', '计算机学院'),
('wangwu', '123456', '王五', 'user', '13800138003', 'wangwu@school.edu', '2021003', '数学学院'),
('zhaoliu', '123456', '赵六', 'user', '13800138004', 'zhaoliu@school.edu', '2021004', '文学院');

INSERT INTO sys_category (name, icon, sort_order) VALUES
('电子产品', '📱', 1), ('证件卡类', '🪪', 2), ('钥匙', '🔑', 3),
('书籍文具', '📚', 4), ('衣物鞋帽', '👕', 5), ('生活用品', '🎒', 6),
('运动器材', '⚽', 7), ('食品饮料', '☕', 8), ('其他', '📦', 9);

INSERT INTO sys_location (name, sort_order) VALUES
('图书馆', 1), ('食堂', 2), ('教学楼', 3), ('宿舍楼', 4),
('操场', 5), ('实验室', 6), ('行政楼', 7), ('体育馆', 8);

INSERT INTO lost_found_item (title, description, category_id, location_id, publish_date, expiry_date, status, owner_type, user_id, phone, views) VALUES
('丢失黑色钱包', '在图书馆二楼丢失黑色钱包，内有身份证和银行卡', 6, 1, '2026-06-01', '2026-07-01', 'pending', 'lost', 2, '13800138001', 25),
('捡到蓝色书包', '在食堂捡到蓝色书包，里面有课本', 6, 2, '2026-06-02', '2026-06-17', 'pending', 'found', 3, '13800138002', 18),
('丢失校园卡', '校园卡丢失，姓名张三，学号2021001', 2, 3, '2026-06-03', '2026-07-03', 'pending', 'lost', 2, '13800138001', 32),
('捡到钥匙一串', '在宿舍楼捡到钥匙一串，共3把', 3, 4, '2026-06-04', '2026-06-19', 'claimed', 'found', 4, '13800138003', 15),
('丢失《数据结构》教材', '《数据结构》第三版，封面有笔记', 4, 1, '2026-06-05', '2026-07-05', 'pending', 'lost', 2, '13800138001', 22),
('捡到眼镜', '在教室捡到一副近视眼镜', 9, 3, '2026-05-01', '2026-05-16', 'expired', 'found', 5, '13800138004', 8),
('丢失耳机', '在操场丢失无线耳机', 1, 5, '2026-06-07', '2026-07-07', 'pending', 'lost', 3, '13800138002', 12),
('捡到水杯', '在图书馆捡到保温杯', 6, 1, '2026-06-08', '2026-06-23', 'pending', 'found', 5, '13800138004', 9),
('丢失雨伞', '在食堂丢失折叠伞', 6, 2, '2026-06-09', '2026-07-09', 'claimed', 'lost', 2, '13800138001', 16),
('捡到U盘', '在教室捡到16G U盘', 1, 3, '2026-06-10', '2026-06-25', 'pending', 'found', 4, '13800138003', 20);

INSERT INTO item_claim (item_id, claimer_id, reason, status, admin_note, reviewed_by, reviewed_at) VALUES
(4, 2, '这是我的钥匙，上面有我的钥匙扣', 'approved', '确认是失主', 1, '2026-06-05 10:30:00'),
(1, 4, '我看到有人在图书馆捡到钱包', 'pending', NULL, NULL, NULL),
(2, 2, '可能是我同学的书包', 'rejected', '描述不符', 1, '2026-06-03 14:20:00'),
(5, 3, '这是我丢失的教材', 'pending', NULL, NULL, NULL),
(9, 4, '这是我丢失的雨伞', 'approved', '确认失主', 1, '2026-06-11 09:00:00'),
(3, 5, '我捡到了一张校园卡', 'pending', NULL, NULL, NULL);

INSERT INTO sys_announcement (title, content, type, publish_by) VALUES
('失物招领系统上线通知', '校园失物招领系统正式上线，欢迎广大师生使用！', 'important', 1),
('认领须知更新', '为了提高认领效率，请认领时提供详细的物品描述和特征信息。', 'normal', 1);
