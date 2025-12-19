-- ==========================================
-- 宠物喂溜服务 - 数据库初始化脚本
-- ==========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS walking_pet_user CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS walking_pet_pet CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS walking_pet_bulletin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS walking_pet_sitter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS walking_pet_order CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- ==========================================
-- 用户服务数据库
-- ==========================================
USE walking_pet_user;

-- 用户表
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码（MD5加密）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入测试数据（密码都是123456的MD5）
INSERT INTO users (user_id, username, password) VALUES 
('u-1000', 'admin', 'e10adc3949ba59abbe56e057f20f883e'),
('u-1001', 'test', 'e10adc3949ba59abbe56e057f20f883e');

-- ==========================================
-- 宠物服务数据库
-- ==========================================
USE walking_pet_pet;

-- 宠物档案表
DROP TABLE IF EXISTS pets;
CREATE TABLE pets (
    pet_id VARCHAR(50) PRIMARY KEY COMMENT '宠物ID',
    user_id VARCHAR(50) NOT NULL COMMENT '所属用户ID',
    type VARCHAR(20) NOT NULL COMMENT '宠物类型：猫/狗/其他',
    name VARCHAR(50) NOT NULL COMMENT '宠物名称',
    age VARCHAR(20) COMMENT '年龄',
    gender VARCHAR(10) COMMENT '性别',
    weight VARCHAR(20) COMMENT '体重',
    breed VARCHAR(50) COMMENT '品种',
    aggressive BOOLEAN DEFAULT FALSE COMMENT '是否有攻击性',
    rabies_vaccine BOOLEAN DEFAULT FALSE COMMENT '是否接种狂犬疫苗',
    intro TEXT COMMENT '简介',
    image LONGTEXT COMMENT '图片（Base64）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物档案表';

-- ==========================================
-- 公告服务数据库
-- ==========================================
USE walking_pet_bulletin;

-- 喂溜公告表
DROP TABLE IF EXISTS bulletins;
CREATE TABLE bulletins (
    bulletin_id VARCHAR(50) PRIMARY KEY COMMENT '公告ID',
    user_id VARCHAR(50) NOT NULL COMMENT '发布人ID',
    service_type VARCHAR(20) NOT NULL COMMENT '服务类型：feed-cat/walk-dog',
    title VARCHAR(100) NOT NULL COMMENT '标题',
    status VARCHAR(20) DEFAULT '待接单' COMMENT '状态：待接单/已接单',
    address VARCHAR(200) NOT NULL COMMENT '地址',
    pet_id VARCHAR(50) COMMENT '宠物ID',
    pet_name VARCHAR(50) COMMENT '宠物名称',
    pet_type VARCHAR(50) COMMENT '宠物类型',
    service_time DATETIME NOT NULL COMMENT '服务时间',
    walker_gender VARCHAR(10) COMMENT '宠托师性别偏好',
    remark TEXT COMMENT '备注',
    sitter_id VARCHAR(50) COMMENT '接单宠托师ID',
    distance VARCHAR(20) COMMENT '距离',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_service_type (service_type),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='喂溜公告表';

-- ==========================================
-- 宠托师服务数据库
-- ==========================================
USE walking_pet_sitter;

-- 宠托师表
DROP TABLE IF EXISTS sitters;
CREATE TABLE sitters (
    sitter_id VARCHAR(50) PRIMARY KEY COMMENT '宠托师ID',
    user_id VARCHAR(50) NOT NULL COMMENT '关联用户ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) NOT NULL COMMENT '性别',
    slogan TEXT COMMENT '口号',
    rating DECIMAL(2,1) DEFAULT 5.0 COMMENT '评分',
    orders INT DEFAULT 0 COMMENT '订单数',
    distance VARCHAR(20) COMMENT '距离',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠托师表';

-- 宠托师标签表
DROP TABLE IF EXISTS sitter_tags;
CREATE TABLE sitter_tags (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    sitter_id VARCHAR(50) NOT NULL COMMENT '宠托师ID',
    tag VARCHAR(50) NOT NULL COMMENT '标签',
    INDEX idx_sitter_id (sitter_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠托师标签表';

-- 宠托师养宠展示表
DROP TABLE IF EXISTS sitter_pets;
CREATE TABLE sitter_pets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    sitter_id VARCHAR(50) NOT NULL COMMENT '宠托师ID',
    name VARCHAR(50) NOT NULL COMMENT '宠物名称',
    description VARCHAR(200) COMMENT '描述',
    cover LONGTEXT COMMENT '封面图（Base64）',
    INDEX idx_sitter_id (sitter_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠托师宠物展示表';

-- 服务配置表
DROP TABLE IF EXISTS sitter_services;
CREATE TABLE sitter_services (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    sitter_id VARCHAR(50) NOT NULL COMMENT '宠托师ID',
    service_type VARCHAR(20) NOT NULL COMMENT '服务类型：feed-cat/walk-dog',
    title VARCHAR(50) NOT NULL COMMENT '标题',
    price VARCHAR(20) NOT NULL COMMENT '价格',
    duration VARCHAR(20) NOT NULL COMMENT '时长',
    INDEX idx_sitter_id (sitter_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠托师服务配置表';

-- ==========================================
-- 订单服务数据库
-- ==========================================
USE walking_pet_order;

-- 订单表
DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    order_id VARCHAR(50) PRIMARY KEY COMMENT '订单ID',
    user_id VARCHAR(50) NOT NULL COMMENT '下单用户ID',
    sitter_id VARCHAR(50) NOT NULL COMMENT '宠托师ID',
    sitter_name VARCHAR(50) COMMENT '宠托师姓名',
    bulletin_id VARCHAR(50) COMMENT '关联公告ID',
    status VARCHAR(20) NOT NULL COMMENT '状态：待宠托师接单/已接单/已完成',
    service_type VARCHAR(20) NOT NULL COMMENT '服务类型：feed-cat/walk-dog',
    service_time DATETIME NOT NULL COMMENT '服务时间',
    address VARCHAR(200) NOT NULL COMMENT '地址',
    pet_id VARCHAR(50) COMMENT '宠物ID',
    walker_gender VARCHAR(10) COMMENT '性别偏好',
    remark TEXT COMMENT '备注',
    complete_content TEXT COMMENT '完成内容',
    complete_image LONGTEXT COMMENT '完成图片（Base64）',
    completed_at DATETIME COMMENT '完成时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_sitter_id (sitter_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 完成
SELECT '数据库初始化完成！' AS message;
