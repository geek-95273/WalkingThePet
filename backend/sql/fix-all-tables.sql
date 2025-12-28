-- ==========================================
-- 修复所有数据库表结构 - 重建方案
-- 执行方式：在MySQL客户端中执行此脚本
-- 警告：会删除旧数据，请先备份重要数据！
-- ==========================================

-- ==========================================
-- 1. 修复宠物服务数据库 (walking_pet_pet)
-- ==========================================
USE walking_pet_pet;

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
-- 2. 修复公告服务数据库 (walking_pet_bulletin)
-- ==========================================
USE walking_pet_bulletin;

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
-- 3. 修复宠托师服务数据库 (walking_pet_sitter)
-- ==========================================
USE walking_pet_sitter;

DROP TABLE IF EXISTS sitter_services;
DROP TABLE IF EXISTS sitter_pets;
DROP TABLE IF EXISTS sitter_tags;
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

CREATE TABLE sitter_tags (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    sitter_id VARCHAR(50) NOT NULL COMMENT '宠托师ID',
    tag VARCHAR(50) NOT NULL COMMENT '标签',
    INDEX idx_sitter_id (sitter_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠托师标签表';

CREATE TABLE sitter_pets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    sitter_id VARCHAR(50) NOT NULL COMMENT '宠托师ID',
    name VARCHAR(50) NOT NULL COMMENT '宠物名称',
    description VARCHAR(200) COMMENT '描述',
    cover LONGTEXT COMMENT '封面图（Base64）',
    INDEX idx_sitter_id (sitter_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠托师宠物展示表';

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
-- 4. 修复订单服务数据库 (walking_pet_order)
-- ==========================================
USE walking_pet_order;

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

-- ==========================================
-- 完成
-- ==========================================
SELECT '所有数据库表结构修复完成！' AS message;
