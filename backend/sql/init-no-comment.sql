-- Database Initialization Script for Walking Pet Service
-- MySQL 8.0+

-- Create databases
CREATE DATABASE IF NOT EXISTS walking_pet_user CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS walking_pet_pet CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS walking_pet_bulletin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS walking_pet_sitter CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS walking_pet_order CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- User Service Database
USE walking_pet_user;

DROP TABLE IF EXISTS users;
CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO users (user_id, username, password) VALUES 
('u-1000', 'admin', 'e10adc3949ba59abbe56e057f20f883e'),
('u-1001', 'test', 'e10adc3949ba59abbe56e057f20f883e');

-- Pet Service Database
USE walking_pet_pet;

DROP TABLE IF EXISTS pets;
CREATE TABLE pets (
    pet_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_name VARCHAR(100) NOT NULL,
    pet_type VARCHAR(50) NOT NULL,
    pet_breed VARCHAR(100),
    pet_age INT,
    pet_weight DECIMAL(5,2),
    pet_gender VARCHAR(10),
    pet_image LONGTEXT,
    owner_id VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_owner_id (owner_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Bulletin Service Database
USE walking_pet_bulletin;

DROP TABLE IF EXISTS bulletins;
CREATE TABLE bulletins (
    bulletin_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    service_type VARCHAR(50) NOT NULL,
    service_date DATE NOT NULL,
    service_time VARCHAR(50),
    location VARCHAR(200),
    price DECIMAL(10,2),
    owner_id VARCHAR(50) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_owner_id (owner_id),
    INDEX idx_status (status),
    INDEX idx_service_date (service_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Sitter Service Database
USE walking_pet_sitter;

DROP TABLE IF EXISTS sitters;
CREATE TABLE sitters (
    sitter_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    avatar LONGTEXT,
    user_id VARCHAR(50) NOT NULL UNIQUE,
    rating DECIMAL(3,2) DEFAULT 5.00,
    order_count INT DEFAULT 0,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_rating (rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS sitter_tags;
CREATE TABLE sitter_tags (
    tag_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sitter_id BIGINT NOT NULL,
    tag_name VARCHAR(50) NOT NULL,
    INDEX idx_sitter_id (sitter_id),
    FOREIGN KEY (sitter_id) REFERENCES sitters(sitter_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS sitter_pets;
CREATE TABLE sitter_pets (
    sitter_pet_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sitter_id BIGINT NOT NULL,
    pet_name VARCHAR(100) NOT NULL,
    pet_image LONGTEXT,
    INDEX idx_sitter_id (sitter_id),
    FOREIGN KEY (sitter_id) REFERENCES sitters(sitter_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS sitter_services;
CREATE TABLE sitter_services (
    service_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sitter_id BIGINT NOT NULL,
    service_name VARCHAR(100) NOT NULL,
    service_price DECIMAL(10,2) NOT NULL,
    INDEX idx_sitter_id (sitter_id),
    FOREIGN KEY (sitter_id) REFERENCES sitters(sitter_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Order Service Database
USE walking_pet_order;

DROP TABLE IF EXISTS orders;
CREATE TABLE orders (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_type VARCHAR(20) NOT NULL,
    service_date VARCHAR(50),
    location VARCHAR(200),
    pet_id BIGINT NOT NULL,
    pet_name VARCHAR(100),
    pet_breed VARCHAR(100),
    owner_id VARCHAR(50) NOT NULL,
    sitter_id VARCHAR(50),
    price DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'pending',
    complete_content TEXT,
    complete_image LONGTEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_owner_id (owner_id),
    INDEX idx_sitter_id (sitter_id),
    INDEX idx_status (status),
    INDEX idx_service_date (service_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
